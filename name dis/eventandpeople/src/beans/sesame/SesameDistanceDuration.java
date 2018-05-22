package beans.sesame;

import org.openrdf.model.URI;

/**
 * Bean containing a DistanceDuration get from sesame
 * @author Damien Goetschi
 *
 */
public class SesameDistanceDuration extends SesameBean {
	/**
	 * Location indicate from where the calcul is
	 */
	private SesameLocation from;
	/**
	 * Distance for driving (in meters)
	 */
	private int driveDistance;
	/**
	 * Duration for driving (in seconds)
	 */
	private int driveDuration;
	/**
	 * Distance for driving (in walking)
	 */
	private int walkDistance;
	/**
	 * Duration for walking (in seconds)
	 */
	private int walkDuration;
	
	/**
	 * Constructor for distance and duration
	 * @param uri URI of this object on Sesame
	 */
	public SesameDistanceDuration(URI uri) {
		super(uri);
	}

	public SesameLocation getFrom() {
		return from;
	}
	public void setFrom(SesameLocation from) {
		this.from = from;
	}
	public int getDriveDistance() {
		return driveDistance;
	}
	public void setDriveDistance(int driveDistance) {
		this.driveDistance = driveDistance;
	}
	public int getDriveDuration() {
		return driveDuration;
	}
	public void setDriveDuration(int driveDuration) {
		this.driveDuration = driveDuration;
	}
	public int getWalkDistance() {
		return walkDistance;
	}
	public void setWalkDistance(int walkDistance) {
		this.walkDistance = walkDistance;
	}
	public int getWalkDuration() {
		return walkDuration;
	}
	public void setWalkDuration(int walkDuration) {
		this.walkDuration = walkDuration;
	}
}
