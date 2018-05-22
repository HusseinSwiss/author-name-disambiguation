package servlet;

import static constant.Cnst.ATTR_FB_OBJ;
import static constant.Cnst.NB_RESULT;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import twitter4j.ResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

import constant.Cnst;

import facebook4j.FacebookException;

import model.GetInfoFacebook;
import model.GetInfoGoogleMaps;

import beans.SocialDistanceDuration;
import beans.SocialEvent;
import beans.SocialLocation;
import beans.SocialPlace;
import beans.facebookPerson;

/**
 * Servlet implementation class FacebookResult
 */
@WebServlet("/FacebookResult")
public class FacebookResult extends HttpServlet {
	
	
	private GetInfoFacebook fb;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public FacebookResult() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.getServletContext().getRequestDispatcher( "/WEB-INF/peoplesearch/facebookResult.jsp" ).forward( request, response );
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.getServletContext().getRequestDispatcher( "/WEB-INF/peoplesearch/facebookResult.jsp" ).forward( request, response );
		String fbname = request.getParameter("fbname");
		fb=(GetInfoFacebook)request.getSession().getAttribute(ATTR_FB_OBJ);
		
			 
				print1();
			 
	}

	public void print() throws  FacebookException{
	//ArrayList<facebookPerson> fbp  = fb.getPeople("hussein hazimeh");
	 //fb.getfbName("hussein hazimeh");
		fb.testfbplace();
	
	}	
	public void print1(){
		ConfigurationBuilder cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true)
		  .setOAuthConsumerKey("fF1piPU2WgxHGp7K76cA")
		  .setOAuthConsumerSecret("iPZFo3KzpwCfiZ2Ow5UqH175sjA5fo5bxSCBNYYmAEI")
		  .setOAuthAccessToken("775612814-b8DVOeG5Vw1Spvdg5uAwn80fxGZv5EixkWTZzmpG")
		  .setOAuthAccessTokenSecret("tDQoSJiw0bs64VbpmhO5vrh2S1u6zg9bzzI7TDBGtOSPW");
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		try {
			ResponseList<User> u = twitter.searchUsers("hassan makki", 1);
			System.out.print(u.get(1).getDescription() + "\n " + u.get(1).getFollowersCount() +" \n" + u.get(1).getLocation() + "\n " +
			u.get(1).getLang() + "\n " + u.get(1).getName() + "\n " + u.get(1).getBiggerProfileImageURL() +
			"\n " + u.get(1).getId() + "\n " + u.get(1).getOriginalProfileImageURL() + " ");
			
		} catch (TwitterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
