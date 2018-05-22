package beans.sesame;

import org.openrdf.model.URI;

/**
 * Bean containing a Eventful Event get from sesame
 * extends SesameEvent by adding information containing on Eventful
 * @author  Hussein
 *
 */
public class SesameEventfulEvent extends SesameEvent{
	/**
	 * Eventful ID of this event
	 */
	private String id;
	/**
	 * Number of people invited to this event
	 */
	private int invitesCount;
	/**
	 * Number of people attending for this event
	 */
	private int attendingCount;
	
	/**
	 * Constructor of Eventful event
	 * @param uri URI of this event on Sesame
	 */
	public SesameEventfulEvent(URI uri) {
		super(uri);
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getInvitesCount() {
		return invitesCount;
	}
	public void setInvitesCount(int invitesCount) {
		this.invitesCount = invitesCount;
	}
	public int getAttendingCount() {
		return attendingCount;
	}
	public void setAttendingCount(int attendingCount) {
		this.attendingCount = attendingCount;
	}
	
}


