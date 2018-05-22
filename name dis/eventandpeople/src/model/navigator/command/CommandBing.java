/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.command;

import model.navigator.Navigator;
import model.navigator.module.bing.BingCrawler;
import model.navigator.page.InternetPage;

/**
 *
 * @author Julien
 */
public class CommandBing implements INavigatorCommand{

    private Navigator navigator;
    private String url;
    
    private BingCrawler bingCrawler = BingCrawler.getInstance();
    
    public CommandBing(Navigator navigator, String url) {
        this.navigator = navigator;
        this.url = url;
    }
    
    @Override
    public void exec() {
        InternetPage page = bingCrawler.getPage(url);
        navigator.addPage(url, page);
    }
    
}
