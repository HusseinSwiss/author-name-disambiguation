package beans;

/**
 * Position of a Person from LinkedIn
 * @author Damien Goetschi
 */
public class LinkedInPosition {
	/**
	 * ID of this position
	 */
	private int id;
	/**
	 * Company in which is this position
	 */
	private LinkedInCompany company;
	/**
	 * Begin year of position
	 */
	private int startYear;
	/**
	 * End year of position
	 */
	private int endYear;
	/**
	 * Boolean if this position is current or not
	 */
	private boolean current;
	/**
	 * Summary of this position
	 */
	private String summary;
	/**
	 * Title of this position
	 */
	private String title;
	


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LinkedInCompany getCompany() {
		return company;
	}

	public void setCompany(LinkedInCompany company) {
		this.company = company;
	}

	public int getStartYear() {
		return startYear;
	}

	public void setStartYear(int startYear) {
		this.startYear = startYear;
	}

	public int getEndYear() {
		return endYear;
	}

	public void setEndYear(int endYear) {
		this.endYear = endYear;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setCurrent(boolean current) {
		this.current = current;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
