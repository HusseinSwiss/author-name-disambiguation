package servlet;

import static constant.Cnst.ATTR_ARNETMINER_CONTENT;
import static constant.Cnst.ATTR_ARNETMINER_LIST;
import static constant.Cnst.ATTR_DBLP_CONTENT;
import static constant.Cnst.ATTR_DBLP_LIST;
import static constant.Cnst.ATTR_FREEBASE_CONTENT;
import static constant.Cnst.ATTR_FREEBASE_LIST;
import static constant.Cnst.ATTR_LINKEDIN_CONTENT;
import static constant.Cnst.ATTR_LINKEDIN_LIST;
import static constant.Cnst.ATTR_MAS_CONTENT;
import static constant.Cnst.ATTR_MAS_LIST;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import model.GET_DBLP_Data;
import model.Get_MAS_XML_Data;
import model.arnetminer.GetArnetminerPerson;
import model.manager.struct.search.engine.freebase.FreebaseSearchEngine;
import model.manager.struct.search.engine.linkedin.LinkedInSearchEngine;
import model.matchers.DBLP_MAS_Matcher;
import beans.ArnetminerPerson;
import beans.DBLP_Profile;
import beans.FreebaseEducation;
import beans.FreebaseEmployment;
import beans.FreebasePerson;
import beans.LinkedInPerson;
import beans.LinkedInPosition;
import beans.MAS_Profile;
import beans.Person;
import facebook4j.internal.org.json.JSONException;

public class SearchPeopleServlet extends HttpServlet {

	private ArrayList<LinkedInPerson> linkedInList = new ArrayList<LinkedInPerson>();
	private ArrayList<MAS_Profile> MASList = new ArrayList<MAS_Profile>();
	private ArrayList<DBLP_Profile> DBLPList = new ArrayList<DBLP_Profile>();
	private ArrayList<Person> persons = new ArrayList<Person>();
	private String match_result = "";
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String name = req.getParameter("name");
		String profession = req.getParameter("profession");
		if (name != null && !name.equals("")) {
			ArrayList<ArnetminerPerson> arnetminerList = null;
			// linkedInList = LinkedInSearchEngine.getResults(name);
			ArrayList<FreebasePerson> freebaseList = FreebaseSearchEngine.getResults(name);
//			try {
//				arnetminerList = new GetArnetminerPerson().getPersonInfo(name);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

			getLinkedInPerson(name);
			//getDBLPInfo(name);
			//getMASInfo(name);
		 
			try {
				persons = new DBLP_MAS_Matcher().match(name);
			} catch (ArrayIndexOutOfBoundsException e) {
				 e.printStackTrace();
			} catch (ParserConfigurationException e) {
				 e.printStackTrace();
			} catch (SAXException e) {
				 e.printStackTrace();
			}
			//createDataset();

			req.getSession().setAttribute(ATTR_LINKEDIN_LIST, linkedInList);
			req.getSession().setAttribute(ATTR_DBLP_LIST, DBLPList);
			req.getSession().setAttribute(ATTR_MAS_LIST, MASList);
			req.getSession().setAttribute(ATTR_FREEBASE_LIST, freebaseList);
			req.getSession().setAttribute(ATTR_ARNETMINER_LIST, arnetminerList);

			req.setAttribute(ATTR_LINKEDIN_CONTENT, linkedInPart(linkedInList));
			req.setAttribute(ATTR_DBLP_CONTENT, dblpPart(DBLPList));
			req.setAttribute(ATTR_MAS_CONTENT, masPart(persons));
			req.setAttribute(ATTR_FREEBASE_CONTENT, freebasePart(freebaseList));
			req.setAttribute(ATTR_ARNETMINER_CONTENT, arnetminerPart(arnetminerList));

			this.getServletContext()
					.getRequestDispatcher("/WEB-INF/peoplesearch/result.jsp")
					.forward(req, resp);
		} else if (profession != null && !profession.equals("")) {
			ArrayList<FreebasePerson> freebaseList = FreebaseSearchEngine
					.getProfessionResults(profession);
			req.getSession().setAttribute(ATTR_FREEBASE_LIST, freebaseList);
			req.setAttribute(ATTR_FREEBASE_CONTENT, freebasePart(freebaseList));
			this.getServletContext()
					.getRequestDispatcher("/WEB-INF/peoplesearch/result.jsp")
					.forward(req, resp);
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		ArrayList<LinkedInPerson> linkedInList = (ArrayList<LinkedInPerson>) req
				.getSession().getAttribute(ATTR_LINKEDIN_LIST);
		ArrayList<FreebasePerson> freebaseList = (ArrayList<FreebasePerson>) req
				.getSession().getAttribute(ATTR_FREEBASE_LIST);
		if (linkedInList != null)
			req.setAttribute(ATTR_LINKEDIN_CONTENT, linkedInPart(linkedInList));
		if (freebaseList != null)
			req.setAttribute(ATTR_FREEBASE_CONTENT, freebasePart(freebaseList));
		this.getServletContext()
				.getRequestDispatcher("/WEB-INF/peoplesearch/result.jsp")
				.forward(req, resp);
	}

	public void getLinkedInPerson(String name) throws IOException {
		ArrayList<LinkedInPerson> list = LinkedInSearchEngine.getResults(name);
		for (int i = 0; i < list.size(); i++) {
			String Name = list.get(i).getFirstName() + " " + list.get(i).getLastName();
			if(Name.toLowerCase().equals(name.toLowerCase())) 
				linkedInList.add(list.get(i));
		}
	}

	public void getDBLPInfo(String name) {
		GET_DBLP_Data dblp = new GET_DBLP_Data();
		ArrayList<DBLP_Profile> list = dblp.get_author(name);
		for (int i = 0; i < list.size(); i++) {
			DBLPList.add(list.get(i));
		}
	}

	public void getMASInfo(String name) {
		Get_MAS_XML_Data mas = new Get_MAS_XML_Data();
		ArrayList<MAS_Profile> list = mas.get_author(name.replace(" ", "%20"));
		for (int i = 0; i < list.size(); i++) {
			MASList.add(list.get(i));
		}
	}

	public void createDataset() {
		int l = linkedInList.size();
		String[][] dataset = new String[l][3];

		for (int i = 0; i < l; i++) {
			for (int j = 0; j < 3; j++) {
				dataset[i][j] = linkedInList.get(i).getFirstName();
			}
			System.out.println();
		}

		for (int i = 0; i < l; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(dataset[i][j]);
			}
			System.out.println();
		}
	}

	private String freebasePart(ArrayList<FreebasePerson> list) {
		String page = "";
		if (list != null)
			for (int i = 0; i < list.size(); i++) {
				FreebasePerson p = list.get(i);
				if (p != null) {
					String pString = "";
					if (p.getPicture() != null)
						pString += "<p style=\"float:left\"/><img src=\""
								+ p.getPicture() + "\"/></p>";
					if (p.getProfessions() != null
							&& p.getProfessions().size() > 0)
						pString += "<center><p><h2><INPUT type=\"checkbox\" checked name=\"freebase\" value="
								+ i
								+ " /><a href=\""
								+ p.getUrl()
								+ "\">"
								+ p.getName()
								+ "</a></h2><h3>"
								+ p.getProfessions().get(0)
								+ "</p></h3></center>";
					else
						pString += "<center><p><h2><INPUT type=\"checkbox\" checked name=\"freebase\" value="
								+ i
								+ " /><a href=\""
								+ p.getUrl()
								+ "\">"
								+ p.getName() + "</a></h2></center>";

					pString += "<table><tr><td valign=\"top\"><table>";
					pString += "<tr><td valign=\"top\">Id:</td><td>"
							+ p.getId() + "</td>";
					if (p.getGender() != null)
						pString += "<tr><td valign=\"top\">Gender:</td><td>"
								+ p.getGender() + "</td></tr>";
					if (p.getBirthDate() != null)
						pString += "<tr><td valign=\"top\">Birth date:</td><td>"
								+ p.getBirthDate() + "</td></tr>";
					if (p.getBirthPlace() != null)
						pString += "<tr><td valign=\"top\">Birth place:</td><td>"
								+ p.getBirthPlace() + "</td></tr>";
					if (p.getReligion().size() > 0) {
						pString += "<tr><td valign=\"top\">Religion:</td><td>";
						for (String s : p.getReligion())
							pString += s + "<br/>";
						pString += "</td>";
					}
					if (p.getEthnicity().size() > 0) {
						pString += "<tr><td valign=\"top\">Ethnicity:</td><td>";
						for (String s : p.getEthnicity())
							pString += s + "<br/>";
						pString += "</td>";
					}
					pString += "</table>";

					pString += "</td><td valign=\"top\">";

					pString += "<table>";

					if (p.getNationalities().size() > 0) {
						pString += "<tr><td valign=\"top\">Nationalities:</td><td>";
						for (String s : p.getNationalities())
							pString += s + "<br/>";
						pString += "</td>";
					}
					if (p.getProfessions().size() > 0) {
						pString += "<tr><td valign=\"top\">Professions:</td><td>";
						for (String s : p.getProfessions())
							pString += s + "<br/>";
						pString += "</td>";
					}
					pString += "</table>";

					pString += "</td><td valign=\"top\">";

					pString += "<table>";

					if (p.getEducations().size() > 0) {
						pString += "<tr><td valign=\"top\">Education:</td><td>";
						for (FreebaseEducation e : p.getEducations()) {
							pString += "<b>" + e.getInstitution() + "</b><br/>";
							if (e.getStart_date() != null)
								pString += "Start date: " + e.getStart_date()
										+ "<br/>";
							if (e.getEnd_date() != null)
								pString += "End date: " + e.getEnd_date()
										+ "<br/>";
							if (e.getDegree() != null)
								pString += "Degree: " + e.getDegree() + "<br/>";
							if (e.getMajor_field_of_study() != null)
								pString += "Major field of study: "
										+ e.getMajor_field_of_study() + "<br/>";
							if (e.getSpecialization() != null)
								pString += "Specialization: "
										+ e.getSpecialization() + "<br/>";
						}
						pString += "</td>";
					}
					pString += "</table>";

					pString += "</td><td valign=\"top\">";

					pString += "<table>";
					if (p.getEmployments().size() > 0) {
						pString += "<tr><td valign=\"top\">Employments:</td><td>";
						for (FreebaseEmployment e : p.getEmployments()) {
							pString += "<b>" + e.getEmployer() + "</b><br/>";
							if (e.getFrom() != null)
								pString += "Start date: " + e.getFrom()
										+ "<br/>";
							if (e.getTo() != null)
								pString += "End date: " + e.getTo() + "<br/>";
							if (e.getTitle() != null)
								pString += "Title: " + e.getTitle() + "<br/>";
						}
						pString += "</td>";
					}

					pString += "</table>";

					pString += "</td></tr><tr><td colspan=\"4\">";

					if (p.getQuotations().size() > 0) {
						pString += "<table><tr><td valign=\"top\">Quotation:</td><td>";
						for (String s : p.getQuotations())
							pString += s + "<br/>";
						pString += "</td></tr></table>";
					}

					pString += "</td></tr></table><br/><br/><br/><br/><br/>";

					page += pString;

				}
			}
		return page;
	}

	private String arnetminerPart(ArrayList<ArnetminerPerson> arnetminerList) {
		String page = "";
		if (arnetminerList != null)
			for (int i = 0; i < arnetminerList.size(); i++) {
				ArnetminerPerson p = arnetminerList.get(i);
				if (p != null) {
					page += "<p style=\"float:left\"/><img width=90 height=90 src=\""
							+ p.getPicUrl() + "\"/></p>";
					page += "<center><p><h2><INPUT type=\"checkbox\" checked name=\"linkedin\" value=0 /><a href=\""
							+ p.getUrl()
							+ "\">"
							+ p.getFullname()
							+ "</a></h2><h3>"
							+ p.getPosition()
							+ "</h3></center>";
					page += "<table><tr><td>";

					page += "<table>";
					page += "<tr><td valign=\"top\">Id:</td><td>" + p.getId()
							+ "</td>";
					page += "<tr><td valign=\"top\">Full name:</td><td>"
							+ p.getFullname() + "</td></tr>";
					page += "<tr><td valign=\"top\">Email:</td><td>"
							+ p.getEmail() + "</td></tr>";
					page += "<tr><td valign=\"top\">Address:</td><td>"
							+ p.getAddress() + "</td></tr>";
					page += "<tr><td valign=\"top\">Phone:</td><td>"
							+ p.getPhone() + "</td></tr>";
					page += "<tr><td valign=\"top\">Position:</td><td>"
							+ p.getPosition() + "</td></tr>";
					page += "<tr><td valign=\"top\">H-index:</td><td>"
							+ p.getHindex() + "</td></tr>";
					page += "</table>";

					page += "</td></tr></table><br/><br/><br/>";
				}
			}
		return page;
	}

	private String linkedInPart(ArrayList<LinkedInPerson> list) {
		String page = "";
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {
			LinkedInPerson p = list.get(i);
			String pString = "";
			// show picture if link is not null
			if (p.getPicture() != null)
				pString += "<p style=\"float:left\"/><img src=\""
						+ p.getPicture() + "\"/></p>";
			// show name and headline
			pString += "<center><p><h2><INPUT type=\"checkbox\" checked name=\"linkedin\" value="
					+ i
					+ " /><a href=\""
					+ p.getUrl()
					+ "\">"
					+ p.getFormattedName()
					+ "</a></h2><h3>"
					+ p.getHeadline()
					+ "</p></h3></center>";

			pString += "<table><tr><td>";

			pString += "<table>";
			pString += "<tr><td valign=\"top\">Id:</td><td>" + p.getId()
					+ "</td>";
			pString += "<tr><td valign=\"top\">First name:</td><td>"
					+ p.getFirstName() + "</td></tr>";
			pString += "<tr><td valign=\"top\">Last name:</td><td>"
					+ p.getLastName() + "</td></tr>";
			pString += "<tr><td valign=\"top\">Country:</td><td>"
					+ p.getCountry() + "</td></tr>";
			pString += "<tr><td valign=\"top\">Location name:</td><td>"
					+ p.getLocationName() + "</td></tr>";
			if (p.getIndustry() != null)
				pString += "<tr><td valign=\"top\">Industry:</td><td>"
						+ p.getIndustry() + "</td></tr>";
			if (p.getSummary() != null)
				pString += "<tr><td valign=\"top\">Summary:</td><td>"
						+ p.getSummary() + "</td></tr>";
			if (p.getSpecialties() != null)
				pString += "<tr><td valign=\"top\">Specialties:</td><td>"
						+ p.getSpecialties() + "</td></tr>";
			pString += "</table>";

			pString += "</td><td>";

			pString += "<table>";
			if (p.getPositions().size() > 0) {
				pString += "<tr><td colspan=\"2\"><h3>Positions:</h3></td></tr>";
			}
			for (LinkedInPosition pos : p.getPositions()) {
				String date;
				if (pos.isCurrent())
					if (pos.getStartYear() == 0)
						date = "now";
					else
						date = pos.getStartYear() + " - now";
				else
					date = pos.getStartYear() + " - " + pos.getEndYear();
				pString += "<tr><td valign=\"top\"><b>" + date
						+ "</b></td><td>";
				if (pos.getCompany() != null) {
					if (pos.getCompany().getName() != null)
						pString += pos.getCompany().getName() + "<br/>";
					if (pos.getCompany().getIndustry() != null)
						pString += pos.getCompany().getIndustry() + "<br/>";
					if (pos.getCompany().getType() != null)
						pString += pos.getCompany().getType() + "<br/>";
					if (pos.getCompany().getSize() != null)
						pString += pos.getCompany().getSize() + "<br/>";
				}
				if (pos.getTitle() != null)
					pString += pos.getTitle() + "<br/>";
				if (pos.getSummary() != null)
					pString += pos.getSummary() + "<br/>";
				pString += "</td></tr>";
			}
			pString += "</table>";

			pString += "</td></tr></table><br/><br/><br/>";
			page += pString;
		}
		return page;
	}
	
	private String dblpPart(ArrayList<DBLP_Profile> dblpList) {
		String page = "";
		if (dblpList != null)
			for (int i = 0; i < dblpList.size(); i++) {
				DBLP_Profile p = dblpList.get(i);
				if (p != null) {
					page += "<center><p><h2><INPUT type=\"checkbox\" checked name=\"linkedin\" value=0 /><a href=\""
							+ p.getHomepage()
							+ "\">"
							+ p.getName()
							+ "</a></h2>"
							+ "</center>";
					page += "<table border=1><tr><td>ID</td><td>";
					page += p.getId() + "</td></tr>";
					page += "<tr><td valign=\"top\">Full name:</td><td>"
							+ p.getName() + "</td></tr>";
					page += "<tr><td valign=\"top\">Email:</td><td>"
							+ p.getEmail() + "</td></tr>";
					page += "<tr><td valign=\"top\">Address:</td><td>"
							+ p.getAddress() + "</td></tr><tr>";
					page += "<td valign=\"top\"> Affiliation:</td><td>"
							+ p.getAffiliation() + "</td></tr><tr>";
					page += "<td valign=\"top\"> Publications: </td><td>";
							for(int j=0;j<p.getPublications().size();j++){
								page += p.getPublications().get(j) + "<br />";
							}
					page += "</td></tr></table><br>";
				}
			}
		return page;
	}
	
	private String masPart(ArrayList<Person> masList) {
		String page = "";
		if (masList != null)
			for (int i = 0; i < masList.size(); i++) {
				Person p = masList.get(i);
				if (p != null) {
					page += "<center><p><h2><INPUT type=\"checkbox\" checked name=\"linkedin\" value=0 /><a href=\""
							+ p.getHomepage()
							+ "\">"
							+ p.getFull_name()
							+ "</a></h2>"
							+ "</center>";
					page += "<table border=0><tr><td><img src='" + p.getPhoto_url() + "'></td><td>ID</td><td>";
					page += p.getId() + "</td></tr>";
					page += "<tr><td>&nbsp;</td><td valign=\"top\">Full name:</td><td>"
							+ p.getFull_name() + "</td></tr>";
					page += "<td>&nbsp;</td><td valign=\"top\"> Affiliation:</td><td>"
							+ p.getAffiliation() + "</td></tr><tr>";
					page += "<td>&nbsp;</td><td valign=\"top\"> Email:</td><td>"
							+ p.getEmail() + "</td></tr><tr>";
					page += "<td>&nbsp;</td><td valign=\"top\"> Location:</td><td>"
							+ p.getLocation() + "</td></tr><tr>";
					page += "<td>&nbsp;</td><td valign=\"top\"> Summary:</td><td>"
							+ p.getBiography() + "</td></tr><tr>";
					page += "<td>&nbsp;</td><td valign=\"top\"> Publications: </td><td>";
							for(int j=0;j<p.getPublications().size();j++){
								page += p.getPublications().get(j) + "<br />";
							}
					page += "</td></tr></table>";
				}
			}
		return page;
	}
}