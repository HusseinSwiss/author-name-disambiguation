package beans.sesame;

import org.openrdf.model.URI;
/**
 * Bean containing a data from sesame (use by extending this in all the beans from sesame)
 * @author Damien Goetschi
 *
 */
public class SesameBean {
	/**
	 * URI of this object in sesame
	 */
	private URI uri;
	
	/**
	 * Constructor of this object
	 * @param uri URI of this object in sesame
	 */
	public SesameBean(URI uri){
		this.uri=uri;
	}
	public URI getUri() {
		return uri;
	}
	public void setUri(URI uri) {
		this.uri = uri;
	}
	
}
