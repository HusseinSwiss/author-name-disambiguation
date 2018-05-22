package model.matchers;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import model.Get_MAS_XML_Data;
import model.manager.struct.search.engine.linkedin.LinkedInSearchEngine;
import beans.LinkedInPerson;
import beans.LinkedinHTMLProfile;
import beans.Person;

public class LinkedInMatcher {

	/**
	 * @param args
	 */
	private ArrayList<LinkedInPerson> linkedInList = new ArrayList<LinkedInPerson>();
	private ArrayList<LinkedinHTMLProfile> linHtml = new ArrayList<LinkedinHTMLProfile>(); 
	private ArrayList<Person> persons = new ArrayList<Person>();

	public ArrayList<Person> getLinkedinProfiles(String name, ArrayList<Person> persons){
		getLinkedInData(name);
		//get first name and last name from api to use them in url as parameters 
		String lin_fname = linkedInList.get(0).getFirstName();
		String lin_lname = linkedInList.get(0).getLastName();
		/**
		 * merging between linkedin profiles (html and api merging)
		 * */ 
		//get xml url list of linkedin profiles with a specific name 
		ArrayList<String> urls = new ArrayList<String>();
		urls = createLinkedinXMLNamesList("http://localhost/linkedin/getUrls.php?fname="+lin_fname+"&lname="+lin_lname);
		for(int i=0;i<urls.size();i++){
			linHtml.add(getLinHTMLProfile(urls.get(i)));
		}       
		for(int i=0;i<linHtml.size();i++){
			System.out.println(linHtml.get(i).getFirst_name()+" "+linHtml.get(i).getLast_name());
			System.out.println(linHtml.get(i).getPublication());
			System.out.println(linHtml.get(i).getLocality()); 
			System.out.println(linHtml.get(i).getSummary());
			System.out.println(linHtml.get(i).getEducation());
			System.out.println(linHtml.get(i).getOrganization());
			System.out.println(linHtml.get(i).getImage());
			System.out.println("-----------------------------------");
		}
		System.out.println("matching with linkedin");
		for(int i=0;i<persons.size();i++){
			for(int j=0;j<linHtml.size();j++){
				String bio = compare(persons.get(i), linHtml.get(j));
				if(bio!=""){
					String [] info = bio.split("~");
					persons.get(i).setBiography(info[0]);
					System.out.println(info[1] + "inside");
					persons.get(i).setPhoto_url(info[1]);
				}
			}
		}
		for (int i = 0; i < persons.size(); i++) {
			System.out.println("Person  :" 
					+ persons.get(i).getId() + " photo: "
					+ persons.get(i).getPhoto_url() + " name : "
					+ persons.get(i).getFull_name() + " aff: "
					+ persons.get(i).getAffiliation() + " Summary: "
					+ persons.get(i).getBiography() + "Homepage: "
					+ persons.get(i).getHomepage()+ " Emial: " + persons.get(i).getEmail());
		}
		this.persons = persons;
		return persons;
	}

	public ArrayList<Person> get_profiles(){
		return this.persons;
	}

	private String compare(Person p, LinkedinHTMLProfile lin){
		int flag = 0; String bio = "";
		DBLP_MAS_Matcher m = new DBLP_MAS_Matcher();
		if(lin.getEducation() != null && p.getAffiliation() != null){
			String [] education_organizations_lin = lin.getEducation().split(",");
			String [] education_organizations_person = p.getAffiliation().split(","); 

			System.out.println("\n--------" + p.getFull_name() + ":::::" + lin.getFirst_name()+" "+lin.getLast_name() +"\n-----");
			System.out.println("matching organizations...");

			for(int i=0;i<education_organizations_person.length;i++){
				for(int j=0;j<education_organizations_lin.length;j++){
					System.out.println(education_organizations_person[i] + " () "+ education_organizations_lin[j]);
					System.out.println(m.string_match2(education_organizations_person[i], education_organizations_lin[j]));
					if(m.string_match2(education_organizations_person[i], education_organizations_lin[j])){
						System.out.println(education_organizations_person[i] + " () "+ education_organizations_lin[j]);
						bio = lin.getSummary() + "~" + lin.getImage();
						flag=1;
						System.out.println(bio + " for loop edu");
						break;
					}
				}
			}}

		if(flag == 0){
			if(lin.getOrganization() != null && p.getAffiliation() != null){
				String [] work_organizations_lin = lin.getOrganization().split(","); 
				String [] education_organizations_person = p.getAffiliation().split(","); 
				for(int i=0;i<education_organizations_person.length;i++){
					for(int j=0;j<work_organizations_lin.length;j++){
						System.out.println(education_organizations_person[i] + " () "+ work_organizations_lin[j]);
						System.out.println(m.string_match2(education_organizations_person[i], work_organizations_lin[j]));
						if(m.string_match2(education_organizations_person[i], work_organizations_lin[j])){
							System.out.println(education_organizations_person[i] + " () "+ work_organizations_lin[j]);
							bio = lin.getSummary() + "~" + lin.getImage();
							System.out.println(bio + " for loop org");
							break;
						}
					}
				}
			}
		}

		System.out.println(bio + " end");
		return bio;
	}	

	private void getLinkedInData(String name){
		ArrayList<LinkedInPerson> list = LinkedInSearchEngine.getResults(name);
		for (int i = 0; i < list.size(); i++) {
			String Name = list.get(i).getFirstName() + " " + list.get(i).getLastName();
			if(Name.toLowerCase().equals(name.toLowerCase())) 
				linkedInList.add(list.get(i));
		}
	}

	/**
	 * create and parse xml profile from html page
	 * @param url
	 * @return linkedin profile
	 */
	private LinkedinHTMLProfile getLinHTMLProfile(String url){
		System.out.println("Creating profies from html .... ::"  );
		LinkedinHTMLProfile linHtmlProfileObject = new LinkedinHTMLProfile();
		Get_MAS_XML_Data parseProfile = new Get_MAS_XML_Data();
		parseProfile.parse_to_file("http://localhost/linkedin/getProfileHtmlInfo.php?url="+url);
		System.out.println(url);
		try {
			File fXmlFile = new File("H:/wamp/www/linkedin/profile.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("profile");

			System.out.println("\n" + nList.getLength() + " html linkedin author(s) found");
			for (int temp = 0; temp < 1; temp++) {
				NodeList n = nList.item(temp).getChildNodes();
				String education = ",";
				String organization = ",";
				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("publication")) {
						linHtmlProfileObject.setPublication(n.item(j).getTextContent());
					}else if(n.item(j).getNodeName().equals("locality")){
						linHtmlProfileObject.setLocality(n.item(j).getTextContent());
					}else if(n.item(j).getNodeName().equals("org")){
						organization += n.item(j).getTextContent() + ",";
						linHtmlProfileObject.setOrganization(organization);
					}else if(n.item(j).getNodeName().equals("education")){
						education += n.item(j).getTextContent() + ",";
						linHtmlProfileObject.setEducation(education);
					}else if(n.item(j).getNodeName().equals("firstName")){
						linHtmlProfileObject.setFirst_name(n.item(j).getTextContent());
					}else if(n.item(j).getNodeName().equals("lastName")){
						linHtmlProfileObject.setLast_name(n.item(j).getTextContent());
					}else if(n.item(j).getNodeName().equals("bio")){
						linHtmlProfileObject.setSummary(n.item(j).getTextContent());
					}else if(n.item(j).getNodeName().equals("img")){
						linHtmlProfileObject.setImage(n.item(j).getTextContent());
					}
				}
			}} catch (Exception e) {
				e.printStackTrace();
			}
		return linHtmlProfileObject;

	}

	/**
	 * get a list of users' urls have a specific name(lastname+familyname)
	 * @param url
	 * @return list of urls  
	 */
	private ArrayList<String> createLinkedinXMLNamesList(String url){
		System.out.println("creating urls.....");
		Get_MAS_XML_Data parseObject = new Get_MAS_XML_Data();
		parseObject.parse_to_file(url);
		ArrayList<String> urls = new ArrayList<String>();
		try {
			File fXmlFile = new File("H:/wamp/www/linkedin/urls.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("urls");

			System.out.println(nList.getLength() + " html linkedin author(s) found");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				NodeList n = nList.item(temp).getChildNodes();

				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("url")) {
						urls.add(n.item(j).getTextContent());
						System.out.println(n.item(j).getTextContent());
					} 				 
				}
			}} catch (Exception e) {
				e.printStackTrace();
			}
		return urls;
	}
}
