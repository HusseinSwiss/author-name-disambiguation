package servlet.sesame;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.Cnst;
import model.sesame.GetInfoSesameSocialEvent;
/**
 * Servlet to show a bigger picture of the event/place
 * @author Damien Goetschi
 *
 */
public class SesameViewPic  extends HttpServlet {
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		String uri = request.getParameter("uri");
		String jsArray ="";
		ArrayList<String> pics = GetInfoSesameSocialEvent.getPics(uri);
		//Create String to add information to a JavaScript array
		for(int j =0;j<pics.size();j++){
			jsArray+="arr["+j+"]='"+pics.get(j)+"';";
		}
		request.setAttribute(Cnst.ATTR_JS_PICS, jsArray);
		this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/pic.jsp" ).forward( request, response );
	}
}
