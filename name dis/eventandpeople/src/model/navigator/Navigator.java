/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator;

import java.util.HashMap;

import org.scribe.model.Token;

import model.navigator.command.CommandBing;
import model.navigator.command.CommandLinkedIn;
import model.navigator.command.CommandWeb;
import model.navigator.command.INavigatorCommand;
import model.navigator.page.InternetPage;


/**
 * @author Julien Tscherrig
 * @author Damien Goetschi
 */
public class Navigator {
    
    private static String UrlLinkedIn = "http://api.linkedin.com";
    private static String UrlBing = "https://api.datamarket.azure.com";
    private static HashMap<String, InternetPage> hash = null;
    private static Navigator navigator = null;

    private Navigator() {
    	
        hash = new HashMap<String, InternetPage>();

    }

    public static Navigator getInstance() {
        if (navigator == null) {
            navigator = new Navigator();
        }

        return navigator;
    }

    public synchronized InternetPage getPage(String url)  {
    	return getPage(url, false);
    }
    
    public synchronized InternetPage getPage(String url, boolean fromInternet) {


        if (fromInternet || !existPage(url)) {
            INavigatorCommand navigatorCommand = null;
            if (url.startsWith(UrlBing)) {
                navigatorCommand = new CommandBing(this, url);
            } else if (url.startsWith(UrlLinkedIn)) {
                navigatorCommand = new CommandLinkedIn(this, url);
            } else {
                navigatorCommand = new CommandWeb(this, url);
            }
            navigatorCommand.exec();
        }

        String body = hash.get(url).getContent();
		InternetPage page = null;
		page = new InternetPage(url, body);
        return page;
    }
  
    public synchronized void addPage(String url, InternetPage page) {

        removePage(url);
        hash.put(url, page);
    }

    public synchronized void removePage(String url) {
        hash.remove(url);
    }

    
    public synchronized boolean existPage(String url) {
        return hash.containsKey(url);

    }
}
