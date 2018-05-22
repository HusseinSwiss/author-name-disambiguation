package servlet.sesame;

import static constant.Cnst.ATTR_EVENTS_TABLE;
import static constant.Cnst.ATTR_JS_ADD_MARKERS;
import static constant.Cnst.ATTR_PLACES_TABLE;
import static constant.Cnst.TYPE_EVENT;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import servlet.Result;
import constant.Cnst;
import model.GetInfoGoogleMaps;
import model.sesame.GetInfoSesameSocialEvent;
import beans.*;
import beans.sesame.*;
/**
 * This servlet get information in sesame and display to the JSP
 * (Extends Result to avoid to copy some methods)
 * @author Damien Goetschi
 *
 */
public class SesameResult extends Result{
	private ArrayList<SesameCity> scity;
	private SesameLocation sloc;
	private HashMap<String, Integer> uri_id = new HashMap<String, Integer>();
	private int id_count;
	
	/**
	 * if post data receive, we search information and show it
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		id_count=0;
		//get parameters
		String city = request.getParameter("city");
		String country = request.getParameter("country");
		//complete location from where calcul distance and duration
		SocialLocation loc = GetInfoGoogleMaps.completeLocation(request.getParameter("location"));
		//set digits for meters in kilometers
		format.setMinimumFractionDigits(3);
		try {
			//search information about a city in sesame
			getCities(city,country,loc);
			
			//set location from where calcul distance and duration to session
			request.getSession().setAttribute(Cnst.ATTR_SESAME_LOC, sloc);
			
			//set cities to session
			request.getSession().setAttribute( Cnst.ATTR_SESAME_CITY,scity);
			
			//set String content HTML/JS to attribute for JSP
			request.setAttribute(ATTR_JS_ADD_MARKERS,jspAddMarkersString());
			request.setAttribute(ATTR_PLACES_TABLE,jspAllPlacesString());
			request.setAttribute(ATTR_EVENTS_TABLE,jspAllEventsString());
			
			//write cache
			GetInfoGoogleMaps.writeDistancesCache();
			//call JSP
			this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/sesameResult.jsp" ).forward( request, response );
			
		} catch (RepositoryException e) {
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			e.printStackTrace();
		} catch (QueryEvaluationException e) {
			e.printStackTrace();	
		}
	}
	
	/**
	 * if there is no post data, we just take information stocked in session to show it
	 * (and show a error message if one or more data in session is null)
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		id_count=0;
		//retrieve information from session
		sloc=(SesameLocation)request.getSession().getAttribute(Cnst.ATTR_SESAME_LOC);
		scity=(ArrayList<SesameCity>)request.getSession().getAttribute( Cnst.ATTR_SESAME_CITY);
		
		//set digits for meters in kilometers
		format.setMinimumFractionDigits(3);
		if(scity==null){
			response.getWriter().println("<html><body><h2>Error, no previous request found.<br/><br/>Please make a new request <a href=\"sesameSearchForm.jsp\">HERE</a></body></html>");
		}else{
			//set String content HTML/JS to attribute for JSP		
			request.setAttribute(ATTR_JS_ADD_MARKERS,jspAddMarkersString());
			request.setAttribute(ATTR_PLACES_TABLE,jspAllPlacesString());
			request.setAttribute(ATTR_EVENTS_TABLE,jspAllEventsString());
			//call JSP
			this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/sesameResult.jsp" ).forward( request, response );
		}
	}
	
	/**
	 * Add all cities with location,events and places informations for a name and a country
	 * @param city name of city to search
	 * @param country String containing uri of country in which search cities
	 * @param loc location from where calcul distance and duration
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 * @throws IOException
	 */
	private void getCities(String city, String country, SocialLocation loc) throws RepositoryException, MalformedQueryException, QueryEvaluationException, IOException {
		sloc = GetInfoSesameSocialEvent.getLocation(GetInfoSesameSocialEvent.addLocation(loc, GetInfoSesameSocialEvent.addCity(loc, GetInfoSesameSocialEvent.addCountry(loc.getCountry()))).stringValue());
		ArrayList<String> uriList = GetInfoSesameSocialEvent.getAllCity(city, country);
		scity = new ArrayList<SesameCity>();
		for(int i=0;i<uriList.size();i++){
			SesameCity sCity = GetInfoSesameSocialEvent.getCity(uriList.get(i));
			for(int j=0;j<sCity.getLocation().size();j++){
				SesameLocation l= sCity.getLocation().get(j);
				l.setDd(GetInfoSesameSocialEvent.getDistanceDuration(sloc, l));
			}
			scity.add(sCity);
		}
	}

	/**
	 * Get a String from all location for JavaScript array
	 * @return A String content all location (events and places) to add into JS array
	 */
	public String jspAddMarkersString(){
		String jsAddMarkers = "";
		for(int i=0;i<scity.size();i++){
	    	for(int j=0;j<scity.get(i).getLocation().size();j++){
	    		SesameLocation l = scity.get(i).getLocation().get(j);
	    		for(int k=0;k<l.getEvents().size();k++){
	    			uri_id.put(l.getEvents().get(k).getUri().stringValue(), id_count);
	    			jsAddMarkers+=("addMarker("+l.getLatitude()+","+l.getLongitude()+",\""+l.getEvents().get(k).getName().replace(',', ' ')+"\","+id_count+");");
	    			id_count++;
	    		}
				for(int k=0;k<l.getPlaces().size();k++){
					uri_id.put(l.getPlaces().get(k).getUri().stringValue(), id_count);
					jsAddMarkers+=("addMarker("+l.getLatitude()+","+l.getLongitude()+",\""+l.getPlaces().get(k).getName().replace(',', ' ')+"\","+id_count+");");
					id_count++;
				}
	    	}
    	}
		return jsAddMarkers;
	}
	
	/**
	 * Create places table for one category
	 * @param category (Hotel,Restaurant or Touristic site)
	 * @return String containing html
	 */
	public String jspPlacesString(String type){
		String res ="";
		for (int h = 0; h < scity.size(); h++) {
			SesameCity sc = scity.get(h);
			for (int i = 0; i < sc.getLocation().size(); i++) {
				SesameLocation sl = sc.getLocation().get(i);
				for (int j = 0; j < sl.getPlaces().size(); j++) {
					SesamePlace sp = sl.getPlaces().get(j);
					if (sp.getTopLevelCat()
							.equals(type)) {
						res+=("<tr>");
						int numId=uri_id.get(sp.getUri().stringValue());
						res+=("<td><INPUT type=\"checkbox\" checked name=\"checkDict\" value=\""+numId+"\"onchange=\"modification("+numId+",this.checked);\"></td>");
						if (sp instanceof SesameFacebookPlace)
							res+=("<td><img src=\"fb.png\"/></td>");
						else if (sp instanceof SesameFoursquarePlace)
							res+=("<td><img src=\"fs.png\"/></td>");
						else if (sp instanceof SesameGooglePlace)
							res+=("<td><img src=\"google.png\"/></td>");
						else
							res+=("<td></td>");
						res+=("<td><img onClick=\"javascript:ouvre_popup('sesamePic.jsp?uri="+ sp.getUri()+ "','pic');\" src=\""+ sp.getSmallPic() + "\"/></td>");
						res+=("<td><a href=" + sp.getLink() + ">" + sp.getName() + "</a></td>");
						res+=("<td>" + sp.getType() + "</td>");
						if (sp instanceof SesameFacebookPlace) {
							res+=("<td>" + ((SesameFacebookPlace) sp).getLikeCount() + "</td>");
							res+=("<td>" + ((SesameFacebookPlace) sp).getWereHereCount() + "</td>");
						} else if (sp instanceof SesameFoursquarePlace) {
							res+=("<td>" + ((SesameFoursquarePlace) sp).getLikeCount() + "</td>");
							res+=("<td>" + ((SesameFoursquarePlace) sp).getWereHereCount() + "</td>");
						} else if (sp instanceof SesameGooglePlace) {
							res+=("<td>" + ((SesameGooglePlace) sp).getLikeCount() + "</td>");
							res+=("<td>" + ((SesameGooglePlace) sp).getWereHereCount() + "</td>");
						} else {
							res+=("<td/><td/>");
						}
						res+=("<td>" + sl.getStreet() + ", "
								+ sc.getZip() + " " + sc.getName() + ","
								+ sc.getCountry().getName() + "</td>");
						res+=("<td>" + sl.getLatitude() + ","
								+ sl.getLongitude() + "</td>");
						res+=("<td>"+format.format(sl.getDd().getWalkDistance()*0.001d)+"</td>");
						res+=("<td>"+sl.getDd().getWalkDuration()/60+"</td>");
						res+=("<td>"+format.format(sl.getDd().getDriveDistance()*0.001d)+"</td>");
						res+=("<td>"+sl.getDd().getDriveDuration()/60+"</td>");
						res+=("</tr>");
					}
				}
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
		for (int h = 0; h < scity.size(); h++) {
			SesameCity sc = scity.get(h);
			for (int i = 0; i < sc.getLocation().size(); i++) {
				SesameLocation sl = sc.getLocation().get(i);
				for (int j = 0; j < sl.getEvents().size(); j++) {
					SesameEvent se = sl.getEvents().get(j);
					res+=("<tr>");
					int numId=uri_id.get(se.getUri().stringValue());
					res+=("<td><INPUT type=\"checkbox\" checked name=\"checkDict\" value=\""+numId+"\"onchange=\"modification("+numId+",this.checked);\"></td>");
					if (se instanceof SesameFacebookEvent)
						res+=("<td><img src=\"fb.png\"/></td>");
					else
						res+=("<td></td>");
					res+=("<td><img onClick=\"javascript:ouvre_popup('sesamePic.jsp?t=events&id="+i+ "','pic');\" src=\""+ se.getSmallPic()+ "\" width=\"50px\"/></td>");
					res+=("<td><a href=" + se.getLink() + ">"+ se.getName() + "</td></a>");
					String descr = se.getDescription();
					if (descr == null)
						descr = "-";
					if (descr.length() > 50)
						descr = descr.substring(0, 50);
					res+=("<td>" + descr + "</td>");
					res+=("<td>" + se.getStart() + "</td>");
					res+=("<td>" + se.getEnd() + "</td>");
					if (se instanceof SesameFacebookEvent) {
						res+=("<td>"+ ((SesameFacebookEvent) se).getInvitesCount() + "</td>");
						res+=("<td>"+ ((SesameFacebookEvent) se).getAttendingCount() + "</td>");
					} else {
						res+=("<td/><td/>");
					}
					res+=("<td>" + sl.getStreet() + ", "+ sc.getZip() + " " + sc.getName() + ","+ sc.getCountry().getName() + "</td>");
					res+=("<td>" + sl.getLatitude() + ","+ sl.getLongitude() + "</td>");
					res+=("<td>"+format.format(sl.getDd().getWalkDistance()*0.001d)+"</td>");
					res+=("<td>"+sl.getDd().getWalkDuration()/60+"</td>");
					res+=("<td>"+format.format(sl.getDd().getDriveDistance()*0.001d)+"</td>");
					res+=("<td>"+sl.getDd().getDriveDuration()/60+"</td>");
					res+=("</tr>");
				}
			}
		}
		res+="</table>";
		return res;
	}
}
