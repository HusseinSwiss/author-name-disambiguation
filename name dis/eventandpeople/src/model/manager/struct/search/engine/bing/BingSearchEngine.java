package model.manager.struct.search.engine.bing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import model.navigator.Navigator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class BingSearchEngine{
	public static int MAX_SEARCH = 40;

	public static ArrayList<String> getBingResult(String query) {
		try {
			ArrayList<String> result = new ArrayList<>();

			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			
			String queryUrl = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/v1/Web?Query=%27"
					+ query.replaceAll(" ", "%20") + "%27&$top=" + MAX_SEARCH;
			
			InputSource is = new InputSource(new StringReader(Navigator.getInstance().getPage(queryUrl).getContent()));
			Document document = builder.parse(is);

			Element racine = document.getDocumentElement();
			NodeList entries = racine.getElementsByTagName("entry");

			for (int i = 0; i < entries.getLength(); i++) {
				Element entry = (Element) entries.item(i);

				NodeList properties = ((Element) entry)
						.getElementsByTagName("m:properties");

				for (int j = 0; j < properties.getLength(); j++) {
					Element property = (Element) properties.item(j);
					String url = property.getElementsByTagName("d:Url").item(0)
							.getTextContent().trim();

					result.add(url);
				}
			}
			return result;
		} catch (SAXException | ParserConfigurationException | IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}
