package beans;

import java.util.ArrayList;

/**
 * Bean containing a Person get from LinkedIn (www.linkedin.com)
 * @author Damien Goetschi
 */
public class LinkedInPerson {
	/**
	 * LinkedIn URL for this person
	 */
	private String url;
	/**
	 * ID of the person
	 */
	private String id;
	
	/**
	 * First Name of this person
	 */
	private String firstName;
	/**
	 * Last Name of this person
	 */
	private String lastName;
	/**
	 * Name of this person (with first name and last name)
	 */
	private String formattedName;
	/**
	 * Headline of this person
	 */
	private String headline;
	
	/**
	 * Country in which this person live
	 */
	private String country;
	/**
	 * Location in which this person is
	 */
	private String locationName;
	/**
	 * Industry in which this person work
	 */
	private String industry;
	/**
	 * Summary of this person (Like a CV)
	 */
	private String summary;
	/**
	 * List of positions of this person
	 */
	private ArrayList<LinkedInPosition> positions;
	/**
	 * Specialties of this person
	 */
	private String specialties;
	
	/**
	 * URL to picture of the person
	 */
	private String picture;
	
	
	public LinkedInPerson(){
		positions=new ArrayList<>();
	}
	
	public LinkedInPerson(String url){
		positions=new ArrayList<>();
		this.url=url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public ArrayList<LinkedInPosition> getPositions() {
		return positions;
	}

	public void setPositions(ArrayList<LinkedInPosition> positions) {
		this.positions = positions;
	}
	
	public void addPosition(LinkedInPosition position) {
		positions.add(position);
	}

	public String getSpecialties() {
		return specialties;
	}

	public void setSpecialties(String specialties) {
		this.specialties = specialties;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
}
