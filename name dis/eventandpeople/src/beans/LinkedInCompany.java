package beans;

/**
 * Bean containing a Company for a Position of LinkedInPerson
 * @author Damien Goetschi
 */
public class LinkedInCompany {
	/**
	 * ID of company
	 */
	private int id;
	/**
	 * Industry in which the company work
	 */
	private String industry;
	/**
	 * Name of the company
	 */
	private String name;
	/**
	 * Size of company
	 */
	private String size;
	/**
	 * Type of company
	 */
	private String type;
	
	public LinkedInCompany(){
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIndustry() {
		return industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
}
