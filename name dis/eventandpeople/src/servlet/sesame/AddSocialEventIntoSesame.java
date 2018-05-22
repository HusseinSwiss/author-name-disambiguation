package servlet.sesame;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.sesame.GetInfoSesameSocialEvent;
import model.sesame.RDFSStoreSocialEvent;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import beans.SocialDistanceDuration;
import beans.SocialEvent;
import beans.SocialLocation;
import beans.SocialPlace;
import static constant.Cnst.*;
import static model.sesame.GetInfoSesameSocialEvent.*;

public class AddSocialEventIntoSesame extends HttpServlet {
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		try{
			SocialLocation baseLoc = (SocialLocation)request.getSession().getAttribute(ATTR_GEOLOC);
			//Add location from where the distance and duration are search
			URI locationBaseURI=addLocation(baseLoc, addCity(baseLoc, addCountry(baseLoc.getCountry())));
			
			//get array with ids of places to add
			String[] resPlaces = request.getParameterValues("places");
			//if resPlaces contains one or more index of places to add
			if(resPlaces!=null){
				ArrayList<SocialPlace> places = (ArrayList<SocialPlace>)request.getSession().getAttribute(ATTR_PLACES_LIST);
				ArrayList<SocialDistanceDuration> placesDistances = (ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_PLACES_DISTANCES);
				for(int i=0;i<resPlaces.length;i++){
					//Get Place, Location and DistanceDuration for this place
					SocialPlace currentPlace = places.get(Integer.valueOf(resPlaces[i]));
					SocialLocation currentLocation = currentPlace.getLocation();
					SocialDistanceDuration currentDD=placesDistances.get(Integer.valueOf(resPlaces[i]));
					//Add country, city, location, place and distance duration
					URI countryURI=addCountry(currentLocation.getCountry());
					URI cityURI=addCity(currentLocation, countryURI);
					URI locationURI=addLocation(currentLocation, cityURI);
					URI placeURI=addPlace(currentPlace, locationURI);
					URI distanceDurationURI=addDistanceDuration(currentDD, locationBaseURI, locationURI);
				}
			}
			System.out.println("END ADD PLACES");
			
			//get array with ids of events to add
			String[] resEvents = request.getParameterValues("events");
			//if resEvents contains one or more index of events to add
			if(resEvents!=null){
				ArrayList<SocialEvent> events = (ArrayList<SocialEvent>)request.getSession().getAttribute(ATTR_EVENTS_LIST);
				ArrayList<SocialDistanceDuration> eventsDistances = (ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_EVENTS_DISTANCES);
				for(int i=0;i<resEvents.length;i++){
					//Get Event, Location and DistanceDuration for this event
					SocialEvent currentEvent = events.get(Integer.valueOf(resEvents[i]));
					SocialLocation currentLocation = currentEvent.getLocation();
					SocialDistanceDuration currentDD=eventsDistances.get(Integer.valueOf(resEvents[i]));
					//Add country, city, location,event and distance duration
					URI countryURI=addCountry(currentLocation.getCountry());
					URI cityURI=addCity(currentLocation, countryURI);
					URI locationURI=addLocation(currentLocation, cityURI);
					URI eventURI=addEvent(currentEvent, locationURI);
					URI distanceDurationURI=addDistanceDuration(currentDD, locationBaseURI, locationURI);
				}
			}
			System.out.println("END ADD EVENTS");
			response.getWriter().println("<html><body>Places and events added to the DataBase<br/><button onclick=\"window.location.href='result.jsp';\" >Back</button></body></html>");
		}catch(RepositoryException e){
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		}
	}
}