/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.page;

import java.io.Serializable;


/**
 *
 * @author Julien
 */
public class InternetPage {
    
    private String url;
    private String content;
            
    public InternetPage(String url, String content) {
        this.url = url;		
		this.content = content;
    }
    
    public String getUrl() {
        return url;
    }
    
    public String getContent() {
        return content;
    }
    
    @Override
    public String toString() {
        String r;
        
        if(getContent().length() > 120) {
            r =  url + " : " + getContent().substring(0, 120);
        } else {
            r =  url + " : " + getContent();
        }
        return r;
    }
   
}
