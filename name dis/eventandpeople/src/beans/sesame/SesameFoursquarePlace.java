package beans.sesame;

import org.openrdf.model.URI;

/**
 * Bean containing a Foursqaure Place get from sesame
 * extends SesamePlace by adding information containing on Foursquare
 * @author Damien Goetschi
 *
 */
public class SesameFoursquarePlace extends SesamePlace{
	/**
	 * Number of people like this page on Foursquare
	 */
	private int likeCount;
	/**
	 * Number of people said that they where here on Foursquare
	 */
	private int wereHereCount;
	/**
	 * Foursquare ID of this place
	 */
	private String id;
	
	/**
	 * Constuctor of Foursquare place
	 * @param uri URI of this place on Sesame
	 */
	public SesameFoursquarePlace(URI uri) {
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
