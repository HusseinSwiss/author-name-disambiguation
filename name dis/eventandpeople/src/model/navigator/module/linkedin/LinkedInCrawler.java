/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model.navigator.module.linkedin;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.navigator.module.ICrawler;
import model.navigator.page.InternetPage;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author Julien
 */
public class LinkedInCrawler implements ICrawler {

	/*
	public static final String consumer_key = "8xz5oep9n00i";
    public static final String consumer_secret = "IAnkfZFY2ve8oPxL";
    public static final String access_token = "87639710-ce8b-4b5f-8573-f19b4df6e1c4";
    public static final String access_token_secret = "86a99abe-f83c-4a9d-a480-579ae9d9bb5d";
    */
	public static final String consumer_key = "77np01eb9iy3wk";
    public static final String consumer_secret = "a4ZUw3bcI13i8Zmj";
    public static final String access_token = "794af420-b862-4b1e-b478-2fc0ab60d3ac";
    public static final String access_token_secret = "57fb306b-6041-41e5-af39-c730c509d622";
    
    private OAuthService oAuthService;
    private Token accessToken;
    
    private static LinkedInCrawler linkedInCrawler = null;

    private LinkedInCrawler() {
        this.oAuthService = new ServiceBuilder()
                .provider(LinkedInApiWithScope.class)
                .apiKey(consumer_key)
                .apiSecret(consumer_secret)
                .build();

        this.accessToken = new Token(access_token, access_token_secret);
    }

    public static LinkedInCrawler getInstance() {
        if (linkedInCrawler == null) {
            linkedInCrawler = new LinkedInCrawler();
        }
        return linkedInCrawler;
    }
    
    public InternetPage getPage(String uri,Token token) {
        try {
            String publicProfile = uri.substring("http://api.linkedin.com/v1/people/url=".length());
            publicProfile = publicProfile.substring(0, publicProfile.indexOf(":("));
           
            publicProfile = URLDecoder.decode(publicProfile, "Cp1252");
            publicProfile = URLEncoder.encode(publicProfile, "Cp1252");
            
            
            String uri2 = "http://api.linkedin.com/v1/people/url=" + publicProfile + uri.substring(uri.indexOf(":("));
         
            
            OAuthRequest request = new OAuthRequest(Verb.GET, uri2);
            oAuthService.signRequest(token, request);
            Response response = request.send();

            String body = response.getBody();
            return new InternetPage(uri2, body);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LinkedInCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }


    @Override
    public InternetPage getPage(String uri) {
        return getPage(uri,accessToken);
    }
}
