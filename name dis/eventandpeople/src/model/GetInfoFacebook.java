package model;

import java.io.BufferedReader;

import java.io.IOException;



import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
 
import org.scribe.builder.*;
import org.scribe.builder.api.*;
import org.scribe.model.*;
import org.scribe.oauth.*;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

import beans.facebookPerson;
import beans.SocialEvent;
import beans.SocialLocation;
import beans.SocialPlace;
import facebook4j.Event;
import facebook4j.Facebook;
import facebook4j.FacebookException;
import facebook4j.FacebookFactory;
import facebook4j.GeoLocation;
import facebook4j.Place;
import facebook4j.Reading;
import facebook4j.ResponseList;
import facebook4j.User;
import facebook4j.auth.AccessToken;
import facebook4j.internal.org.json.JSONObject;
import static constant.Cnst.*;

/**
 * Connect the user to get access token and get places and events
 * @author Morgane Fauvet
 * @author Damien Goetschi
 */
public class GetInfoFacebook
{
	/**
	 * EMPTY_TOKEN (for test if token is empty or not)
	 */
	private Token EMPTY_TOKEN = null;
	/**
	 * Indicate if user is connected on Facebook or not
	 */
	private boolean connected=false;
	/**
	 * OAuthService for connect the user to this app
	 */
	private OAuthService service = new ServiceBuilder()
	.provider(FacebookApi.class)
	.apiKey(FB_KEY)
	.apiSecret(FB_SECRET)
	.callback(FB_CALLBACK)
	.build();

	/**
	 * Token for send request to Facebook from the user
	 */
	private Token accessTokenPointer;
	
	/**
	 * Facebook object use for connect user and send basic queries
	 */
	private Facebook facebook = new FacebookFactory().getInstance();

	/**
	 * Constructor without parameters
	 * @throws IOException
	 */
	public GetInfoFacebook() throws IOException
	{
		facebook.setOAuthAppId(FB_KEY, FB_SECRET);
	}

	/**
	 * Get URL
	 * @return URL to connect the user from Facebook
	 */
	public String getAuthorizationUrl()
	{
		String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
		return authorizationUrl;
	}


	/**
	 * Get access token for the user to send request to facebook
	 * @param code code return from Facebook when the user is connected
	 */
	public void getAccessToken(String code)
	{
		Verifier verif = new Verifier(code) ;
		accessTokenPointer = service.getAccessToken(EMPTY_TOKEN, verif);
		String accessToken = accessTokenPointer.getToken();
		facebook.setOAuthAccessToken(new AccessToken(accessToken, null));
		facebook.setOAuthPermissions("ads_management, ads_read, create_event, create_note, email," +
				" export_stream, friends_about_me, friends_actions.books, friends_actions.music, " +
				"friends_actions.news, friends_actions.video, friends_activities, friends_birthday," +
				" friends_education_history, friends_events, friends_games_activity, friends_groups," +
				" friends_hometown, friends_interests, friends_likes, friends_location, friends_notes, " +
				"friends_online_presence, friends_photo_video_tags, friends_photos, friends_questions," +
				" friends_relationship_details, friends_relationships, friends_religion_politics, " +
				"friends_status, friends_subscriptions, friends_videos, friends_website, friends_work_history," +
				" manage_friendlists, manage_notifications, manage_pages, photo_upload, publish_actions," +
				" publish_stream, read_friendlists, read_insights, read_mailbox, read_page_mailboxes, " +
				"read_requests, read_stream, rsvp_event, share_item, sms, status_update, user_about_me," +
				" user_actions.books, user_actions.music, user_actions.news, user_actions.video, " +
				"user_activities, user_birthday, user_education_history, user_events, user_friends, " +
				"user_games_activity, user_groups, user_hometown, user_interests, user_likes, user_location," +
				" user_notes, user_online_presence, user_photo_video_tags, user_photos, user_questions," +
				" user_relationship_details, user_relationships, user_religion_politics, user_status, " +
				"user_subscriptions, user_videos, user_website, user_work_history, video_upload, xmpp_login");
		connected=true;
	}

	/**
	 * Search places for a city
	 * @param q name of city in which search places
	 * @param nb max number of result
	 * @param offset offset for search
	 * @return an ArrayList of SocialPlace found or null if a IOExeption occurred 
	 * @throws FacebookException
	 */
	public ArrayList<SocialPlace> getPlaces(String q,int nb,int offset) throws FacebookException{
		//Queries with facebook api to get basic information (id,name and location)
		ResponseList<Place> places =facebook.searchPlaces(q,new Reading().limit(nb).offset(offset));
		//Transform the ResponseList of Place to a ArrayList of SocialPlace and add more information (like and were here count and pictures)
		ArrayList<SocialPlace> fbPlaces = RLtoAL_places(places);
		//For each place change the top level category if type is hotel or restaurant/cafe else, the top category stay "Touristic Site"
		for(int i=0; i<fbPlaces.size();i++){
			if(fbPlaces.get(i).getType().equals("HOTEL"))
				fbPlaces.get(i).setTopLevelCat(TYPE_HOTEL);
			if(fbPlaces.get(i).getType().equals("RESTAURANT/CAFE"))
				fbPlaces.get(i).setTopLevelCat(TYPE_RESTAURANT);
		}
		return fbPlaces;
	}
	/**
	 * 
	 * test fb place
	 * @throws FacebookException 
	 * */
	/*public void testfbplace() throws FacebookException{
		
		ResponseList<User> users = facebook.searchUsers("Oğuzhan Aksu");
		for (User user : users) {
		    facebook.getUser(user.getId() );    
		    System.out.println(user);
		}
	}*/
	public void testfbplace() throws FacebookException{
		
		User user = facebook.getUser("100004328229716");
		System.out.println(user);
	}
	
	/**
	 * Transform a ResponseList of Place to a ArrayList of FbPlace with more informations
	 * @param places ResponseList of Place
	 * @return the ArrayList of FbPlace
	 * @throws FacebookException 
	 */
	public ArrayList<SocialPlace> RLtoAL_places(ResponseList<Place> places) throws FacebookException{
		ArrayList<SocialPlace> list = new ArrayList<SocialPlace>();

		for(int i =0;i<places.size();i++){
			Place p = places.get(i);
			//Create a SocialPlace for each place in ResponseList and set site to facebook										
			SocialPlace fbPlace= new SocialPlace(p.getId(), p.getName(), new SocialLocation(p.getLocation()),"http://facebook.com/"+p.getId());
			fbPlace.setSite(SITE_FACEBOOK);
			try {
				//get Json with more information for this place and add this information to place
				String res = getMoreInfosPlace(p.getId());
				JsonFactory f = new JsonFactory();
				JsonParser jp = f.createJsonParser(res);
				while (jp.nextToken() != null)
				{
					String curTok = jp.getText();
					if(curTok.equals("type")){
						jp.nextToken();
						fbPlace.setType(jp.getText());
					}else if(curTok.equals("fan_count")){
						jp.nextToken();
						fbPlace.setLikeCount(jp.getIntValue());
					}else if(curTok.equals("were_here_count")){
						jp.nextToken();       //
						fbPlace.setWereHereCount(jp.getIntValue());
					}else if(curTok.equals("pic_square")){
						jp.nextToken();
						fbPlace.setSmallPic(jp.getText());
					}else if(curTok.equals("pic_large")){
						jp.nextToken();
						fbPlace.addLargePic(jp.getText());
					}
					
				}
				//add this place to the ArrayList
				list.add(fbPlace);
			} catch (IOException e) {
				System.err.println("Error reading JSON Data / Adding fbPlace but without type,fan_count and were_here_count");
				list.add(fbPlace);
			}
		}
		return list;
	}
	
	

	/**
	 * Get more informations about a place
	 * @param idPage id of the page of the place
	 * @return A JSON String with type of place, number of like on facebook and number of people that said they was here and pictures
	 * @throws IOException
	 */
	private String getMoreInfosPlace(String idPage) throws IOException{
		String res="";
		//create url for the FQL query with field to get and id of page and add token of the user
		String urlString = "https://graph.facebook.com/fql?q=SELECT%20type,fan_count,were_here_count,pic_square,pic_large%20FROM%20page%20WHERE%20page_id="+idPage+"&access_token="+accessTokenPointer.getToken();
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input;
		//create a String with the response to the query
		while ((input = br.readLine()) != null){
			res+=input;
		}
		br.close();
		return res;
	}

	/**
	 * Search events for a keyword
	 * @param q keyword to search
	 * @param nb max number of result
	 * @param offset offset for search
	 * @return an ArrayList of FbEvent found
	 * @throws FacebookException
	 */
	public ArrayList<SocialEvent> getEvents(String q,int nb,int offset) throws FacebookException{
		ArrayList<SocialEvent> list = new ArrayList<SocialEvent>();
		//Use facebook api to query basic information about events
		ResponseList<Event> events = facebook.searchEvents(q,new Reading().limit(nb).offset(offset).fields("venue","start_time","end_time","name","location","description"));
		for(int i=0;i<events.size();i++){
			Event e = events.get(i);
			//create a SocialEvent for each Event in ResponseList and set site to Facebook
			SocialEvent fbEvent = new SocialEvent(e.getId(), e.getName(), e.getStartTime(), e.getEndTime(), new SocialLocation(e.getVenue()), e.getLocation(),e.getDescription(),"http://facebook.com/"+e.getId());
			fbEvent.setSite(SITE_FACEBOOK);
			try {
				//Get more informations about event and add this information to this event
				String res = getMoreInfosEvent(e.getId());
				JsonFactory f = new JsonFactory();
				JsonParser jp = f.createJsonParser(res);
				while (jp.nextToken() != null)
				{
					String curTok = jp.getText();
					if(curTok.equals("all_members_count")){
						fbEvent.setInvites(jp.nextIntValue(-1));
					}else if(curTok.equals("attending_count")){
						fbEvent.setAttending(jp.nextIntValue(-1));
					}else if(curTok.equals("pic_small")){
						jp.nextToken();
						fbEvent.setSmallPic(jp.getText());
					}else if(curTok.equals("pic_big")){
						jp.nextToken();
						fbEvent.addLargePic(jp.getText());
					}
				}
				//add this event to the ArrayList
				list.add(fbEvent);
			} catch (IOException exc) {
				System.err.println("Error reading JSON Data / Adding fbEvent but without number of invitees and accepting");
				list.add(fbEvent);
			}
		}
		return list;
	} 
	
	/**
	 * Get more informations about a event
	 * @param idPage id of the page of the place
	 * @return A JSON String with number of invitees, number of invitees accepting the invite and pictures
	 * @throws IOException
	 */
	private String getMoreInfosEvent(String eid) throws IOException{
		String res="";
		//create url for the FQL query with field to get and id of event and add token of the user
		String urlString = "https://graph.facebook.com/fql?q=SELECT%20all_members_count,attending_count,pic_small,pic_big%20FROM%20event%20WHERE%20eid="+eid+"&access_token="+accessTokenPointer.getToken();
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input;
		//create a String with the response to the query
		while ((input = br.readLine()) != null){
			res+=input;
		}
		br.close();
		return res;
	}
	
	/**
	 * Verify if user is connected
	 * @return a boolean connected or not
	 */
	public boolean isConnected(){
		return connected;
	}
}