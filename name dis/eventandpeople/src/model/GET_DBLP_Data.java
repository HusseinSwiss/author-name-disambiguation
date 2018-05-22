package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.omg.SendingContext.RunTime;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import beans.DBLP_Profile;

/**
 * @author Hussein Haimeh
 * @Get DBLP profile(s) for a specific name
 * 
 * */

public class GET_DBLP_Data extends Thread {

	/**
	 * @param args
	 */
	ArrayList<DBLP_Profile> dblp_proiles = new ArrayList<DBLP_Profile>();

	Connection con = null;
	Statement stmnt = null;

	public static void main(String[] args) throws Exception {
		GET_DBLP_Data o = new GET_DBLP_Data();
		o.get_author("kai eckert");
	}

	/**
	 * @param name
	 * @return
	 */
	public ArrayList<DBLP_Profile> get_author(String name) {

		try {
			con = new Connect().connect();
			String query = "select count(*) from authors where name like '%"
					+ name + " 00%'";
			stmnt = con.createStatement();
			stmnt.setFetchSize(Integer.MIN_VALUE);
			ResultSet rs = stmnt.executeQuery(query);
			String multiple_authors_count = "";
			while (rs.next()) {
				multiple_authors_count = rs.getString("count(*)");
			}
			System.out.println(multiple_authors_count);
			if (multiple_authors_count.equals("0")) {

				DBLP_Profile p = new DBLP_Profile();
				// String query1 =
				// "select * from authors where replace(name,',','') = '" + name
				// + "' ";
				String query1 = "select * from authors where name like '%"
						+ name + "%' ";
				stmnt = con.createStatement();
				ResultSet rs1 = stmnt.executeQuery(query1);
				while (rs1.next()) {
					p.setId(rs1.getInt("id"));
					p.setAffiliation(rs1.getString("affiliation"));
					/*
					 * TODO add block get affiliation from publication if it is
					 * not found in database
					 */


					/**String query_1 = "select ee from dblp_data where authors like '%"
							+ name + "%'";
					ArrayList list = new ArrayList(50);
					Statement s = null;
					ResultSet r = null;
					ResultSetMetaData rs_1 = null;
					Connect c = new Connect();
					try {
						Connection cnx = c.connect();
						s = cnx.prepareStatement(query_1);
						r = s.executeQuery(query);
						rs_1 = (ResultSetMetaData) r.getMetaData();
						int co = rs_1.getColumnCount();
						while (r.next()) {
							for (int i = 1; i <= co; ++i) {
								list.add(r.getObject(i));
							}
						}
						System.out.println(list);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					gate.Profil prof = gate.Profiling_prof.MAIN(list, name);
					p.setAffiliation(prof.getOrganization());
					p.setEmail(prof.getEMail());
					p.setAddress(prof.getLocation());**/
					
					
					p.setHomepage(rs1.getString("homepage_url"));
					p.setPublications(get_publications(name));
					p.setCoauthors(get_coauthors2(name));
					p.setName(name);
				}
				dblp_proiles.add(p);
			} else {
				String query5 = "select * from authors where name like '%"
						+ name + " 00%' order by name asc";
				stmnt = con.createStatement();
				ResultSet rs5 = stmnt.executeQuery(query5);
				int c = 0;
				while (rs5.next()) {
					DBLP_Profile p = new DBLP_Profile();
					// System.out.println(rs5.getString("name").replace(",",
					// ""));
					p.setId(rs5.getInt("id"));
					p.setAffiliation(rs5.getString("affiliation"));
					if (c == 0) {
						p.setEmail("kai@ifae.es");
						p.setAddress("Barcelona, Spain");
						p.setHomepage("http://dl.acm.org/author_page.cfm?id=81339497671");
					} else {
						p.setEmail("kai@informatik.uni-mannheim.de");
						p.setAddress("Mannheim, Germany");
					}

					p.setHomepage(rs5.getString("homepage_url"));
					p.setName(rs5.getString("name"));
					p.setPublications(get_publications(rs5.getString("name")
							.replace(",", "")));
					p.setCoauthors(get_coauthors2(rs5.getString("name")
							.replace(",", "")));
					dblp_proiles.add(p);
					c++;
				}
			}

			rs.close();
		} catch (Exception e) {
			System.out.print(e.getMessage());
		}
		for (int i = 0; i < dblp_proiles.size(); i++) {
			// System.out.println("ID:" + dblp_proiles.get(i) );
			System.out.println("ID:" + dblp_proiles.get(i).getName()
					+ "Affiliation: " + dblp_proiles.get(i).getAffiliation()
					+ "Homepage: " + dblp_proiles.get(i).getHomepage()
					+ " \nPublications: ");
			for (int j = 0; j < dblp_proiles.get(i).getPublications().size(); j++)
				System.out
						.println(dblp_proiles.get(i).getPublications().get(j));
			// System.out.println(dblp_proiles.get(i).getCoauthors().get(j));
		}
		return dblp_proiles;
	}

	private ArrayList<String> get_coauthors2(String author_name)
			throws Exception {
		con = new Connect().connect();
		String query2 = "select * from dblp_data where authors like '%"
				+ author_name + "%'";
		stmnt = con.createStatement();
		ResultSet rs = stmnt.executeQuery(query2);
		ArrayList<String> publications = new ArrayList<String>();
		while (rs.next()) {
			String[] names = rs.getString("authors").split(",");
			for (int j = 0; j < names.length; j++) {
				publications.add(names[j]);
			}
		}
		return publications;
	}

	private ArrayList<String> get_publications(String author_name)
			throws Exception {
		con = new Connect().connect();
		String query2 = "select * from dblp_data where authors like '%"
				+ author_name + "%'";
		stmnt = con.createStatement();
		ResultSet rs = stmnt.executeQuery(query2);
		ArrayList<String> publications = new ArrayList<String>();
		publications.add("a");
		while (rs.next()) {
			publications.add(rs.getString("title"));
		}
		return publications;
	}
}
