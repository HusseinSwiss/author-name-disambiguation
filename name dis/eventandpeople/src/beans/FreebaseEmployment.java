package beans;
/**
 * Bean containing a Employment of a person get from Freebase (www.freebase.com)
 * @author Damien Goetschi
 */
public class FreebaseEmployment {
	/**
	 * ID of the employment
	 */
	private String id;
	/**
	 * Name of employer for this employment
	 */
	private String employer;
	/**
	 * Title of this job
	 */
	private String title;
	/**
	 * Date when the person has started to work here
	 */
	private String from;
	/**
	 * Date when the person has finished to work here
	 */
	private String to;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployer() {
		return employer;
	}
	public void setEmployer(String employer) {
		this.employer = employer;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	
}
