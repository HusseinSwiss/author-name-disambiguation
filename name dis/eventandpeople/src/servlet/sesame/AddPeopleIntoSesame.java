package servlet.sesame;

import static constant.Cnst.*;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.Normalizer;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.sesame.GetInfoSesamePeopleSearch;
import model.sesame.RDFSStorePeopleSearch;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.repository.RepositoryException;

import beans.*;

public class AddPeopleIntoSesame extends HttpServlet {
	/**
	 * Add all selected peoples to sesame database.
	 */
	public void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		//get array with ids of freebase people to add
		String[] resFreebase = request.getParameterValues("freebase");
		
		if(resFreebase!=null){
			ArrayList<FreebasePerson> freebaseList = (ArrayList<FreebasePerson>)request.getSession().getAttribute(ATTR_FREEBASE_LIST);
			for(int i=0;i<resFreebase.length;i++){
				//Get data about people
				FreebasePerson currentPerson = freebaseList.get(Integer.valueOf(resFreebase[i]));
				//Add data in sesame
				try {
					URI personURI=GetInfoSesamePeopleSearch.addFreebasePerson(currentPerson);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			response.getWriter().println("<html><body>Peoples from Freebase added to the DataBase<br/>");
		}else
			response.getWriter().println("<html><body>No data from Freebase to add<br/>");
		//get array with ids of freebase people to add
		String[] resLinkedIn = request.getParameterValues("linkedin");
		
		if(resLinkedIn!=null){
			ArrayList<LinkedInPerson> linkedInList = (ArrayList<LinkedInPerson>)request.getSession().getAttribute(ATTR_LINKEDIN_LIST);
			for(int i=0;i<resLinkedIn.length;i++){
				//Get data about people
				LinkedInPerson currentPerson = linkedInList.get(Integer.valueOf(resLinkedIn[i]));
				//Add data in sesame
				try {
					URI personURI=GetInfoSesamePeopleSearch.addLinkedInPerson(currentPerson);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			response.getWriter().println("Peoples from LinkedIn added to the DataBase<br/><button onclick=\"window.location.href='result.jsp';\" >Back</button></body></html>");
		}else
			response.getWriter().println("No data from LinkedIn to add<br/><button onclick=\"window.location.href='result.jsp';\" >Back</button></body></html>");

	}
}