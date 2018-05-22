package beans.sesame;

import java.util.ArrayList;
/**
 * Bean containing a LinkedIn person from Sesame
 * @author Damien Goetschi
 */
public class SesameLinkedInPerson extends SesamePerson {
	/**
	 * Country in which this person live
	 */
	private String country;
	/**
	 * Industry in which this person work
	 */
	private String industry;
	/**
	 * Headline of this person
	 */
	private String headline;
	/**
	 * List of specialties of this person
	 */
	private ArrayList<String> specialities;
	/**
	 * Summary of this person (Like a CV)
	 */
	private String summary;
	/**
	 * List of employments for this person
	 */
	private ArrayList<SesameEmployment> employments;
	
	public SesameLinkedInPerson(){
		specialities=new ArrayList<>();
		employments=new ArrayList<>();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public ArrayList<String> getSpecialities() {
		return specialities;
	}

	public void setSpecialities(ArrayList<String> specialities) {
		this.specialities = specialities;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public ArrayList<SesameEmployment> getEmployements() {
		return employments;
	}

	public void setEmployements(ArrayList<SesameEmployment> employements) {
		this.employments = employements;
	}
	
}
