package servlet.sesame;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * Servlet of form to search data in Sesame, just redirect to the JSP
 * @author Damien Goetschi
 *
 */
public class SesameSearchForm extends HttpServlet{
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/sesameSearchForm.jsp" ).forward( request, response );
	}
}
