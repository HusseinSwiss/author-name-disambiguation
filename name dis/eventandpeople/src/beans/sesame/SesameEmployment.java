package beans.sesame;

/**
 * Bean containing a employment from Sesame
 * @author Damien Goetschi
 */
public class SesameEmployment {
	/**
	 * Id of employment
	 */
	private String id;
	/**
	 * Title of this employment
	 */
	private String title;
	/**
	 * Name of employer
	 */
	private String employer;
	/**
	 * Date when person start to work here
	 */
	private String start;
	/**
	 * Date when person finish to work here
	 */
	private String end;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getEmployer() {
		return employer;
	}
	public void setEmployer(String employer) {
		this.employer = employer;
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
	
	
}
