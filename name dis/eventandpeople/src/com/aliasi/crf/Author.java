package com.aliasi.crf;

import beans.CRFObject;
import beans.DBLP_Profile;

import com.aliasi.crf.URLReader;

import gate.Profil;
import gate.*;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import com.aliasi.crf.*;
import com.aliasi.lda.LDA;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

public class Author {

	public static TreeMap<String, TreeMap<String, ArrayList<String>>> downloadFile(
			String author) throws IOException {
		ArrayList<String> hrefs = new ArrayList<String>();
		TreeMap<String, TreeMap<String, ArrayList<String>>> resultats = new TreeMap<String, TreeMap<String, ArrayList<String>>>();
		TreeMap<String, ArrayList<String>> href_authors = new TreeMap<String, ArrayList<String>>();
		TreeMap<String, ArrayList<String>> href_authors_date = new TreeMap<String, ArrayList<String>>();
		String the_date = null;
		ArrayList<String> titles = new ArrayList<String>();
		author = author.toLowerCase();
		String name = author;
		author = author.replaceAll(" ", "_");
		author = author.replaceAll("\\.", "");
		author = author.replaceAll("-", "_");
		String DBLP = "http://www.dblp.org/search/index.php?query=author:"
				+ author + ":";
		BufferedInputStream in = null;
		TreeMap<String, String> href_date = new TreeMap<String, String>();
		try {
			URL urldblp = new URL(DBLP);
			URLConnection conn = urldblp.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine, date1;
			boolean body = false;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.contains("<body"))
					body = true;

				// search date
				int begin_date, end_date, end_pub = 0;
				if (body && inputLine.contains("<th ")) {
					date1 = inputLine.substring(inputLine.indexOf("<th ") + 1);
					begin_date = date1.indexOf(">");
					end_date = date1.indexOf("<");
					the_date = date1.substring(begin_date + 1, end_date);
					end_pub = date1.indexOf("<th ");
				}
				int debut = 0;
				if (body && inputLine.contains(">EE<")) {
					int ee = 0;
					while (ee != -1) {
						ee = inputLine.indexOf(">EE<");
						if (ee != -1) {
							int sa = 0;
							String title = "";
							String ctd = "";
							String lineHref = inputLine.substring(0, ee);
							int h = lineHref.lastIndexOf("href=\"");
							int fin = lineHref.lastIndexOf("\"");
							String href = lineHref.substring(h + 6, fin);
							href = href.substring(href.lastIndexOf("http"));
							hrefs.add(href);

							// search title
							int td = inputLine.substring(ee).indexOf("<td>")
									+ ee;
							if (td >= ee) {
								int std = inputLine.substring(td + 1).indexOf(
										"</td>")
										+ td;
								ctd = inputLine.substring(td, std);
								int a = ctd.lastIndexOf("<a");
								sa = ctd.substring(0, a).lastIndexOf("</a>");
								title = ctd.substring(sa + 6, a - 2);
								titles.add(title);
							}

							// search co-authors
							int endauthor = 0;
							ArrayList<String> coauthors = new ArrayList<String>();
							boolean exist = true;
							while (exist) {
								int aa = ctd.indexOf("<a");
								endauthor = ctd.substring(aa).indexOf("</a>")
										+ aa;
								int beginauthor = ctd.substring(aa)
										.indexOf(">") + aa;
								if (beginauthor + 1 < endauthor) {
									String coauthor = ctd.substring(
											beginauthor + 1, endauthor);
									if (!coauthor.toLowerCase().equals(name)
											&& !name.startsWith(coauthor
													.toLowerCase()))
										coauthors.add(coauthor);
									ctd = ctd.substring(endauthor + 4);
								} else
									exist = false;
							}

							// search date
							if (body && inputLine.contains("<th ")) {
								String date = inputLine.substring(inputLine
										.indexOf("<th ") + 1);
								int begin_date1 = date.indexOf(">");
								int end_date1 = date.indexOf("<");
								String the_date1 = date.substring(
										begin_date1 + 1, end_date1);
								if (end_pub < ee + debut) {
									resultats.put(the_date, href_authors_date);
									href_authors_date = new TreeMap<String, ArrayList<String>>();
									the_date = the_date1;
									end_pub = date.indexOf("<th ") + debut;
									href_date.put(href, the_date);
								} else {
									href_date.put(href, the_date);
								}
							}
							href_authors.put(href, coauthors);
							href_authors_date.put(href, coauthors);
							inputLine = inputLine.substring(ee + 2);
							debut = debut + ee + 2;
						}
					}

				}
			}
		} catch (MalformedURLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		}
		if (the_date != null && href_authors_date != null)
			resultats.put(the_date, href_authors_date);

		return resultats;
	}// downloadFile

	public static int NBPublication(String html) {
		int count = 0;
		if (html.contains("Publications")) {
			int i = html.indexOf("\"item-count\"");
			if (i > -1) {
				int beginNB = html.substring(i).indexOf("(") + i;
				int endNB = html.substring(i).indexOf(")") + i;
				String nb = html.substring(beginNB + 1, endNB);
				count = Integer.parseInt(nb);
			}
		}
		return count;
	}// NBPublication

	public static TreeMap<String, String> href_title(String author)
			throws IOException {
		// ArrayList<String> hrefs = new ArrayList<String>();
		TreeMap<String, String> resultats = new TreeMap<String, String>();
		String the_date = null;
		ArrayList<String> titles = new ArrayList<String>();
		author = author.toLowerCase();
		String name = author;
		author = author.replaceAll(" ", "_");
		author = author.replaceAll("\\.", "");
		author = author.replaceAll("-", "_");
		String DBLP = "http://www.dblp.org/search/index.php?query=author:"
				+ author + ":";
		BufferedInputStream in = null;
		TreeMap<String, String> href_date = new TreeMap<String, String>();
		try {
			URL urldblp = new URL(DBLP);
			URLConnection conn = urldblp.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine, date1;
			boolean body = false;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.contains("<body"))
					body = true;

				// search date
				int begin_date, end_date, end_pub = 0;
				if (body && inputLine.contains("<th ")) {
					date1 = inputLine.substring(inputLine.indexOf("<th ") + 1);
					begin_date = date1.indexOf(">");
					end_date = date1.indexOf("<");
					the_date = date1.substring(begin_date + 1, end_date);
					end_pub = date1.indexOf("<th ");
				}
				int debut = 0;
				if (body && inputLine.contains(">EE<")) {
					int ee = 0;
					while (ee != -1) {
						ee = inputLine.indexOf(">EE<");
						if (ee != -1) {
							int sa = 0;
							String title = "";
							String ctd = "";
							String lineHref = inputLine.substring(0, ee);
							int h = lineHref.lastIndexOf("href=\"");
							int fin = lineHref.lastIndexOf("\"");
							String href = lineHref.substring(h + 6, fin);
							href = href.substring(href.lastIndexOf("http"));
							// hrefs.add(href);

							// search title
							int td = inputLine.substring(ee).indexOf("<td>")
									+ ee;
							if (td >= ee) {
								int std = inputLine.substring(td + 1).indexOf(
										"</td>")
										+ td;
								ctd = inputLine.substring(td, std);
								int a = ctd.lastIndexOf("<a");
								sa = ctd.substring(0, a).lastIndexOf("</a>");
								title = ctd.substring(sa + 6, a - 2);
								titles.add(title);
								resultats.put(href, title);
							}
							inputLine = inputLine.substring(ee + 2);
							debut = debut + ee + 2;
						}
					}
				}
			}
		} catch (IOException e) {
		}
		System.out.println("titles: " + resultats + "\nsize = "
				+ resultats.size());
		return resultats;
	}// href_title

	// Abstracts
	public static TreeMap<String, String> Abstract(String name,
			ArrayList<String> hrefs) {
		TreeMap<String, String> abstracts = new TreeMap<String, String>();
		int nb = 1;
		Iterator i = hrefs.iterator();
		while (i.hasNext()) {
			String href = (String) i.next();
			String nameFile = name + " " + nb;
			nb++;
			if (href.contains(".pdf")) {
				download.downloadPDF(href, nameFile);
				System.out.println("DONE");
				String abs = iTextRead.Abstract(nameFile + ".pdf");
				abs = abs.replaceAll("-\n", "");
				abs = abs.replaceAll("\n", " ");
				System.out.println("abstract du pdf " + nameFile + "\n" + abs
						+ "\n\n");
				abstracts.put(nameFile, abs);
			} else {
				try {
					if (href.substring(href.length() - 1).equals("/"))
						href = href.substring(0, href.length() - 2);
					URLReader.saveUrl(nameFile + ".html", href);
					String abs = URLReader.Abstract(nameFile + ".html");
					abs = abs.replaceAll("\n", "");
					while (abs.length() > 1
							&& (abs.substring(0, 1).equals(" ") || abs
									.substring(0, 1).equals("\n")))
						abs = abs.substring(1);
					System.out.println("abstract du html " + nameFile + "\n"
							+ abs + "\n\n");
					abstracts.put(nameFile, abs);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return abstracts;
	}// Abstract

	public static ArrayList<String> LDA(String abs, int numTopics)
			throws IOException {
		ArrayList<String> topics = new ArrayList<String>();
		List<String> docs = new ArrayList<String>();
		int end_sent = abs.indexOf(".");
		while (end_sent != -1) {
			String sentence = abs.substring(0, end_sent);
			sentence = sentence.replaceAll(", ", " ");
			sentence = sentence.replaceAll(" is ", " ");
			sentence = sentence.replaceAll(" to ", " ");
			sentence = sentence.replaceAll(" in ", " ");
			sentence = sentence.replaceAll(" on ", " ");
			docs.add(sentence);
			abs = abs.substring(end_sent + 1);
			end_sent = abs.indexOf(".");
		}
		LDA lda = new LDA(numTopics, docs);
		long startTime = System.currentTimeMillis();
		lda.runLDA(1000);
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		// System.out.println(elapsedTime);

		// topics.add(lda.printTopicTopWords(0, 5));
		// topics.add(lda.printTopicTopWords(1, 5));
		// topics.add(lda.printTopicTopWords(2, 5));
		topics.add(lda.printTopicTopWords(3, 5));
		return topics;
	}// LDA

	// Biographies
	public static ArrayList<String> biographies(String name,
			TreeMap<String, ArrayList<String>> href_author,
			TreeMap<String, String> href_title, String d) {
		ArrayList<String> biographies = new ArrayList<String>();
		TreeMap<String, ArrayList<String>> new_href_pdf = new TreeMap<String, ArrayList<String>>();
		Set<String> hrefs = href_author.keySet();
		int nb = 1, pdf = 0;
		Iterator i = hrefs.iterator();
		while (i.hasNext()) {
			String href = (String) i.next();
			String nameFile = name + " " + nb + " " + d;
			nb++;
			if (href.contains(".pdf")) {
				pdf++;
				System.out.println("path du pdf" + href);
				download.downloadPDF(href, nameFile);
				ArrayList<String> coauthors = href_author.get(href);
				ArrayList<String> coauthor = names(coauthors);
				String bio = iTextRead.biographie(nameFile + ".pdf", name,
						coauthor);
				if (!bio.equals("")) {
					biographies.add(bio);
				}
			} else {
				URL url;
				boolean trouver = false;
				try {
					url = new URL(href);
					URLConnection conn = url.openConnection();
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					String new_url = conn.toString();
					new_url = new_url.substring(Integer.max(
							new_url.indexOf("http://"),
							new_url.indexOf("https://")));
					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						if (inputLine.contains("href=\"")) {
							int debut = inputLine.indexOf("href=\"") + 6;
							int fin = inputLine.substring(debut).indexOf("\"")
									+ debut;
							if (fin > debut && debut > -1) {
								String new_href = inputLine.substring(debut,
										fin);
								if (new_href.contains(".pdf")) {
									new_href = new_href.substring(0,
											new_href.indexOf(".pdf") + 4);
									if (!new_href.startsWith("http")) {
										boolean continuer = true;
										int fois = 1;
										while (continuer) {
											while (new_href.substring(0, 1)
													.equals("."))
												new_href = new_href
														.replaceFirst("\\.", "");
											if (new_href.startsWith("/")
													&& new_href.substring(1)
															.startsWith(".")) {
												new_href = new_href
														.substring(1);
												fois++;
												continuer = true;
											} else
												continuer = false;
										}
										String principal = "";
										int finale = new_url.substring(8)
												.indexOf("/") + 8;
										int k = 1;
										while (k < fois) {
											k++;
											finale = new_url.substring(
													finale + 1).indexOf("/")
													+ finale;
										}
										principal = new_url.substring(0,
												finale + 1);
										if (new_href.startsWith("/")
												&& principal.endsWith("/"))
											new_href = new_href.substring(1);
										new_href = principal + new_href;
									}
									System.out
											.println("new href : " + new_href);
									if (!new_href
											.contains("www.isca-speech.org")) {
										new_href_pdf.put(new_href,
												href_author.get(href));
										trouver = true;
									}
								}
							}
						}

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					// e.printStackTrace();
				}
				if (!trouver) {
					String new_href;
					try {
						new_href = Bing.bingPDF(href_title.get(href));
						if (new_href != "") {
							new_href_pdf.put(new_href, href_author.get(href));
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		if (!new_href_pdf.isEmpty()) {
			System.out.println("tour 2:::::::::::::::::::");
			biographies.addAll(biographies2(name, new_href_pdf, d + "a"));
		}
		System.out.println(pdf + " " + new_href_pdf.size());
		int somme = pdf + new_href_pdf.size();
		System.out.println("nombre des hrefs trouves = " + somme);
		return biographies;
	}// biographies

	public static String SearchPDF(String href) {
		String result = "";
		URL url;
		boolean trouver = false;
		try {
			url = new URL(href);
			URLConnection conn = url.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String new_url = conn.toString();
			new_url = new_url.substring(Integer.max(new_url.indexOf("http://"),
					new_url.indexOf("https://")));
			String inputLine;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.contains("href=\"")) {
					int debut = inputLine.indexOf("href=\"") + 6;
					int fin = inputLine.substring(debut).indexOf("\"") + debut;
					if (fin > debut && debut > -1) {
						String new_href = inputLine.substring(debut, fin);
						if (new_href.contains(".pdf")) {
							new_href = new_href.substring(0,
									new_href.indexOf(".pdf") + 4);
							if (!new_href.startsWith("http")) {
								boolean continuer = true;
								int fois = 1;
								while (continuer) {
									while (new_href.substring(0, 1).equals("."))
										new_href = new_href.replaceFirst("\\.",
												"");
									if (new_href.startsWith("/")
											&& new_href.substring(1)
													.startsWith(".")) {
										new_href = new_href.substring(1);
										fois++;
										continuer = true;
									} else
										continuer = false;
								}
								String principal = "";
								int finale = new_url.substring(8).indexOf("/") + 8;
								int k = 1;
								while (k < fois) {
									k++;
									finale = new_url.substring(finale + 1)
											.indexOf("/") + finale;
								}
								principal = new_url.substring(0, finale + 1);
								if (new_href.startsWith("/")
										&& principal.endsWith("/"))
									new_href = new_href.substring(1);
								new_href = principal + new_href;
							}
							System.out.println("new href : " + new_href);
							if (!new_href.contains("www.isca-speech.org")) {
								result = new_href;
								trouver = true;
							}
						}
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		}
		return result;
	}

	public static ArrayList<String> biographies2(String name,
			TreeMap<String, ArrayList<String>> new_href_pdf, String d) {
		ArrayList<String> biographies = new ArrayList<String>();
		Set<String> hrefs = new_href_pdf.keySet();
		int nb = 1;
		Iterator i = hrefs.iterator();
		while (i.hasNext()) {
			String href = (String) i.next();
			String nameFile = name + " " + nb + " " + d;
			nb++;
			System.out.println("path dans la deuxieme tour : " + href);
			download.downloadPDF(href, nameFile);
			ArrayList<String> coauthors = new_href_pdf.get(href);
			ArrayList<String> coauthor = names(coauthors);
			String bio = iTextRead
					.biographie(nameFile + ".pdf", name, coauthor);
			if (!bio.equals(""))
				biographies.add(bio);
		}
		return biographies;
	}// biographies2

	// Biographies + date
	public static TreeMap<String, ArrayList<String>> biography(String name,
			TreeMap<String, TreeMap<String, ArrayList<String>>> date_auth,
			TreeMap<String, String> href_title) {
		TreeMap<String, ArrayList<String>> results = new TreeMap<String, ArrayList<String>>();
		Set<String> dates = date_auth.keySet();
		Iterator i_date = dates.iterator();
		while (i_date.hasNext()) {
			String date = (String) i_date.next();
			TreeMap<String, ArrayList<String>> papers = date_auth.get(date);
			ArrayList<String> bio = biographies(name, papers, href_title, date);
			results.put(date, bio);
		}
		return results;
	}// biography

	/** extract set of affiliations and set of topics from biographies **/
	public static ArrayList<Biog_Profil> extract_biog(ArrayList<String> bio,
			String name) throws ClassNotFoundException, IOException {
		CRFObject crfObj = new CRFObject();
		ArrayList<String> position = new ArrayList<String>();
		ArrayList<String> phd = new ArrayList<String>();
		ArrayList<String> aff = new ArrayList<String>();
		String biog;
		Iterator i = bio.iterator();
		ArrayList<Biog_Profil> profils = new ArrayList<Biog_Profil>();
		Biog_Profil profil = null;
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
				biog = biog.substring(end_sentence + 2);
				SimplePosTrain SPTrain = new SimplePosTrain();
				SimplePosTag spt = new SimplePosTag();
				crfObj = spt.get_tagged_attributes(sentence);
				if (!crfObj.getPosition().equals(""))
					position.add(crfObj.getPosition());
				if (!crfObj.getPhD().equals(""))
					phd.add(crfObj.getPhD());
				if (!crfObj.getEducation_univ().equals("")) {
					String affi = crfObj.getEducation_univ();
					affi.toLowerCase();
					aff.add(affi);
				}
			}
			profil = new Biog_Profil(name, aff, phd);
		}
		profils.add(profil);
		return profils;
	}// extract_biog

	// comparison between biographies
	public static ArrayList<String> biogr(
			TreeMap<String, ArrayList<String>> biogs, String name)
			throws ClassNotFoundException, IOException {
		ArrayList<String> results = new ArrayList<String>();
		Set<String> dates = biogs.keySet();
		Iterator i_dates = dates.iterator();
		while (i_dates.hasNext()) {
			int nb_biog = 0;
			String date = (String) i_dates.next();
			ArrayList<String> biographies = biogs.get(date);
			if (!biographies.isEmpty()) {
				ArrayList<Biog_Profil> res_biog = extract_biog(biographies,
						name);
				Iterator plus_grand_dates = i_dates;
				while (plus_grand_dates.hasNext()) {
					String plus_grand_date = (String) plus_grand_dates.next();
					ArrayList<String> plus_grand_biographies = biogs
							.get(plus_grand_date);
					ArrayList<Biog_Profil> res_plus_grand_biog = extract_biog(
							plus_grand_biographies, name);
					Iterator i_biog = res_biog.iterator();
					while (i_biog.hasNext()) {
						Biog_Profil biog = (Biog_Profil) i_biog.next();
						boolean meme = false;
						Iterator i = res_plus_grand_biog.iterator();
						while (i.hasNext() && !meme) {
							Biog_Profil b = (Biog_Profil) i.next();
							if (b != null)
								meme = b.contains(biog);
						}
						if (meme)
							biographies.remove(nb_biog);
						else
							nb_biog++;
					}

				}
			}
		}
		results = transf1(biogs);
		return results;
	}

	public static ArrayList<String> names(ArrayList<String> author) {
		ArrayList<String> names = new ArrayList<String>();
		Iterator i = author.iterator();
		while (i.hasNext()) {
			String auteur = (String) i.next();
			int nom = auteur.indexOf(" ");
			names.add(auteur.substring(0, nom).toLowerCase());
		}
		return names;
	}// names

	/****************************************/
	/** FIRST STEP of Name Disambiguation: **/
	/****************************************/

	// clustering based co-authors
	public static ArrayList<DBLP_Profile> first_step(
			TreeMap<String, TreeMap<String, ArrayList<String>>> pub,
			String author) throws IOException {
		TreeMap<String, String> href_titles = href_title(author);
		// first step in clustering based on co-author relationship
		int nb = 0;
		TreeMap<Integer, ArrayList<String>> cluster = new TreeMap<Integer, ArrayList<String>>();
		TreeMap<Integer, TreeMap<String, String>> href_date = new TreeMap<Integer, TreeMap<String, String>>();
		Set<String> dates = pub.keySet();
		Iterator idates = dates.iterator();
		while (idates.hasNext()) {
			String date = (String) idates.next();
			TreeMap<String, ArrayList<String>> pubs = pub.get(date);
			Set<String> hrefs = pubs.keySet();
			Iterator ihrefs = hrefs.iterator();
			ArrayList<String> co1;
			while (ihrefs.hasNext()) {
				String href = (String) ihrefs.next();
				TreeMap<String, String> hd = new TreeMap<String, String>();
				hd.put(href, date);
				if (cluster.size() == 0) {
					cluster.put(nb, pubs.get(href));
					href_date.put(nb, hd);
					nb++;
				} else {
					boolean meme = false;
					co1 = pubs.get(href);
					for (int i = 0; i < nb && !meme; i++) {
						ArrayList<String> co = cluster.get(i);
						Iterator ico = co1.iterator();
						if (!co.isEmpty() || !co1.isEmpty()) {
							while (ico.hasNext()) {
								String coauthor = (String) ico.next();
								if (co.contains(coauthor)) {
									meme = true;
									TreeMap<String, String> hrd = href_date
											.get(i);
									hrd.put(href, date);
									ico = co1.iterator();
									while (ico.hasNext()) {
										String auth = (String) ico.next();
										if (!co.contains(auth))
											co.add(auth);
									}
								}
							}
						} else {
							meme = true;
							TreeMap<String, String> hrd = href_date.get(i);
							hrd.put(href, date);
						}
					}
					if (!meme) {
						cluster.put(nb, co1);
						href_date.put(nb, hd);
						nb++;
					}
				}
			}
		}
		System.out.println(cluster + "\n" + cluster.size());
		// second step : relation between co-authors
		boolean fin = false;
		int k = 0;
		if (nb != 1)
			while (k < nb) {
				fin = false;
				while (!fin) {
					fin = true;
					if (cluster.containsKey(k)) {
						ArrayList<String> authors = cluster.get(k);
						TreeMap<String, String> hrd1 = href_date.get(k);
						Iterator iauth = authors.iterator();
						boolean stop = false;
						while (iauth.hasNext() && !stop) {
							String auth = (String) iauth.next();
							ArrayList<String> coauthors = coAuthor(auth);
							if (k < nb) {
								for (int j = k + 1; j < nb; j++) {
									if (cluster.containsKey(j)) {
										ArrayList<String> coau = cluster.get(j);
										Iterator i2 = coau.iterator();
										while (i2.hasNext() && fin) {
											String name1 = (String) i2.next();
											if (Merging.comp_name_array(name1,
													coauthors)) {
												fin = false;
												Iterator l = coau.iterator();
												while (l.hasNext()) {
													String d = (String) l
															.next();
													if (!authors.contains(d))
														authors.add(d);
												}
												cluster.remove(j);
												TreeMap<String, String> hrd = href_date
														.get(j);
												hrd1.putAll(hrd);
												href_date.remove(j);
												stop = true;
											}
										}
									}
								}
							}
						}
						k++;
					} else
						k++;
				}
			}
		// hay l partie bt3tine l publication l kol cluster
		TreeMap<Integer, ArrayList<String>> cluster_pub = new TreeMap<Integer, ArrayList<String>>();
		System.out.println(nb);
		for (int nomb = 0; nomb < nb; nomb++) {
			if (href_date.containsKey(nomb)) {
				ArrayList<String> titles = new ArrayList<String>();
				TreeMap<String, String> href_des_pub = href_date.get(nomb);
				Set<String> hrefs = href_des_pub.keySet();
				Iterator i_h = hrefs.iterator();
				while (i_h.hasNext()) {
					String h = (String) i_h.next();
					titles.add(href_titles.get(h));
				}
				cluster_pub.put(nomb, titles);
			}
		}
		System.out.println(cluster_pub);
		ArrayList<DBLP_Profile> profils_dblp = new ArrayList<DBLP_Profile>();
		for (int nomb = 0; nomb < nb; nomb++) {
			if (href_date.containsKey(nomb)) {
				System.out.println(author + " " + nomb);
				DBLP_Profile profil = new DBLP_Profile(author, nomb);
				profil.setCoauthors(cluster.get(nomb));
				profil.setPublications(cluster_pub.get(nomb));
				TreeMap<String, String> href_des_pub = href_date.get(nomb);
				Set<String> hrefs = href_des_pub.keySet();
				profil.setHref(hrefs);
				profils_dblp.add(profil);
			}
		}
		System.out.println("DBLP_Profile " + profils_dblp.toString());
		return profils_dblp;

	}// meme NbAuthor output :

	/** DBLP_Profile **/

	public static ArrayList<String> coAuthor(String author) throws IOException {
		ArrayList<String> coauthors = new ArrayList<String>();
		ArrayList<String> hrefs = new ArrayList<String>();
		TreeMap<String, TreeMap<String, ArrayList<String>>> resultats = new TreeMap<String, TreeMap<String, ArrayList<String>>>();
		TreeMap<String, ArrayList<String>> href_authors = new TreeMap<String, ArrayList<String>>();
		TreeMap<String, ArrayList<String>> href_authors_date = new TreeMap<String, ArrayList<String>>();
		String the_date = null;
		ArrayList<String> titles = new ArrayList<String>();
		author = author.toLowerCase();
		String name = author;
		author = author.replaceAll(" ", "_");
		author = author.replaceAll("\\.", "");
		author = author.replaceAll("-", "_");
		String DBLP = "http://www.dblp.org/search/index.php?query=author:"
				+ author + ":";
		BufferedInputStream in = null;
		TreeMap<String, String> href_date = new TreeMap<String, String>();
		try {
			URL urldblp = new URL(DBLP);
			URLConnection conn = urldblp.openConnection();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			String inputLine, date1;
			boolean body = false;
			while ((inputLine = br.readLine()) != null) {
				if (inputLine.contains("<body"))
					body = true;

				// search date
				int begin_date, end_date, end_pub = 0;
				if (body && inputLine.contains("<th ")) {
					date1 = inputLine.substring(inputLine.indexOf("<th ") + 1);
					begin_date = date1.indexOf(">");
					end_date = date1.indexOf("<");
					the_date = date1.substring(begin_date + 1, end_date);
					end_pub = date1.indexOf("<th ");
				}
				int debut = 0;
				if (body && inputLine.contains(">EE<")) {
					int ee = 0;
					while (ee != -1) {
						ee = inputLine.indexOf(">EE<");
						if (ee != -1) {
							int sa = 0;
							String title = "";
							String ctd = "";
							String lineHref = inputLine.substring(0, ee);
							int h = lineHref.lastIndexOf("href=\"");
							int fin = lineHref.lastIndexOf("\"");
							String href = lineHref.substring(h + 6, fin);
							if (href.lastIndexOf("http") != -1)
								href = href.substring(href.lastIndexOf("http"));
							hrefs.add(href);

							// search title
							int td = inputLine.substring(ee).indexOf("<td>")
									+ ee;
							if (td >= ee) {
								int std = inputLine.substring(td + 1).indexOf(
										"</td>")
										+ td;
								ctd = inputLine.substring(td, std);
								int a = ctd.lastIndexOf("<a");
								sa = ctd.substring(0, a).lastIndexOf("</a>");
								title = ctd.substring(sa + 6, a - 2);
								titles.add(title);
							}

							// search co-authors
							int endauthor = 0;
							// ArrayList<String> coauthors = new
							// ArrayList<String>();
							boolean exist = true;
							while (exist) {
								int aa = ctd.indexOf("<a");
								endauthor = ctd.substring(aa).indexOf("</a>")
										+ aa;
								int beginauthor = ctd.substring(aa)
										.indexOf(">") + aa;
								if (beginauthor + 1 < endauthor) {

									String coauthor = ctd.substring(
											beginauthor + 1, endauthor);
									if (!coauthor.toLowerCase().equals(name))
										coauthors.add(coauthor);
									ctd = ctd.substring(endauthor + 4);
								} else
									exist = false;
							}

							// search date
							if (body && inputLine.contains("<th ")) {
								String date = inputLine.substring(inputLine
										.indexOf("<th ") + 1);
								int begin_date1 = date.indexOf(">");
								int end_date1 = date.indexOf("<");
								String the_date1 = date.substring(
										begin_date1 + 1, end_date1);
								if (end_pub < ee + debut) {
									resultats.put(the_date, href_authors_date);
									href_authors_date = new TreeMap<String, ArrayList<String>>();
									the_date = the_date1;
									end_pub = date.indexOf("<th ") + debut;
									href_date.put(href, the_date);
								} else {
									href_date.put(href, the_date);
								}
							}
							href_authors.put(href, coauthors);
							href_authors_date.put(href, coauthors);
							inputLine = inputLine.substring(ee + 2);
							debut = debut + ee + 2;
						}
					}

				}
			}
		} catch (MalformedURLException e) { // TODO Auto-generated catch
			e.printStackTrace();
		}
		if (the_date != null && href_authors_date != null)
			resultats.put(the_date, href_authors_date);

		System.out.println("name author: " + name + "\nco-authors: "
				+ coauthors);
		return coauthors;
	}// coAuthor

	/***********************************/
	/********* End First Step **********/
	/***********************************/

	// affiliation
	public static TreeMap<String, ArrayList<String>> affiliation(
			TreeMap<String, ArrayList<String>> date_href, String name) {
		TreeMap<String, ArrayList<String>> date_aff = new TreeMap<String, ArrayList<String>>();
		int nb = 1;
		Set<String> dates = date_href.keySet();
		Iterator idate = dates.iterator();
		while (idate.hasNext()) {
			ArrayList<String> affiliations = new ArrayList<String>();
			String date = (String) idate.next();
			ArrayList<String> hrefs = date_href.get(date);
			Iterator i = hrefs.iterator();
			ArrayList<String> pdfhref = new ArrayList<String>();
			ArrayList<String> htmlhref = new ArrayList<String>();
			while (i.hasNext()) {
				String href = (String) i.next();
				String nameFile = name + " " + nb;
				nb++;
				if (href.contains(".pdf")) {
					pdfhref.add(href);
				} else {
					if (href.substring(href.length() - 1).equals("/"))
						href = href.substring(0, href.length() - 2);
					htmlhref.add(href);
					// URLReader.saveUrl(nameFile + ".html", href);
				}
			}
			if (!pdfhref.isEmpty()) {
				ArrayList<gate.Profil> profs = gate.Profiling_prof.MAIN(
						pdfhref, name, 0);
				ArrayList<String> profils = new ArrayList<String>();
				Iterator i_profs = profs.iterator();
				while (i_profs.hasNext()) {
					gate.Profil prof = (Profil) i_profs.next();
					if (prof.getOrganization() != null) {
						profils.add(prof.getOrganization());
					}
				}

				// Iterator iprof = profs.iterator();
				// while(iprof.hasNext()){
				// gate.Profil p = (Profil) iprof.next();
				// profils.add(p.getOrganization());
				// }
				date_aff.put(date, profils);
			}
		}
		return date_aff;
	}// affiliation

	/*****************************************/
	/** SECOND STEP of Name Disambiguation: **/
	/*****************************************/

	/**
	 * * 1) extract set of affiliations for the clusters result in the first
	 * step
	 **/
	public static ArrayList<DBLP_Profile> Affs(ArrayList<DBLP_Profile> profil,
			String name, TreeMap<String, String> title_href) {
		TreeMap<Integer, ArrayList<String>> results = new TreeMap<Integer, ArrayList<String>>();
		Iterator i_dblp = profil.iterator();
		while (i_dblp.hasNext()) {
			DBLP_Profile prof_dblp = (DBLP_Profile) i_dblp.next();
			Set<String> hrefs = prof_dblp.getHref();
			ArrayList<String> profils = new ArrayList<String>();
			// ArrayList hrefs = cluster_href.get(nb);
			Iterator i = hrefs.iterator();
			ArrayList<String> pdfhref = new ArrayList<String>();
			ArrayList<String> htmlhref = new ArrayList<String>();
			ArrayList<String> titlehref = new ArrayList<String>();
			int nb = 0;
			while (i.hasNext()) {
				String href = (String) i.next();
				// String nameFile = name + " " + nb;
				// nb++;
				if (href.contains(".pdf")) {
					pdfhref.add(href);
				} else {
					htmlhref.add(href);
					// titlehref.add(title_href.get(href));
					// URLReader.saveUrl(nameFile + ".html", href);
				}
			}
			if (!pdfhref.isEmpty()) {
				System.out.println("search 1");
				ArrayList<gate.Profil> profs = gate.Profiling_prof.MAIN(
						pdfhref, name, nb);
				nb = nb + pdfhref.size();
				Iterator i_profs = profs.iterator();
				while (i_profs.hasNext()) {
					gate.Profil prof = (Profil) i_profs.next();
					if (prof.getOrganization() != null) {
						profils.add(prof.getOrganization());
						prof_dblp.setEmail(prof.getEMail());
					}
				}
			}
			if (!htmlhref.isEmpty()) {
				System.out.println("search 2");
				Iterator i_html = htmlhref.iterator();
				ArrayList<String> new_pdf = new ArrayList<String>();
				while (i_html.hasNext()) {
					String href = (String) i_html.next();
					String new_href = SearchPDF(href);
					if (new_href.equals(""))
						titlehref.add(title_href.get(href));
					else
						new_pdf.add(new_href);
				}
				if (!new_pdf.isEmpty()) {
					ArrayList<gate.Profil> profs = gate.Profiling_prof.MAIN(
							new_pdf, name, nb);
					nb = nb + htmlhref.size();
					Iterator i_profs = profs.iterator();
					while (i_profs.hasNext()) {
						gate.Profil prof = (Profil) i_profs.next();
						if (prof.getOrganization() != null) {
							profils.add(prof.getOrganization());
							prof_dblp.setEmail(prof.getEMail());
						}
					}
				}
			}
			if (!titlehref.isEmpty()) {
				System.out.println("search 3");
				ArrayList<String> new_href_pdf = new ArrayList<String>();
				Iterator i_title = titlehref.iterator();
				while (i_title.hasNext()) {
					String new_href;
					try {
						new_href = Bing.bingPDF((String) i_title.next());
						if (new_href != "") {
							new_href_pdf.add(new_href);
						}
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (!new_href_pdf.isEmpty()) {
					profils.addAll(affiliations(new_href_pdf, name, nb));
				}
			}
			if (!profils.isEmpty())
				prof_dblp.setAffiliations(profils);
		}
		return profil;
	}// Affs

	public static ArrayList<String> affiliations(ArrayList<String> pdfhref,
			String name, int nb) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<gate.Profil> profs = gate.Profiling_prof.MAIN(pdfhref, name,
				nb);
		Iterator i_profs = profs.iterator();
		while (i_profs.hasNext()) {
			gate.Profil prof = (Profil) i_profs.next();
			if (prof.getOrganization() != null) {
				result.add(prof.getOrganization());
				// prof_dblp.setEmail(prof.getEMail());
			}
		}
		return result;
	}// affiliations

	/** 2) extract set of topics for the clusters result in the first step **/
	public static ArrayList<DBLP_Profile> Topics(
			ArrayList<DBLP_Profile> profil, String name) throws IOException {
		Iterator i_prof = profil.iterator();
		while (i_prof.hasNext()) {
			ArrayList<String> topics = new ArrayList<String>();
			DBLP_Profile dblp = (DBLP_Profile) i_prof.next();
			Set<String> href = dblp.getHref();
			ArrayList<String> hrefs = new ArrayList<String>();
			Iterator i_href = href.iterator();
			while (i_href.hasNext()) {
				hrefs.add((String) i_href.next());
			}
			TreeMap<String, String> abstracts = Abstract(name, hrefs);
			Set<String> keys = abstracts.keySet();
			Iterator i = keys.iterator();
			int k = 4;
			while (i.hasNext()) {
				String key = (String) i.next();
				String abs = abstracts.get(key);
				topics.addAll(LDA(abs, k));
			}
			dblp.setTopics(topics);
		}
		return profil;
	}// Topics

	// comparison between: set of affiliations and topics extract from clusters
	// and
	// set of affiliations and topics extract from biographies
	public static ArrayList<DBLP_Profile> comp_profils(
			ArrayList<Biog_Profil> biog, ArrayList<DBLP_Profile> clusters,
			String name) {
		System.out.println("comparaion des profils");
		TreeMap<Integer, DBLP_Profile> results = new TreeMap<Integer, DBLP_Profile>();
		ArrayList<DBLP_Profile> result = new ArrayList<DBLP_Profile>();
		Iterator i_cluster = clusters.iterator();
		boolean vrai;
		int cle = biog.size() + 1;
		while (i_cluster.hasNext()) {
			int nb_biog = 0;
			DBLP_Profile profil = (DBLP_Profile) i_cluster.next();
			ArrayList<String> affs = profil.getAffiliations();
			ArrayList<String> topics = profil.getTopics();
			Iterator i_biog = biog.iterator();
			vrai = false;
			System.out
					.println(":::::::::::::: comparison between affiliations :::::::::::::::::");
			while (i_biog.hasNext() && !vrai) {
				nb_biog++;
				Biog_Profil b = (Biog_Profil) i_biog.next();
				ArrayList<String> biog_aff = b.getAffiliations();
				ArrayList<String> biog_top = b.getTopics();
				System.out.println("the biography is :\n" + b
						+ "\naffiliations :\n" + biog_aff + "\ntopics :\n"
						+ biog_top);
				boolean inclue = true;
				if (affs.isEmpty())
					inclue = false;
				Iterator i_af = affs.iterator();
				while (i_af.hasNext() && inclue) {
					String af = (String) i_af.next();
					af = af.toLowerCase();
					Iterator i_bio = biog_aff.iterator();
					inclue = false;
					while (i_bio.hasNext() && !inclue) {
						String bi = (String) i_bio.next();
						if (!bi.contains(af))
							inclue = false;
						else
							inclue = true;
					}
				}
				vrai = inclue;
				if (vrai) {
					if (results.containsKey(nb_biog)) {
						DBLP_Profile prof = results.get(nb_biog);
						prof.getAffiliations().addAll(profil.getAffiliations());
						prof.getTopics().addAll(profil.getTopics());
						// results.remove(nb_biog);
						// results.put(nb_biog, href_date);
						// final_sets.get(nb_biog).addAllPublications(
						// href_date_of_this_cluster.keySet());
					} else {
						// final_sets.put(nb_biog, b);
						// final_sets.get(nb_biog).addAllPublications(
						// href_date_of_this_cluster.keySet());
						results.put(nb_biog, profil);
					}
				} else {
					System.out
							.println(":::::::::::: comparison between topics :::::::::::");
					if (!inclue) {
						nb_biog = 0;
						i_biog = biog.iterator();
						while (i_biog.hasNext() && !vrai) {
							nb_biog++;
							b = (Biog_Profil) i_biog.next();
							biog_top = b.getTopics();
							Iterator i_topic = topics.iterator();
							inclue = true;
							while (i_topic.hasNext() && inclue) {
								String top = (String) i_topic.next();
								top = top.toLowerCase();
								inclue = false;
								Iterator i_bio = biog_top.iterator();
								while (i_bio.hasNext() && !inclue) {
									String bi = (String) i_bio.next();
									if (!top.contains(bi))
										inclue = false;
									else
										inclue = true;
								}
							}
						}
					}
					vrai = inclue;
					if (vrai) {
						if (results.containsKey(nb_biog)) {
							DBLP_Profile prof = results.get(nb_biog);
							prof.getAffiliations().addAll(
									profil.getAffiliations());
							prof.getTopics().addAll(profil.getTopics());
							// results.remove(nb_biog);
							// results.put(nb_biog, href_date);
							// final_sets.get(nb_biog).addAllPublications(
							// href_date_of_this_cluster.keySet());
						} else {
							// final_sets.put(nb_biog, b);
							// final_sets.get(nb_biog).addAllPublications(
							// href_date_of_this_cluster.keySet());
							results.put(nb_biog, profil);
						}
					} else {
						results.put(cle, profil);
						// final_sets.put(cle, new Biog_Profil(name,
						// transf1(affs), topics,
						// href_date_of_this_cluster.keySet()));
						cle++;
					}
				}
			}
		}
		Set<Integer> keys = results.keySet();
		Iterator i = keys.iterator();
		while (i.hasNext()) {
			result.add((DBLP_Profile) results.get(i.next()));
		}
		return result;
	}// comp_profils

	/************************************************/
	/************** End Second Step *****************/
	/************************************************/

	// transfer TreeMap<href, date> ==> TreeMap<date, <hrefs>>
	public static TreeMap<Integer, TreeMap<String, ArrayList<String>>> transf(
			TreeMap<Integer, TreeMap<String, String>> clusters) {
		Set<Integer> nb = clusters.keySet();
		TreeMap<Integer, TreeMap<String, ArrayList<String>>> clusters_date_papers = new TreeMap<Integer, TreeMap<String, ArrayList<String>>>();
		Iterator inb = nb.iterator();
		while (inb.hasNext()) {
			TreeMap<String, ArrayList<String>> date_hrefs = new TreeMap<String, ArrayList<String>>();
			int nbcluster = (int) inb.next();
			TreeMap<String, String> href_date = clusters.get(nbcluster);
			Set<String> hrefs = href_date.keySet();
			Iterator ihrefs = hrefs.iterator();
			while (ihrefs.hasNext()) {
				String href = (String) ihrefs.next();
				String date = href_date.get(href);
				if (date_hrefs.containsKey(date)) {
					date_hrefs.get(date).add(href);
				} else {
					ArrayList<String> hd = new ArrayList<String>();
					hd.add(href);
					date_hrefs.put(date, hd);
				}
			}
			clusters_date_papers.put(nbcluster, date_hrefs);
		}
		return clusters_date_papers;
	}// transf

	public static ArrayList<String> transf1(
			TreeMap<String, ArrayList<String>> biogs) {
		ArrayList<String> results = new ArrayList<String>();
		Set<String> keys = biogs.keySet();
		Iterator i_keys = keys.iterator();
		while (i_keys.hasNext()) {
			ArrayList<String> a = biogs.get(i_keys.next());
			results.addAll(a);
		}
		return results;
	}// transf1

	/*************************************/
	/******* THE MAIN FUNCTION ***********/
	/******* NAME DISAMBIGUATION *********/
	/*************************************/
	public static ArrayList<DBLP_Profile> NameDis(String name)
			throws ClassNotFoundException {
		System.out.println("NAME DISAMBIGUATION");
		ArrayList<DBLP_Profile> final_results = new ArrayList<DBLP_Profile>();
		TreeMap<Integer, TreeMap<String, String>> clusters = new TreeMap<Integer, TreeMap<String, String>>();
		try {
			TreeMap<String, TreeMap<String, ArrayList<String>>> a = downloadFile(name);
			System.out.println(a + "\n" + a.size());
			TreeMap<String, String> href_title = href_title(name);

			// dblp profiles
//			System.out.println("BEGIN CLUSTERING DBLP");
//			ArrayList<DBLP_Profile> dblp_profils = first_step(a, name);
//			System.out.println(dblp_profils + "\n" + dblp_profils.size());
//			System.out.println("THE END OF THE FIRST STEP");
//			dblp_profils = Affs(dblp_profils, name, href_title);
//			System.out.println(dblp_profils);
//			System.out.println("EXTRACT ALL AFFILIATIONS");
//			dblp_profils = Topics(dblp_profils, name);
//			System.out.println("EXTRACT ALL TOPICS");

			// biographies
			TreeMap<String, ArrayList<String>> date_biog = biography(name, a,
					href_title);
			ArrayList<String> bio = biogr(date_biog, name);
			System.out.println("biographies " + bio);

//			// comparaison between biographies and dblp_profile
//			if (!bio.isEmpty() && dblp_profils.size() > 1) {
//				ArrayList<Biog_Profil> biog = extract_biog(bio, name);
//				final_results = comp_profils(biog, dblp_profils, name);
//				System.out.println(biog);
//			} else
//				final_results = dblp_profils;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(clusters + "\n" + clusters.size());
		return final_results;
	}// NameDis

	// Main
	public static void main(String[] args) throws IOException,
			ClassNotFoundException {
		NameDis("Michael WAGNER 0004");
//
//		Scanner s = new Scanner(System.in);
//		System.out.println("entrez le nom d'auteur:");
//		String name = s.nextLine();
//		ArrayList<DBLP_Profile> res = NameDis(name);
//		System.out.println(res + "\n" + res.size());
		// try {
		// TreeMap<String, TreeMap<String, ArrayList<String>>> a =
		// downloadFile(name);
		// System.out.println(a);
		// Set<String> dates = a.keySet();
		// TreeMap<String, ArrayList<String>> href_author = new TreeMap<String,
		// ArrayList<String>>();
		// Iterator i = dates.iterator();
		// while (i.hasNext()) {
		// TreeMap<String, ArrayList<String>> b = (TreeMap<String,
		// ArrayList<String>>) a
		// .get(i.next());
		// href_author.putAll(b);
		// }
		// System.out.println(href_author);
		//
		// TreeMap<String, String> href_title = href_title(name);
		//
		// // biographies
		//
		// ArrayList<String> bio = biographies(name, href_author,
		// href_title,
		// "");
		// System.out.println(bio);
		//
		// // comparison between biographys and clusters
		// // TreeMap<Integer, TreeMap<String, String>> clusters = NbAuthor(a,
		// // name);
		// // System.out.println(clusters + "\n" + clusters.size());
		//
		// ArrayList<DBLP_Profile> dblp_profils = first_step(a, name);
		// System.out.println("first step :\n" + dblp_profils);
		// dblp_profils = Affs(dblp_profils, name);
		// System.out.println("avec affiliation\n" + dblp_profils);
		//
		// dblp_profils = Topics(dblp_profils, name);
		// System.out.println(dblp_profils);
		//
		// ArrayList<Biog_Profil> biog = extract_biog(bio, name);
		// System.out.println(comp_profils(biog, dblp_profils, name));
		//
		// // co-authors relationship
		// /**
		// * TreeMap<Integer, TreeMap<String, String>> clusters = NbAuthor(a,
		// * name); System.out.println(clusters + "\n" + clusters.size());
		// * System.out.println(AffSet(clusters, name));
		// * System.out.println(TopicSet(clusters, name));
		// **/
		//
		// // abstracts
		// /**
		// * Set<String> hrefs = href_author.keySet(); TreeMap<String, String>
		// * abstracts = Abstracts(name, hrefs);
		// * System.out.println(abstracts);
		// **/
		//
		// // set of topics using LDA
		// /**
		// * Set<String> keys = abstracts.keySet(); Iterator i =
		// * keys.iterator(); int k = 4 ; while(i.hasNext()){ String key =
		// * (String) i.next(); System.out.println(key); String abs =
		// * abstracts.get(key); LDA(abs,k); }
		// **/
		//
		// // set of affiliations
		//
		// /**
		// * TreeMap<String, ArrayList<String>> aff = affiliations(a, name);
		// * System.out.println(aff);
		// **/
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}
}

class Biog_Profil {

	String name;
	ArrayList<String> affiliations;
	ArrayList<String> topics;
	ArrayList<String> href_publications;

	public Biog_Profil(String n, ArrayList<String> a, ArrayList<String> t) {
		name = n;
		affiliations = a;
		topics = t;
	}

	public Biog_Profil(String n, ArrayList<String> a, ArrayList<String> t,
			ArrayList<String> p) {
		name = n;
		affiliations = a;
		topics = t;
		href_publications = p;
	}

	public Biog_Profil(String n, ArrayList<String> a, ArrayList<String> t,
			Set<String> p) {
		name = n;
		affiliations = a;
		topics = t;
		href_publications = new ArrayList<String>();
		Iterator i = p.iterator();
		while (i.hasNext()) {
			href_publications.add((String) i.next());
		}
	}

	public void setAffiliations(ArrayList<String> a) {
		affiliations = a;
	}

	public void setTopics(ArrayList<String> t) {
		topics = t;
	}

	public void setPublications(ArrayList<String> p) {
		href_publications = p;
	}

	public ArrayList<String> getAffiliations() {
		return affiliations;
	}

	public ArrayList<String> getTopics() {
		return topics;
	}

	public ArrayList<String> getPublications() {
		return href_publications;
	}

	public void addAff(String a) {
		affiliations.add(a);
	}

	public void addTopic(String t) {
		topics.add(t);
	}

	public void addPublication(String p) {
		href_publications.add(p);
	}

	public void addAllAff(ArrayList<String> a) {
		Iterator i = a.iterator();
		while (i.hasNext()) {
			String aff = (String) i.next();
			affiliations.add(aff);
		}
	}

	public void addAllTopic(ArrayList<String> t) {
		Iterator i = t.iterator();
		while (i.hasNext()) {
			String topic = (String) i.next();
			topics.add(topic);
		}
	}

	public void addAllPublications(ArrayList<String> p) {
		href_publications.addAll(p);
	}

	public void addAllPublications(Set<String> p) {
		if (href_publications == null)
			href_publications = new ArrayList<String>();
		Iterator i = p.iterator();
		while (i.hasNext()) {
			String pub = (String) i.next();
			href_publications.add(pub);
		}
	}

	public String toString() {
		if (href_publications == null)
			return "\n\n" + "the author name : " + name
					+ "\nher affiliation or university is : " + affiliations
					+ "\nher topics is : " + topics;
		else
			return "\n\n" + "the author name : " + name
					+ "\nher affiliation or university are : " + affiliations
					+ "\nher topics are : " + topics
					+ "\nher publications are : " + href_publications;
	}

	public boolean contains(Biog_Profil b) {
		boolean meme = false;
		ArrayList<String> aff1 = this.affiliations;
		ArrayList<String> aff2 = b.affiliations;
		if (aff1.contains(aff2)) {
			ArrayList<String> topics1 = this.topics;
			ArrayList<String> topics2 = b.topics;
			if (topics1.contains(topics2))
				meme = true;
		}
		return meme;
	}
}