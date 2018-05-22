package servlet.sesame;

import static constant.Cnst.*;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.FreebaseEducation;
import beans.FreebaseEmployment;
import beans.FreebasePerson;
import beans.LinkedInPerson;
import beans.LinkedInPosition;
import beans.sesame.SesameEducation;
import beans.sesame.SesameEmployment;
import beans.sesame.SesameFreebasePerson;
import beans.sesame.SesameLinkedInPerson;
import beans.sesame.SesamePerson;
import model.manager.struct.search.engine.freebase.FreebaseSearchEngine;
import model.manager.struct.search.engine.linkedin.LinkedInSearchEngine;
import model.sesame.GetInfoSesamePeopleSearch;

public class SesameSearchPeopleServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
		String name = req.getParameter("name");
		ArrayList<SesamePerson> sesameList = GetInfoSesamePeopleSearch.getByName(name);
		req.getSession().setAttribute(ATTR_SESAME_LIST,sesameList);
		req.setAttribute(ATTR_SESAME_CONTENT,getContent(sesameList));
		this.getServletContext().getRequestDispatcher( "/WEB-INF/peoplesearch/sesameResult.jsp" ).forward(req,resp);
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		ArrayList<SesamePerson> sesameList = (ArrayList<SesamePerson>) req.getSession().getAttribute(ATTR_SESAME_LIST);
		if(sesameList!=null)
			req.setAttribute(ATTR_SESAME_CONTENT,getContent(sesameList));
		this.getServletContext().getRequestDispatcher( "/WEB-INF/peoplesearch/sesameResult.jsp" ).forward(req,resp);
	}
	
	private String getContent(ArrayList<SesamePerson> sesameList) {
		String content = "";
		for(SesamePerson p : sesameList){
			if(p.getClass().equals(SesameFreebasePerson.class))
				content+=freebasePerson((SesameFreebasePerson)p);
			else if(p.getClass().equals(SesameLinkedInPerson.class))
				content+=linkedInPerson((SesameLinkedInPerson)p);
		}
		return content;
	}
	
	private String freebasePerson(SesameFreebasePerson p) {
		if(p!=null){
			String pString = "From Freebase";
			if(p.getPicture()!=null)
				pString += "<p style=\"float:left\"/><img src=\""+p.getPicture()+"\"/></p>";
			if(p.getProfessions()!=null&&p.getProfessions().size()>0)
				pString+="<center><p><h2><a href=\""+p.getUrl()+"\">"+p.getName()+"</a></h2><h3>"+p.getProfessions().get(0)+"</p></h3></center>";
			else
				pString+="<center><p><h2><a href=\""+p.getUrl()+"\">"+p.getName()+"</a></h2></center>";
			pString+="<table><tr><td valign=\"top\"><table>";
			pString+="<tr><td valign=\"top\">Id:</td><td>"+p.getId()+"</td>";
			if(p.getGender()!=null)
				pString+="<tr><td valign=\"top\">Gender:</td><td>"+p.getGender()+"</td></tr>";
			if(p.getBirthDate()!=null)
				pString+="<tr><td valign=\"top\">Birth date:</td><td>"+p.getBirthDate()+"</td></tr>";
			if(p.getBirthPlace()!=null)
				pString+="<tr><td valign=\"top\">Birth place:</td><td>"+p.getBirthPlace()+"</td></tr>";
			if(p.getReligions().size()>0){
				pString+="<tr><td valign=\"top\">Religion:</td><td>";
				for(String s : p.getReligions())
					pString+=s+"<br/>";
				pString+="</td>";
			}
			if(p.getEthnicities().size()>0){
				pString+="<tr><td valign=\"top\">Ethnicity:</td><td>";
				for(String s : p.getEthnicities())
					pString+=s+"<br/>";
				pString+="</td>";
			}
			pString+="</table>";
			
			pString+="</td><td valign=\"top\">";
			
			pString+="<table>";
			
			if(p.getNationalities().size()>0){
				pString+="<tr><td valign=\"top\">Nationalities:</td><td>";
				for(String s : p.getNationalities())
					pString+=s+"<br/>";
				pString+="</td>";
			}
			if(p.getProfessions().size()>0){
				pString+="<tr><td valign=\"top\">Professions:</td><td>";
				for(String s : p.getProfessions())
					pString+=s+"<br/>";
				pString+="</td>";
			}
			pString+="</table>";
			
			pString+="</td><td valign=\"top\">";
			
			pString+="<table>";
			
			
			if(p.getEducations().size()>0){
				pString+="<tr><td valign=\"top\">Education:</td><td>";
				for(SesameEducation e : p.getEducations()){
					pString+="<b>"+e.getInstitution()+"</b><br/>";
					if(e.getStart()!=null)
						pString+="Start date: "+e.getStart()+"<br/>";
					if(e.getEnd()!=null)
						pString+="End date: "+e.getEnd()+"<br/>";
					if(e.getDegree()!=null)
						pString+="Degree: "+e.getDegree()+"<br/>";
					if(e.getMajorFieldOfStudy()!=null)
						pString+="Major field of study: "+e.getMajorFieldOfStudy()+"<br/>";
					if(e.getSpecialization()!=null)
						pString+="Specialization: "+e.getSpecialization()+"<br/>";
				}
				pString+="</td>";
			}
			pString+="</table>";
			
			pString+="</td><td valign=\"top\">";
			
			pString+="<table>";
			if(p.getEmployements().size()>0){
				pString+="<tr><td valign=\"top\">Employments:</td><td>";
				for(SesameEmployment e : p.getEmployements()){
					pString+="<b>"+e.getEmployer()+"</b><br/>";
					if(e.getStart()!=null)
						pString+="Start date: "+e.getStart()+"<br/>";
					if(e.getEnd()!=null)
						pString+="End date: "+e.getEnd()+"<br/>";
					if(e.getTitle()!=null)
						pString+="Title: "+e.getTitle()+"<br/>";
				}
				pString+="</td>";
			}
			
			pString+="</table>";
			
			pString+="</td></tr><tr><td colspan=\"4\">";
			
			if(p.getQuotations().size()>0){
				pString+="<table><tr><td valign=\"top\">Quotation:</td><td>";
				for(String s : p.getQuotations())
					pString+=s+"<br/>";
				pString+="</td></tr></table>";
			}
			
			pString+="</td></tr></table><br/><br/><br/><br/><br/>";
			return pString;	
		}
		return "";
	}
	
	private String linkedInPerson(SesameLinkedInPerson p){
		String pString = "From LinkedIn";
		//show picture if link is not null
		if(p.getPicture()!=null)
			pString += "<p style=\"float:left\"/><img src=\""+p.getPicture()+"\"/></p>";
		//show name and headline
		pString+="<center><p><h2><a href=\""+p.getUrl()+"\">"+p.getName()+"</a></h2><h3>"+p.getHeadline()+"</p></h3></center>";
		
		pString+="<table><tr><td>";
		
		pString+="<table>";
		pString+="<tr><td valign=\"top\">Id:</td><td>"+p.getId()+"</td>";
		pString+="<tr><td valign=\"top\">Country:</td><td>"+p.getCountry()+"</td></tr>";
		if(p.getIndustry()!=null)
			pString+="<tr><td valign=\"top\">Industry:</td><td>"+p.getIndustry()+"</td></tr>";
		if(p.getSummary()!=null)
			pString+="<tr><td valign=\"top\">Summary:</td><td>"+p.getSummary()+"</td></tr>";
		if(p.getSpecialities()!=null)
			pString+="<tr><td valign=\"top\">Specialties:</td><td>"+p.getSpecialities()+"</td></tr>";
		pString+="</table>";
		
		pString+="</td><td>";
		
		pString+="<table>";
		if(p.getEmployements().size()>0){
			pString+="<tr><td valign=\"top\">Employments:</td><td>";
			for(SesameEmployment e : p.getEmployements()){
				pString+="<b>"+e.getEmployer()+"</b><br/>";
				if(e.getStart()!=null)
					pString+="Start date: "+e.getStart()+"<br/>";
				if(e.getEnd()!=null)
					pString+="End date: "+e.getEnd()+"<br/>";
				if(e.getTitle()!=null)
					pString+="Title: "+e.getTitle()+"<br/>";
			}
			pString+="</td>";
		}
		
		pString+="</table>";
		
		pString+="</td></tr></table><br/><br/><br/>";
		return pString;
	}
}
