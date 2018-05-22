package beans;

public class LinkedinHTMLProfile {
	
//	public LinkedinHTMLProfile() {
//		super();
//		this.locality = "a";
//		this.publication = "a";
//		this.education = "a";
//		this.summary = "a";
//		this.first_name = "a";
//		this.last_name = "a";
//		this.organization = "a";
//		this.image = "a";
//	}
	private String locality;
	private String publication;
	private String education;
	private String summary;
	private String first_name;
	private String last_name;
	private String organization;
	private String image;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	
	public String getLocality() {
		return locality;
	}
	public void setLocality(String locality) {
		this.locality = locality;
	}
	public String getPublication() {
		return publication;
	}
	public void setPublication(String publication) {
		this.publication = publication;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
}
