package model.manager.struct.search.engine.freebase;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

import model.navigator.Navigator;
import beans.FreebaseEducation;
import beans.FreebaseEmployment;
import beans.FreebasePerson;
/**
 * This class do search in Freebase by name or by profession
 * @author Damien Goetschi
 */
public class FreebaseSearchEngine {
	/**
	 * URL of freebase API for queries
	 */
	public static String baseUrl="https://www.googleapis.com/freebase/v1/mqlread/?query=";
	
	/**
	 * Get result for a query
	 * @param query the name of person to search
	 * @return a ArrayList of FreebasePerson match with query name
	 */
	public static ArrayList<FreebasePerson> getResults(String query){
		String url="";
		try {
			url=baseUrl+URLEncoder.encode(buildArgs(query), "Cp1252");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		String content = Navigator.getInstance().getPage(url).getContent();
		try {
			return parseContent(content);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Get result for a query
	 * @param query the profession of person to search
	 * @return a ArrayList of FreebasePerson match with query profession
	 */
	public static ArrayList<FreebasePerson> getProfessionResults(String query) {
		String url="";
		try {
			url=baseUrl+URLEncoder.encode(buildArgsProfession(query), "Cp1252");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		String content = Navigator.getInstance().getPage(url).getContent();
		try {
			return parseContent(content);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Build the parameters for query people by name
	 * @param query name of person
	 * @return String with all parameters for the search
	 */
	private static String buildArgs(String query){
		String args =	"";
		args+="[{";
		args+="\"name~=\": \""+query+"\",";
		args+=dataToGetArgs();
		args+="}]";
		return args;
	}
	
	/**
	 * Build the parameters for query people by profession
	 * @param query profession of person
	 * @return String with all parameters for the search
	 */
	private static String buildArgsProfession(String query){
		String args =	"";
		args+="[{";
		args+="\"ns0:profession\": \""+query+"\",";
		args+=dataToGetArgs();
		args+="}]";
		return args;
	}
	
	/**
	 * 
	 * @return list of fields for getting datas
	 */
	private static String dataToGetArgs(){
		String args = "";
		args+="\"name\": null,";
		args+="\"type\": \"/people/person\",";
		args+="\"id\": null,";
		args+="\"date_of_birth\": null,";
		args+="\"place_of_birth\": null,";
		args+="\"nationality\": [],";
		args+="\"gender\": null,";
		args+="\"religion\": [],";
		args+="\"profession\": [],";
		args+="\"ethnicity\": [],";
		args+="\"employment_history\": [{}],";
		args+="\"education\": [{}],";
		args+="\"quotations\": [],";
		args+="\"limit\": 5";
		return args;
	}
	
	/**
	 * Parse a JSON String into ArrayList of FreebasePerson
	 * @param content JSON String content result of freebase query
	 * @return a ArrayList of FreebasePerson was contained in the JSON String
	 */
	private static ArrayList<FreebasePerson> parseContent(String content) throws JsonParseException, IOException{
		ArrayList<FreebasePerson> persons = new ArrayList<>();
		FreebasePerson person=null;
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(content);		
		jp.nextToken();
		while(jp.getText()!=null&&!jp.getText().equals("result")){
			jp.nextToken();
		}
		if(jp.getText()==null)
			return null;
		while(jp.nextToken()!=null){
			switch(jp.getText()){
				case "{":
					person = new FreebasePerson();
					break;
				case "}":
					if(!persons.contains(person)&&person!=null&&person.getId()!=null&&person.getName()!=null&&person.getUrl()!=null){
						persons.add(person);
					}
					break;
				case "name":
					jp.nextToken();
					if(!jp.getText().equals("null"))
						person.setName(jp.getText());
					break;
				case "id":
					jp.nextToken();
					if(!jp.getText().equals("null"))
						person.setId(jp.getText());
					break;
				case "place_of_birth":
					jp.nextToken();
					if(!jp.getText().equals("null"))
						person.setBirthPlace(jp.getText());
					break;
				case "ethnicity":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						if(!jp.getText().equals("[")&&!jp.getText().equals("ethnicity")&&!jp.getText().equals("null")){
							person.addEthnicity(jp.getText());
						}
						jp.nextToken();
					}
					break;
				case "gender":
					jp.nextToken();
					if(!jp.getText().equals("null"))
						person.setGender(jp.getText());
					break;
				case "date_of_birth":
					jp.nextToken();
					if(!jp.getText().equals("null"))
						person.setBirthDate(jp.getText());
					break;
				case "religion":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						if(!jp.getText().equals("[")&&!jp.getText().equals("religion")&&!jp.getText().equals("null")){
							person.addReligion(jp.getText());
						}
						jp.nextToken();
					}
					break;
				case "quotations":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						if(!jp.getText().equals("[")&&!jp.getText().equals("quotations")&&!jp.getText().equals("null")){
							person.addQuotation(jp.getText());
						}
						jp.nextToken();
					}
					break;
				case "profession":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						if(!jp.getText().equals("[")&&!jp.getText().equals("profession")&&!jp.getText().equals("null")){
							person.addProfession(jp.getText());
						}
						jp.nextToken();
					}
					break;
				case "nationality":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						if(!jp.getText().equals("[")&&!jp.getText().equals("nationality")&&!jp.getText().equals("null")){
							person.addNationality(jp.getText());
						}
						jp.nextToken();
					}
					break;
				case "employment_history":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						switch(jp.getText()){
						case "id":
							jp.nextToken();
							person.addEmployment(getEmployment(jp.getText()));
							break;
						case "type":
							while(jp.getText()!=null&&!jp.getText().equals("]"))
								jp.nextToken();
							break;
						}
						jp.nextToken();
					}
					break;
				case "education":
					while(jp.getText()!=null&&!jp.getText().equals("]")){
						switch(jp.getText()){
						case "id":
							jp.nextToken();
							person.addEducation(getEducation(jp.getText()));
							break;
						case "type":
							while(jp.getText()!=null&&!jp.getText().equals("]"))
								jp.nextToken();
							break;
						}
						jp.nextToken();
					}
					break;
			}
		}
		return persons;
	}
	
	/**
	 * Build the parameters for query employment by id
	 * @param query id of employment
	 * @return String with all parameters for the search
	 */
	private static String buildArgsEmployment(String id){
		String args="";
		args+="[{";
		args+="\"id\": \""+id+"\",";
		args+="\"type\": \"/business/employment_tenure\",";
		args+="\"company\": null,";
		args+="\"title\": null,";
		args+="\"from\": null,";
		args+="\"to\": null";
		args+="}]";
		return args;
	}
	
	/**
	 * Build the parameters for query education by id
	 * @param query id of education
	 * @return String with all parameters for the search
	 */
	private static String buildArgsEducation(String id){
		String args="";
		args+="[{";
		args+="\"id\": \""+id+"\",";
		args+="\"type\": \"/education/education\",";
		args+="\"institution\": null,";
		args+="\"start_date\": null,";
		args+="\"end_date\": null,";
		args+="\"degree\": null,";
		args+="\"major_field_of_study\": null,";
		args+="\"specialization\": null";
		args+="}]";
		return args;
	}
	
	/**
	 * Search a employment by id on freebase
	 * @param id ID of employment
	 * @return a FreebaseEmployment for this employment
	 */
	public static FreebaseEmployment getEmployment(String id){
		String url="";
		try {
			url=baseUrl+URLEncoder.encode(buildArgsEmployment(id), "Cp1252");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		String content = Navigator.getInstance().getPage(url).getContent();
		try {
			return parseEmploymentContent(content);
		} catch (IOException e) {
			return null;
		}
	}
	
	
	/**
	 * Search a education by id on freebase
	 * @param id ID of education
	 * @return a FreebaseEducation for this employment
	 */
	public static FreebaseEducation getEducation(String id){
		String url="";
		try {
			url=baseUrl+URLEncoder.encode(buildArgsEducation(id), "Cp1252");
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		String content = Navigator.getInstance().getPage(url).getContent();
		try {
			return parseEducationContent(content);
		} catch (IOException e) {
			return null;
		}
	}
	
	/**
	 * Parse a JSON String into a FreebaseEmployment
	 * @param content JSON String containing result of freebase query
	 * @return the FreebaseEmployment that was contained in the JSON String
	 */
	private static FreebaseEmployment parseEmploymentContent(String content) throws IOException {
		FreebaseEmployment fe = new FreebaseEmployment();
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(content);
		
		jp.nextToken();
		while(jp.getText()!=null&&!jp.getText().equals("result")){
			jp.nextToken();
		}
		if(jp.getText()==null)
			return null;
		while(jp.nextToken()!=null){
			switch(jp.getText()){
			case "to":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setTo(jp.getText());
				break;
			case "from":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setFrom(jp.getText());
				break;
			case "company":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setEmployer(jp.getText());
				break;
			case "title":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setTitle(jp.getText());
				break;
			case "id":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setId(jp.getText());
				break;
			}
		}
		return fe;
	}
	
	/**
	 * Parse a JSON String into a FreebaseEducation
	 * @param content JSON String containing result of freebase query
	 * @return the FreebaseEducation that was contained in the JSON String
	 */
	private static FreebaseEducation parseEducationContent(String content) throws IOException {
		FreebaseEducation fe = new FreebaseEducation();
		JsonFactory f = new JsonFactory();
		JsonParser jp = f.createJsonParser(content);
		
		jp.nextToken();
		while(jp.getText()!=null&&!jp.getText().equals("result")){
			jp.nextToken();
		}
		if(jp.getText()==null)
			return null;
		while(jp.nextToken()!=null){
			switch(jp.getText()){
			case "start_date":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setStart_date(jp.getText());
				break;
			case "end_date":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setEnd_date(jp.getText());
				break;
			case "degree":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setDegree(jp.getText());
				break;
			case "institution":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setInstitution(jp.getText());
				break;
			case "id":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setId(jp.getText());
				break;
			case "major_field_of_study":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setMajor_field_of_study(jp.getText());
				break;
			case "specialization":
				jp.nextToken();
				if(!jp.getText().equals("null"))
					fe.setSpecialization(jp.getText());
				break;
			}
		}
		return fe;
	}
	
}
