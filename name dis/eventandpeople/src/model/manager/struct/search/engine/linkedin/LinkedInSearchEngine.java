package model.manager.struct.search.engine.linkedin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.scribe.model.Token;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

import beans.LinkedInCompany;
import beans.LinkedInPerson;
import beans.LinkedInPosition;
import model.navigator.Navigator;
import model.manager.struct.search.engine.bing.BingSearchEngine;

/**
 * This class do search linkedIn profiles on Bing and get result in ArrayList
 * @author Damien Goetschi
 */
public class LinkedInSearchEngine{

	public static String DETECTION_WORD = "linkedin.com";
	
	/**
	 * Get result for a query
	 * @param query the name of person to search
	 * @return a ArrayList of LinkedInPerson match with query name
	 */
	public static ArrayList<LinkedInPerson> getResults(String query) {
		ArrayList<LinkedInPerson> searchResults = new ArrayList<>();
		ArrayList<String> bingResults = BingSearchEngine.getBingResult(query + " +linkedin");
		for (String bingResult : bingResults) {
			if (bingResult.contains(DETECTION_WORD)) {
				String publicProfileURL = bingResult;
				String u = "";
				try {
					u = URLEncoder.encode(publicProfileURL, "Cp1252");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
				String url = "http://api.linkedin.com/v1/people/url=" + u + ":"
						+ buildArgs() + "?format=json";
				String content = Navigator.getInstance().getPage(url).getContent();
				try {
					LinkedInPerson p = parseContent(content,publicProfileURL);
					if(p!=null&&p.getFormattedName()!=null)
						searchResults.add(p);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return searchResults;
	}
	
	/**
	 * Parse a JSON String into ArrayList of LinkedInPerson
	 * @param content JSON String content result of freebase query
	 * @return a ArrayList of LinkedInPerson was contained in the JSON String
	 */
	private static LinkedInPerson parseContent(String content,String url) throws JsonParseException, IOException{
		LinkedInPerson person = new LinkedInPerson(url);
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(content);
		//If json don't begin with apiStandardProfileRequest there is a error and must return null
		jp.nextToken();
		while(jp.getText()!=null&&!jp.getText().equals("{")){
			jp.nextToken();
		}
		if(jp.getText()==null)
			return null;
		while(jp.nextToken()!=null){
			switch(jp.getText()){
				//Public fields
				case "firstName":
					jp.nextToken();
					person.setFirstName(jp.getText());
					break;
				case "formattedName":
					jp.nextToken();
					person.setFormattedName(jp.getText());
					break;
				case "lastName":
					jp.nextToken();
					person.setLastName(jp.getText());
					break;
				case "headline":
					jp.nextToken();
					person.setHeadline(jp.getText());
					break;
				case "id":
					jp.nextToken();
					person.setId(jp.getText());
					break;
				case "industry":
					jp.nextToken();
					person.setIndustry(jp.getText());
					break;
				case "code":
					jp.nextToken();
					person.setCountry(jp.getText());
					break;
				case "name":
					jp.nextToken();
					person.setLocationName(jp.getText());
					break;
				case "pictureUrl":
					jp.nextToken();
					person.setPicture(jp.getText());
					break;	
				case "summary":
					jp.nextToken();
					person.setSummary(jp.getText());
					break;	
				case "specialties":
					jp.nextToken();
					person.setSpecialties(jp.getText());
					break;
				case "positions":
					while(jp.getText()!=null&&!jp.getText().equals("_total")){
						jp.nextToken();
					}
					jp.nextToken();
					int nb = jp.getIntValue();
					for(int i=0;i<nb;i++){
						LinkedInPosition lip = new LinkedInPosition();
						jp.nextToken();
						while(jp.getText()!=null&&!jp.getText().equals("}")){
							switch(jp.getText()){
							case "company":
								jp.nextToken();
								LinkedInCompany lic = new LinkedInCompany();
								while(jp.getText()!=null&&!jp.getText().equals("}")){
									switch(jp.getText()){
									case "industry":
										jp.nextToken();
										lic.setIndustry(jp.getText());
										break;
									case "name":
										jp.nextToken();
										lic.setName(jp.getText());
										break;
									case "id":
										jp.nextToken();
										lic.setId(jp.getIntValue());
										break;
									case "type":
										jp.nextToken();
										lic.setType(jp.getText());
										break;
									case "size":
										jp.nextToken();
										lic.setSize(jp.getText());
										break;
									}
									jp.nextToken();
								}
								lip.setCompany(lic);
								break;
							case "id":
								jp.nextToken();
								lip.setId(jp.getIntValue());
								break;
							case "startDate":
								while(jp.getText()!=null&&!jp.getText().equals("year")){
									jp.nextToken();
								}
								jp.nextToken();
								lip.setStartYear(jp.getIntValue());
								jp.nextToken();
								break;
							case "endDate":
								while(jp.getText()!=null&&!jp.getText().equals("year")){
									jp.nextToken();
								}
								jp.nextToken();
								lip.setEndYear(jp.getIntValue());
								jp.nextToken();
								break;
							case "title":
								jp.nextToken();
								lip.setTitle(jp.getText());
								break;
							case "isCurrent":
								jp.nextToken();
								lip.setCurrent(jp.getBooleanValue());
								break;
							case "summary":
								jp.nextToken();
								lip.setSummary(jp.getText());
								break;
							}
							jp.nextToken();
						}
						person.addPosition(lip);
					}
					break;
					 
										 	
				//Can be private fields
			}
		}
		return person;
	}

	/**
	 * List of fields to get information about
	 */
	private static String[] fields = { "id", "first-name", "last-name",
			"maiden-name", "formatted-name", "headline",
			"location:(name,country:(code))", "publications:(id,title,publisher,authors,date,url,summary)", "industry", "summary" ,
			"specialties", "positions", "picture-url",
			"site-standard-profile-request",
			"api-standard-profile-request:(url,headers)", "public-profile-url"};

	/**
	 * Build a args String with content of String[] fields
	 * @return String with args for query to LinkedIn
	 */
	private static String buildArgs() {

		String args = "";

		for (String field : fields) {
			args += field + ",";
		}

		if (args.length() > 0)
			args = args.substring(0, args.length() - 1);

		return "(" + args + ")";
	}
}
