package model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static constant.Cnst.*;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import beans.SocialPlace;
/**
 * Retrieve Venues on Foursquare for a city (or a city and a category) without need of a user connection
 * @author Damien Goetschi
 */
public class GetInfoFoursquare {
	
	/**
	 * base URL for request on Foursquare
	 */
	private final static String REQUEST_URL = "https://api.foursquare.com/v2/";
	
	
	/**
	 * Retrieve venues for a city
	 * @param location the city for which retrieve informations
	 * @return a ArrayList content the SocialPlace
	 * @throws IOException
	 */
	public static ArrayList<SocialPlace> getVenuesData(String location)throws IOException
	{
		ArrayList<SocialPlace> fsPlaces = new ArrayList<SocialPlace>();
		//Retrive Hotels
		ArrayList<SocialPlace> tmp =getVenuesData(location,CATEGORY_HOTEL);
		for(int i=0;i<tmp.size();i++){
			//set site and cytegory for each venue
			tmp.get(i).setSite(SITE_FOURSQUARE);
			tmp.get(i).setTopLevelCat(TYPE_HOTEL);
			fsPlaces.add(tmp.get(i));
		}
		//Retrive Restaurants
		tmp =getVenuesData(location,CATEGORY_FOOD);
		for(int i=0;i<tmp.size();i++){
			//set site and category for each venue
			tmp.get(i).setSite(SITE_FOURSQUARE);
			tmp.get(i).setTopLevelCat(TYPE_RESTAURANT);
			fsPlaces.add(tmp.get(i));
		}
		//Retrive Touristic Sites
		tmp =getVenuesData(location,CATEGORY_TOURISTIC);
		for(int i=0;i<tmp.size();i++){
			//set site and category for each venue
			tmp.get(i).setSite(SITE_FOURSQUARE);
			fsPlaces.add(tmp.get(i));
		}
		return fsPlaces;
	}
	
	/**
	 * Retrieve venues for a city and a category
	 * @param location the city for witch retrieve informations
	 * @param category the id of the categories to search (or "" to search in all category)
	 * @return a ArrayList content the SocialPlace
	 * @throws IOException
	 */
	public static ArrayList<SocialPlace> getVenuesData(String location,String category)throws IOException
	{
		String res="";
		//create URL with all parameters for search and key/secret for this app
		String urlString = REQUEST_URL + "venues/search?near=" + location;
		if(category!="")
			urlString+="&categoryId=" + category;
		urlString+="&radius="+FS_RANGE+"&limit=" + NB_RESULT + "&client_id=" + FS_KEY + "&client_secret=" + FS_SECRET + "&v="+FS_VERSION;
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input;
		//create a String with the response to the query
		while ((input = br.readLine()) != null){
			res+=input;
	    }
	    br.close();
		ArrayList<SocialPlace> list =  parseVenuesJSON(res);
		//Add photos to the SocialPlace in the list
		getPhoto(list);
		return list;
	}
	
	/**
	 * Parse a JSON String content a list of Foursquare venues to a ArrayList of SocialPlace
	 * @param json String content JSON data
	 * @return the ArrayList of SocialPlace
	 * @throws IOException
	 */
	public static ArrayList<SocialPlace> parseVenuesJSON(String json) throws IOException{
		//convert characters from UTF-8
		json = new String ( json.getBytes(), "UTF-8" );
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(json);
		ArrayList<SocialPlace> list = new ArrayList<SocialPlace>();
		if(tokenNextTo(jp, "venues")!=null){
			jp.nextToken();jp.nextToken();
			int brace=1,bracket=1;
			SocialPlace current = new SocialPlace();
			String s="";
			//containing if token in json is in category part (to get category's name)
			boolean inCategorie=false;
			//through the JSON string and add informations to SocialPlace
			while(jp.nextToken()!=null&&bracket>0){
				s=jp.getText();
				switch(s){
					//count the number of brace to know where is the end of a place and the begin of the next
					case "{":
						brace++;break;
					case "}":
						brace--;
						if(brace==0){
							list.add(current);
							current=new SocialPlace();
						}
						break;
					//count the number of bracket to know where is the end of the JSON result
					case "[":
						bracket++;break;
					case "]":
						bracket--;
						break;
					//set id only first time (first is id of place, others id in JSON are category id, page id, and others)
					case "id":
						jp.nextToken();
						if(current.getId().equals(""))
							current.setId(jp.getText());
						break;
					//set name only the first time and set type if token is in category
					case "name":
						jp.nextToken();
						if(inCategorie){
							current.setType(jp.getText());
							inCategorie=false;
						}else
							if(current.getName().equals(""))
								current.setName(jp.getText());
						break;
					case "address":
						jp.nextToken();
						current.getLocation().setStreet(jp.getText());
						break;
					case "lat":
						jp.nextToken();
						current.getLocation().setLatitude(jp.getDoubleValue());
						break;
					case "lng":
						jp.nextToken();
						current.getLocation().setLongitude(jp.getDoubleValue());
						break;
					case "postalCode":
						jp.nextToken();
						current.getLocation().setZip(jp.getText());
						break;
					case "city":
						jp.nextToken();
						current.getLocation().setCity(jp.getText());
						break;
					case "country":
						jp.nextToken();
						current.getLocation().setCountry(jp.getText());
						break;
					case "canonicalUrl":
						jp.nextToken();
						current.setUrl(jp.getText());
						break;
					case "url":
						jp.nextToken();
						current.setUrl(jp.getText());
						break;
					case "checkinsCount":
						jp.nextToken();
						current.setWereHereCount(jp.getIntValue());
						break;
					case "usersCount":
						jp.nextToken();
						current.setLikeCount(jp.getIntValue());
						break;
					case "categories":
						inCategorie=true;
						break;
				}
			}
		}
		return list;
	}
	
	/**
	 * This method will add photo in all SocialPlace of the ArrayList in parameter
	 * @param places ArrayList of SocialPlace for which add photos
	 * @throws IOException
	 */
	private static void getPhoto(ArrayList<SocialPlace> places) throws IOException{
		for(int i=0;i<places.size();i++){
			SocialPlace p = places.get(i);
			String res="";
			String urlString = REQUEST_URL + "venues/"+p.getId()+"/photos?client_id=" + FS_KEY + "&client_secret=" + FS_SECRET + "&v="+FS_VERSION;
			URL url = new URL(urlString);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String input;
			while ((input = br.readLine()) != null){
				res+=input;
		    }
		    br.close();
		    //Convert res from UTF-8
		    res = new String ( res.getBytes(), "UTF-8" );
			JsonFactory f = new JsonFactory();
			JsonParser jp = f.createJsonParser(res);
			//take just the first picture of this place (small and big)
			if(tokenNextTo(jp, "prefix")!=null){
				jp.nextToken();
				String prefix = jp.getText();
				tokenNextTo(jp, "suffix");
				jp.nextToken();
				String suffix = jp.getText();
				//create link with prefix+size+suffix
				p.setSmallPic(prefix+"50x50"+suffix);
				p.addLargePic(prefix+"original"+suffix);
			}
			//take the other picture (only big)
			while(tokenNextTo(jp, "prefix")!=null){
				jp.nextToken();
				String prefix = jp.getText();
				tokenNextTo(jp, "suffix");
				jp.nextToken();
				String suffix = jp.getText();
				p.addLargePic(prefix+"original"+suffix);
			}
		    
		}
		
	}
	
	/**
	 * Push token up to next text(parameter)
	 * @param jp JsonParser to use
	 * @param text Go until witch text
	 * @throws IOException
	 */
	private static JsonToken tokenNextTo(JsonParser jp, String text) throws IOException{
		JsonToken t = jp.nextToken();
		while(t!=null&&!jp.getText().equals(text)){
			t=jp.nextToken();
		}
		return t;
	}
	
}
