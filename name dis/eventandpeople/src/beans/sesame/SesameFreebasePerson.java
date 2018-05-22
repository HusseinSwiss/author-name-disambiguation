package beans.sesame;

import java.util.ArrayList;
/**
 * Bean containing a Freebase person from Sesame
 * @author Damien Goetschi
 */
public class SesameFreebasePerson extends SesamePerson {
	/**
	 * Birth date of this person
	 */
	private String birthDate;
	/**
	 * Birth place of this person
	 */
	private String birthPlace;
	/**
	 * Gender of this person
	 */
	private String gender;
	/**
	 * List of nationalities of this person
	 */
	private ArrayList<String> nationalities;
	/**
	 * List of religions of this person
	 */
	private ArrayList<String> religions;
	/**
	 * List of professions of this person
	 */
	private ArrayList<String> professions;
	/**
	 * List of ethnicities of this person
	 */
	private ArrayList<String> ethnicities;
	/**
	 * List of quotations of this person
	 */
	private ArrayList<String> quotations;
	/**
	 * List of employments of this person
	 */
	private ArrayList<SesameEmployment> employments;
	/**
	 * List of educations of this person
	 */
	private ArrayList<SesameEducation> educations;
	
	public SesameFreebasePerson(){
		nationalities=new ArrayList<>();
		religions=new ArrayList<>();
		professions=new ArrayList<>();
		ethnicities=new ArrayList<>();
		quotations=new ArrayList<>();
		employments=new ArrayList<>();
		educations=new ArrayList<>();
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getBirthPlace() {
		return birthPlace;
	}

	public void setBirthPlace(String birthPlace) {
		this.birthPlace = birthPlace;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public ArrayList<String> getNationalities() {
		return nationalities;
	}

	public void setNationalities(ArrayList<String> nationalities) {
		this.nationalities = nationalities;
	}

	public ArrayList<String> getReligions() {
		return religions;
	}

	public void setReligions(ArrayList<String> religions) {
		this.religions = religions;
	}

	public ArrayList<String> getProfessions() {
		return professions;
	}

	public void setProfessions(ArrayList<String> professions) {
		this.professions = professions;
	}

	public ArrayList<String> getEthnicities() {
		return ethnicities;
	}

	public void setEthnicities(ArrayList<String> ethnicities) {
		this.ethnicities = ethnicities;
	}

	public ArrayList<String> getQuotations() {
		return quotations;
	}

	public void setQuotations(ArrayList<String> quotations) {
		this.quotations = quotations;
	}

	public ArrayList<SesameEmployment> getEmployements() {
		return employments;
	}

	public void setEmployements(ArrayList<SesameEmployment> employements) {
		this.employments = employements;
	}

	public ArrayList<SesameEducation> getEducations() {
		return educations;
	}

	public void setEducations(ArrayList<SesameEducation> educations) {
		this.educations = educations;
	}
	
	
}
