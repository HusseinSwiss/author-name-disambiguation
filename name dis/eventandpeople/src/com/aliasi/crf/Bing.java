package com.aliasi.crf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Bing {

	public static String bingPDF(String title) throws IOException {
		String result = "", title_url = title.replace(" ", "+");
		String bing = "https://www.bing.com/search?q=" + title_url
				+ "+pdf&go=Submit&qs=ds&form=QBLH";
		BufferedInputStream in = null;
		URL url_bing = new URL(bing);
		URLConnection conn = url_bing.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		String inputLine;
		boolean trouver = false;
		while ((inputLine = br.readLine()) != null && !trouver) {
			while (inputLine.contains("<li class=\"b_algo\">") && !trouver) {
				int debut = inputLine.indexOf("<li class=\"b_algo\">");
				int fin = inputLine.substring(debut + 19).indexOf(
						"<li class=\"b_algo\">")
						+ debut + 19;
				String phrase = "";
				if (debut < fin)
					phrase = inputLine.substring(debut, fin);
				else
					phrase = inputLine.substring(debut);
				int debut_titre = phrase.indexOf("<span dir=\"ltr\">") + 16;
				int fin_titre = phrase.indexOf("</span>");
				String titre = "";
				if (fin_titre != -1)
					titre = phrase.substring(debut_titre, fin_titre)
							.toLowerCase();
				else
					titre = phrase.substring(debut_titre).toLowerCase();
				titre = titre.replaceAll("<strong>", "");
				titre = titre.replaceAll("</strong>", "");
				titre = titre.replaceAll(";", "");
				titre = titre.replaceAll(":", "");
				titre = titre.replaceAll("-", "");
				titre = titre.replaceAll("\\.", "");
				titre = titre.replaceAll("  ", " ");
				title = title.toLowerCase();
				title = title.replaceAll("\\.", "");
				title = title.replaceAll(":", "");
				title = title.replaceAll("-", "");
				title = title.replaceAll(";", "");
				title = title.replaceAll("  ", " ");
				title = title.replaceAll(" ", "");
				title = title.replaceAll("&", "and");

				int debut1 = phrase.indexOf("<a href=\"") + 9;
				int fin1 = phrase.substring(debut1).indexOf("\"") + debut1;
				titre = phrase.substring(debut1, fin1).toLowerCase();
				titre = titre.replaceAll("_", "");
				titre = titre.replaceAll("-", "");

				// if (titre.contains(title) || title.contains(titre)) {
				// if (phrase.contains("PDF")) {

				result = phrase.substring(debut1, fin1);
				trouver = true;
				// }
				// }
				inputLine = inputLine.substring(fin);
			}
		}
		return result;
	}// bingPDF

	/*
	 * public static String bingPDF(String title) throws IOException { String
	 * result = "", title_url = title.replace(" ", "+"); String bing =
	 * "https://www.bing.com/search?q=" + title_url +
	 * "+pdf&go=Submit&qs=ds&form=QBLH"; BufferedInputStream in = null; URL
	 * url_bing = new URL(bing); URLConnection conn = url_bing.openConnection();
	 * BufferedReader br = new BufferedReader(new InputStreamReader(
	 * conn.getInputStream())); String inputLine; boolean trouver = false; while
	 * ((inputLine = br.readLine()) != null && !trouver) {
	 * 
	 * while (inputLine.contains("<li class=\"b_algo\">") && !trouver) {
	 * 
	 * int debut = inputLine.indexOf("<li class=\"b_algo\">"); int fin =
	 * inputLine.substring(debut + 19).indexOf( "<li class=\"b_algo\">") + debut
	 * + 19; String phrase = ""; if (debut < fin) phrase =
	 * inputLine.substring(debut, fin); else phrase =
	 * inputLine.substring(debut); int debut_titre =
	 * phrase.indexOf("<span dir=\"ltr\">") + 16; int fin_titre =
	 * phrase.indexOf("</span>"); String titre = ""; if (fin_titre != -1) titre
	 * = phrase.substring(debut_titre, fin_titre) .toLowerCase(); else titre =
	 * phrase.substring(debut_titre).toLowerCase(); titre =
	 * titre.replaceAll("<strong>", ""); titre = titre.replaceAll("</strong>",
	 * ""); titre = titre.replaceAll(";", ""); titre = titre.replaceAll(":",
	 * ""); titre = titre.replaceAll("-", ""); titre = titre.replaceAll("\\.",
	 * ""); titre = titre.replaceAll("  ", " "); title = title.toLowerCase();
	 * title = title.replaceAll("\\.", ""); title = title.replaceAll(":", "");
	 * title = title.replaceAll("-", ""); title = title.replaceAll(";", "");
	 * title = title.replaceAll("  ", " "); // System.out.println(title + "\n" +
	 * titre); if (titre.equals(title)) {
	 * 
	 * // if (phrase.contains("PDF")) {
	 * 
	 * int debut1 = phrase.indexOf("<a href=\"") + 9; int fin1 =
	 * phrase.substring(debut1).indexOf("\"") + debut1; result =
	 * phrase.substring(debut1, fin1); trouver = true;
	 * 
	 * // } else { // int debut1 = phrase.indexOf("<a href=\"") + 9; // int fin1
	 * = phrase.substring(debut1).indexOf("\"") // + debut1; // if
	 * (phrase.substring(debut1, fin1).contains(".pdf")) { // result =
	 * phrase.substring(debut1, fin1); // trouver = true; // } // // }
	 * 
	 * }
	 * 
	 * inputLine = inputLine.substring(fin);
	 * 
	 * }
	 * 
	 * }
	 * 
	 * return result; }
	 */

	public static void main(String[] args) {
		// "Data Science for Social Good - 2014 KDD Highlights."
		// "Transfer Feature Representation via Multiple Kernel Learning."
		try {
			String title = "Conference knowledge modeling for conference-video-recordings querying & visualization";
			String url_title = bingPDF(title);
			System.out.println("url : " + url_title);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
