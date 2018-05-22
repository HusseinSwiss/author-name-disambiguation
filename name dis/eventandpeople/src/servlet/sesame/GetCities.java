package servlet.sesame;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.sesame.RDFSStoreSocialEvent;

import org.openrdf.model.Value;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import constant.Cnst;
/**
 * This Servlet is use to get a list of all the cities of a country
 * @author Damien Goetschi
 *
 */
public class GetCities  extends HttpServlet{
	public void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
		response.addHeader("ENCODING", "ISO-8859-1");
		try{
			//Connect to RDFSStore
			new RDFSStoreSocialEvent(Cnst.REPOSITORY_HOSTNAME, Cnst.REPOSITORY_PORT, Cnst.REPOSITORY_ID_SOCIAL_EVENT);
			//Create request to get all URI of city from selected country
			String requestCity = "SELECT ?subject WHERE{?subject <"+ Cnst.IS_CITY_FROM +"> <"+ request.getParameter("country") +">}";
			TupleQueryResult resCity = RDFSStoreSocialEvent.execSelectQuery(requestCity);
			ArrayList<String> cityList = new ArrayList<String>();
			//for each city, search the name and add it in arraylist only if it's not already
			while(resCity.hasNext())
			{
				BindingSet test = resCity.next();
				Binding s = test.getBinding("subject");
				String requestName = "SELECT ?object WHERE{<"+ s.getValue() +"> <"+ Cnst.HAS_NAME_SOCIAL_EVENT +"> ?object}";
				TupleQueryResult resName = RDFSStoreSocialEvent.execSelectQuery(requestName);
				Value name ;
				if(resName.hasNext()){
					BindingSet names = resName.next();
					name = names.getBinding("object").getValue();
					if(cityList.indexOf(name.stringValue())==-1&&!name.stringValue().equals("")){
						cityList.add(name.stringValue());
					}
				}
			}
			//create String with all cities in ArrayList
			String sOut="";
			for(int i=0;i<cityList.size();i++){
				if(i!=0){
					sOut+="|";
				}
				sOut+=cityList.get(i);
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
