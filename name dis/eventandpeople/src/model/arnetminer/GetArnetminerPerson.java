package model.arnetminer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import beans.ArnetminerPerson;
import facebook4j.internal.org.json.JSONArray;
import facebook4j.internal.org.json.JSONException;
import facebook4j.internal.org.json.JSONObject;

/**
 * Get person/list of persons from Arnetminer (www.arnetminer.com)
 * 
 * @author Hussein Hazimeh
 * */
public class GetArnetminerPerson {

	private String name;

	/**
	 * @param name
	 * @return list
	 * @throws IOException
	 * @throws JSONException
	 */
	public ArrayList<ArnetminerPerson> getPersonInfo(String name)
			throws IOException, JSONException {
		
		String id;
		String email;
		String phone;
		String homepage;
		String position;
		String address;
		String pictureurl;
		String hindex;
		String name1;
		
		this.name = name;
		String res = getPersonInfo();
		String open = "{ \"results\" : ";
		String close = "}";
		JSONObject obj = new JSONObject(open+res+close);
		JSONArray arr = obj.getJSONArray("results");
		JSONObject o = arr.getJSONObject(0);
		 
		try{
			 name1 = o.getString("Name");
		}catch(JSONException e){ name1 = ""; }
		try{
			 email = o.getString("Email");
		}catch(JSONException e){ email = ""; }
		try{
			 id = o.getString("Id");
		}catch(JSONException e){ id = ""; }
		try{
			 phone = o.getString("Phone");
		}catch(JSONException e){ phone = ""; }
		try{
			 homepage = o.getString("Homepage");
		}catch(JSONException e){ homepage = ""; }
		try{
			 position = o.getString("Position");
		}catch(JSONException e){ position = ""; }
		try{
			 address = o.getString("Address");
		}catch(JSONException e){ address = ""; }
		try{
			 pictureurl = o.getString("PictureUrl");
		}catch(JSONException e){ pictureurl = ""; }
		try{
			 hindex = o.getString("Hindex");
		}catch(JSONException e){ hindex = ""; }
		
		ArrayList<ArnetminerPerson> list = new ArrayList<ArnetminerPerson>();
		
			ArnetminerPerson amPerson = new ArnetminerPerson();
			amPerson.setId(id);
			amPerson.setEmail(email);
			amPerson.setFullname(name1);
			amPerson.setAddress(address);
			amPerson.setPhone(phone);
			amPerson.setHindex(hindex);
			amPerson.setPicUrl(pictureurl);
			amPerson.setPosition(position);
			amPerson.setUrl(homepage);
			list.add(amPerson);
			
		return list;
	}
	private String getPersonInfo() throws IOException {
		String res = "";
		String validName = this.name.replace(' ', '_');
		String urlString = "http://arnetminer.org/services/person/" + validName + "?u=oyster&o=ttf";
		System.out.println(urlString);
		URL url = new URL(urlString);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String input;
		while ((input = br.readLine()) != null) {
			res += input;
		}
		br.close();
		return res;
	}

}
