package beans.sesame;

import java.util.ArrayList;
import java.util.Date;

import org.openrdf.model.URI;

/**
 * Bean containing a Event get from sesame
 * @author Damien Goetschi
 *
 */
public class SesameEvent extends SesameBean{
	/**
	 * Name of this event
	 */
	private String name;
	/**
	 * Description of this event (can be null)
	 */
	private String description;
	/**
	 * Link to Facebook event page or website
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
	 * Date when this event begins (can be null)
	 */
	private Date start;
	/**
	 * Date when this event ends (can be null)
	 */
	private Date end;
	
	/**
	 * Constructor of the event (initialize the ArrayList)
	 * @param uri URI of this event in Sesame
	 */
	public SesameEvent(URI uri) {
		super(uri);
		bigPic=new ArrayList<String>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public void setBigPic(ArrayList<String> bigPic) {
		this.bigPic = bigPic;
	}
}
