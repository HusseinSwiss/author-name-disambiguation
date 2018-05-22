package servlet;
import model.GetInfoFacebook;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static constant.Cnst.*;

/**
 * Servlet for search data on Facebook/Foursquare, verify if user is connected to facebook
 * @author Damien
 *
 */
public class SearchForm extends HttpServlet{
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		HttpSession session = request.getSession();
		if(session.getAttribute(ATTR_FB_OBJ)==null){
			GetInfoFacebook fb = new GetInfoFacebook();
			session.setAttribute(ATTR_FB_OBJ, fb);
			response.sendRedirect(fb.getAuthorizationUrl());
		}else{
			if(((GetInfoFacebook)session.getAttribute(ATTR_FB_OBJ)).isConnected()){
				this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/searchForm.jsp" ).forward( request, response );
			}else{
				GetInfoFacebook fb=(GetInfoFacebook)session.getAttribute(ATTR_FB_OBJ);
				response.sendRedirect(fb.getAuthorizationUrl());
			}
		}
		System.out.print("ttttttttttttttttttttttttttttttttttttttttttt");
    }
}
