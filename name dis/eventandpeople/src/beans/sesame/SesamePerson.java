package beans.sesame;
/**
 * Bean containing a person from Sesame
 * @author Damien Goetschi
 */
public class SesamePerson {
	/**
	 * ID of person
	 */
	private String id;
	/**
	 * URL of person on the website (LinkedIn or Freebase)
	 */
	private String url;
	/**
	 * URL to picture of person
	 */
	private String picture;
	/**
	 * Name of this person
	 */
	private String name;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
