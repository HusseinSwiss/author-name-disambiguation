package beans;
import java.io.Serializable;
/**
 * Bean containing duration and distance from origin to destination
 * (Origin and Destination are the key to the HashMap cache)
 * @author Damien Goetschi
 */
public class SocialDistanceDuration implements Serializable{
	/**
	 * Version (for serializable)
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Time to walk to destination
	 */
	private int durationWalking;
	/**
	 * Time to drive to destination
	 */
	private int durationDriving;
	/**
	 * Distance to walk to destination
	 */
	private int distanceWalking;
	/**
	 * Distance to drive to destination
	 */
	private int distanceDriving;
	
	
	public int getDurationWalking() {
		return durationWalking;
	}
	public void setDurationWalking(int durationWalking) {
		this.durationWalking = durationWalking;
	}
	public int getDurationDriving() {
		return durationDriving;
	}
	public void setDurationDriving(int durationDriving) {
		this.durationDriving = durationDriving;
	}
	public int getDistanceWalking() {
		return distanceWalking;
	}
	public void setDistanceWalking(int distanceWalking) {
		this.distanceWalking = distanceWalking;
	}
	public int getDistanceDriving() {
		return distanceDriving;
	}
	public void setDistanceDriving(int distanceDriving) {
		this.distanceDriving = distanceDriving;
	}
}
