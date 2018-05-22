package com.aliasi.crf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import model.Get_MAS_XML_Data;
import beans.DBLP_Profile;
import beans.MAS_Profile;
import beans.Publication;

public class Merging {

	public static boolean comp_names(String name1, String name2) {

		name1 = name1.toLowerCase();
		name2 = name2.toLowerCase();

		if (!name1.equals(name2)) {
			int lastSpace1 = name1.lastIndexOf(" "), lastSpace2 = name2
					.lastIndexOf(" ");
			String lastName1 = name1.substring(lastSpace1 + 1), lastName2 = name2
					.substring(lastSpace2 + 1);
			if (lastName1.equals(lastName2)) {
				int firstSpace1 = name1.indexOf(" "), firstSpace2 = name2
						.indexOf(" ");
				String firstName1 = name1.substring(0, firstSpace1), firstName2 = name2
						.substring(0, firstSpace2);
				if (firstName1.equals(firstName2)) {
					if (count_space(name1) > 2 && count_space(name2) > 2) {
						int secondSpace1 = name1.substring(firstSpace1 + 1)
								.indexOf(" "), secondSpace2 = name2.substring(
								firstSpace2 + 1).indexOf(" ");
						String secondName1 = name1.substring(firstSpace1 + 1,
								secondSpace1), secondName2 = name2.substring(
								firstSpace2 + 1, secondSpace2);
						String first_char_secondName1 = secondName1.substring(
								0, 1), first_char_secondName2 = secondName2
								.substring(0, 1);
						if ((first_char_secondName1
								.equals(first_char_secondName2))
								&& (secondName1.length() == 2 || secondName2
										.length() == 2)) {
							return true;
						}
						return false;
					}
				} else {
					String first_char_firstName1 = firstName1.substring(0, 1), first_char_firstName2 = firstName2
							.substring(0, 1);
					if ((first_char_firstName1.equals(first_char_firstName2))
							&& (firstName1.length() == 2 || firstName2.length() == 2)) {
						if (count_space(name1) > 2 && count_space(name2) > 2) {
							int secondSpace1 = name1.substring(firstSpace1 + 1)
									.indexOf(" "), secondSpace2 = name2
									.substring(firstSpace2 + 1).indexOf(" ");
							String secondName1 = name1.substring(
									firstSpace1 + 1, secondSpace1), secondName2 = name2
									.substring(firstSpace2 + 1, secondSpace2);
							String first_char_secondName1 = secondName1
									.substring(0, 1), first_char_secondName2 = secondName2
									.substring(0, 1);
							if ((first_char_secondName1
									.equals(first_char_secondName2))
									&& (secondName1.length() == 2 || secondName2
											.length() == 2)) {
								return true;
							}
							return false;
						}
						return true;
					}
				}
			}
			return false;
		}
		return true;
	}// comp_names
	
	public static boolean comp_name_array(String name, ArrayList<String> array){
		boolean meme = false;
		Iterator i = array.iterator();
		while(i.hasNext() && !meme){
			String name2 = (String) i.next();
			meme = comp_names(name, name2);
		} 
		return meme;
	}

	private static int count_space(String name) {
		int space = 0;
		while (name.contains(" ")) {
			space++;
			name = name.substring(name.indexOf(" ") + 1);
		}
		return space;
	}// count_space

	public static String E_Mail(String name,
			TreeMap<String, ArrayList<String>> date_emails) {
		String true_email = "";
		String lastName = name.substring(name.lastIndexOf(" ") + 1), first_char_name = name
				.substring(0, 1);
		Set<String> dates = date_emails.keySet();
		int j = dates.size();
		String[] les_dates = new String[dates.size()];
		Iterator i_dates = dates.iterator();
		while (i_dates.hasNext()) {
			j--;
			String date = (String) i_dates.next();
			les_dates[j] = date;
		}
		boolean trouver = false;
		for (int i = 0; i < les_dates.length && !trouver; i++) {
			ArrayList<String> mails = date_emails.get(les_dates[i]);
			Iterator i_emails = mails.iterator();
			while (i_emails.hasNext()) {
				String email = (String) i_emails.next();
				if (email.contains(lastName)) {
					if (email.substring(0, email.indexOf(lastName)).contains(
							first_char_name)) {
						true_email = email;
						trouver = true;
					}
				}
			}
		}
		return true_email;
	}// E_Mail

	public static String E_Mail(String name, ArrayList<String> mails) {
		String true_email = "";
		boolean trouver = false;
		String lastName = name.substring(name.lastIndexOf(" ") + 1), first_char_name = name
				.substring(0, 1);
		Iterator i_emails = mails.iterator();
		while (i_emails.hasNext()) {
			String email = (String) i_emails.next();
			if (email.contains(lastName)) {
				if (email.substring(0, email.indexOf(lastName)).contains(
						first_char_name)) {
					true_email = email;
					trouver = true;
				}
			}
		}
		return true_email;
	}// E_Mail

	public static TreeMap<String, ArrayList<String>> e_mail_date(
			TreeMap<String, ArrayList<String>> date_url_MAS,
			TreeMap<String, ArrayList<String>> date_url_DBLP) {
		TreeMap<String, ArrayList<String>> results = new TreeMap<String, ArrayList<String>>();

		return results;
	}

	public static boolean MemeCluster(ArrayList<String> titles_DBLP,
			ArrayList<String> titles_MAS) {
		boolean meme = false;
		Iterator i_DBLP = titles_DBLP.iterator();
		while (i_DBLP.hasNext()) {
			String title_DBLP = (String) i_DBLP.next();
			title_DBLP = title_DBLP.replaceAll("\\.", "");
			title_DBLP = title_DBLP.replaceAll(",", "");
			title_DBLP = title_DBLP.replaceAll(";", "");
			title_DBLP = title_DBLP.replaceAll("\"", "");
			title_DBLP = title_DBLP.replaceAll("'", "");
			title_DBLP = title_DBLP.replaceAll(":", "");
			title_DBLP = title_DBLP.toLowerCase();
			Iterator i_MAS = titles_MAS.iterator();
			while (i_MAS.hasNext()) {
				String title_MAS = (String) i_MAS.next();
				title_MAS = title_MAS.replaceAll("\\.", "");
				title_MAS = title_MAS.replaceAll(",", "");
				title_MAS = title_MAS.replaceAll(";", "");
				title_MAS = title_MAS.replaceAll("\"", "");
				title_MAS = title_MAS.replaceAll("'", "");
				title_MAS = title_MAS.replaceAll(":", "");
				title_MAS = title_MAS.toLowerCase();
				if (title_MAS.contains(title_DBLP)
						|| title_DBLP.contains(title_MAS))
					return true;
			}
		}
		return meme;
	}// MemeCluster

	public static TreeMap<Integer, ArrayList<Integer>> Clustering_DBLP_MAS(
			TreeMap<Integer, Set<String>> DBLP,
			TreeMap<Integer, ArrayList<String>> MAS,
			TreeMap<String, String> href_title) {
		TreeMap<Integer, ArrayList<Integer>> results = new TreeMap<Integer, ArrayList<Integer>>();
		Set<Integer> key_dblp = DBLP.keySet();
		Set<Integer> key_mas = MAS.keySet();
		Iterator i_dblp = key_dblp.iterator();
		while (i_dblp.hasNext()) {
			int dblp = (int) i_dblp.next();
			Set<String> href_dblp = DBLP.get(dblp);
			Iterator i_href = href_dblp.iterator();
			ArrayList<String> titles = new ArrayList<String>();
			while (i_href.hasNext()) {
				titles.add((String) i_href.next());
			}

			Iterator i_MAS = key_mas.iterator();
			while (i_MAS.hasNext()) {
				int mas = (Integer) i_MAS.next();
				boolean meme = MemeCluster(titles, MAS.get(mas));
				if (meme) {
					if (results.containsKey(mas))
						results.get(mas).add(dblp);
					else {
						ArrayList<Integer> array_dblp = new ArrayList<Integer>();
						array_dblp.add(dblp);
						results.put(mas, array_dblp);
					}
				}
			}
		}
		return results;
	}// Clustering_DBLP_MAS : this function return the numbers of clusters in
		// MAS & DBLP in cluster

	public static ArrayList<Final_Profil> merging_clusters(
			ArrayList<MAS_Profile> MAS_Prof, ArrayList<DBLP_Profile> DBLP_Prof,
			TreeMap<String, String> href_title) {
		int key = 1;
		TreeMap<Integer, Profil> results = new TreeMap<Integer, Profil>();
		ArrayList<Final_Profil> final_results = new ArrayList<Final_Profil>();
		TreeMap<Integer, Set<String>> DBLP = new TreeMap<Integer, Set<String>>();
		TreeMap<Integer, ArrayList<String>> MAS = new TreeMap<Integer, ArrayList<String>>();
		int nb_dblp = 0, nb_mas = 0;
		Iterator i_DBLP = DBLP_Prof.iterator();
		while (i_DBLP.hasNext()) {
			DBLP_Profile profil = (DBLP_Profile) i_DBLP.next();
			DBLP.put(nb_dblp, profil.getHref());
			nb_dblp++;
		}
		Iterator i_MAS = MAS_Prof.iterator();
		while (i_MAS.hasNext()) {
			MAS_Profile profil = (MAS_Profile) i_MAS.next();
			ArrayList<Publication> pub = profil.getPublications();
			ArrayList<String> hrefs = new ArrayList<String>();
			Iterator i = pub.iterator();
			while (i.hasNext()) {
				Publication p = (Publication) i.next();
				String href = p.getTitle();
			}
			MAS.put(nb_mas, hrefs);
			nb_mas++;
		}
		TreeMap<Integer, ArrayList<Integer>> clustering = Clustering_DBLP_MAS(
				DBLP, MAS, href_title);
		Set<Integer> cluster_MAS = clustering.keySet();
		Iterator i_clusters = cluster_MAS.iterator();
		Final_Profil profile = new Final_Profil();
		while (i_clusters.hasNext()) {
			int key_mas = (int) i_clusters.next();
			ArrayList<Integer> keys_dblp = clustering.get(key_mas);
			i_MAS = MAS_Prof.iterator();
			int mas = 0;
			while (i_MAS.hasNext() && mas <= key_mas) {
				MAS_Profile profil_mas = (MAS_Profile) i_MAS.next();
				if (mas == key_mas) {
					int dblp = 0;
					profile.setId(profil_mas.getId());
					profile.setName(profil_mas.getName());
					profile.setHomePage(profil_mas.getHomepage());
					profile.setAffiliation(profil_mas.getAffiliation());
					ArrayList<Integer> DBLP_keys = clustering.get(key_mas);
					ArrayList<String> emails = new ArrayList<String>();
					Iterator i_keys_dblp = DBLP_keys.iterator();
					while (i_keys_dblp.hasNext()) {
						int key_dblp = (int) i_keys_dblp.next();
						i_DBLP = DBLP_Prof.iterator();
						while (i_DBLP.hasNext() && dblp <= key_dblp) {
							DBLP_Profile profil_dblp = (DBLP_Profile) i_DBLP
									.next();
							if (dblp == key_dblp) {
								ArrayList<String> publications = addArray(
										profile.getPublications(),
										profil_dblp.getPublications());
								profile.setPublications(publications);
								ArrayList<String> affiliations = addArray(
										profile.getAffiliations(),
										profil_dblp.getAffiliations());
								profile.setAffiliations(affiliations);
								ArrayList<String> topics = addArray(
										profile.getTopics(),
										profil_dblp.getTopics());
								profile.setTopics(topics);
								ArrayList<String> hrefs = addArraySet(
										profile.getHrefs(),
										profil_dblp.getHref());
								profile.setHrefs(hrefs);
								emails.add(profil_dblp.getEmail());
							}
							dblp++;
						}
					}
					profile.setEMail(E_Mail(profil_mas.getName(), emails));
					final_results.add(profile);
				}
				mas++;
			}
		}
		if(clustering.isEmpty()){
			Iterator i_Mas = MAS_Prof.iterator();
			while(i_Mas.hasNext()){
				final_results.add(Final_Profil.trans_MAS_Profile((MAS_Profile) i_Mas.next()));
			}
			Iterator i_Dblp = DBLP_Prof.iterator();
			while(i_Dblp.hasNext()){
				final_results.add(Final_Profil.trans_DBLP_Profile((DBLP_Profile) i_Dblp.next()));
			}
		}
		return final_results;
	}// merging_clusters : this function return the finaly clusters between MAS
		// & DBLP

	public static ArrayList<Final_Profil> MAIN(String author)
			throws ClassNotFoundException, IOException {

		// DBLP clusters
		ArrayList<DBLP_Profile> dblp_results = Author.NameDis(author);
		TreeMap<String, String> href_title = Author.href_title(author);

		// MAS clusters
		Get_MAS_XML_Data p = new Get_MAS_XML_Data();
		ArrayList<MAS_Profile> mas_results = p.get_author(author);

		// Merging MAS & DBLP
		ArrayList<Final_Profil> results = merging_clusters(mas_results,
				dblp_results, href_title);

		return results;
	}//MAIN

	public static Profil merging_Profils(MAS_Profile mas,
			ArrayList<DBLP_Profile> dblp) {
		// Profil result = new Profil();

		String aff_mas = mas.getAffiliation();
		ArrayList<String> coau_mas = mas.getCoauthors();
		String home_mas = mas.getHomepage();
		ArrayList<Publication> pub_mas = mas.getPublications();

		Iterator i_pub = pub_mas.iterator();
		ArrayList<String> title_mas = new ArrayList<String>();
		while (i_pub.hasNext()) {
			Publication p = (Publication) i_pub.next();
			title_mas.add(p.getTitle());
		}

		ArrayList<String> coau_profil = coau_mas;
		ArrayList<String> pub_profil = title_mas;
		ArrayList<String> aff_profil = new ArrayList<String>();
		ArrayList<String> home_profil = new ArrayList<String>();
		ArrayList<String> ad_profil = new ArrayList<String>();

		Iterator i_dblp = dblp.iterator();
		while (i_dblp.hasNext()) {
			DBLP_Profile dblp_prof = (DBLP_Profile) i_dblp.next();
			String aff_dblp = dblp_prof.getAffiliation();
			ArrayList<String> coau_dblp = dblp_prof.getCoauthors();
			ArrayList<String> pub_dblp = dblp_prof.getPublications();
			String home_dblp = dblp_prof.getHomepage();
			String email_dblp = dblp_prof.getEmail(); // lzm ykoun fi date l
														// o2dr 7addid aya mail
														// l asa7
			String ad_dblp = dblp_prof.getAddress();

			coau_profil = addCoAuthor(coau_profil, coau_dblp);
			pub_profil = addArray(pub_profil, pub_dblp);
			aff_profil.add(aff_mas);
			aff_profil.add(aff_dblp);
			home_profil.add(home_mas);
			home_profil.add(home_dblp);
			ad_profil.add(ad_dblp);

		}
		return null;
	}// merging_Profils

	// public static TreeMap<String, ArrayList<String>> extract_E_Mail(
	// TreeMap<String, ArrayList<String>> date_pub) {
	// TreeMap<String, ArrayList<String>> results = new TreeMap<String,
	// ArrayList<String>>();
	// int n = 0;
	// Set<String> dates = date_pub.keySet();
	// Iterator i_date = dates.iterator();
	// while (i_date.hasNext()) {
	// if (!i_date.hasNext()) {
	// TreeMap<String, ArrayList<String>> publication = new TreeMap<String,
	// ArrayList<String>>();
	//
	// }
	// }
	// return results;
	// }

	private static ArrayList<String> addArray(ArrayList<String> a1,
			ArrayList<String> a2) {
		ArrayList<String> result = a1;
		Iterator i = a2.iterator();
		while (i.hasNext()) {
			String s = (String) i.next();
			if (!result.contains(s))
				result.add(s);
		}
		return result;
	}// addArray

	private static ArrayList<String> addArraySet(ArrayList<String> a1,
			Set<String> a2) {
		ArrayList<String> result = a1;
		Iterator i = a2.iterator();
		while (i.hasNext()) {
			String s = (String) i.next();
			if (!result.contains(s))
				result.add(s);
		}
		return result;
	}// addArray

	private static ArrayList<String> addCoAuthor(ArrayList<String> a1,
			ArrayList<String> a2) {
		ArrayList<String> results = a1;
		Iterator i2 = a2.iterator();
		while (i2.hasNext()) {
			String n2 = (String) i2.next();
			boolean meme = false;
			Iterator i1 = a1.iterator();
			while (i1.hasNext() && !meme) {
				String n1 = (String) i1.next();
				meme = comp_names(n1, n2);
			}
			if (!meme)
				results.add(n2);
		}
		return results;
	}// addCoAuthor

	public static void main(String[] args) throws ClassNotFoundException, IOException {
		Scanner s = new Scanner(System.in);
		System.out.println("entrez le nom d'auteur:");
		String name = s.nextLine();
		ArrayList<Final_Profil> profils = MAIN(name);
		System.out.println(profils + "\n" + profils.size());
//		// comparaison des noms
//		String name1 = "k. k. eckert", name2 = "kai k. eckert";
//		System.out.println(comp_names(name1, name2));
//
//		// detecter l' e-mail le plus convenable suivant la date la plus
//		// precedante
//		TreeMap<String, ArrayList<String>> date_emails = new TreeMap<String, ArrayList<String>>();
//		ArrayList<String> emails = new ArrayList<String>();
//		emails.add("k.eckert@hotmail.com");
//		emails.add("univ.kai.eckert@edu");
//		date_emails.put("2008", emails);
//
//		emails = new ArrayList<String>();
//		emails.add("k.2010.eckert@hotmail.com");
//		emails.add("univ.ul.kai.ecker@edu");
//		date_emails.put("2010", emails);
//
//		System.out.println(E_Mail("kai eckert", date_emails));
//
//		System.out.println(E_Mail("kai eckert", emails));
	}

}
