package beans.sesame;

/**
 * Bean containing a education from Sesame
 * @author Damien Goetschi
 */
public class SesameEducation {
	/**
	 * ID of education
	 */
	private String id;
	/**
	 * Institution in which this education is
	 */
	private String institution;
	/**
	 * Degree of study
	 */
	private String degree;
	/**
	 * Date when person start to study here
	 */
	private String start;
	/**
	 * Date when person finish to study here
	 */
	private String end;
	/**
	 * Specialization of this education
	 */
	private String specialization;
	/**
	 * Major field of study for this education
	 */
	private String majorFieldOfStudy;
	
	
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
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getSpecialization() {
		return specialization;
	}
	public void setSpecialization(String specialization) {
		this.specialization = specialization;
	}
	public String getMajorFieldOfStudy() {
		return majorFieldOfStudy;
	}
	public void setMajorFieldOfStudy(String majorFieldOfStudy) {
		this.majorFieldOfStudy = majorFieldOfStudy;
	}

}
