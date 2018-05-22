package beans;
import java.util.ArrayList;

/**
  * @author  Hussein Hazimeh 
  * 
  */
public class MAS_Profile{
	
	private String name;
	private int id;
	private String affiliation;
	private String homepage;
	private ArrayList<Publication> publications = new ArrayList<Publication>();
	private ArrayList<String> coauthors = new ArrayList<String>();
	
	public ArrayList<String> getCoauthors() {
		return coauthors;
	}

	public void setCoauthors(ArrayList<String> coauthors) {
		this.coauthors = coauthors;
	}

	public ArrayList<Publication> getPublications() {
		return publications;
	}

	public void setPublications(ArrayList<Publication> publications) {
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

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
}