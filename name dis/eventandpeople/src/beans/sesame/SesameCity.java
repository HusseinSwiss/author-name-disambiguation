package beans.sesame;

import java.util.ArrayList;

import org.openrdf.model.URI;

/**
 * Bean containing a City get from sesame
 * @author Damien Goetschi
 *
 */
public class SesameCity extends SesameBean {
	/**
	 * ZIP code of the city
	 */
	private String zip;
	/**
	 * Name of the city
	 */
	private String name;
	/**
	 * Ref. to country in which the city is
	 */
	private SesameCountry country;
	/**
	 * List of all location is this city
	 */
	private ArrayList<SesameLocation> location;

	/**
	 * Constructor of the city (initialize the ArrayList)
	 * @param uri URI of this city in Sesame
	 */
	public SesameCity(URI uri) {
		super(uri);
		location = new ArrayList<SesameLocation>();
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SesameCountry getCountry() {
		return country;
	}

	public void setCountry(SesameCountry country) {
		this.country = country;
	}

	public ArrayList<SesameLocation> getLocation() {
		return location;
	}

	public void addLocation(SesameLocation location) {
		this.location.add(location);
	}

}
