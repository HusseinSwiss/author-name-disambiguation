package beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Hussein Hazimeh
 * @DBLP DBLP Author bean
 * 
 **/
public class DBLP_Profile {

	private String name;
	private int id;
	private String affiliation;
	private String homepage;
	private String email;
	private String address;
	private ArrayList<String> coauthors = new ArrayList<String>();
	private ArrayList<String> publications = new ArrayList<String>();
	private ArrayList<String> affiliations = new ArrayList<String>();
	private ArrayList<String> topics = new ArrayList<String>();
	private Set<String> href;

	public DBLP_Profile() {
	}

	public DBLP_Profile(String name, int id) {
		this.id = id;
		this.name = name;
	}

	public ArrayList<String> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(ArrayList<String> coauthors) {
		this.coauthors = coauthors;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<String> getHref() {
		return href;
	}

	public void setHref(Set<String> href) {
		this.href = href;
	}

	public ArrayList<String> getPublications() {
		return publications;
	}

	public void setPublications(ArrayList<String> publications) {
		this.publications = publications;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public ArrayList<String> getAffiliations() {
		return affiliations;
	}

	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}

	public ArrayList<String> getTopics() {
		return topics;
	}

	public void setAffiliations(ArrayList<String> affiliation) {
		this.affiliations = affiliation;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String toString() {
		Iterator i = publications.iterator();
		String pub = "", coau = "";
		while (i.hasNext()) {
			pub += i.next() + "\n";
		}
		i = coauthors.iterator();
		while (i.hasNext()) {
			coau += i.next() + "\n";
		}
		String d = "id: " + id + "\nname: " + name + "\npublications :" + pub
				+ "\ncoauthors: " + coau + "\naffiliation : " + affiliation;
		if (!affiliations.isEmpty())
			d += "\naffiliations : " + affiliations;
		if (!topics.isEmpty())
			d += "\ntopics : " + topics;
		if (email != null)
			d += "\nemail : " + email;
		return d;
	}
}