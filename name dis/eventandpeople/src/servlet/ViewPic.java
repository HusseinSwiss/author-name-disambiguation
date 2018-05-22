package servlet;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import constant.Cnst;
import beans.SocialEvent;
import beans.SocialPlace;
/**
 * Servlet to show a bigger picture of the event/place
 * @author Damien Goetschi
 *
 */
public class ViewPic extends HttpServlet {
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		String type = request.getParameter("t");
		int i =	Integer.valueOf(request.getParameter("id"));
		String jsArray ="";
		ArrayList<String> pics;
		if(type.equals("places")){
			pics=((ArrayList<SocialPlace>)request.getSession().getAttribute(Cnst.ATTR_PLACES_LIST)).get(i).getLargePic();
		}else{
			pics=((ArrayList<SocialEvent>)request.getSession().getAttribute(Cnst.ATTR_EVENTS_LIST)).get(i).getLargePic();
		}
		//Create String to add information to a JavaScript array
		for(int j =0;j<pics.size();j++){
			jsArray+="arr["+j+"]='"+pics.get(j)+"';";
		}
		request.setAttribute(Cnst.ATTR_JS_PICS, jsArray);
		this.getServletContext().getRequestDispatcher( "/WEB-INF/socialevent/pic.jsp" ).forward( request, response );
	}
}
