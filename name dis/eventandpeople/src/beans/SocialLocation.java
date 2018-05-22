package beans;

import static constant.Cnst.*;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import model.GetInfoGoogleMaps;
import constant.CnstMethods;
import facebook4j.Event.Venue;
import facebook4j.Place.Location;
/**
 * Bean containing a location for a place or a event
 * @author Damien Goetschi
 */
public class SocialLocation implements Serializable{
	/**
	 * Version (for serializable)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Street with number
	 */
	private String street;
	/**
	 * ZIP Code of the City
	 */
	private String zip;
	/**
	 * Name of the city
	 */
	private String city;
	/**
	 * Country
	 */
	private String country;
	
	/**
	 * Latitude of this location
	 */
	private double latitude;
	/**
	 * Longitude of this location
	 */
	private double longitude;
	/**
	 * Location ID (hash MD5 with street and city)
	 */
	private String id;
	
	/**
	 * Constructor of a SocialLocation from a Venue(facebook4j.Event.Venue)
	 * @param v the Facebook Venue
	 */
	public SocialLocation(Venue v){
		this();
		if(v!=null){
			if(v.getStreet()!=null)this.street=v.getStreet();
			if(v.getCity()!=null)this.city=v.getCity();
			if(v.getCountry()!=null)this.country=v.getCountry();
			this.longitude=v.getLongitude();
			this.latitude=v.getLatitude();
		}
		
	}
	
	/**
	 * Constructor of a SocialLocation from a Location(facebook4j.Place.Location)
	 * @param l the Facebook Location 
	 */
	public SocialLocation(Location l){
		this();
		if(l!=null){
			if(l.getStreet()!=null)this.street=l.getStreet();
			if(l.getCity()!=null)this.city=l.getCity();
			if(l.getCountry()!=null)this.country=l.getCountry();
			this.longitude=l.getLongitude();
			this.latitude=l.getLatitude();
			if(l.getZip()!=null)this.zip=l.getZip();
		}
	}
	
	/**
	 * Constructor of SocialLocation without parameters
	 */
	public SocialLocation(){
		street="";
		city="";
		country="";
		zip="";
		longitude=ERROR;
		latitude=ERROR;
	}
	
	/**
	 * toString method return a formated address
	 */
	public String toString(){
		String s ="";
		if(!street.equals(""))
			s+=street+", ";
		if(!zip.equals(""))
			s+=zip+" ";
		if(!city.equals(""))
			s+=city;
		if(!country.equals("")){
			s+=", "+country;
		}
		return s;
	}
	
	/**
	 * Return a formated gps location
	 * @return a String content the latitude and longitude (lat , lng)
	 */
	public String latLngToString(){
		if(latitude!=ERROR&&longitude!=ERROR)
			return latitude+" , "+longitude;
		else
			return "-";
	}

	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getId() {
		return id;
	}
	public void setID(String id) {
		this.id=id;
	}
	
	/**
	 * Create a ID for the location (for uri in sesame)
	 */
	public void createID(){
		id=CnstMethods.MD5(street+city);
	}
	
}
