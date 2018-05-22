package servlet;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import facebook4j.GeoLocation;
import static constant.Cnst.*;
import model.GetInfoGoogleMaps;
import beans.SocialDistanceDuration;
import beans.SocialLocation;
/**
 * Servlet for retrieve caches when the server start
 * @author Damien Goetschi
 *
 */
public class CacheRetrieve extends HttpServlet {
	public void init() throws ServletException{
		//Retrieve location cache
		try {
	    	PATH=this.getServletContext().getRealPath("WEB-INF/socialevent/res/");
	    	FileInputStream fichier = new FileInputStream(PATH+"\\"+CACHE_LOCATIONS);
	    	ObjectInputStream ois = new ObjectInputStream(fichier);
	    	GetInfoGoogleMaps.cacheLoc = (HashMap<String,SocialLocation>) ois.readObject();
	    }
	    catch (ClassNotFoundException e) {
	    	e.printStackTrace();
	    } catch (FileNotFoundException e) {
			GetInfoGoogleMaps.cacheLoc=new HashMap<String,SocialLocation>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Retrive Geolocation cache
		try {
	    	FileInputStream fichier = new FileInputStream(PATH+"\\"+CACHE_GEOLOCATIONS);
	    	ObjectInputStream ois = new ObjectInputStream(fichier);
	    	GetInfoGoogleMaps.cacheGeoLoc = (HashMap<String,GeoLocation>) ois.readObject();
	    }
	    catch (ClassNotFoundException e) {
	    	e.printStackTrace();
	    } catch (FileNotFoundException e) {
			GetInfoGoogleMaps.cacheGeoLoc=new HashMap<String,GeoLocation>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Retrieve distance cache
		try {
	    	FileInputStream fichier = new FileInputStream(PATH+"\\"+CACHE_DISTANCES);
	    	ObjectInputStream ois = new ObjectInputStream(fichier);
	    	GetInfoGoogleMaps.cacheDistance = (HashMap<String,SocialDistanceDuration>) ois.readObject();
	    }
	    catch (ClassNotFoundException e) {
	    	e.printStackTrace();
	    } catch (FileNotFoundException e) {
			GetInfoGoogleMaps.cacheDistance=new HashMap<String,SocialDistanceDuration>();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Retrieve autocomplete cache
		try {
	    	FileInputStream fichier = new FileInputStream(PATH+"\\"+CACHE_AUTOCOMPLETE);
	    	ObjectInputStream ois = new ObjectInputStream(fichier);
	    	GetInfoGoogleMaps.cacheAutocomplete = (HashMap<String,String>) ois.readObject();
	    }
	    catch (ClassNotFoundException e) {
	    	e.printStackTrace();
	    } catch (FileNotFoundException e) {
			GetInfoGoogleMaps.cacheAutocomplete = new HashMap<String,String>();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
