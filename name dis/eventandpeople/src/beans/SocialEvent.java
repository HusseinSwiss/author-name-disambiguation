package beans;

import java.util.ArrayList;
import java.util.Date;

import constant.Cnst;
import constant.CnstMethods;

/**
 * Bean containing a event from Facebook or custom
 * @author Damien Goetschi
 */

public class SocialEvent {
	/**
	 * ID of the event
	 */
	private String id;
	/**
	 * name of the event
	 */
	private String name;
	/**
	 * description of the event
	 */
	private String description;
	/**
	 * start date/hour of the event
	 */
	private Date start;
	/**
	 * end date/hour of the event
	 */
	private Date end;
	/**
	 * location of the event
	 */
	private SocialLocation location;
	/**
	 * a String containing the location of event (not the same of location)
	 */
	private String locationString;
	/**
	 * numbers of invites of the event
	 */
	private int invites;
	/**
	 * number of people who accepting the invitation
	 */
	private int attending;
	/**
	 * link to event on Facebook
	 */
	private String url;
	/**
	 * Link to a small picture (50px x 50px)
	 */
	private String smallPic;
	/**
	 * Link to a large picture (original size)
	 */
	private ArrayList<String> largePic;
	/**
	 * The site from where the data is
	 */
	private String site;
	
	
	/**
	 * Constructor of the bean with params get from facebook
	 * @param id 
	 * @param name
	 * @param start
	 * @param end
	 * @param location
	 * @param locationString
	 * @param description
	 * @param url
	 */
	public SocialEvent(String id, String name, Date start,
			Date end, SocialLocation location, String locationString, String description,String url) {
		this.id = id;
		this.name = name;
		this.start = start;
		this.end = end;
		this.location = location;
		this.locationString = locationString;
		this.description = description;
		this.url=url;
		smallPic="";
		largePic=new ArrayList<String>();
		site=Cnst.SITE_CUSTOM;
	}
	
	/**
	 * Constructor without parameters
	 */
	public SocialEvent(){
		id="";
		name="";
		description="";
		location=new SocialLocation();
		locationString="";
		url="";
		smallPic="";
		largePic=new ArrayList<String>();
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
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public SocialLocation getLocation() {
		return location;
	}
	public void setLocation(SocialLocation location) {
		this.location = location;
	}
	public String getLocationString() {
		return locationString;
	}
	public void setLocationString(String locationString) {
		this.locationString = locationString;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getInvites() {
		return invites;
	}
	public void setInvites(int invites) {
		this.invites = invites;
	}
	public int getAttending() {
		return attending;
	}
	public void setAttending(int attending) {
		this.attending = attending;
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
	
	 
	
	/**
	 * Create a ID if the event is added manually by a user
	 */
	public void createID(){
		if(start==null)
			id=CnstMethods.MD5(name+location.getStreet());
		else
			id=CnstMethods.MD5(name+location.getStreet()+start);
	}
	
}
