package model.navigator.module.bing;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

import model.navigator.module.ICrawler;
import model.navigator.page.InternetPage;

/**
 *
 * @author Julien
 */
public class BingCrawler implements ICrawler {

    public static final String API_KEY = "M0QuHIn968QULiM1vaxN3HAZlubmp22DQYV982qgDQc=";
    private static BingCrawler bingCrawler = null;

    private BingCrawler() {
    }

    public static BingCrawler getInstance() {
        if (bingCrawler == null) {
            bingCrawler = new BingCrawler();
        }
        return bingCrawler;
    }

    @Override
    public InternetPage getPage(String uri) {
        
        String uri2 = uri.replaceAll(" ", "%20");
        uri2 = uri2.replaceAll("'", "%27");
        
        try {
            byte[] accountKeyBytes = Base64.encodeBase64((API_KEY + ":" + API_KEY).getBytes());

            String accountKeyEnc = new String(accountKeyBytes);
            URL url;
            url = new URL(uri2);

            System.out.println(uri2);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            StringBuilder sb = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            conn.disconnect();
            String result = sb.toString();
//System.out.println("AAAA" + result);
            return new InternetPage(uri, result);
            
        } catch (IOException ex) {
   
            return new InternetPage(uri, "");
        }
    }
}
