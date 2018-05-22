package model.sesame;

import static constant.Cnst.*;

import java.io.IOException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.GetInfoGoogleMaps;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import constant.Cnst;
import beans.SocialDistanceDuration;
import beans.SocialEvent;
import beans.SocialLocation;
import beans.SocialPlace;
import beans.sesame.SesameCity;
import beans.sesame.SesameCountry;
import beans.sesame.SesameDistanceDuration;
import beans.sesame.SesameEvent;
import beans.sesame.SesameEventfulEvent;
import beans.sesame.SesameFacebookEvent;
import beans.sesame.SesameFacebookPlace;
import beans.sesame.SesameFoursquarePlace;
import beans.sesame.SesameGooglePlace;
import beans.sesame.SesameLocation;
import beans.sesame.SesamePlace;

/**
 * This class contains statics methods to get and add information from/to Sesame with RDFSStore
 * @author Damien Goetschi
 *
 */
public class GetInfoSesameSocialEvent {
	/**
	 * RSFSStore to use for queries sesame
	 */
	private static RDFSStoreSocialEvent rdfsstore=null;
	
	/**
	 * Get RDFSStore object and create it if it is null
	 * @return RDFSStore for queries sesame
	 * @throws RepositoryException
	 */
	public static RDFSStoreSocialEvent getRDFSstore() throws RepositoryException{
		if(rdfsstore==null){
			rdfsstore=new RDFSStoreSocialEvent(Cnst.REPOSITORY_HOSTNAME, Cnst.REPOSITORY_PORT, Cnst.REPOSITORY_ID_SOCIAL_EVENT);
		}
		return rdfsstore;
	}
	
	/**
	 * Get ValueFactory use for create URI and literal for sesame
	 * @return ValueFactory
	 * @throws RepositoryException
	 */
	public static ValueFactory getF() throws RepositoryException{
		return getRDFSstore().getFactory();
	}
	
	/**
	 * Get a SesameCoutry object from sesame
	 * @param uri String containing uri of country to get
	 * @return SesameCountry object with name and uri of this city
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static SesameCountry getCountry(String uri) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		URI countryURI = getF().createURI(uri);
		SesameCountry c = new SesameCountry(countryURI);
		c.setName(getValue(countryURI, Cnst.HAS_NAME_SOCIAL_EVENT));
		return c;
	}
	
	/**
	 * Get a SesameCity object from sesame
	 * @param uri String containing uri of city to get
	 * @return SesameCity object with name, uri, zip and country of this city and a list of all location in it
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static SesameCity getCity(String uri) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		URI cityURI = getF().createURI(uri);
		SesameCity c = new SesameCity(cityURI);
		//set name, zip and country
		c.setName(getValue(cityURI, Cnst.HAS_NAME_SOCIAL_EVENT));
		c.setZip(getValue(cityURI, Cnst.HAS_ZIP));
		c.setCountry(getCountry(getValue(cityURI, Cnst.IS_CITY_FROM)));
		ArrayList<String> arrayLocation = getValues(cityURI, Cnst.CONTAINS_LOCATION);
		for(int i=0;i<arrayLocation.size();i++){
			//add location for each uri of location get
			c.addLocation(getLocation(arrayLocation.get(i)));
		}
		return c;
	}
	
	/**
	 * Get a ArrayList of uri (in String) of each city with name and country in params
	 * @param name name of city to search
	 * @param uriCountry String uri of country in which search cities
	 * @return	a ArrayList a String
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static ArrayList<String> getAllCity(String name, String uriCountry) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		ArrayList<String> al = new ArrayList<String>();
		String request = "SELECT ?uri WHERE{?uri <"+Cnst.HAS_NAME_SOCIAL_EVENT+"> ?name . FILTER REGEX(?name,\"^"+name+"$\") ?uri <http://www.openrdf.org/schema/sesame#directType> <"+Cnst.CITY+"> . ?uri <"+Cnst.IS_CITY_FROM+"> <"+uriCountry+">}";
		TupleQueryResult res = RDFSStoreSocialEvent.execSelectQuery(request);
		while(res.hasNext())
		{
			BindingSet bs = res.next();
			al.add((bs.getBinding("uri").getValue().stringValue()));
		}
		return al;
	}
	
	/**
	 * Get location for a URI in params
	 * @param uri String URI of location to get
	 * @return SesameLocation contaning street, latitude and longitude of this loation and a List of places and events in it
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static SesameLocation getLocation(String uri) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		URI locationURI = getF().createURI(uri);
		SesameLocation l = new SesameLocation(locationURI);
		//Add street, latitude and longitude
		l.setStreet(getValue(locationURI, Cnst.HAS_STREET));
		l.setLatitude(Double.valueOf(getValue(locationURI, Cnst.HAS_LATITUDE)));
		l.setLongitude(Double.valueOf(getValue(locationURI, Cnst.HAS_LONGITUDE)));
		//Get places URI (string) and add the SesamePlace for each
		ArrayList<String> places = getValues(locationURI, Cnst.CONTAINS_PLACE);
		for(int i=0;i<places.size();i++){
			l.addPlace(getPlace(places.get(i)));
		}
		//Get events URI (string) and add the SesameEvent for each
		ArrayList<String> events = getValues(locationURI, Cnst.CONTAINS_EVENT);
		for(int i=0;i<events.size();i++){
			l.addEvent(getEvent(events.get(i)));
		}
		return l;
	}

	/**
	 * Get a event ( facebook event or eventful event) for uri in params
	 * @param uri String URI of event
	 * @return SesameEvent or SesameFacebookEvent containing all informations about this event
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static SesameEvent getEvent(String uri) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		URI eventURI = getF().createURI(uri);
		String type=getValue(eventURI,"http://www.openrdf.org/schema/sesame#directType");
		SesameEvent e;
		//if type of data is a facebook event, create a SesameFacebookEvent and add information containing only in facebook events
		if(type.equals(Cnst.FACEBOOK_EVENT)){
			SesameFacebookEvent fbe=new SesameFacebookEvent(eventURI);
			fbe.setId(getValue(eventURI, Cnst.HAS_ID_SOCIAL_EVENT));
			fbe.setInvitesCount(Integer.valueOf(getValue(eventURI, Cnst.HAS_INVITES_COUNT)));
			fbe.setAttendingCount(Integer.valueOf(getValue(eventURI, Cnst.HAS_ATTENDING_COUNT)));
			e=fbe;			
		}
		//block added by  Hussein add eventful event 
		else if(type.equals(Cnst.EVENTFUL_EVENT)){
			SesameEventfulEvent evf=new SesameEventfulEvent(eventURI);
			evf.setId(getValue(eventURI, Cnst.HAS_ID_SOCIAL_EVENT));
			evf.setInvitesCount(Integer.valueOf(getValue(eventURI, Cnst.HAS_INVITES_COUNT)));
			evf.setAttendingCount(Integer.valueOf(getValue(eventURI, Cnst.HAS_ATTENDING_COUNT)));
			e=evf;			
		}
		//else just create a SesameEvent
		else{
			e = new SesameEvent(eventURI);
		}
		//add information which are in events and facebook events
		e.setDescription(getValue(eventURI, Cnst.HAS_DESCRIPTION));
		e.setName(getValue(eventURI, Cnst.HAS_NAME_SOCIAL_EVENT));
		e.setStart(getDateValue(eventURI, Cnst.HAS_START_SOCIAL_EVENT));
		e.setEnd(getDateValue(eventURI, Cnst.HAS_END_SOCIAL_EVENT));
		e.setLink(getValue(eventURI, Cnst.HAS_LINK));
		e.setSmallPic(getValue(eventURI, Cnst.HAS_SMALL_PIC));
		e.setBigPic(getValues(eventURI, Cnst.HAS_BIG_PIC));
		return e;
	}
	
	/**
	 * Get a place (or facebook/foursquare place) for uri in params
	 * @param uri String URI of place
	 * @return SesameEvent or SesameFacebookEvent or SesameFoursquareEvent containing all informations about this event
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static SesamePlace getPlace(String uri) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		URI placeURI = getF().createURI(uri);
		String type=getValue(placeURI, "http://www.openrdf.org/schema/sesame#directType");
		SesamePlace p;
		//if type of data is a facebook place, create a SesameFacebookPlace and add information containing only in facebook places
		if(type.equals(Cnst.FACEBOOK_PLACE)){
			SesameFacebookPlace fbp=new SesameFacebookPlace(placeURI);
			fbp.setId(getValue(placeURI, Cnst.HAS_ID_SOCIAL_EVENT));
			fbp.setLikeCount(Integer.valueOf(getValue(placeURI, Cnst.HAS_LIKE_COUNT)));
			fbp.setWereHereCount(Integer.valueOf(getValue(placeURI, Cnst.HAS_WERE_HERE_COUNT)));
			p=fbp;
		}
		//if type of data is a foursquare place, create a SesameFoursquareEvent and add information containing only in foursquare places
		else if(type.equals(Cnst.FOURSQUARE_PLACE)){
			SesameFoursquarePlace fsp=new SesameFoursquarePlace(placeURI);
			fsp.setId(getValue(placeURI, Cnst.HAS_ID_SOCIAL_EVENT));
			fsp.setLikeCount(Integer.valueOf(getValue(placeURI, Cnst.HAS_LIKE_COUNT)));
			fsp.setWereHereCount(Integer.valueOf(getValue(placeURI, Cnst.HAS_WERE_HERE_COUNT)));
			p=fsp;
		}
		//block added by Hussein add Google place
		else if(type.equals(Cnst.GOOGLEPLACE_PLACE)){
			SesameGooglePlace gp=new SesameGooglePlace(placeURI);
			gp.setId(getValue(placeURI, Cnst.HAS_ID_SOCIAL_EVENT));
			gp.setLikeCount(Integer.valueOf(getValue(placeURI, Cnst.HAS_LIKE_COUNT)));
			gp.setWereHereCount(Integer.valueOf(getValue(placeURI, Cnst.HAS_WERE_HERE_COUNT)));
			p=gp;
		}
		//else just create a SesamePlace
		else{
			p = new SesamePlace(placeURI);
		}
		//add information which are in places and facebook/foursquare places
		p.setName(getValue(placeURI, Cnst.HAS_NAME_SOCIAL_EVENT));
		p.setLink(getValue(placeURI, Cnst.HAS_LINK));
		p.setSmallPic(getValue(placeURI, Cnst.HAS_SMALL_PIC));
		p.setBigPic(getValues(placeURI, Cnst.HAS_BIG_PIC));
		p.setTopLevelCat(getValue(placeURI, Cnst.HAS_TOP_LEVEL_CAT));
		p.setType(getValue(placeURI, Cnst.HAS_TYPE));
		return p;
	}
	
	/**
	 * Get distance and duration from a location to an other
	 * @param from location from where search distance
	 * @param to location of place/event
	 * @return SesameDistanceDuration
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 * @throws IOException
	 */
	public static SesameDistanceDuration getDistanceDuration(SesameLocation from, SesameLocation to)throws RepositoryException, MalformedQueryException, QueryEvaluationException, IOException{
		String request = "SELECT ?uri WHERE{?uri <"+Cnst.CONTAINS_DATA_DURATION_FROM+"> <"+from.getUri()+"> . ?uri <"+Cnst.CONTAINS_DATA_DURATION_TO+"> <"+to.getUri()+">}";
		TupleQueryResult res = RDFSStoreSocialEvent.execSelectQuery(request);
		//if data is not in sesame, adding it
		if(!res.hasNext()){
			addDistanceDuration(GetInfoGoogleMaps.getSocialDistanceDuration(from, to), from.getUri(), to.getUri());
			res = RDFSStoreSocialEvent.execSelectQuery(request);
		}
		if(res.hasNext()){
			URI uriSdd = getF().createURI(res.next().getBinding("uri").getValue().stringValue());
			SesameDistanceDuration sdd = new SesameDistanceDuration(uriSdd);
			sdd.setFrom(from);
			sdd.setDriveDistance(Integer.valueOf(getValue(uriSdd, HAS_DRIVE_DISTANCE)));
			sdd.setDriveDuration(Integer.valueOf(getValue(uriSdd, HAS_DRIVE_DURATION)));
			sdd.setWalkDistance(Integer.valueOf(getValue(uriSdd, HAS_WALK_DISTANCE)));
			sdd.setWalkDuration(Integer.valueOf(getValue(uriSdd, HAS_WALK_DURATION)));
			return sdd;
		}
		return null;
	}
	
	/**
	 * Get a list of big pictures for a event or a place (Use for picture gallery)
	 * @param uri String URI of event or place for which get the pics
	 * @return ArrayList of String (link of pics)
	 */
	public static ArrayList<String> getPics(String uri){
		try {
			URI toGetURI = getF().createURI(uri);
			return (getValues(toGetURI, Cnst.HAS_BIG_PIC));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Get fisrt value for a URI and a predicat
	 * @param uri URI of object in which search value
	 * @param predicat String URI of predicat to get value
	 * @return String with value
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static String getValue(URI uri, String predicat) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		String request = "SELECT ?object WHERE{<"+uri+"> <"+ predicat +"> ?object}";
		TupleQueryResult res = RDFSStoreSocialEvent.execSelectQuery(request);
		String s = null;
		if(res.hasNext())
		{
			BindingSet bs = res.next();
			s=(bs.getBinding("object").getValue().stringValue());
		}
		return s;
	}

	/**
	 * Get date value for a URI and a predicat
	 * @param uri URI of object in which search value
	 * @param predicat String URI of predicat to get value
	 * @return Date or null if there is no date
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static Date getDateValue(URI uri, String predicat) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		String request = "SELECT ?object WHERE{<"+uri+"> <"+ predicat +"> ?object}";
		TupleQueryResult res = RDFSStoreSocialEvent.execSelectQuery(request);
		Date d = null;
		if(res.hasNext())
		{
			BindingSet bs = res.next();
			try {
				d=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX").parse(bs.getBinding("object").getValue().stringValue());
			} catch (ParseException e) {
				System.err.println("String not contains a date");
			}
		}
		return d;
	}
	
	/**
	 * Get all values for a URI and a predicat
	 * @param uri URI of object in which search value
	 * @param predicat String URI of predicat to get value
	 * @return ArrayList of String with value
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static ArrayList<String> getValues(URI uri, String predicat) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		String request = "SELECT ?object WHERE{<"+uri+"> <"+ predicat +"> ?object}";
		TupleQueryResult res = RDFSStoreSocialEvent.execSelectQuery(request);
		ArrayList<String> list = new ArrayList<String>();
		while(res.hasNext())
		{
			BindingSet bs = res.next();
			list.add((bs.getBinding("object").getValue().stringValue()));
		}
		return list;
	}
	
	/**
	 * Add a country if it dosen't be already in sesame. And return URI
	 * @param country name of country to add
	 * @return URI of country
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addCountry(String country) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		//For country ID, remove space and special characters
		String countryId=country.replaceAll(" ", "");
		countryId = Normalizer.normalize(countryId, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
		URI countryURI = getF().createURI(NAMESPACE_ONTOLOGY_SOCIAL_EVENT+countryId);
		if(!RDFSStoreSocialEvent.isStored(countryURI)){
			//Put country in DB
			getRDFSstore().add(countryURI, RDF.TYPE, getF().createURI(COUNTRY));
			getRDFSstore().add(countryURI, getF().createURI(HAS_NAME_SOCIAL_EVENT), getF().createLiteral(country));
		}
		return countryURI;
	}
	
	/**
	 * Add a city if it dosen't be already in sesame. And return URI
	 * @param l location of city to add
	 * @param country URI of country in which the city is
	 * @return URI of city
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addCity(SocialLocation l, URI country) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		String cityId=l.getZip()+l.getCity();
		//For city ID, remove space and special characters
		cityId=cityId.replaceAll(" ", "");
		cityId = Normalizer.normalize(cityId, Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "");
		URI cityURI = getF().createURI(NAMESPACE_ONTOLOGY_SOCIAL_EVENT+cityId);
		if(!RDFSStoreSocialEvent.isStored(cityURI)){
			//Put city in  DB
			getRDFSstore().add(cityURI, RDF.TYPE, getF().createURI(CITY));
			getRDFSstore().add(cityURI, getF().createURI(IS_CITY_FROM), country);
			getRDFSstore().add(cityURI, getF().createURI(HAS_NAME_SOCIAL_EVENT), getF().createLiteral(Normalizer.normalize(l.getCity(), Normalizer.Form.NFD).replaceAll("[\u0300-\u036F]", "")));
			getRDFSstore().add(cityURI, getF().createURI(HAS_ZIP), getF().createLiteral(l.getZip()));
		}
		return cityURI;
	}
	
	/**
	 * Add a location if it dosen't be already in sesame. And return URI
	 * @param l location to add
	 * @param city URI of city in which the location is
	 * @return URI of location
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addLocation(SocialLocation l, URI city) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI locationURI = getF().createURI(NAMESPACE_ONTOLOGY_SOCIAL_EVENT+l.getId());
		if(!RDFSStoreSocialEvent.isStored(locationURI)){
			//Put location in DB
			getRDFSstore().add(locationURI, RDF.TYPE, getF().createURI(LOCATION));
			getRDFSstore().add(city, getF().createURI(CONTAINS_LOCATION), locationURI);
			getRDFSstore().add(locationURI, getF().createURI(HAS_STREET), getF().createLiteral(l.getStreet()));
			getRDFSstore().add(locationURI, getF().createURI(HAS_LATITUDE), getF().createLiteral(l.getLatitude()));
			getRDFSstore().add(locationURI, getF().createURI(HAS_LONGITUDE), getF().createLiteral(l.getLongitude()));
		}
		return locationURI;
	}
	
	/**
	 * Add a event if it dosen't be already in sesame. And return URI
	 * @param e Event to add
	 * @param location URI of location in which the event is
	 * @return URI of event
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addEvent(SocialEvent e, URI location) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI eventURI = getF().createURI(NAMESPACE_ONTOLOGY_SOCIAL_EVENT+e.getId());
		if(!RDFSStoreSocialEvent.isStored(eventURI)){
			//if the site is Facebook, adding  a facebook_event type
			if(e.getSite().equals(SITE_FACEBOOK)){
				getRDFSstore().add(eventURI, RDF.TYPE, getF().createURI(FACEBOOK_EVENT));
				getRDFSstore().add(eventURI, getF().createURI(HAS_INVITES_COUNT), getF().createLiteral(e.getInvites()));
				getRDFSstore().add(eventURI, getF().createURI(HAS_ATTENDING_COUNT), getF().createLiteral(e.getAttending()));
				getRDFSstore().add(eventURI, getF().createURI(HAS_ID_SOCIAL_EVENT), getF().createLiteral(e.getId()));
			//Block added by Hussein , if the site is EVENTFUL, adding  a EVENTFUL event type
			}else if(e.getSite().equals(SITE_EVENTFUL)){
				getRDFSstore().add(eventURI, RDF.TYPE, getF().createURI(EVENTFUL_EVENT));
				getRDFSstore().add(eventURI, getF().createURI(HAS_INVITES_COUNT), getF().createLiteral(e.getInvites()));
				getRDFSstore().add(eventURI, getF().createURI(HAS_ATTENDING_COUNT), getF().createLiteral(e.getAttending()));
				getRDFSstore().add(eventURI, getF().createURI(HAS_ID_SOCIAL_EVENT), getF().createLiteral(e.getId()));
			//else add a event type
			}else{
				getRDFSstore().add(eventURI, RDF.TYPE, getF().createURI(EVENT));
			}
			getRDFSstore().add(location, getF().createURI(CONTAINS_EVENT), eventURI);
			getRDFSstore().add(eventURI, getF().createURI(HAS_NAME_SOCIAL_EVENT), getF().createLiteral(e.getName()));
			getRDFSstore().add(eventURI, getF().createURI(HAS_LINK), getF().createLiteral(e.getUrl()));
			if(e.getDescription()!=null)
				getRDFSstore().add(eventURI, getF().createURI(HAS_DESCRIPTION), getF().createLiteral(e.getDescription()));
			if(e.getStart()!=null)
				getRDFSstore().add(eventURI, getF().createURI(HAS_START_SOCIAL_EVENT), getF().createLiteral(e.getStart()));
			if(e.getEnd()!=null)
				getRDFSstore().add(eventURI, getF().createURI(HAS_END_SOCIAL_EVENT), getF().createLiteral(e.getEnd()));
			if(!e.getSmallPic().equals(""))
				getRDFSstore().add(eventURI, getF().createURI(HAS_SMALL_PIC), getF().createLiteral(e.getSmallPic()));
			for(int k=0;k<e.getLargePic().size();k++)
				getRDFSstore().add(eventURI, getF().createURI(HAS_BIG_PIC), getF().createLiteral(e.getLargePic().get(k)));
		}
		return eventURI;
	}
	
	/**
	 * Add a place if it dosen't be already in sesame. And return URI
	 * @param p Place to add
	 * @param location URI of location in which the place is
	 * @return URI of place
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addPlace(SocialPlace p, URI location) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI placeURI = getF().createURI(NAMESPACE_ONTOLOGY_SOCIAL_EVENT+p.getId());
		if(!RDFSStoreSocialEvent.isStored(placeURI)){
			//if the site is Facebook, adding  a facebook_place type
			if(p.getSite().equals(SITE_FACEBOOK)){
				getRDFSstore().add(placeURI, RDF.TYPE, getF().createURI(FACEBOOK_PLACE));
				getRDFSstore().add(placeURI, getF().createURI(HAS_LIKE_COUNT), getF().createLiteral(p.getLikeCount()));
				getRDFSstore().add(placeURI, getF().createURI(HAS_WERE_HERE_COUNT), getF().createLiteral(p.getWereHereCount()));
				getRDFSstore().add(placeURI, getF().createURI(HAS_ID_SOCIAL_EVENT), getF().createLiteral(p.getId()));
			//if the site is Foursquare, adding  a foursquare_place type
			}else if(p.getSite().equals(SITE_FOURSQUARE)){
				getRDFSstore().add(placeURI, RDF.TYPE, getF().createURI(FOURSQUARE_PLACE));
				getRDFSstore().add(placeURI, getF().createURI(HAS_LIKE_COUNT), getF().createLiteral(p.getLikeCount()));
				getRDFSstore().add(placeURI, getF().createURI(HAS_WERE_HERE_COUNT), getF().createLiteral(p.getWereHereCount()));
				getRDFSstore().add(placeURI, getF().createURI(HAS_ID_SOCIAL_EVENT), getF().createLiteral(p.getId()));
			//if the site is Google, adding  a googleplaces_event type, Block added by Hussein Hazimeh
			}else if(p.getSite().equals(SITE_GP)){
				getRDFSstore().add(placeURI, RDF.TYPE, getF().createURI(GOOGLEPLACE_PLACE));
				getRDFSstore().add(placeURI, getF().createURI(HAS_LIKE_COUNT), getF().createLiteral(p.getLikeCount()));
				getRDFSstore().add(placeURI, getF().createURI(HAS_WERE_HERE_COUNT), getF().createLiteral(p.getWereHereCount()));
				getRDFSstore().add(placeURI, getF().createURI(HAS_ID_SOCIAL_EVENT), getF().createLiteral(p.getId()));
			//else add a place type
			}else{
				getRDFSstore().add(placeURI, RDF.TYPE, getF().createURI(PLACE));
			}
			getRDFSstore().add(location, getF().createURI(CONTAINS_PLACE), placeURI);
			getRDFSstore().add(placeURI, getF().createURI(HAS_TOP_LEVEL_CAT), getF().createLiteral(p.getTopLevelCat()));
			getRDFSstore().add(placeURI, getF().createURI(HAS_TYPE), getF().createLiteral(p.getType()));
			getRDFSstore().add(placeURI, getF().createURI(HAS_NAME_SOCIAL_EVENT), getF().createLiteral(p.getName()));
			getRDFSstore().add(placeURI, getF().createURI(HAS_LINK), getF().createLiteral(p.getUrl()));
			if(!p.getSmallPic().equals(""))
				getRDFSstore().add(placeURI, getF().createURI(HAS_SMALL_PIC), getF().createLiteral(p.getSmallPic()));
			for(int k=0;k<p.getLargePic().size();k++)
				getRDFSstore().add(placeURI, getF().createURI(HAS_BIG_PIC), getF().createLiteral(p.getLargePic().get(k)));
		}
		return placeURI;
	}
	
	/**
	 * Add a distance and duration object if it dosen't be already in sesame. And return URI
	 * @param dd the DistanceDuration to add
	 * @param locationFrom URI of location from where the distance and duration are
	 * @param locationTo URI of location to where the distance and duration are
	 * @return URI of distance and duration in sesame
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addDistanceDuration(SocialDistanceDuration dd, URI locationFrom, URI locationTo) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI ddURI = getF().createURI(NAMESPACE_ONTOLOGY_SOCIAL_EVENT+locationFrom.getLocalName()+"."+locationTo.getLocalName());
		if(!RDFSStoreSocialEvent.isStored(ddURI)){
			//Put distance and duration in DB
			getRDFSstore().add(ddURI, RDF.TYPE, getF().createURI(DISTANCE_DURATION));
			getRDFSstore().add(ddURI, getF().createURI(CONTAINS_DATA_DURATION_FROM), locationFrom);
			getRDFSstore().add(ddURI, getF().createURI(CONTAINS_DATA_DURATION_TO), locationTo);
			getRDFSstore().add(ddURI, getF().createURI(HAS_DRIVE_DISTANCE), getF().createLiteral(dd.getDistanceDriving()));
			getRDFSstore().add(ddURI, getF().createURI(HAS_DRIVE_DURATION), getF().createLiteral(dd.getDurationDriving()));
			getRDFSstore().add(ddURI, getF().createURI(HAS_WALK_DISTANCE), getF().createLiteral(dd.getDistanceWalking()));
			getRDFSstore().add(ddURI, getF().createURI(HAS_WALK_DURATION), getF().createLiteral(dd.getDurationWalking()));
		}
		return ddURI;
	}
}
