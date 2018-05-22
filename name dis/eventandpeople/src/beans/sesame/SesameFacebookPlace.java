package beans.sesame;

import org.openrdf.model.URI;

/**
 * Bean containing a Facebook Place get from sesame
 * extends SesamePlace by adding information containing on Facebook
 * @author Damien Goetschi
 *
 */
public class SesameFacebookPlace extends SesamePlace{
	/**
	 * Number of people like this page on Facebook
	 */
	private int likeCount;
	/**
	 * Number of people said that they where here on Facebook
	 */
	private int wereHereCount;
	/**
	 * Facebook ID of this place
	 */
	private String id;
	
	/**
	 * Constuctor of Facebook place
	 * @param uri URI of this place on Sesame
	 */
	public SesameFacebookPlace(URI uri) {
		super(uri);
	}
	
	public int getLikeCount() {
		return likeCount;
	}
	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}
	public int getWereHereCount() {
		return wereHereCount;
	}
	public void setWereHereCount(int wereHereCount) {
		this.wereHereCount = wereHereCount;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
