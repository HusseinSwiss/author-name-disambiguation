package beans.sesame;

import org.openrdf.model.URI;

/**
 * Bean containing a Google Place get from sesame
 * extends SesamePlace by adding information containing on Google
 * @author Hussein
 *
 */
public class SesameGooglePlace extends SesamePlace{

	/**
	 * Number of people like this page on Google
	 */
	private int likeCount;
	/**
	 * Number of people said that they where here on Google
	 */
	private int wereHereCount;
	/**
	 * Google ID of this place
	 */
	private String id;
	
	/**
	 * Constuctor of Google place
	 * @param uri URI of this place on Sesame
	 */
	public SesameGooglePlace(URI uri) {
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

