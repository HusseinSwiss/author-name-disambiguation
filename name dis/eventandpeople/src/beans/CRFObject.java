package beans;

public class CRFObject {

	/**
	 * @param args
	 */
	private String PhD;
	private String position;
	private String affiliation;
	private String education_univ;
	
	public String getPhD() {
		return PhD;
	}
	public void setPhD(String phD) {
		PhD = phD;
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
	public String getEducation_univ() {
		return education_univ;
	}
	public void setEducation_univ(String education_univ) {
		this.education_univ = education_univ;
	}
	public String toString(){
		return "PhD : " + getPhD() + "\nposition : " + getPosition() + "\neducation_univ : " + getEducation_univ() ;
	}
}
