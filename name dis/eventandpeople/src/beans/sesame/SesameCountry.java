package beans.sesame;

import org.openrdf.model.URI;

/**
 * Bean containing a Country get from sesame
 * @author Damien Goetschi
 *
 */
public class SesameCountry extends SesameBean{
	/**
	 * Name of this country
	 */
	private String name;
	
	/**
	 * Constructor of the country
	 * @param uri URI of this country on Sesame
	 */
	public SesameCountry(URI uri) {
		super(uri);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
