package com.aliasi.crf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.itextpdf.awt.geom.Rectangle;
//iText imports
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class iTextRead {

	/**
	 * @param args
	 */
	public static String Abstract(String path) {
		String abs = "";
		try {
			PdfReader reader = new PdfReader(path);
			String page = PdfTextExtractor.getTextFromPage(reader, 1);
			String first_page = page.toLowerCase();
			int beginIndex = first_page.indexOf("abstract") + 9;
			int endIndex;
			if (first_page.indexOf("introduction") != -1
					&& first_page.indexOf("keyword") != -1)
				endIndex = Integer.min(first_page.indexOf("introduction"),
						first_page.indexOf("keyword"));
			else
				endIndex = Integer.max(first_page.indexOf("introduction"),
						first_page.indexOf("keyword"));
			if (endIndex > beginIndex)
				abs = first_page.substring(beginIndex, endIndex);
			else {
				abs = first_page.substring(beginIndex);
				String second_page = PdfTextExtractor
						.getTextFromPage(reader, 2).toLowerCase();
				if (second_page.indexOf("introduction") != -1
						&& second_page.indexOf("keyword") != -1)
					endIndex = Integer.min(second_page.indexOf("introduction"),
							second_page.indexOf("keyword"));
				else
					endIndex = Integer.max(second_page.indexOf("introduction"),
							second_page.indexOf("keyword"));
				String suite = second_page.substring(0, endIndex);
				abs = abs + suite;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return abs;

	}

	public static String biographieOneColumn(String path, String name,
			ArrayList<String> coauthors) {
		String biog = "";
		try {
			PdfReader reader = new PdfReader(path);
			int NbPage = reader.getNumberOfPages();
			String page = PdfTextExtractor.getTextFromPage(reader, NbPage);
			String end_page = page.toLowerCase();
			if (end_page.contains("biographie")) {
				int beginBiog = end_page.indexOf("biographie") + 11;

				int biog_author = end_page.substring(beginBiog).indexOf(name)
						+ beginBiog;
				String biographies = end_page.substring(biog_author);
				int end_author = end(biographies, coauthors);
				biog = biographies.substring(0, end_author);
				// biog = biographies;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return biog;
	}

	public static String biography_page(PdfReader reader, String name,
			ArrayList<String> coauthors, int NbPage) throws IOException {
		String biog = "";
		Rectangle left = new Rectangle(0, 0, 306, 792);
		Rectangle right = new Rectangle(306, 0, 612, 792);
		RenderFilter leftFilter = new RegionTextRenderFilter(left);
		RenderFilter rightFilter = new RegionTextRenderFilter(right);
		TextExtractionStrategy strategy = new FilteredTextRenderListener(
				new LocationTextExtractionStrategy(), leftFilter);
		String original_content = PdfTextExtractor.getTextFromPage(reader,
				NbPage, strategy);
		original_content += " ";
		strategy = new FilteredTextRenderListener(
				new LocationTextExtractionStrategy(), rightFilter);
		original_content += PdfTextExtractor.getTextFromPage(reader, NbPage,
				strategy);
		String end_page = original_content.toLowerCase();
		if (end_page.contains("biographie")) {
			int beginBiog = end_page.indexOf("biographie") + 11;

			int biog_author = end_page.substring(beginBiog).indexOf(name)
					+ beginBiog;
			String biographies = end_page.substring(biog_author);
			int end_author = end(biographies, coauthors);
			biog = biographies.substring(0, end_author);
		}
		return biog;
	}

	public static String biographieTowColumns(String path, String name,
			ArrayList<String> coauthors) throws ClassNotFoundException {
		String biog = "";
		String original_content = "";
		// try {
		PdfReader reader;
		try {
			reader = new PdfReader(path);
			int NbPage = reader.getNumberOfPages();
			biog = biography_page(reader, name, coauthors, NbPage);
			if (biog.equals("") && NbPage > 1)
				biog = biography_page(reader, name, coauthors, NbPage - 1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Rectangle left = new Rectangle(0, 0, 306, 792);
		// Rectangle right = new Rectangle(306, 0, 612, 792);
		// RenderFilter leftFilter = new RegionTextRenderFilter(left);
		// RenderFilter rightFilter = new RegionTextRenderFilter(right);
		// TextExtractionStrategy strategy = new FilteredTextRenderListener(
		// new LocationTextExtractionStrategy(), leftFilter);
		// original_content = PdfTextExtractor.getTextFromPage(reader, NbPage,
		// strategy);
		// original_content += " ";
		// strategy = new FilteredTextRenderListener(
		// new LocationTextExtractionStrategy(), rightFilter);
		// original_content += PdfTextExtractor.getTextFromPage(reader,
		// NbPage, strategy);
		// String end_page = original_content.toLowerCase();
		// if (end_page.contains("biographie")) {
		// int beginBiog = end_page.indexOf("biographie") + 11;
		//
		// int biog_author = end_page.substring(beginBiog).indexOf(name)
		// + beginBiog;
		// String biographies = end_page.substring(biog_author);
		// int end_author = end(biographies, coauthors);
		// biog = biographies.substring(0, end_author);

		// }
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		return biog;
	}

	public static String biographie(String path, String name,
			ArrayList<String> coauthors) {
		name = name.toLowerCase();
		File p = new File(path);
		if (p.exists()) {
			// String biog1 = biographieOneColumn(path, name, coauthors);
			String biog2 = null;
			try {
				biog2 = biographieTowColumns(path, name, coauthors);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// if (biog2.contains(biog1))
			// return biog2;
			return biog2;
		}
		return "";
	}

	public static int end(String biog, ArrayList<String> coauthor) {
		int end = biog.length();
		Iterator i = coauthor.iterator();
		while (i.hasNext()) {
			String coa = (String) i.next();
			int end_this = biog.indexOf(coa);
			if (end_this > -1) {
				end = Integer.min(end, end_this);
			}
		}
		// if(end != biog.length()) return end;
		return end;
	}

	public static void main(String[] args) throws ClassNotFoundException {
		String path = "anna rohunen 1.pdf";
		ArrayList<String> coauthor = new ArrayList<String>();
		coauthor.add("kai ");
		coauthor.add("matti ");
		coauthor.add("tero ");
		coauthor.add("kari ");
		String biog = biographieTowColumns(path, "anna rohunen", coauthor);
		System.out.println("anna rohunen 1 :\n " + biog);
		path = "anna rohunen 1.pdf";
		biog = biographieOneColumn(path, "anna rohunen", coauthor);
		System.out.println("anna rohunen 2 :\n " + biog);

		System.out.println("anna rohunen :"
				+ biographie(path, "anna rohunen", coauthor));

	}

}
