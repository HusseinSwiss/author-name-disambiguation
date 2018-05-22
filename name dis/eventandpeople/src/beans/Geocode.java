package beans;

/**
 * Bean takes a place   and returns the geocode of it
 * @author Hussein Hazimeh
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import constant.Cnst;

import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

public class Geocode {
	private String city;
	private String country;
	private String lat;
	private String lng;

	public Geocode(String city) {
		this.city = city;
	}

	public void setlnglat() throws IOException {
		// get Json with more information for this place and add this
		// information to place

		try {
			String res = getJSONFromURL();
			JSONObject obj = new JSONObject(res);
			JSONObject res1 = obj.getJSONArray("results").getJSONObject(0);
			JSONObject loc = res1.getJSONObject("geometry").getJSONObject(
					"location");
			this.lat = loc.getDouble("lat") + "";
			this.lng = loc.getDouble("lng") + "";
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getLat() {
		return this.lat;
	}

	public String getLng() {
		return this.lng;
	}

	private String getJSONFromURL() throws IOException {
		String res = "";
		// create url for the Google places query with field to get and id of
		// page and add token of the user
		// the request must be under https
		String urlString = "https://maps.google.com/maps/api/geocode/json?sensor=false&address="
				+ this.city;
		URL url = new URL(urlString);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String input;
		// create a String with the response to the query
		while ((input = br.readLine()) != null) {
			res += input;
		}
		br.close();
		return res;
	}

}
