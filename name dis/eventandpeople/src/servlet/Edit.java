package servlet;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GetInfoGoogleMaps;
import facebook4j.GeoLocation;
import static constant.Cnst.*;
import beans.SocialDistanceDuration;
import beans.SocialEvent;
import beans.SocialLocation;
import beans.SocialPlace;
/**
 * Servlet for add/edit data
 * @author Damien Goetschi
 *
 */
public class Edit extends HttpServlet{
	/**
	 * when we receive data with get, we show a page with current data of a place/location
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/edit.jsp" ).forward( request, response );
	}

	/**
	 * when we receive data with post method, (from form) we add or edit the data
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		String type = request.getParameter("t");
		int i =	Integer.valueOf(request.getParameter("id"));
		if(type.equals(TYPE_PLACE)){
			//Executing changes
			SocialPlace p;
			if(i!=-1) p= ((ArrayList<SocialPlace>)request.getSession().getAttribute(ATTR_PLACES_LIST)).get(i);
			else p = new SocialPlace();
			p.setName(request.getParameter("name"));
			p.setType(request.getParameter("type"));
			p.setUrl(request.getParameter("url"));
			p.setTopLevelCat(request.getParameter("topLevelCat"));
			SocialLocation l= p.getLocation();
			if((i==-1)||!(l.getStreet().equals(request.getParameter("street"))&&l.getZip().equals(request.getParameter("zip"))&&l.getCity().equals(request.getParameter("city"))&&l.getCountry().equals(request.getParameter("country")))){
				l.setStreet(request.getParameter("street"));
				l.setZip(request.getParameter("zip"));
				l.setCity(request.getParameter("city"));
				l.setCountry(request.getParameter("country"));
				GeoLocation gl = GetInfoGoogleMaps.getGeocode(l.toString());
				l.setLatitude(gl.getLatitude());
				l.setLongitude(gl.getLongitude());
				l.createID();
				p.setLocation(l);
				if(i!=-1)((ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_PLACES_DISTANCES)).set(i,GetInfoGoogleMaps.getSocialDistanceDuration((SocialLocation)request.getSession().getAttribute(ATTR_GEOLOC), l));
				else ((ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_PLACES_DISTANCES)).add(GetInfoGoogleMaps.getSocialDistanceDuration((SocialLocation)request.getSession().getAttribute(ATTR_GEOLOC), l));
			}
			if(p.getSite().equals(SITE_CUSTOM))
				p.createID();
			if(i!=-1)((ArrayList<SocialPlace>)request.getSession().getAttribute(ATTR_PLACES_LIST)).set(i, p);
			else ((ArrayList<SocialPlace>)request.getSession().getAttribute(ATTR_PLACES_LIST)).add(p);
			
		}else if(type.equals(TYPE_EVENT)){
			//Executing changes
			SocialEvent e;
			if(i!=-1) e = ((ArrayList<SocialEvent>)request.getSession().getAttribute(ATTR_EVENTS_LIST)).get(i);
			else e = new SocialEvent();
			e.setName(request.getParameter("name"));
			e.setDescription(request.getParameter("description"));
			e.setUrl(request.getParameter("url"));
			SocialLocation l = e.getLocation();
			if(request.getParameter("start")!=""){
				try {
					e.setStart(FORMATER.parse(request.getParameter("start")));
				} catch (ParseException e1) {
					e.setStart(null);
				}
			}
			if(request.getParameter("end")!=""){
				try {
					e.setEnd(FORMATER.parse(request.getParameter("end")));
				} catch (ParseException e1) {
					e.setEnd(null);
				}
			}
			if((i==-1)||!(l.getStreet().equals(request.getParameter("street"))&&l.getZip().equals(request.getParameter("zip"))&&l.getCity().equals(request.getParameter("city"))&&l.getCountry().equals(request.getParameter("country")))){
				l.setStreet(request.getParameter("street"));
				l.setZip(request.getParameter("zip"));
				l.setCity(request.getParameter("city"));
				l.setCountry(request.getParameter("country"));
				GeoLocation gl = GetInfoGoogleMaps.getGeocode(l.toString());
				l.setLatitude(gl.getLatitude());
				l.setLongitude(gl.getLongitude());
				l.createID();
				e.setLocation(l);
				if(i!=-1) ((ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_EVENTS_DISTANCES)).set(i,GetInfoGoogleMaps.getSocialDistanceDuration((SocialLocation)request.getSession().getAttribute(ATTR_GEOLOC), l));
				else ((ArrayList<SocialDistanceDuration>)request.getSession().getAttribute(ATTR_EVENTS_DISTANCES)).add(GetInfoGoogleMaps.getSocialDistanceDuration((SocialLocation)request.getSession().getAttribute(ATTR_GEOLOC), l));
			}
			if(e.getSite().equals(SITE_CUSTOM))
				e.createID();
			if(i!=-1) ((ArrayList<SocialEvent>)request.getSession().getAttribute(ATTR_EVENTS_LIST)).set(i, e);
			else ((ArrayList<SocialEvent>)request.getSession().getAttribute(ATTR_EVENTS_LIST)).add(e);
			
		}
		//Redirect page
		this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/done.jsp" ).forward( request, response );
	}

}
