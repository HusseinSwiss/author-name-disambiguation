package servlet.sesame;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.sesame.RDFSStoreSocialEvent;

import org.openrdf.model.Value;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import constant.Cnst;
/**
 * This Servlet is use to get a list of all the countries in sesame
 * @author Damien Goetschi
 *
 */
public class GetCountries  extends HttpServlet{
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		response.addHeader("ENCODING", "ISO-8859-1");
		try{
			//Connect to RDFSStore
			new RDFSStoreSocialEvent(Cnst.REPOSITORY_HOSTNAME, Cnst.REPOSITORY_PORT, Cnst.REPOSITORY_ID_SOCIAL_EVENT);
			//Create request to get all URI of object of type Country
			String requestCountry = "SELECT ?subject WHERE{?subject <"+ RDF.TYPE +"> <"+ Cnst.COUNTRY +">}";
			TupleQueryResult resCountry = RDFSStoreSocialEvent.execSelectQuery(requestCountry);
			String sOut = "";
			//for each country found, search the name end add to String sOut separed by |
			while(resCountry.hasNext())
			{
				if(!sOut.equals(""))
					sOut+="|";
				BindingSet test = resCountry.next();
				Binding s = test.getBinding("subject");
				String requestName = "SELECT ?object WHERE{<"+ s.getValue() +"> <"+ Cnst.HAS_NAME_SOCIAL_EVENT +"> ?object}";
				TupleQueryResult resName = RDFSStoreSocialEvent.execSelectQuery(requestName);
				Value name ;
				if(resName.hasNext())
				{
					BindingSet names = resName.next();
					name = names.getBinding("object").getValue();
					sOut+=(s.getValue()+"|"+name.stringValue());
				}
			}
			response.getWriter().print(sOut);
		} catch (QueryEvaluationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RepositoryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedQueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
