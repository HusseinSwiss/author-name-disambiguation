package beans;
/**
 * Bean containing a Education of a person get from Freebase (www.freebase.com)
 * @author Damien Goetschi
 */
public class FreebaseEducation {
	/**
	 * ID of the education
	 */
	private String id;
	/**
	 * Institution of the education (School)
	 */
	private String institution;
	/**
	 * Date when the person has started to study here
	 */
	private String start_date;
	/**
	 * Date when the person has finished to study here
	 */
	private String end_date;
	/**
	 * Degree of education
	 */
	private String degree;
	/**
	 * Major field of study for this education
	 */
	private String major_field_of_study;
	/**
	 * Specialization of this education
	 */
	private String specialization;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getInstitution() {
		return institution;
	}
	public void setInstitution(String institution) {
		this.institution = institution;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getEnd_date() {
		return end_date;
	}
	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getMajor_field_of_study() {
		return major_field_of_study;
	}
	public void setMajor_field_of_study(String major_field_of_study) {
		this.major_field_of_study = major_field_of_study;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}

}
