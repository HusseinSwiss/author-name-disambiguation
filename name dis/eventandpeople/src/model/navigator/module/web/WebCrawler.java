/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.module.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import model.navigator.module.ICrawler;
import model.navigator.page.InternetPage;

/**
 *
 * @author Julien
 */
public class WebCrawler implements ICrawler {
    
    private static String URL_AUTHOR = "http://dblp.uni-trier.de/rec/pers/";
    private static String URL_PUBLICATION = "http://dblp.uni-trier.de/rec/bibtex/";
    private static String URL_AUTHORS_SEARCH = "http://dblp.uni-trier.de/search/author?xauthor=";
    
    private static String URL_CONF = "???";
    
    private static WebCrawler webCrawler = null;

    private WebCrawler() {
        
    }

    public static WebCrawler getInstance() {

        if (webCrawler == null) {
            webCrawler = new WebCrawler();
        }
        return webCrawler;
    }

    @Override
    public InternetPage getPage(String uri) {
        String content = "";
			
        try {
            URL url = new URL(uri);
            
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                    content += inputLine;
            }
            
            in.close();
        } catch (IOException ex) {
        	System.err.println(ex.getMessage());
            return new InternetPage(uri, "");
        }
	
        return new InternetPage(uri, content);
    }
}
