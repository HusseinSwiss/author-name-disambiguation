/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.module;

import model.navigator.page.InternetPage;

/**
 *
 * @author Julien
 */
public interface ICrawler {
    
    public InternetPage getPage(String uri);
    
}
