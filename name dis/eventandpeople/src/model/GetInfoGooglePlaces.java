package model;

import static constant.Cnst.SITE_GP;
import static constant.Cnst.TYPE_HOTEL;
import static constant.Cnst.TYPE_RESTAURANT;
import static constant.Cnst.TYPE_TOURISTIC_SITE;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import beans.Geocode;
import beans.SocialPlace;
import constant.Cnst;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

/**
 * Retrieve data from google places 
 * @author Hussein Hazimeh
 * */

public class GetInfoGooglePlaces {

	/**
	 * @param args
	 */
	private String lat;
	private String lng;
	private double lngp;
	private double latp;
	private String photo_ref;
	private String vicinity;
	private String url;
	private JSONObject o2;
	private int rate;
	private JSONArray typesArr; 
	private int c=0;
	private String id;
	public GetInfoGooglePlaces(){
		
		 
	}
	
	
	 
	/**
	 * @param city
	 * @return list
	 * @throws JSONException
	 * @throws IOException
	 */
	public ArrayList<SocialPlace> getPlaces(String city) throws JSONException, IOException{
		Geocode gl2=new  Geocode(city);
		gl2.setlnglat();
		this.lat = gl2.getLat();
		this.lng = gl2.getLng();
		ArrayList<SocialPlace> list = new ArrayList<SocialPlace>();	
		//types = restaurant, hotel,tuoristic cite 
		String [] typesList = {"restaurant","lodging","store"};	
		String [] TypesCnst = {TYPE_RESTAURANT,TYPE_HOTEL,TYPE_TOURISTIC_SITE};
		for(int j=0;j<=2;j++){
		try {
			String res = getInfosPlace("",typesList[j]);
			this.c++;
			JSONObject obj = new JSONObject(res);
			JSONArray arr = obj.getJSONArray("results");
			int i=0;
			while( i<arr.length()){
				SocialPlace gpPlace= new SocialPlace();
				JSONObject data = arr.getJSONObject(i);
				JSONObject gl = data.getJSONObject("geometry").getJSONObject("location");	
				
				//get detailed info about a place
				String res2 = getInfosPlace(data.getString("reference"),"");
				JSONObject o = new JSONObject(res2);
			
				try
				{
					o2 = o.getJSONObject("result");
					this.url = o2.getString("url");
				}catch(JSONException e){
					this.url = "http://www.google.com";
				}
				
				
				try{
					this.typesArr = data.getJSONArray("types");
				}catch(JSONException e){
					System.err.print("Failed to get data types for touristic \n");
				}
				
				try{
					JSONArray pl = data.getJSONArray("photos");
					JSONObject p2 = pl.getJSONObject(0);
					this.photo_ref = p2.getString("photo_reference");
				}catch(JSONException e){
					System.err.print("Failed to get data\n");
					this.photo_ref = "";
				}
				this.latp = gl.getDouble("lat");
				this.lngp = gl.getDouble("lng");
				try{
					this.vicinity = data.getString("vicinity");
					this.id = data.getString("id");
				}catch(JSONException e){
				
				}
				String name = data.getString("name");
				try{
					this. rate = data.getInt("rating");
				}catch(JSONException e){
					
				}
				String typesValue = "";
				for(int x=0;x<this.typesArr.length();x++){
					typesValue += typesArr.getString(x).toUpperCase() + "/";
				}
				String icon = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=100&maxheight=100&photoreference=" + this.photo_ref + "&sensor=true&key=AIzaSyDrpThD44CH7UPkVCejJKL9qzvl5b2Cklc";
				gpPlace.setId(id);
				gpPlace.getLocation().setLatitude(this.latp);
				gpPlace.getLocation().setLongitude(this.lngp);
				gpPlace.getLocation().setCity(city);
				gpPlace.getLocation().setStreet(this.vicinity);
				gpPlace.getLocation().setZip(" ");
				gpPlace.getLocation().setCountry("country");
				gpPlace.setUrl(this.url);
				gpPlace.setSite(SITE_GP);
				gpPlace.setName(name);
				gpPlace.setSmallPic(icon);
				gpPlace.setLikeCount(this.rate);
				gpPlace.setWereHereCount(0);
				gpPlace.setTopLevelCat(TypesCnst[j]);
				gpPlace.setType(TypesCnst[j]);
				gpPlace.setSite(SITE_GP);
				//add this place to the ArrayList
				list.add(gpPlace);
				i++;
				 
				}
			} catch (IOException e) {
				System.err.println("Error reading JSON Data");
				//list.add(gpPlace);
			}
		}
		return list;
	}

	private String getInfosPlace(String reference, String types) throws IOException{
		
		String res="";
		String urlString ;
		//create url for the Google places query with field to get and id of page and add token of the user
		if(reference.equals("")){
			if(this.c==0)
				urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
			   + this.lat + "," + this.lng + "&radius=1000&types=restaurant|cafe&sensor=false&key=" + Cnst.GP_KEY;
			else if(this.c==1)
				urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
			    + this.lat + "," + this.lng + "&radius=1000&types=restaurant|cafe&sensor=false&key=" + Cnst.GP_KEY;
			else
				urlString = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="
				+ this.lat + "," + this.lng + "&radius=1000&types=meseum|airport|zoo|shopping_mall|stadium|university|park&sensor=false&key=" + Cnst.GP_KEY;
		   }else{
			    urlString = "https://maps.googleapis.com/maps/api/place/details/json?reference=" + reference + 
				"&sensor=true&key=" + Cnst.GP_KEY;
		}
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
}
