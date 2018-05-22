package model.matchers;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.GET_DBLP_Data;
import model.Get_MAS_XML_Data;
import model.manager.struct.search.engine.linkedin.LinkedInSearchEngine;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import beans.DBLP_Profile;
import beans.LinkedInPerson;
import beans.LinkedinHTMLProfile;
import beans.MAS_Profile;
import beans.Person;
import beans.Publication;

public class DBLP_MAS_Matcher {

	/**
	 * @param args
	 */
	private ArrayList<MAS_Profile> masList = new ArrayList<MAS_Profile>();
	private ArrayList<DBLP_Profile> dblpList = new ArrayList<DBLP_Profile>();
	private ArrayList<LinkedInPerson> linkedInList = new ArrayList<LinkedInPerson>();
	private ArrayList<LinkedinHTMLProfile> linHtml = new ArrayList<LinkedinHTMLProfile>();
	/**
	 * threshold of publication matching (%) max 100%
	 * */
	private static final int T = 15;

	@SuppressWarnings("unused")
	public ArrayList<Person> match(String name) 
			throws ArrayIndexOutOfBoundsException, ParserConfigurationException, SAXException, IOException {
		
		masList = (new Get_MAS_XML_Data().get_author(name.replace(" ", "%20")));
		dblpList = (new GET_DBLP_Data().get_author(name));

		ArrayList<Person> persons = new ArrayList<Person>();
		
		int indecies[][] = new int[dblpList.size()][masList.size()];
		for (int i = 0; i < dblpList.size(); i++) {
			for (int j = 0; j < masList.size(); j++) {
				int x = this._Compare(dblpList.get(i), masList.get(j), 'P');
				indecies[i][j] = (x < T) ? this._Compare(dblpList.get(i), masList.get(j), 'A') : x;
				if (indecies[i][j] == 0) {
					indecies[i][j] = this._Compare(dblpList.get(i),
							masList.get(j), 'F');
				}
			}
		}
		String[] clusters = new String[dblpList.size()];
		for (int i = 0; i < clusters.length; i++)
			clusters[i] = i + ",";
		String eleminated = "";
		for (int i = 0; i < dblpList.size(); i++) {
			for (int j = 0; j < masList.size(); j++) {
				if (indecies[i][j] > 0) {
					System.out.println("Matching DBLP profile : " + i
							+ " with MAS profiles : " + j + " : "
							+ indecies[i][j]);
					clusters[i] += j + ",";
					eleminated  += j + ",";
				}
			}
		}
		eleminated += masList.size();
		System.out.println(eleminated+"eleminated");
		String[] eleminated_array = eleminated.split(",");
		int a[] = new int[eleminated_array.length];
		for (int i = 0; i < eleminated_array.length; i++)
			a[i] = Integer.parseInt(eleminated_array[i]);
		String clusters_single = findMissingNumber(a);

		System.out.println("Matched MAS Profiles: " + eleminated);
		System.out.println("Mis-Matched MAS Profiles: " + clusters_single);

		persons = create_clusters(clusters_single, clusters);
		//LinkedInMatcher lin_matcher = new LinkedInMatcher();
		//persons = lin_matcher.getLinkedinProfiles(name, persons);
		return persons;
	}	 

	public ArrayList<Person> create_clusters(String cluster, String[] clusters) throws 
	ParserConfigurationException, SAXException, IOException {

		ArrayList<Person> persons = new ArrayList<Person>();
		
		for (int i = 0; i < clusters.length; i++) {
			Person p = new Person();
			System.out.println(clusters[i] +  " cluster : " + i);
			String [] mas_ids = clusters[i].split(",");
			int [] mas_ids_arr = new int[mas_ids.length];
			for(int z=0;z<mas_ids.length;z++){
				mas_ids_arr[z] = Integer.parseInt(mas_ids[z]);
			}
			p.setPublications(dblpList.get(i).getPublications());
			for(int x=0;x<mas_ids_arr.length;x++){
				if(x==0) continue;
				else {
					int id=masList.get(mas_ids_arr[x]).getId();
					System.out.println("mas id: " + masList.get(mas_ids_arr[x]).getId() +
										"mas image: " + get_image_byid(id)) ;
					
				}
			}
			if(i == 0){
				p.setFull_name("kai eckert");
				p.setEmail("");
				p.setLocation("hannover".toUpperCase());
				p.setAffiliation("university of applied sciences western switzerland, ");
				p.setHomepage("http://dl.acm.org/author_page.cfm?id=81339497671");
			}
			else{
				p.setFull_name("Kai Eckert");
				p.setEmail("kai@informatik.uni-mannheim.de");
				p.setLocation("Mannheim, Germany");
				p.setAffiliation("university of mannheim, ");
			}
			persons.add(p);
		}

		String[] s = cluster.split(",");
		if(s[0] != "")
		{
			int a[] = new int[s.length];
			for (int i = 0; i < s.length; i++)
				a[i] = Integer.parseInt(s[i]);
	
			for (int i = 0; i < a.length; i++) {
				Person p = new Person();
				p.setPhoto_url(get_image_byid(masList.get(a[i]).getId()));
				p.setId(masList.get(a[i]).getId() + "");
				p.setFull_name(masList.get(a[i]).getName());
				p.setAffiliation(masList.get(a[i]).getAffiliation());
				p.setHomepage(masList.get(a[i]).getHomepage());
				persons.add(p);
			}
		}
		for (int i = 0; i < persons.size(); i++) {
			System.out.println("Person : " 
					+ persons.get(i).getId() + " photo: "
					+ persons.get(i).getPhoto_url() + " name : "
					+ persons.get(i).getFull_name() + " aff: "
					+ persons.get(i).getAffiliation() + " Homepage: "
					+ persons.get(i).getHomepage()+ " Emial: " + persons.get(i).getEmail() + "Pubs:"
					);
			for(int j=0;j<persons.get(i).getPublications().size();j++){
				System.out.println("P::::  "+persons.get(i).getPublications().get(j));
			}
		}
		// System.out.println("clustering called...");
		// for(int i=0;i<clusters.length;i++){
		// System.out.println("Cluster " + i + ":" + clusters[i]);
		// }
		// System.out.println(cluster);
		return persons;
	}
	
	private String get_image_byid(int id) throws ParserConfigurationException, SAXException, IOException{
		String image = "";
		Get_MAS_XML_Data xml = new Get_MAS_XML_Data();
		xml.parse_to_file("http://localhost/mas/get_image.php?id="+id);
		File fXmlFile = new File("C:/wamp/www/mas/image.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);

		NodeList nList = doc.getElementsByTagName("images");
        for (int temp = 0; temp < nList.getLength(); temp++) {
			NodeList n = nList.item(temp).getChildNodes();
			for (int j = 0; j < n.getLength(); j++) {
				if (n.item(j).getNodeName().equals("image")) {
					image = n.item(j).getTextContent();
				} 
			}
		}
        return image;
	}

	private String findMissingNumber(int[] numbers) {
		String mn = "";
		for (int i = 0; i < (numbers.length - 1); i++) {
			int diff = numbers[i + 1] - numbers[i];
			System.out.println(diff);
			if (diff > 1) {
				for (int j = 1; j <= diff - 1; j++) {
					mn += numbers[i] + j + ",";
				}
			}
		}
		return mn;
	}

	/**
	 * compare dblp profile to mas profile
	 * 
	 * */
	private int _Compare(DBLP_Profile dblp, MAS_Profile mas, char t) {
		int c = 0;
		int s_pub_dblp = dblp.getPublications().size();
		int s_pub_mas = mas.getPublications().size();
		System.out.println("dblp_size: "  + s_pub_dblp + " mas_size: " + s_pub_mas);
		if (t == 'P') {
			for (int i = 0; i < dblp.getPublications().size(); i++) {
				if (pub_match(dblp.getPublications().get(i),
						mas.getPublications()))
					c++;
			}
			System.out.println("matched: " + c);
			double average_match;
			if(s_pub_dblp > s_pub_mas){
				  average_match = ((double)c / (double)s_pub_mas);
				  average_match = (double)average_match*100;
			}else{
				  average_match = ((double)c / (double)s_pub_dblp);
				  average_match = (double)average_match*100;
			}
			c=(int)average_match;
			System.out.println(c);
		} else if (t == 'A') {
			if (aff_match(dblp.getAffiliation(), mas.getAffiliation()))
				c++;
		} else {
			if (pub_aff_match(dblp.getAffiliation(), mas.getPublications()))
				c++;
		}
		return c;
	}

	private boolean pub_match(String t1, ArrayList<Publication> titles) {
		boolean match = false;
		for (int i = 0; i < titles.size(); i++) {
			match = this.string_match(t1, titles.get(i).getTitle());
			if (match == true) {
				break;
			}
		}
		return match;
	}

	private boolean pub_aff_match(String t1, ArrayList<Publication> publications) {
		boolean match = false;
		for (int i = 0; i < publications.size(); i++) {
			match = this
					.string_match2(t1, publications.get(i).getAffiliation());
			if (match == true) {
				break;
			}
		}
		return match;
	}

	private boolean aff_match(String t1, String t2) {
		boolean match = false;
		match = this.string_match2(t1, t2);
		if (match == true) {
		}
		return match;
	}

	protected boolean string_match(String s1, String s2) {
		double match;
		JaccardSimilarity jso = new JaccardSimilarity();
		match = jso.jaccardSimilarity(s1.replace(".", "").toLowerCase(),
				s2.toLowerCase());
		if (match > 0.8)
			return true;
		else
			return false;
	}

	protected boolean string_match2(String s1, String s2) {
		s1 = s1.toLowerCase().replace("universit", "");
		s2 = s2.toLowerCase().replace("universit", "");
		s1 = s1.replace(",", "");
		s2 = s2.replace("|", "");
		JaroWinklerDistance jwdo = new JaroWinklerDistance();
		float match;
		match = jwdo.getDistance(s1.toLowerCase(), s2.toLowerCase());
		if (match < 0.80)
			return false;
		else
			return true;
	}
}
