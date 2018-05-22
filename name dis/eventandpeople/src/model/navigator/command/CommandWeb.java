/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.command;

import model.navigator.Navigator;
import model.navigator.module.web.WebCrawler;
import model.navigator.page.InternetPage;

/**
 *
 * @author Julien
 */
public class CommandWeb implements INavigatorCommand{
    
    private Navigator navigator;
    private String url;
    
    private WebCrawler webCrawler = WebCrawler.getInstance();
    
    public CommandWeb(Navigator navigator, String url) {
        this.navigator = navigator;
        this.url = url;
    }
    
    @Override
    public void exec() {
        InternetPage page = webCrawler.getPage(url);
        navigator.addPage(url, page);
    }
       
}
