package servlet;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.GetInfoFacebook;

import static constant.Cnst.*;

/**
 * Servlet for manage the Facebook connection of the user
 * @author Damien Goetschi
 *
 */
public class Conn extends HttpServlet {
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		GetInfoFacebook fb=(GetInfoFacebook)request.getSession().getAttribute(ATTR_FB_OBJ);
		//if the login return a error -> show a page with infos and a link for connect the user
		if(request.getParameter("error")!=null){
			request.setAttribute("url", fb.getAuthorizationUrl());
			this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/infos.jsp" ).forward( request, response );
		}
		//else show the index page in witch user can send request
		else{
			fb.getAccessToken(request.getParameter("code"));
			this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/searchForm.jsp" ).forward( request, response );
		}
	}
}
