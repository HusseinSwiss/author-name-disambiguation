package beans;

import java.util.ArrayList;

import constant.Cnst;
import constant.CnstMethods;

/**
 * Bean containing a place from Facebook, Foursquare or custom
 * @author Damien Goetschi
 */
public class SocialPlace {
	
	 public String test;
	/**
	 * ID of the page
	 */
	private String id;
	/**
	 * From where the data is (Facebook, Foursquare or Custom)
	 */
	private String site;
	/**
	 * Category of place (Hotel, Restaurant or TouristicSite)
	 */
	private String topLevelCat;
	/**
	 * Name of the place
	 */
	private String name;
	/**
	 * Type of the place (more specific than category)
	 */
	private String type;
	/**
	 * Number of like 
	 */
	private int likeCount;
	/**
	 * Location containing street, city, country and latitude/longitude
	 */
	private SocialLocation location;
	private String loc;
	/**
	 * Number of people that said they were here
	 */
	private int wereHereCount;
	/**
	 * URL of Facebook Page, Foursquare Page or Custom
	 */
	private String url;
	/**
	 * Link to a small picture (50px x 50px)
	 */
	private String smallPic;
	/**
	 * List of lLink to large pictures (original size)
	 */
	private ArrayList<String> largePic;
	/**
	 * Geo location of aplace
	 */
	private String geocode;
	
	
	/**
	 * Constructor of the bean with basic data from Facebook
	 * @param id
	 * @param name
	 * @param location
	 */
	public SocialPlace(String id, String name, SocialLocation location,String url) {
		this.id = id;
		this.name = name;
		this.location = location;
		this.url=url;
		type="";
		smallPic="";
		largePic=new ArrayList<String>();
		topLevelCat=Cnst.TYPE_TOURISTIC_SITE;
		site=Cnst.SITE_CUSTOM;
	}
	
	/**
	 * Constructor without parameters
	 */
	public SocialPlace() {
		id="";
		name="";
		location=new SocialLocation();
		url="";
		type="";
		smallPic="";
		largePic=new ArrayList<String>();
		topLevelCat=Cnst.TYPE_TOURISTIC_SITE;
		site=Cnst.SITE_CUSTOM;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getLikeCount() {
		return likeCount;
	}
	
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public SocialLocation getLocation() {
		return location;
	}
	public String getLoc(){
		return this.loc;
	}
	
	public void setLoc(String loc){
		this.loc = loc;
	}

	public void setLocation(SocialLocation location) {
		this.location = location;
	}

	public int getWereHereCount() {
		return wereHereCount;
	}

	public void setWereHereCount(int wereHereCount) {
		this.wereHereCount = wereHereCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getSmallPic() {
		return smallPic;
	}

	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}

	public ArrayList<String> getLargePic() {
		return largePic;
	}

	public void setLargePic(ArrayList<String> largePic) {
		this.largePic = largePic;
	}
	
	public void addLargePic(String oneLargePic){
		largePic.add(oneLargePic);
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getTopLevelCat() {
		return topLevelCat;
	}

	public void setTopLevelCat(String topLevelCat) {
		this.topLevelCat = topLevelCat;
	}
	
	public void setGeocode(String geocode){
		this.geocode = geocode;
	}
	
	public String getGeocode(){
		return this.geocode;
	}
	
	/**
	 * Create a ID if the place is added manually by a user
	 */
	public void createID(){
		id=CnstMethods.MD5(name+location.getStreet());
	}
}
