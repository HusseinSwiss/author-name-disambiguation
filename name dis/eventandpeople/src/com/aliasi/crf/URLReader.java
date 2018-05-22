package com.aliasi.crf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLReader {

	public static void saveUrl(final String filename, final String urlString)
			throws MalformedURLException, IOException {
		BufferedInputStream in = null;
		FileOutputStream fout = null;
		try {
			URL url = new URL(urlString);
			in = new BufferedInputStream(url.openStream());
			fout = new FileOutputStream(filename);

			final byte data[] = new byte[1024];
			int count;
			while ((count = in.read(data, 0, 1024)) != -1) {
				fout.write(data, 0, count);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (fout != null) {
				fout.close();
			}
		}
	}

	public static String readHtml(String filename) {
		String content = "";
		try {
			FileReader fr = new FileReader(new File(filename));
			BufferedReader br = new BufferedReader(fr);
			String s;
			while ((s = br.readLine()) != null) {
				content = content + "\n" + s;
			}
		} catch (Exception ex) {
			System.out.println(ex.toString());
		}
		return content;
	}

	public static String textHtml(String html, String text) {
		int debut;
		debut = html.indexOf("<");
		int fin = html.indexOf(">");
		if (debut != -1)
			if (fin != -1 && fin + 1 < html.length() && debut > -1)
				text = text + html.substring(0, debut)
						+ textHtml(html.substring(fin + 1), text);
			else
				text = text + html.substring(0, debut);
		return text;
	}

	public static String TagAbstract(String body, String classe) {
		String abs = "";
		body = body.toLowerCase();
		int beginClass = 0;
		while (abs.equals("") && beginClass != -1) {
			beginClass = body.indexOf("class=");
			if (beginClass != -1) {
				int end = body.substring(beginClass).indexOf(">") + beginClass;

				String body1 = body.substring(beginClass, end);
				if (body1.contains(classe)) {
					int beginTag = body.substring(0, beginClass).lastIndexOf(
							"<");
					int endTag = body.substring(beginTag + 1, beginClass + 1)
							.indexOf(" ") + beginTag;
					String tag = body.substring(beginTag + 1, endTag + 1);
					int endAbs = body.substring(endTag + 1).indexOf("</" + tag)
							+ endTag;
					abs = textHtml(body.substring(end + 1, endAbs), "");
					return abs;
				}
				body = body.substring(end);
			}
		}
		return abs;
	}

	public static String Abstract(String path) {
		String abs = "";
		String html = readHtml(path);
		String text = textHtml(html, "");
		text = text.toLowerCase();
		int numabs = text.indexOf("abstract");
		int beginBody = html.indexOf("<body");
		int endBody = html.indexOf("</body");
		String body = "";
		if (beginBody > -1)
			body = html.substring(beginBody, endBody);
		else
			body = html;
		abs = TagAbstract(body, "abstract");
		String k = abs.replaceAll(" ", "");
		k = k.replaceAll("\n", "");

		if (k.equals("") || abs.equals(" "))
			abs = TagAbstract(body, "article-blk\"");

		k = abs.replaceAll(" ", "");
		k = k.replaceAll("\n", "");
		if (k.equals("") || abs.equals(" "))
			if (numabs != -1) {
				if (numabs != text.indexOf("abstracts")) {
					text = text.substring(numabs + 9);
					while (text.substring(0, 1).equals("\n")
							|| text.substring(0, 1).equals(" "))
						text = text.substring(1);
					int line = text.indexOf("\n");
					abs = text.substring(0, line);
				} else {
					text = text.substring(numabs + 9);
					numabs = text.indexOf("abstract");
					text = text.substring(numabs + 9);
					while (text.substring(0, 1).equals("\n")
							|| text.substring(0, 1).equals(" "))
						text = text.substring(1);
					int line = text.indexOf("\n");
					abs = text.substring(0, line);
				}
			}

		return abs;
	}

	public static String savePage(final String URL) throws IOException {
		String line = "", all = "";
		URL myUrl = null;
		BufferedReader in = null;
		try {
			myUrl = new URL(URL);
			in = new BufferedReader(new InputStreamReader(myUrl.openStream()));

			while ((line = in.readLine()) != null) {
				all += line;
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

		return all;
	}

	public static void main(String[] args) {

		try {
			saveUrl("dl.html", "http://dx.doi.org/10.1109/HICSS.2010.204");
			// "http://dl.acm.org/citation.cfm?doid=1298406.1298426");
			System.out.println("Done");
			System.out.println("abstract : \n" + Abstract("dl.html"));
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// url = "http://dl.acm.org/citation.cfm?doid=1298406.1298426";
		// try {
		// saveUrl("a.html", url);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
