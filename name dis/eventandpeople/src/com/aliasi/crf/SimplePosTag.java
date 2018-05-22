package com.aliasi.crf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import beans.CRFObject;

import com.aliasi.tag.ScoredTagging;
import com.aliasi.tag.TagLattice;
import com.aliasi.tag.Tagging;
import com.aliasi.util.AbstractExternalizable;

/**
 * @author Hussein
 * 
 *
 */
public class SimplePosTag {

	public CRFObject get_tagged_attributes(String text)
			throws ClassNotFoundException, IOException {
		CRFObject crfObj = new CRFObject();
		new SimplePosTrain();
		File modelFile = new File(
				"C:/Users/Hazimeh/Desktop/lingpipe-4.1.0/demos/tutorial/crf/simplePos.ChainCrf");
		@SuppressWarnings("unchecked")
		ChainCrf<String> crf = (ChainCrf<String>) AbstractExternalizable
				.readObject(modelFile);

		for (int i = 1; i < 2; ++i) {
			String text1 = text;
			List<String> tokens = Arrays.asList(text1.split(" "));

			System.out.println("\nFIRST BEST r");
			Tagging<String> tagging = crf.tag(tokens);
			System.out.println(tagging);

			int maxNBest = 5;
			 System.out.println("\n" + maxNBest + " BEST CONDITIONAL");
			 System.out.println("Rank log p(tags|tokens)  Tagging");
			Iterator<ScoredTagging<String>> it = crf.tagNBestConditional(
					tokens, maxNBest);
			for (int rank = 0; rank < maxNBest && it.hasNext(); ++rank) {
				ScoredTagging<String> scoredTagging = it.next();
				 System.out.println(rank + "    " + scoredTagging);
			}

			String education_aff = "";
			String domain = "";
			String prp = "";
			String phddegree = "";
			String position = "";

			 System.out.println("\nMARGINAL TAG PROBABILITIES");
			 System.out.println("Token .. Tag log p(tag|pos,tokens)");
			TagLattice<String> fbLattice = crf.tagMarginal(tokens);
			for (int n = 0; n < tokens.size(); ++n) {
				double p = -100;
				 System.out.println(tokens.get(n));
				for (int k = 0; k < fbLattice.numTags(); ++k) {
					String tag = fbLattice.tag(k);
					double prob = fbLattice.logProbability(n, k);
					 System.out.println("     " + tag + " " + prob);
					if (prob > p) {
						switch (tag) {
						case "PHDDegree":
							phddegree += tokens.get(n);
							break;
						case "PRP":
							prp += tokens.get(n);
							break;
						case "DOMAIN":
							domain += " " + tokens.get(n);
							break;
						case "UNIV":
							education_aff += " " + tokens.get(n);
							break;
						case "Position":
							position += " " + tokens.get(n);
							break;
						}
						p = prob;
					}
				}
			}
			crfObj.setPhD(domain);
			crfObj.setEducation_univ(education_aff);
			crfObj.setPosition(position);
			// System.out.println(crfObj.getPhD() + " :: " +
			// crfObj.getEducation_univ());
		}
		return crfObj;
	}

	public static void main(String[] args) throws ClassNotFoundException,
			IOException {
		// SimplePosTrain SPTrain = new SimplePosTrain();
		// SimplePosTag spt = new SimplePosTag();
		// CRFObject crfObj =
		// spt.get_tagged_attributes("include facility location, logistics simulation and  supply chain management");
		// System.out.println(crfObj.toString());

		Scanner s = new Scanner(System.in);
		System.out.println("entrez le nom d'auteur:");
		String name = s.nextLine();
		
		TreeMap<String,TreeMap<String, ArrayList<String>>> a = Author.downloadFile(name);
		System.out.println(a);
		Set<String> dates = a.keySet();
		TreeMap<String, ArrayList<String>> href_author = new TreeMap<String, ArrayList<String>>();
		Iterator id = dates.iterator();
		while(id.hasNext()){
			TreeMap<String, ArrayList<String>> b = (TreeMap<String, ArrayList<String>>) a.get(id.next());
			href_author.putAll(b);
		}
		TreeMap<String, String> href_title = Author.href_title(name);
//		TreeMap<String, ArrayList<String>> href_author = Author
//				.downloadFile(name);
		ArrayList<String> bio = Author.biographies(name, href_author, href_title, "");

		CRFObject crfObj = new CRFObject();
		ArrayList<String> position = new ArrayList<String>();
		ArrayList<String> phd = new ArrayList<String>();
		ArrayList<String> aff = new ArrayList<String>();
		String biog;
		Iterator i = bio.iterator();
		ArrayList<Profil> profils = new ArrayList<Profil>();
		Profil profil = null;
		while (i.hasNext()) {
			biog = (String) i.next();
			biog = biog.replaceAll("-\n", "");
			biog = biog.replaceAll("\n", " ");
			int end_sentence;
			while (biog.contains(". ")) {
				if (biog.indexOf(". ") > -1 && biog.indexOf(", ") > -1) {
					if (biog.indexOf(". ") != biog.indexOf(". degree "))
						end_sentence = Integer.min(biog.indexOf(". "),
								biog.indexOf(", "));
					else
						end_sentence = Integer.max(
								biog.substring(biog.indexOf(". ") + 2).indexOf(
										". ")
										+ biog.indexOf(". ") + 2,
								biog.indexOf(", "));
				} else
					end_sentence = biog.indexOf(". ");
				String sentence = biog.substring(0, end_sentence);
				if (sentence.contains(" is "))
					sentence = sentence.substring(sentence.indexOf(" is ") + 4);
				if (sentence.contains(" has "))
					sentence = sentence
							.substring(sentence.indexOf(" has ") + 5);
				if (sentence.contains(" a "))
					sentence = sentence.substring(sentence.indexOf(" a ") + 3);
				if (sentence.contains(" her "))
					sentence = sentence
							.substring(sentence.indexOf(" her ") + 5);
				if (sentence.contains(" for "))
					sentence = sentence
							.substring(sentence.indexOf(" for ") + 5);
				System.out.println(sentence);
				biog = biog.substring(end_sentence + 2);
				SimplePosTrain SPTrain = new SimplePosTrain();
				SimplePosTag spt = new SimplePosTag();
				crfObj = spt.get_tagged_attributes(sentence);
				System.out.println(crfObj.toString());
				if (!crfObj.getPosition().equals(""))
					position.add(crfObj.getPosition());
				if (!crfObj.getPhD().equals(""))
					phd.add(crfObj.getPhD());
				if (!crfObj.getEducation_univ().equals(""))
					aff.add(crfObj.getEducation_univ());
			}
			profil = new Profil(name, aff, phd);
			System.out.println(profil);
		}
		profils.add(profil);
	}
}
class Profil{
	
	String name;
	ArrayList<String> affiliations;
	ArrayList<String> topics;
	
	public Profil(String n, ArrayList<String> a, ArrayList<String> t){
		name = n;
		affiliations = a;
		topics = t;
	}
	
	public void setAffiliations(ArrayList<String> a){affiliations = a;}
	public void setTopics(ArrayList<String> t){topics = t;}
	
	public ArrayList<String> getAffiliations(){return affiliations;}
	public ArrayList<String> getTopics(){return topics;}
	
	public void addAff(String a){affiliations.add(a);}
	public void addTopic(String t){topics.add(t);}
	
	public void addAllAff(ArrayList<String> a){
		Iterator i = a.iterator();
		while(i.hasNext()){
			String aff = (String) i.next();
			affiliations.add(aff);
		}
	}
	public void addAllTopic(ArrayList<String> t){
		Iterator i = t.iterator();
		while(i.hasNext()){
			String topic = (String) i.next();
			topics.add(topic);
		}
	}
	
	public String toString(){
		return "\n\n" + "the author name : " + name
						+ "\nher affiliation or university is : " + affiliations
						+ "\nher topics is : " + topics;
	}
}