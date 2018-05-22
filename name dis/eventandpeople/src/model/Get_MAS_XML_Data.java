package model;
import org.omg.CORBA.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import beans.MAS_Profile;
import beans.Publication;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

/**
 * @author Hussein Hazimeh
 *
 */
public class Get_MAS_XML_Data extends Thread {

	private ArrayList<MAS_Profile> profiles;

	public static void main(String argv[]) throws IOException,
	InterruptedException {
		Get_MAS_XML_Data p = new Get_MAS_XML_Data();
		String name = "kai eckert";
		String newname = name.replace(" ", "%20");
		p.get_author(newname);
	}

	public ArrayList<MAS_Profile> get_author(String name) {
		profiles = new ArrayList<MAS_Profile>();
		parse_to_file("http://localhost/mas/get_author.php?name="+name);
		try {
			File fXmlFile = new File("C:/wamp/www/mas/test.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("m:properties");

			System.out.println(nList.getLength() + " author(s) found");
			String namedb = "";
			int id = 0;
			String affiliation = "";
			String affiliation_id = "";
			String homepage = "";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				NodeList n = nList.item(temp).getChildNodes();
				MAS_Profile m = new MAS_Profile();
				ArrayList<Publication> pubs = new ArrayList<Publication>();
				ArrayList<String> coauthors = new ArrayList<String>();
				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("d:ID")) {
						id = Integer.parseInt(n.item(j).getTextContent());
						m.setId(id);
					} else if (n.item(j).getNodeName().equals("d:Name")) {
						namedb = n.item(j).getTextContent();
						m.setName(namedb);
					} else if (n.item(j).getNodeName().equals("d:Affiliation")) {
						affiliation = n.item(j).getTextContent();
						m.setAffiliation(affiliation);
					} else if (n.item(j).getNodeName().equals("d:Homepage")) {
						homepage = n.item(j).getTextContent();
						m.setHomepage(homepage);
					} else if (n.item(j).getNodeName().equals("d:AffiliationID")) {
						if(affiliation.equals("")){
							affiliation_id = n.item(j).getTextContent();
							m.setAffiliation(get_affiliation(affiliation_id));
						}
					}
				}
				pubs = get_publications(id);
				coauthors = get_coauthors(id);
				m.setPublications(pubs);
				m.setCoauthors(coauthors);
				profiles.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
				for(int i=0;i<profiles.size();i++){
					 System.out.println("ID:" + profiles.get(i).getId());
					 System.out.println("ID:" + profiles.get(i).getId()+ 
					 "Affiliation: " + profiles.get(i).getAffiliation()+ 
					 "Homepage: " + profiles.get(i).getHomepage() + " \nPublications: ");
						 for(int j=0;j<profiles.get(i).getPublications().size();j++)
							 System.out.println(profiles.get(i).getPublications().get(j).getAffiliation() + " , " +
									 			profiles.get(i).getPublications().get(j).getTitle());
							 for(int j=0;j<profiles.get(i).getCoauthors().size();j++)
								 System.out.println("coauthors: " + profiles.get(i).getCoauthors().get(j));
						System.out.println();
						 }
		return profiles;
	}

	private ArrayList<Publication> get_publications(int id) {
		parse_to_file("http://localhost/mas/get_publications.php?id="+id);
		ArrayList<Publication> publications = new ArrayList<Publication>();
		try {
			File fXmlFile = new File("C:/wamp/www/mas/pub_ids.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("m:properties");

			System.out.println(nList.getLength() + " publicaion(s) found");
			String affiliation;
			String author_name;
			String title;
			for (int temp = 0; temp < nList.getLength(); temp++) {
				NodeList n = nList.item(temp).getChildNodes();
				Publication p = new Publication();
				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("d:PaperID")) {
						title = get_pub_title(Integer.parseInt(n.item(j).getTextContent()));
						p.setId(Integer.parseInt(n.item(j).getTextContent()));
						p.setTitle(title);
					}else if(n.item(j).getNodeName().equals("d:Name")){
						author_name = n.item(j).getTextContent();
						p.setAuthor_name(author_name);
					}else if(n.item(j).getNodeName().equals("d:Affiliation")){
						affiliation = n.item(j).getTextContent();
						p.setAffiliation(affiliation);
					}
				}
				publications.add(p);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return publications;
	}

	private String get_pub_title(int id){
		parse_to_file("http://localhost/mas/get_publication_title.php?id="+id);
		String title = "";
		try {
			File fXmlFile = new File("C:/wamp/www/mas/pub_title.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("m:properties");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				NodeList n = nList.item(temp).getChildNodes();
				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("d:Title")) {
						title=n.item(j).getTextContent();
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	private String get_affiliation(String id){
		parse_to_file("http://localhost/mas/get_affiliation.php?id="+id);
		String title = "";
		try {
			File fXmlFile = new File("C:/wamp/www/mas/affiliation.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("m:properties");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				NodeList n = nList.item(temp).getChildNodes();
				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("d:DisplayName")) {
						title=n.item(j).getTextContent();
					} 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return title;
	}

	private ArrayList<String> get_coauthors(int id) {
		parse_to_file("http://localhost/mas/get_publications.php?id="+id);
		ArrayList<String> coauthors = new ArrayList<String> ();
		try {
			File fXmlFile = new File("C:/wamp/www/mas/pub_ids.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("m:properties");

			int paper_id; 
			String name= "";
			for (int temp = 0; temp < nList.getLength(); temp++) {
				NodeList n = nList.item(temp).getChildNodes();
				for (int j = 0; j < n.getLength(); j++) {
					if (n.item(j).getNodeName().equals("d:PaperID")) {
						paper_id = Integer.parseInt(n.item(j).getTextContent());
						parse_to_file("http://localhost/mas/get_coauthor.php?paper_id="+paper_id);

						try {
							File fXmlFile1 = new File("C:/wamp/www/mas/coauthor.xml");
							DocumentBuilderFactory dbFactory1 = DocumentBuilderFactory
									.newInstance();
							DocumentBuilder dBuilder1 = dbFactory1.newDocumentBuilder();
							Document doc1 = dBuilder1.parse(fXmlFile1);

							NodeList nList1 = doc1.getElementsByTagName("m:properties");
							for (int temp1 = 0; temp1 < nList1.getLength(); temp1++) {
								NodeList n1 = nList1.item(temp1).getChildNodes();
								for (int j1 = 0; j1 < n1.getLength(); j1++) {
									if (n.item(j1).getNodeName().equals("d:Name")) {
										name = n1.item(j1).getTextContent();
									}
								}
								coauthors.add(name);
							}
						}catch(Exception e){

						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return coauthors;

	}

	public void parse_to_file(String urlString1){
		try {
			URL url = new URL(urlString1);
			URLConnection urlConnection = url.openConnection();
			HttpURLConnection connection = null;
			if (urlConnection instanceof HttpURLConnection) {
				connection = (HttpURLConnection) urlConnection;
			} else {
				System.out.println("Please enter an HTTP URL.");
				return;
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String urlString = "";
			String current;
			while ((current = in.readLine()) != null) {
				urlString += current;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
