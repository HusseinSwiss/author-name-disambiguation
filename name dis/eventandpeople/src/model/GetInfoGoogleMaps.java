package model;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import beans.SocialDistanceDuration;
import beans.SocialEvent;
import beans.SocialPlace;

import com.fasterxml.jackson.core.JsonFactory; 
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import constant.Cnst;
import facebook4j.GeoLocation;
import beans.SocialLocation;
import beans.sesame.SesameLocation;
import static constant.Cnst.*;

/**
 * This class contains statics methods to get information from GoogleMaps
 * @author Damien Goetschi
 */
public class GetInfoGoogleMaps {
	/**
	 * 
	 */
	private final static String REQUEST_URL = "https://maps.googleapis.com/maps/api/";
	/**
	 * Boolean indicate if new data was add into the location cache
	 */
	private static boolean newToWriteLoc = false;
	/**
	 * Boolean indicate if new data was add into the geolocation cache
	 */
	private static boolean newToWriteGeoLoc = false;
	/**
	 * Boolean indicate if new data was add into the distance and duration cache
	 */
	private static boolean newToWriteDistance = false;
	/**
	 * Boolean indicate if new data was add into the autocompletion cache
	 */
	private static boolean newToWriteAutocomplete = false;
	/**
	 * HashMap containing url query and a SocialLocation containing the response
	 */
	public static HashMap<String,SocialLocation> cacheLoc=null;
	/**
	 * HashMap containing url query and a GeoLocation (latitude and longitude) containing the response
	 */
	public static HashMap<String,GeoLocation> cacheGeoLoc=null;
	/**
	 * HashMap containing url query and a SocialDistanceDuration containing the response
	 */
	public static HashMap<String, SocialDistanceDuration> cacheDistance=null;
	/**
	 * HashMap containing url query and a String containing the response
	 */
	public static HashMap<String, String> cacheAutocomplete=null;
	
	/**
	 * This method create a array with distance from origin to all destinations
	 * @param origin GeoLocation content latitude and longitude of origin (location of conference)
	 * @param destinations FbPlace ArrayList content all places to calculate the distance
	 * @return a ArrayList contents the distance and duration for each destination
	 * @throws IOException
	 */
	public static ArrayList<SocialDistanceDuration> getDistanceMatrix(SocialLocation origin, ArrayList<SocialPlace> destinations) throws IOException
	{
		ArrayList<SocialDistanceDuration> result = new ArrayList<SocialDistanceDuration>();
		for(int i = 0;i<destinations.size();i++){
			//for each place in destination ArrayList we get the SocialDistanceDuration and add it into result ArrayList
			result.add(getSocialDistanceDuration(origin, destinations.get(i).getLocation()));
		}
	    return result;
	}
	
	/**
	 * This method create a array with distance from origin to all destinations
	 * @param origin GeoLocation content latitude and longitude of origin (location of conference)
	 * @param destinations FbPlace ArrayList content all places to calculate the distance
	 * @return a ArrayList contents the distance and duration for each destination
	 * @throws IOException
	 */
	public static ArrayList<SocialDistanceDuration> getDistanceMatrixEvent(SocialLocation origin, ArrayList<SocialEvent> destinations) throws IOException
	{
		ArrayList<SocialDistanceDuration> result = new ArrayList<SocialDistanceDuration>();
		for(int i = 0;i<destinations.size();i++){
			//for each event in destination ArrayList we get the SocialDistanceDuration and add it into result ArrayList
			result.add(getSocialDistanceDuration(origin,destinations.get(i).getLocation()));
		}
	    return result;
	}
	
	
	/**
	 * Get the distance and duration for walking and driving form a location to an other
	 * @param origin location of origin (enter in form)
	 * @param destination location of touristic site/event
	 * @return a object containing the duration and distance
	 * @throws IOException
	 */
	public static SocialDistanceDuration getSocialDistanceDuration(SocialLocation origin, SocialLocation destination) throws IOException{
		String urlStringBase = REQUEST_URL + "distancematrix/json?&sensor=false&origins=" + origin.getLatitude()+","+origin.getLongitude() + "&destinations=";
		String urlString = urlStringBase+destination.getLatitude()+","+destination.getLongitude();
		//try to get data from cache
		SocialDistanceDuration sdd = cacheDistance.get(urlString);
		if(sdd==null){
			//if data is not in cache, get information, create the new object and add it to cache
			sdd=new SocialDistanceDuration();
		    int[] dd = getDistance(urlString, MODE_WALKING);
		    sdd.setDistanceWalking(dd[DISTANCE]);
		    sdd.setDurationWalking(dd[DURATION]);
		    dd = getDistance(urlString, MODE_DRIVING);
		    sdd.setDistanceDriving(dd[DISTANCE]);
		    sdd.setDurationDriving(dd[DURATION]);
		    cacheDistance.put(urlString, sdd);
		    newToWriteDistance=true;
		}
		return sdd;
	}
	
	/**
	 * Get distance and duration to a SesameLocation to an other (is distance and duration from and to this place are not in database)
	 * @param origin location of origin (enter in form)
	 * @param destination location of touristic site/event
	 * @return a object containing the duration and distance
	 * @throws IOException
	 */
	public static SocialDistanceDuration getSocialDistanceDuration(SesameLocation origin, SesameLocation destination) throws IOException{
		String urlStringBase = REQUEST_URL + "distancematrix/json?&sensor=false&origins=" + origin.getLatitude()+","+origin.getLongitude() + "&destinations=";
		String urlString = urlStringBase+destination.getLatitude()+","+destination.getLongitude();
		//try to get data from cache
		SocialDistanceDuration sdd = cacheDistance.get(urlString);
		if(sdd==null){
			//if data is not in cache, get information, create the new object and add it to cache
			sdd=new SocialDistanceDuration();
		    int[] dd = getDistance(urlString, MODE_WALKING);
		    sdd.setDistanceWalking(dd[DISTANCE]);
		    sdd.setDurationWalking(dd[DURATION]);
		    dd = getDistance(urlString, MODE_DRIVING);
		    sdd.setDistanceDriving(dd[DISTANCE]);
		    sdd.setDurationDriving(dd[DURATION]);
		    cacheDistance.put(urlString, sdd);
		    newToWriteDistance=true;
		}
		return sdd;
	}
	
	/**
	 * Get distance and duration for a URL and a mode
	 * @param urlString url without mode
	 * @param mode mode for which search distance and duration
	 * @return a array of 2 int with distance and duration
	 * @throws IOException
	 */
	private static int[] getDistance(String urlString,String mode) throws IOException{
		String res="";
		//create full url with mode and get result
		URL url = new URL(urlString+"&mode="+mode);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input;
		while ((input = br.readLine()) != null){
			res+=input;
	    }
	    br.close();
	    //parse result to array for return it
	    return parseDistance(res);
	}
	
	/**
	 * Parse JSON String to a array
	 * @param json the JSON String to parse
	 * @return array of 2 int with distance/duration parsed
	 * @throws IOException
	 */
	private static int[] parseDistance(String json) throws IOException {
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(json);
		int[] res = new int[2];
		tokenNextTo(jp, "elements");
		tokenNextTo(jp, "{");
		jp.nextToken();
		//status is at end of json, so if status is here, there is a error
		if(!jp.getText().equals("status")){
			tokenNextTo(jp, "value");
			res[DISTANCE]=jp.nextIntValue(-1);
			tokenNextTo(jp, "value");
			res[DURATION]=jp.nextIntValue(-1);
		}
		return res;
	}
	
	/**
	 * Found geolocation of a address
	 * @param address address to found geolocation
	 * @return GeoLocation content geolocation of address
	 * @throws IOException
	 */
	public static GeoLocation getGeocode(String address) throws IOException{
		String res="";
		//replace space by url value
		address=address.replaceAll(" ", "%20");
		//remove special characters 
		address = Normalizer.normalize(address, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
		String urlString = REQUEST_URL + "geocode/json?sensor=false&address=" + address;
		GeoLocation gl = cacheGeoLoc.get(urlString);
		if(gl==null){
			URL url = new URL(urlString);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String input;
			while ((input = br.readLine()) != null){
				res+=input;
		    }
		    br.close();
		    gl=parseGeocode(res);
		    cacheGeoLoc.put(urlString, gl);
		    newToWriteGeoLoc=true;
		}
		return gl;
	}
	
	/**
	 * Parse JSON geocode to GeoLocation
	 * @param json the JSON String to parse
	 * @return GeoLocation content geolocation content in JSON
	 * @throws IOException
	 */
	private static GeoLocation parseGeocode(String json) throws IOException{
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(json);
		double lat=0,lng=0;
		tokenNextTo(jp, "location");
		tokenNextTo(jp, "lat");
		jp.nextToken();
		lat=jp.getDoubleValue();
		tokenNextTo(jp, "lng");
		jp.nextToken();
		lng=jp.getDoubleValue();
		return new GeoLocation(lat, lng);
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
	
	/**
	 * Complete a location by query Google Geocode API
	 * @param name name of event/place or address
	 * @return the completed location
	 * @throws IOException
	 * Usable
	 */
	public static SocialLocation completeLocation(String name) throws IOException {
		String res="";
		//replace space by url value
		name=name.replaceAll(" ", "%20");
		//remove special characters 
		name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
		String urlString = REQUEST_URL + "geocode/json?sensor=false&address=" + name;
		//try to get data from cache
		SocialLocation l = cacheLoc.get(urlString);
		if(l==null){
			//if data is not in cache, get information, create the new object and add it to cache
			URL url = new URL(urlString);
			HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String input;
			while ((input = br.readLine()) != null){
				res+=input;
		    }
		    br.close();
		    //parse json String to a SocialLocation object
		    l = parseCompleteLocation(res);
		    cacheLoc.put(name, l);
		    newToWriteLoc=true;
		}
		return l;
	}
	
	/**
	 * Complete a location by query Google Geocode API
	 * @param name name of event or place
	 * @param l location (imcomplete)
	 * @return the completed location
	 * @throws IOException
	 */
	public static SocialLocation completeLocation(String name, SocialLocation l) throws IOException {
		String req = "";
		//create query String with name and/or location
		if(!l.latLngToString().equals("-")){
			req=l.latLngToString();
		}else{
			req=name+" "+l;
		}
		return completeLocation(req);
	}
	
	/**
	 * Parse json String containing all information about the location
	 * @param json Json String
	 * @return a SocialLocation with data from JSON String
	 * @throws IOException
	 */
	private static SocialLocation parseCompleteLocation(String json) throws IOException{
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(json);
		SocialLocation l = new SocialLocation();
		if(tokenNextTo(jp, "address_components")!=null){
			String res="";
			String name="";
			//part until "geometry" of this JSON string content a long_name element, a short_name element and a type element
			while(jp.nextToken()!=null&&!(res=jp.getText()).equals("geometry")){
				switch(res){
					case "long_name":
						//stock long_name value
						name=jp.nextTextValue();
						break;
					case "types":
						//stock value in field by type
						jp.nextToken();
						String type = jp.nextTextValue();
						if(type!=null){
							switch(type){
								case "locality":
									l.setCity(name);
									break;
								case "country":
									l.setCountry(name);
									break;
								case "postal_code":
									l.setZip(name);
									break;
								case "street_number":
									l.setStreet(name);
									break;
								case "route":
									l.setStreet(name+" "+l.getStreet());
									break;
								case "establishment":
									l.setStreet(name);
									break;
							}
						}
						break;
				}
			}
			//get lat and lng in location part
			tokenNextTo(jp, "location");
			tokenNextTo(jp, "lat");
			jp.nextToken();
			l.setLatitude(jp.getDoubleValue());
			tokenNextTo(jp, "lng");
			jp.nextToken();
			l.setLongitude(jp.getDoubleValue());
		}
		l.createID();
		return l;
	}
	
	/**
	 * This methode write the LocationCache ArrayList into a file if there are new data in it
	 */
	public static void writeLocationsCache(){
		if(newToWriteLoc){
		    try {
		      FileOutputStream fichier = new FileOutputStream(PATH+"\\"+CACHE_LOCATIONS);
		      ObjectOutputStream oos = new ObjectOutputStream(fichier);
		      oos.writeObject(cacheLoc);
		      oos.flush();
		      oos.close();
		      newToWriteLoc=false;
		    }
		    catch (java.io.IOException e) {
		      e.printStackTrace();
		    }
		}
	}
	
	/**
	 * This methode write the GeoLocationCache ArrayList into a file if there are new data in it
	 */
	public static void writeGeoLocationCache(){
		if(newToWriteGeoLoc){
		    try {
		      FileOutputStream fichier = new FileOutputStream(PATH+"\\"+CACHE_GEOLOCATIONS);
		      ObjectOutputStream oos = new ObjectOutputStream(fichier);
		      oos.writeObject(cacheGeoLoc);
		      oos.flush();
		      oos.close();
		      newToWriteGeoLoc=false;
		    }
		    catch (java.io.IOException e) {
		      e.printStackTrace();
		    }
		}
	}
	
	/**
	 * This methode write the DistanceCache ArrayList into a file if there are new data in it
	 */
	public static void writeDistancesCache(){
		if(newToWriteDistance){
		    try {
		      FileOutputStream fichier = new FileOutputStream(PATH+"\\"+CACHE_DISTANCES);
		      ObjectOutputStream oos = new ObjectOutputStream(fichier);
		      oos.writeObject(cacheDistance);
		      oos.flush();
		      oos.close();
		      newToWriteDistance=false;
		    }
		    catch (java.io.IOException e) {
		      e.printStackTrace();
		    }
		}
	}
	
	/**
	 * Get a String with automcompletion proposition
	 * @param search begin of street write in form
	 * @param gl location of city in with search the proposition
	 * @return a String with proposition separed by |
	 * @throws IOException
	 */
	public static String getAutocomplete(String search, GeoLocation gl) throws IOException{
		//create url for seach
		String urlString = "https://maps.googleapis.com/maps/api/place/autocomplete/json?types=geocode&sensor=false";
		// replace space by url value
		search=search.replace(" ", "%20");
		// add street search, google api key and location of city to query
		urlString+="&input="+search+"&key="+Cnst.GOOGLE_KEY;
		urlString+="&location="+gl.getLatitude()+","+gl.getLongitude()+"&radius="+Cnst.FS_RANGE;
		//check if search data is in cache
		if(cacheAutocomplete.get(urlString)!=null)
			return cacheAutocomplete.get(urlString);
		//if data is not in cache send query and parse result to add it in cache and return
		newToWriteAutocomplete=true;
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String input,res="";
		while ((input = br.readLine()) != null){
			res+=input;
	    }
	    br.close();
	    String result=parseAutocomplete(res);
	    cacheAutocomplete.put(urlString, result);
		return result;
	}
	
	/**
	 * This methode write the AutocompleteCache ArrayList into a file if there are new data in it
	 */
	public static void writeAutocompleteCache(){
		if(newToWriteAutocomplete){
		    try {
		      FileOutputStream fichier = new FileOutputStream(PATH+"\\"+CACHE_AUTOCOMPLETE);
		      ObjectOutputStream oos = new ObjectOutputStream(fichier);
		      oos.writeObject(cacheAutocomplete);
		      oos.flush();
		      oos.close();
		      newToWriteAutocomplete=false;
		    }
		    catch (java.io.IOException e) {
		      e.printStackTrace();
		    }
		}
	}
	
	/**
	 * Parse a JSON string containing location proposition
	 * @param json JSON string to parse
	 * @return a String with all proposition separed by |
	 * @throws IOException
	 */
	public static String parseAutocomplete(String json) throws IOException{
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(json);
		String s="";
		//description containing a String with street, city and country
		while(tokenNextTo(jp, "description")!=null){
			jp.nextToken();
			if(!s.equals(""))
				s+="|";
			s+=jp.getText();
		}
		return s;
	}

}