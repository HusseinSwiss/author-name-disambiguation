/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.command;

import org.scribe.model.Token;

import model.navigator.Navigator;
import model.navigator.module.linkedin.LinkedInCrawler;
import model.navigator.page.InternetPage;

/**
 *
 * @author Julien
 */
public class CommandLinkedIn implements INavigatorCommand{
    
    private Navigator navigator;
    private String url;
    
    private LinkedInCrawler linkedInCrawler = LinkedInCrawler.getInstance();
    
    public CommandLinkedIn(Navigator navigator, String url) {
        this.navigator = navigator;
        this.url = url;
    }
    
    @Override
    public void exec() {
    	InternetPage page  = linkedInCrawler.getPage(url);
        navigator.addPage(url, page);
    }
       
}
