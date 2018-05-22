package servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GetInfoGoogleMaps;
import facebook4j.GeoLocation;
/**
 * This Servlet is use for Autocompletion of address
 * @author Damien Goetschi
 *
 */
public class Autocomplete extends HttpServlet {
	/**
	 * Take city and search params and print the result (proposition separed by | )
	 */
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		GeoLocation gl = GetInfoGoogleMaps.getGeocode(request.getParameter("city"));
		response.getWriter().print(GetInfoGoogleMaps.getAutocomplete(request.getParameter("search"), gl));
		GetInfoGoogleMaps.writeAutocompleteCache();
		GetInfoGoogleMaps.writeGeoLocationCache();
	}
}
