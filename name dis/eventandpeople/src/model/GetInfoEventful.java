package model;

import java.util.ArrayList;
import java.util.List;

import beans.SocialEvent;

import com.evdb.javaapi.APIConfiguration;
import com.evdb.javaapi.EVDBAPIException;
import com.evdb.javaapi.EVDBRuntimeException;
import com.evdb.javaapi.data.Event;
import com.evdb.javaapi.data.Image;
import com.evdb.javaapi.data.SearchResult;
import com.evdb.javaapi.data.request.EventSearchRequest;
import com.evdb.javaapi.operations.EventOperations;

import constant.Cnst;
/**
 * Retrieve data from Eventful  
 * @author Hussein Hazimeh
 * */
public class GetInfoEventful {

	private SearchResult sr;

	public GetInfoEventful() {
		connect();
	}

	/**
	 * Connect to api
	 * */
	@SuppressWarnings("static-access")
	public void connect() {
		APIConfiguration a = new APIConfiguration();
		a.setApiKey(Cnst.EVENTFUL_KEY);
		a.setBaseURL(Cnst.EVENTFUL_BASEURL);
		a.setEvdbPassword(Cnst.EVENTFUL_PASSWORD);
		a.setEvdbUser(Cnst.EVENTFUL_USERNAME);
	}

	/**
	 * Search events for a keyword
	 * 
	 * @param q
	 *            keyword to search
	 */
	public ArrayList<SocialEvent> getEvents(String q) {
		ArrayList<SocialEvent> list = new ArrayList<SocialEvent>();

		EventOperations eo = new EventOperations();
		EventSearchRequest esr = new EventSearchRequest();
		esr.setLocation(q);
		//yyyymmddhh
		esr.setDateRange("2014031700-2014033000");
		
		try {
			this.sr = eo.search(esr);
		} catch (EVDBRuntimeException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		} catch (EVDBAPIException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}

		assert (this.sr.getTotalItems() > 1);
		List<Event> events = this.sr.getEvents();

		for (int i = 0; i < events.size(); i++) {

			Event e = events.get(i);

			SocialEvent se = new SocialEvent();

			List<Image> images = e.getImages();
			Image image = images.get(0);
			String url = image.getUrl();
			se.setId(e.getSeid());
			se.setSmallPic(url);
			se.setAttending(e.getGoing().size());
			se.setSite(Cnst.SITE_EVENTFUL);
			se.setId(e.getSeid());
			se.setName(e.getTitle());
			se.setStart(e.getStartTime());
			se.setEnd(e.getStopTime());
			se.getLocation().setCity(q);
			se.getLocation().setCountry(e.getVenueCountry()+" ");
			se.getLocation().setStreet(e.getVenueRegion()+" ");
			se.getLocation().setZip(e.getVenuePostalCode()+" ");
			se.getLocation().setLatitude(e.getVenueLatitude());
			se.getLocation().setLongitude(e.getVenueLongitude());
			se.setLocationString(e.getVenueAddress());
			se.setDescription(e.getDescription());
			se.setUrl(e.getURL());
			se.setLocationString(e.getVenueRegion() + ","
					+ e.getVenuePostalCode() + "," + e.getVenueCity() + " , "
					+ e.getVenueCountry());
			list.add(se);
		}
		return list;

	}
}
