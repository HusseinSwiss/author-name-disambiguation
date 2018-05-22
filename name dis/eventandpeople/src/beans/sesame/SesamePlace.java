package beans.sesame;

import java.util.ArrayList;

import org.openrdf.model.URI;

/**
 * Bean containing a Place get from sesame
 * @author Damien Goetschi
 *
 */
public class SesamePlace extends SesameBean{
	/**
	 * Category of this place (Touristic site, restaurant or hotel)
	 */
	private String topLevelCat;
	/**
	 * Specific type of this place
	 */
	private String type;
	/**
	 * Name of this place
	 */
	private String name;
	/**
	 * Link to Facebook page, Foursquare page  or website
	 */
	private String link;
	/**
	 * Link the a small picture
	 */
	private String smallPic;
	/**
	 * List of link of big picture for this place
	 */
	private ArrayList<String> bigPic;
	
	/**
	 * Constructor of the place (initialize the ArrayList)
	 * @param uri URI of this place in Sesame
	 */
	public SesamePlace(URI uri) {
		super(uri);
		bigPic=new ArrayList<String>();
	}
	public String getTopLevelCat() {
		return topLevelCat;
	}
	public void setTopLevelCat(String topLevelCat) {
		this.topLevelCat = topLevelCat;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getSmallPic() {
		return smallPic;
	}
	public void setSmallPic(String smallPic) {
		this.smallPic = smallPic;
	}
	public ArrayList<String> getBigPic() {
		return bigPic;
	}
	public void addBigPic(String pic) {
		this.bigPic.add(pic);
	}
	public void setBigPic(ArrayList<String> bigPic) {
		this.bigPic = bigPic;
	}
	
}
