package servlet;

import static constant.Cnst.ATTR_EVENTS_DISTANCES;
import static constant.Cnst.ATTR_EVENTS_LIST;
import static constant.Cnst.ATTR_EVENTS_TABLE;
import static constant.Cnst.ATTR_FB_OBJ;
import static constant.Cnst.ATTR_GEOLOC;
import static constant.Cnst.ATTR_JS_ADD_MARKERS;
import static constant.Cnst.ATTR_PLACES_DISTANCES;
import static constant.Cnst.ATTR_PLACES_LIST;
import static constant.Cnst.ATTR_PLACES_TABLE;
import static constant.Cnst.NB_RESULT;
import static constant.Cnst.TYPE_EVENT;
import static constant.Cnst.TYPE_PLACE;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GetInfoEventful;
import model.GetInfoFacebook;
import model.GetInfoFoursquare;
import model.GetInfoGoogleMaps;
import model.GetInfoGooglePlaces;
import beans.Geocode;
import beans.SocialDistanceDuration;
import beans.SocialEvent;
import beans.SocialLocation;
import beans.SocialPlace;
import constant.Cnst;
import facebook4j.FacebookException;
/**
 * This servlet search information about location from Facebook and Foursquare and display to the JSP
 * @author Damien Goetschi
 *
 */
public class Result extends HttpServlet{
	private ArrayList<SocialPlace> places;
	private ArrayList<SocialDistanceDuration> placesDistances;
	private ArrayList<SocialEvent> events;
	private ArrayList<SocialDistanceDuration> eventsDistances;
	private SocialLocation loc;
	private GetInfoFacebook fb;
	private GetInfoEventful evf;
	private GetInfoGooglePlaces gp;
	private Geocode gc;
	protected NumberFormat format=NumberFormat.getInstance();
	
	/**
	 * if there is no post data, we just take information stocked in session to show it
	 * (and show a error message if one or more data in session is null)
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		//retrieve information from session
		places = (ArrayList<SocialPlace>)request.getSession().getAttribute(ATTR_PLACES_LIST);
		placesDistances = (ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_PLACES_DISTANCES);
		events = (ArrayList<SocialEvent>)request.getSession().getAttribute(ATTR_EVENTS_LIST);
		eventsDistances= (ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_EVENTS_DISTANCES);
		
		//set digits for meters in kilometers
		format.setMinimumFractionDigits(3);

		if(places==null||placesDistances==null||events==null||eventsDistances==null){
			response.getWriter().println("<html><body><h2>Error, no previous request found.<br/><br/>Please make a new request <a href=\"searchForm.jsp\">HERE</a></body></html>");
		}else{
			//set String content HTML/JS to attribute for JSP
			request.setAttribute(ATTR_JS_ADD_MARKERS,jspAddMarkersString());
			request.setAttribute(ATTR_PLACES_TABLE,jspAllPlacesString());
			request.setAttribute(ATTR_EVENTS_TABLE,jspAllEventsString());
			//Call JSP
			this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/result.jsp" ).forward( request, response );
		}
	}
	
	/**
	 * if post data receive, we search information and show it
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		//retrieve parameters
		String city = request.getParameter("city");
		String location = request.getParameter("location");
		
		//create ArrayList
		places = new ArrayList<SocialPlace>();
		events = new ArrayList<SocialEvent>();
		placesDistances = new ArrayList<SocialDistanceDuration>();
		eventsDistances = new ArrayList<SocialDistanceDuration>();
		
		//complete location from where calcul distance and duration
		loc = GetInfoGoogleMaps.completeLocation(location);
		
		//set digits for meters in kilometers
		format.setMinimumFractionDigits(3);
		
		//set location from where calcul distance and duration to session
		request.getSession().setAttribute(ATTR_GEOLOC, loc);
		
		//get GetInfoFacebook in sesssion
		fb=(GetInfoFacebook)request.getSession().getAttribute(ATTR_FB_OBJ);
		
		evf=new GetInfoEventful();
		
		gp=new GetInfoGooglePlaces();	
		
		//search facebook, GooglePlaces, and foursquare for places and facebook, GooglePlaces, and Eventful for events
		getFacebookPlaces(city);
		getFoursquarePlaces(city);
		getGooglePlaces(city);	
		getFacebookEvents(city);
		getEventfulEvents(city);
		
		//set ArrayList contents result to session
		request.getSession().setAttribute(ATTR_PLACES_LIST, places);
		request.getSession().setAttribute(ATTR_PLACES_DISTANCES, placesDistances);
		request.getSession().setAttribute(ATTR_EVENTS_LIST, events);
		request.getSession().setAttribute(ATTR_EVENTS_DISTANCES, eventsDistances);
		
		//set String content HTML/JS to attribute for JSP
		request.setAttribute(ATTR_JS_ADD_MARKERS,jspAddMarkersString());
		request.setAttribute(ATTR_PLACES_TABLE,jspAllPlacesString());
		request.setAttribute(ATTR_EVENTS_TABLE,jspAllEventsString());
		
		//write cache
		GetInfoGoogleMaps.writeLocationsCache();
		GetInfoGoogleMaps.writeDistancesCache();
		GetInfoGoogleMaps.writeGeoLocationCache();
		//Call JSP
		this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/result.jsp" ).forward( request, response );
    }
	
	/**
	 * Get places from google places
	 * 
	 * */
	
	/**
	 * Get places in facebook for city in parameter and add to places ArrayList
	 * Get DistanceDuration for these places too
	 * @param city city for which search places
	 * @throws IOException
	 */
	public void getFacebookPlaces(String city) throws IOException{
		//Recupération des endroits Facebook
		try {
			ArrayList<SocialPlace> list = fb.getPlaces(city,NB_RESULT,0);
			for(int i=0;i<list.size();i++){
				SocialPlace p = list.get(i);
				SocialLocation l = p.getLocation();
				if(l.latLngToString().equals("-")&&l.toString().equals("")){
					p.setLocation(GetInfoGoogleMaps.completeLocation(p.getName()));
				}
				else {
					p.setLocation(GetInfoGoogleMaps.completeLocation(p.getName(),l));
				}
				p.getLocation().createID();
			}
			ArrayList<SocialDistanceDuration> distances = GetInfoGoogleMaps.getDistanceMatrix(loc, list);
			for(int i=0;i<list.size();i++){
				places.add(list.get(i));
				placesDistances.add(distances.get(i));
			}
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get places in foursquare for city in parameter and add to places ArrayList
	 * Get DistanceDuration for these places too
	 * @param city city for which search places
	 * @throws IOException
	 */
	public void getFoursquarePlaces(String city){
		//Récuperation des endroits Foursquare
		try {
			ArrayList<SocialPlace> list = GetInfoFoursquare.getVenuesData(city);
			for(int i=0;i<list.size();i++){
				SocialPlace p = list.get(i);
				p.getLocation().createID();
			}
			ArrayList<SocialDistanceDuration> distances = GetInfoGoogleMaps.getDistanceMatrix(loc, list);
			for(int i=0;i<list.size();i++){
				list.get(i).getLocation().createID();
				places.add(list.get(i));
				placesDistances.add(distances.get(i));
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Get places in Google places for city in parameter and add to places ArrayList
	 * @param city city for which search places
	 * @throws IOException
	 */
	public void getGooglePlaces(String city) throws IOException{
		//Recupération des endroits Google places
		try {
			
			ArrayList<SocialPlace> list = gp.getPlaces(city);
			for(int i=0;i<list.size();i++){
				SocialPlace p = list.get(i);
				p.getLocation().createID();
			}
			ArrayList<SocialDistanceDuration> distances = GetInfoGoogleMaps.getDistanceMatrix(loc, list);
			for(int i=0;i<list.size();i++){
				places.add(list.get(i));
				placesDistances.add(distances.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * Get events in facebook for city in parameter and add to events ArrayList
	 * Get DistanceDuration for these events too
	 * @param city city for which search events
	 * @throws IOException
	 */
	public void getFacebookEvents(String city) throws IOException{
		//Récuperation des "Events"
		try {
			ArrayList<SocialEvent> list = fb.getEvents(city,NB_RESULT,0);
			for(int i=0;i<list.size();i++){
				SocialEvent e = list.get(i);
				SocialLocation l = e.getLocation();
				if(l.latLngToString().equals("-")&&l.toString().equals("")&&e.getLocationString()!=null){
					e.setLocation(GetInfoGoogleMaps.completeLocation(e.getLocationString()));
				}
				else if((l.getStreet().equals("")||l.getCity().equals("")||l.latLngToString().equals("-"))){
					e.setLocation(GetInfoGoogleMaps.completeLocation(e.getName(),l));
				}
				e.getLocation().createID();
			}
			ArrayList<SocialDistanceDuration> distances = GetInfoGoogleMaps.getDistanceMatrixEvent(loc, list);
			for(int i=0;i<list.size();i++){
				events.add(list.get(i));
				eventsDistances.add(distances.get(i));
			}
		} catch (FacebookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Get events in Eventful for city in parameter and add to events ArrayList
	 * Get DistanceDuration for these events too
	 * @param city city for which search events
	 * @throws IOException
	 */
	public void getEventfulEvents(String city) throws IOException{
		//Récuperation des "Events Eventful"
		try {
			ArrayList<SocialEvent> list = evf.getEvents(city);
			for(int i=0;i<list.size();i++){
				SocialEvent e = list.get(i);
				e.getLocation().createID();
			}
			ArrayList<SocialDistanceDuration> distances = GetInfoGoogleMaps.getDistanceMatrixEvent(loc, list);
			for(int i=0;i<list.size();i++){
				events.add(list.get(i));
				eventsDistances.add(distances.get(i));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Get a String from all location for JavaScript array
	 * @return A String content all location (events and places) to add into JS array
	 */
	public String jspAddMarkersString(){
		String jsAddMarkers = "";
		for(int i=0;i<places.size();i++){
    		jsAddMarkers+=("addMarker(markersPlaces,"+places.get(i).getLocation().latLngToString()+",\""+places.get(i).getName().replace(',', ' ')+"\","+i+");");
    	}
	    for(int i=0;i<events.size();i++){
	    	jsAddMarkers+=("addMarker(markersEvents,"+events.get(i).getLocation().latLngToString()+",\""+events.get(i).getName().replace(',', ' ')+"\","+i+");");
    	}
	    return jsAddMarkers;
	}
	
	/**
	 * Create String containing a the table of place to show in JSP
	 * @return String containing html
	 */
	public String jspAllPlacesString(){
		String res="";
		res+="<table>";
		res+=jspTableHeader(TYPE_PLACE);
		res+="<tr><td colspan=\"3\">Touristic site:</td></tr>";
		res+=jspPlacesString(Cnst.TYPE_TOURISTIC_SITE);
		res+="<tr><td colspan=\"3\">Hotel:</td></tr>";
		res+=jspPlacesString(Cnst.TYPE_HOTEL);
		res+="<tr><td colspan=\"3\">Restaurant:</td></tr>";
		res+=jspPlacesString(Cnst.TYPE_RESTAURANT);
		res+="</table>";
		return res;
	}
	
	/**
	 * Create String containing header of a table
	 * @param type Indicate if header is for events or places
	 * @return String containing html
	 */
	public String jspTableHeader(String type){
		String res="";
		res+= "<tr>";
		if(type.equals(TYPE_EVENT))
			res+="<td colspan=\"11<\"/>";
		else
			res+="<td colspan=\"9<\"/>";
		res+= "<td colspan=\"2\"><center>Walking</center></td>"
			+ "<td colspan=\"2\"><center>Driving</center></td>"
			+ "</tr>";
		res+= "<tr>"
			+ "<td/>"
			+ "<td>Site:</td>"
			+ "<td/>"
			+ "<td>Name:</td>";
		if(type.equals(TYPE_EVENT)){
			res+="<td>Description:</td>"
				+"<td>Start date:</td>"
				+"<td>End date:</td>"
				+"<td>Invitees:</td>"
				+"<td>Attending:</td>";
		}else{
			res += "<td>Type:</td>"
				+ "<td>Likes count:</td>"
				+ "<td>Were here count:</td>";
		}
		res+= "<td>Location:</td>"
			+ "<td>Geolocation:</td>"
			+ "<td>Distance(km):</td>"
			+ "<td>Duration(min):</td>"
			+ "<td>Distance(km):</td>"
			+ "<td>Duration(min):</td>"
			+ "</tr>";
		return res;
	}
	
	/**
	 * Create places table for one category
	 * @param category (Hotel,Restaurant or Touristic site)
	 * @return String containing html
	 */
	public String jspPlacesString(String category){
		String res ="";
		for(int i=0;i<places.size();i++){
			if(places.get(i).getTopLevelCat().equals(category)){
				SocialPlace place = places.get(i);
				SocialDistanceDuration dist = placesDistances.get(i);
				res+=("<tr>");
				res+=("<td><INPUT type=\"checkbox\" checked name=\"places\" value=\""+i+"\"onclick=\"modification();\"></td>");
				if(place.getSite().equals(Cnst.SITE_FACEBOOK))res+=("<td><img src=\"fb.png\"/></td>");
				else if(place.getSite().equals(Cnst.SITE_FOURSQUARE))res+=("<td><img src=\"fs.png\"/></td>");
				else if(place.getSite().equals(Cnst.SITE_GP))res+=("<td><img src=\"google.png\"/></td>");
				else res+=("<td></td>");
				res+=("<td><img width=50 height=50 onClick=\"javascript:ouvre_popup('pic.jsp?t=places&id="+i+"','pic');\" src=\""+place.getSmallPic()+"\"/></td>");
				res+=("<td><a href="+place.getUrl()+">"+place.getName()+"</a></td>");
				res+=("<td>"+place.getType()+"</td>");
				res+=("<td>"+place.getLikeCount()+ "</td>");
				res+=("<td>"+place.getWereHereCount()+"</td>");
				res+=("<td>"+place.getLocation()+"</td>");
				res+=("<td>"+place.getLocation().latLngToString()+"</td>");
				res+=("<td>"+format.format(dist.getDistanceWalking()*0.001d)+"</td>");
				res+=("<td>"+(dist.getDurationWalking()/60)+"</td>");
				res+=("<td>"+format.format(dist.getDistanceDriving()*0.001d)+"</td>");
				res+=("<td>"+(dist.getDurationDriving()/60)+"</td>");
				res+=("<td><button type=\"button\" onclick=\"javascript:ouvre_popup('edit.jsp?t="+TYPE_PLACE+"&id="+i+"','add_edit');\">Edit</button></td>");
				
				res+=("</tr>");
			}
		}
		return res;
	}
	
	/**
	 * Create String containing a the table of events to show in JSP
	 * @return String containing html
	 */
	public String jspAllEventsString(){
		String res ="";
		res+="<table>";
		res+=jspTableHeader(TYPE_EVENT);
		for(int i=0;i<events.size();i++){
			SocialEvent event = events.get(i);
			SocialDistanceDuration dist = eventsDistances.get(i);
			res+=("<tr>");
			res+=("<td><INPUT type=\"checkbox\" checked name=\"events\" value=\""+i+"\"onclick=\"modification();\"></td>");
			if(event.getSite().equals(Cnst.SITE_FACEBOOK))res+=("<td><img src=\"fb.png\"/></td>");
			else if(event.getSite().equals(Cnst.SITE_EVENTFUL))res+=("<td><img src=\"evf.jpg\"/></td>");
			else res+=("<td></td>");
			res+=("<td><img onClick=\"javascript:ouvre_popup('pic.jsp?t=events&id="+i+"','pic');\" src=\""+event.getSmallPic()+"\" height=\"50px\" width=\"50px\"/></td>");
			res+=("<td><a href="+event.getUrl()+">"+event.getName()+"</a></td>");
			String descr = event.getDescription();
			if(descr==null)
				descr="-";
			if(descr.length()>50)
				descr=descr.substring(0, 50);
			res+=("<td>"+descr+"</td>");
			res+=("<td>"+event.getStart()+"</td>");
			res+=("<td>"+event.getEnd()+"</td>");
			res+=("<td>"+event.getInvites()+"</td>");
			res+=("<td>"+event.getAttending()+"</td>");
			res+=("<td>"+event.getLocation()+"</td>");
			res+=("<td>"+event.getLocation().latLngToString()+"</td>");
			res+=("<td>"+format.format(dist.getDistanceWalking()*0.001d)+"</td>");
			res+=("<td>"+(dist.getDurationWalking()/60)+"</td>");
			res+=("<td>"+format.format(dist.getDistanceDriving()*0.001d)+"</td>");
			res+=("<td>"+(dist.getDurationDriving()/60)+"</td>");
			res+=("<td><button type=\"button\" onclick=\"javascript:ouvre_popup('edit.jsp?t="+TYPE_EVENT+"&id="+i+"','add_edit');\">Edit</button></td>");
			res+=("</tr>");
		}
		res+="</table>";
		return res;
	}
}
