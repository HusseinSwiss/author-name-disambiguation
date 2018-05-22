package beans;

import java.util.ArrayList;
import java.util.Date;

/**
 * Bean containing a Person get from Freebase (www.freebase.com)
 * @author Damien Goetschi
 */
public class FreebasePerson {
	/**
	 * ID of the person
	 */
	private String id;
	/**
	 * URL of freebase page
	 */
	private String url;
	/**
	 * Name of the person
	 */
	private String name;
	/**
	 * Birth date of the person
	 */
	private String birthDate;
	/**
	 * Birth place of the person
	 */
	private String birthPlace;
	/**
	 * List of nationalities of the person
	 */
	private ArrayList<String> nationalities;
	/**
	 * Gender of the person 
	 */
	private String gender;
	/**
	 * List of religions of the person
	 */
	private ArrayList<String> religion;
	/**
	 * List of professions of the person
	 */
	private ArrayList<String> professions;
	/**
	 * List of ethnicities of the person
	 */
	private ArrayList<String> ethnicity;
	/**
	 * List of quotations of the person
	 */
	private ArrayList<String> quotations;
	/**
	 * URL to picture of the person
	 */
	private String picture;
	
	/**
	 * List of employment of the person
	 */
	private ArrayList<FreebaseEmployment> employments;
	/**
	 * List of educations of the person
	 */
	private ArrayList<FreebaseEducation> educations;
	
	public FreebasePerson(){
		nationalities=new ArrayList<>();
		professions=new ArrayList<>();
		religion=new ArrayList<>();
		ethnicity=new ArrayList<>();
		employments = new ArrayList<>();
		educations = new ArrayList<>();
		quotations = new ArrayList<>();
	}
	

	public ArrayList<FreebaseEmployment> getEmployments() {
		return employments;
	}

	public void setEmployments(ArrayList<FreebaseEmployment> employments) {
		this.employments = employments;
	}
	
	public void addEmployment(FreebaseEmployment employment) {
		this.employments.add(employment);
	}
	
	public ArrayList<String> getQuotations() {
		return quotations;
	}

	public void setQuotations(ArrayList<String> quotations) {
		this.quotations = quotations;
	}
	
	public void addQuotation(String quotation) {
		this.quotations.add(quotation);
	}
	
	public ArrayList<FreebaseEducation> getEducations() {
		return educations;
	}

	public void setEducations(ArrayList<FreebaseEducation> educations) {
		this.educations = educations;
	}
	
	public void addEducation(FreebaseEducation education) {
		this.educations.add(education);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		setUrl("http://www.freebase.com"+id);
		setPicture("https://www.googleapis.com/freebase/v1/image/"+id+"?maxwidth=150&maxheight=150&errorid=%2Ffreebase%2Fno_image_png");
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

	public ArrayList<String> getNationalities() {
		return nationalities;
	}

	public void setNationalities(ArrayList<String> nationalities) {
		this.nationalities = nationalities;
	}
	
	public void addNationality(String nationality) {
		this.nationalities.add(nationality);
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public ArrayList<String> getProfessions() {
		return professions;
	}

	public void setProfessions(ArrayList<String> professions) {
		this.professions = professions;
	}
	
	public void addProfession(String profession) {
		this.professions.add(profession);
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public ArrayList<String> getReligion() {
		return religion;
	}

	public void setReligion(ArrayList<String> religion) {
		this.religion = religion;
	}
	
	public void addReligion(String religion) {
		this.religion.add(religion);
	}

	public ArrayList<String> getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(ArrayList<String> ethnicity) {
		this.ethnicity = ethnicity;
	}
	
	public void addEthnicity(String ethnicity) {
		this.ethnicity.add(ethnicity);
	}
	
	
	
}
