package beans.sesame;

import java.util.ArrayList;

import org.openrdf.model.URI;

/**
 * Bean containing a Location get from sesame
 * @author Damien Goetschi
 *
 */
public class SesameLocation extends SesameBean{
	/**
	 * latitude of this location
	 */
	private double latitude;
	/**
	 * longitude of this location
	 */
	private double longitude;
	/**
	 * street with number
	 */
	private String street;
	/**
	 * List of places at this location
	 */
	private ArrayList<SesamePlace> places;
	/**
	 * Number of events at this location
	 */
	private ArrayList<SesameEvent> events;
	/**
	 * Distance and duration from the location enter in the form to here
	 */
	private SesameDistanceDuration dd;
	
	/**
	 * Constructor of the location (initialize the two ArrayList)
	 * @param uri URI of this location on Sesame
	 */
	public SesameLocation(URI uri) {
		super(uri);
		places=new ArrayList<SesamePlace>();
		events=new ArrayList<SesameEvent>();
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public ArrayList<SesamePlace> getPlaces() {
		return places;
	}
	public void addPlace(SesamePlace place) {
		places.add(place);
	}
	public ArrayList<SesameEvent> getEvents() {
		return events;
	}
	public void addEvent(SesameEvent event) {
		events.add(event);
	}
	public SesameDistanceDuration getDd() {
		return dd;
	}
	public void setDd(SesameDistanceDuration dd) {
		this.dd = dd;
	}
}
