package com.aliasi.crf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import beans.DBLP_Profile;
import beans.MAS_Profile;
import beans.Publication;

public class Final_Profil {

	private int id;
	private String name;
	private String homepage;
	private String affiliation;
	private String email;
	private ArrayList<String> publications;
	private ArrayList<String> affiliations;
	private ArrayList<String> coAuthors;
	private ArrayList<String> hrefs;
	private ArrayList<String> topics = new ArrayList<String>();
	private static int ID = 0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHomePage() {
		return homepage;
	}

	public void setHomePage(String homepage) {
		this.homepage = homepage;
	}

	public String getAffiliation() {
		return affiliation;
	}

	public void setAffiliation(String affiliation) {
		this.affiliation = affiliation;
	}

	public String getEMAil() {
		return email;
	}

	public void setEMail(String email) {
		this.email = email;
	}

	public ArrayList<String> getPublications() {
		return publications;
	}

	public void setPublications(ArrayList<String> publications) {
		this.publications = publications;
	}

	public ArrayList<String> getAffiliations() {
		return affiliations;
	}

	public void setAffiliations(ArrayList<String> affiliations) {
		this.affiliations = affiliations;
	}

	public ArrayList<String> getCoAuthors() {
		return coAuthors;
	}

	public void setCoauthors(ArrayList<String> coauthors) {
		this.coAuthors = coauthors;
	}

	public ArrayList<String> getHrefs() {
		return hrefs;
	}

	public void setHrefs(ArrayList<String> hrefs) {
		this.hrefs = hrefs;
	}

	public ArrayList<String> getTopics() {
		return topics;
	}

	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}

	public String toString() {
		String d = "Id= " + id + "\nName= " + name + "\nAffiliation= "
				+ affiliation + "\nemail= " + email + "\nHome Page= "
				+ homepage + "\nPublications= " + publications
				+ "\nCo-Authors= " + coAuthors + "\nAffilistions= "
				+ affiliations + "\ntopics : " + topics;
		return d;
	}

	public static Final_Profil trans_MAS_Profile(MAS_Profile profil) {
		Final_Profil result = new Final_Profil();
		result.affiliation = profil.getAffiliation();
		result.coAuthors = profil.getCoauthors();
		result.homepage = profil.getHomepage();
		result.name = profil.getName();
		result.id = profil.getId();
		ArrayList<Publication> pubs = profil.getPublications();
		ArrayList<String> pub = new ArrayList<String>();
		Iterator i = pubs.iterator();
		while (i.hasNext()) {
			Publication p = (Publication) i.next();
			pub.add(p.getTitle());
		}
		result.publications = pub;
		return result;
	}

	public static Final_Profil trans_DBLP_Profile(DBLP_Profile profil) {
		Final_Profil result = new Final_Profil();
		result.affiliations = profil.getAffiliations();
		result.coAuthors = profil.getCoauthors();
		result.publications = profil.getPublications();
		result.topics = profil.getTopics();
		result.email = profil.getEmail();
		Set<String> hrefs = profil.getHref();
		ArrayList<String> href = new ArrayList<String>();
		Iterator i = hrefs.iterator();
		while (i.hasNext()) {
			href.add((String) i.next());
		}
		result.hrefs = href;
		result.name = profil.getName();
		result.id = ID;
		ID++;
		return result;
	}
}
