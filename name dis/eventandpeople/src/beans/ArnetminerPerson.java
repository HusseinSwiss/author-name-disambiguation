package beans;



/**
 * Bean containing a Person get from Arnetminer (www.arnetminer.com)
 * @author Hussein Hazimeh
 */
public class ArnetminerPerson {
	
	/**
	 * id of person 
	 * */
	private String id;
	/**
	 * fullname of person
	 * */
	private String fullname;
	/**
	 * phone of person
	 * */
	private String phone;
	/**
	 * fax of person 
	 * */
	private String fax;
	/**
	 * email of person 
	 * */
	private String email;
	/**
	 * url page of person 
	 * */
	private String url;
	/**
	 * position of person 
	 * */
	private String position;
	/**
	 * affiliation of person 
	 * */
	private String affiliation;
	/**
	 * full address of person 
	 * */
	private String address;
	/**
	 * picture url of person 
	 * */
	private String picUrl;
	/**
	 * number of publications of person 
	 * */
	private String publicationCount;
	/**
	 * citation number of person 
	 * */
	private String CitationNum;
	/**
	 * Hindex  of person 
	 * */
	private String Hindex;
	/**
	 * educations of person
	 * */
	private ArnetminerEducation educations;
	
	//auto generated methods
	
	public ArnetminerPerson() {
		this.id = "";
		this.fullname = "";
		this.phone = "";
		this.fax = "";
		this.email = "";
		this.url = "";
		this.position = "";
		this.affiliation = "";
		this.address = "";
		this.picUrl = "";
		this.publicationCount = "";
		this.CitationNum = "";
		this.Hindex = "";
		this.educations = new ArnetminerEducation();
	}

	public ArnetminerEducation getEducations() {
		return educations;
	}

	public void setEducations(ArnetminerEducation educations) {
		this.educations = educations;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPublicationCount() {
		return publicationCount;
	}

	public void setPublicationCount(String publicationCount) {
		this.publicationCount = publicationCount;
	}

	public String getCitationNum() {
		return CitationNum;
	}

	public void setCitationNum(String citationNum) {
		CitationNum = citationNum;
	}

	public String getHindex() {
		return Hindex;
	}

	public void setHindex(String hindex) {
		Hindex = hindex;
	}
}
