package model.sesame;



import static constant.Cnst.*;

import java.util.ArrayList;

import model.sesame.RDFSStorePeopleSearch;

import org.openrdf.model.URI;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.vocabulary.RDF;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryException;

import constant.Cnst;
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


/**
 * This class contains statics methods to get and add information from/to Sesame with RDFSStore
 * @author Damien Goetschi
 *
 */
public class GetInfoSesamePeopleSearch {
	/**
	 * RSFSStore to use for queries sesame
	 */
	private static RDFSStorePeopleSearch rdfsstore=null;
	
	/**
	 * Get RDFSStore object and create it if it is null
	 * @return RDFSStore for queries sesame
	 * @throws RepositoryException
	 */
	public static RDFSStorePeopleSearch getRDFSstore() throws RepositoryException{
		if(rdfsstore==null){
			rdfsstore=new RDFSStorePeopleSearch(REPOSITORY_HOSTNAME, REPOSITORY_PORT, REPOSITORY_ID_PEOPLE_SEARCH);
		}
		return rdfsstore;
	}
	/**
	 * Get ValueFactory use for create URI and literal for sesame
	 * @return ValueFactory
	 * @throws RepositoryException
	 */
	public static ValueFactory getF() throws RepositoryException{
		return getRDFSstore().getFactory();
	}
	
	/**
	 * Add a FreebasePerson if it dosen't be already in sesame. And return URI
	 * @param fp object FreebasePerson to add
	 * @return URI of person
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addFreebasePerson(FreebasePerson fp) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		//For country ID, remove space and special characters
		URI freebasePersonURI = getF().createURI(NAMESPACE_ONTOLOGY_PEOPLE_SEARCH+fp.getId());
		if(!RDFSStorePeopleSearch.isStored(freebasePersonURI)){
			//Put person in DB
			getRDFSstore().add(freebasePersonURI, RDF.TYPE, getF().createURI(FREEBASE_PERSON));
			getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_URL), getF().createLiteral(fp.getUrl()));
			getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_NAME_PEOPLE_SEARCH), getF().createLiteral(fp.getName()));
			
			if(fp.getBirthDate()!=null)
				getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_BIRTH_DATE), getF().createLiteral(fp.getBirthDate()));
			if(fp.getBirthPlace()!=null)
				getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_BIRTH_PLACE), getF().createLiteral(fp.getBirthPlace()));
			if(fp.getGender()!=null)
				getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_GENDER), getF().createLiteral(fp.getGender()));
			if(fp.getPicture()!=null)
				getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_PICTURE), getF().createLiteral(fp.getPicture()));
			if(fp.getNationalities().size()>0)
				for(String nationality : fp.getNationalities())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_NATIONALITY), getF().createLiteral(nationality));
			if(fp.getReligion().size()>0)
				for(String religion : fp.getReligion())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_RELIGION), getF().createLiteral(religion));
			if(fp.getProfessions().size()>0)
				for(String profession : fp.getProfessions())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_PROFESSION), getF().createLiteral(profession));
			if(fp.getEthnicity().size()>0)
				for(String ethnicity : fp.getEthnicity())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_ETHNICITY), getF().createLiteral(ethnicity));
			if(fp.getQuotations().size()>0)
				for(String quotation : fp.getQuotations())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_QUOTATION), getF().createLiteral(quotation));
			
			//Call add freebase employement method for each employement
			if(fp.getEmployments().size()>0)
				for(FreebaseEmployment fe : fp.getEmployments())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_EMPLOYMENT), addFreebaseEmployment(freebasePersonURI, fe));
			
			//Call add freebase education method for each education
			if(fp.getEducations().size()>0)
				for(FreebaseEducation fe : fp.getEducations())
					getRDFSstore().add(freebasePersonURI, getF().createURI(HAS_EDUCATION), addFreebaseEducation(freebasePersonURI, fe));
			
			
		}
		return freebasePersonURI;
	}
	
	/**
	 * Add a FreebaseEmployement if it dosen't be already in sesame. And return URI
	 * @param fpIRU: uri of people have this employement
	 * 				 fe object FreebaseEmployement to add
	 * @return URI of employement
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addFreebaseEmployment(URI fpURI, FreebaseEmployment fe) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI freebaseEmployementURI = getF().createURI(NAMESPACE_ONTOLOGY_PEOPLE_SEARCH+fe.getId());
		if(!RDFSStorePeopleSearch.isStored(freebaseEmployementURI)){
			getRDFSstore().add(freebaseEmployementURI, RDF.TYPE, getF().createURI(EMPLOYMENT));
			if(fe.getTitle()!=null)
			getRDFSstore().add(freebaseEmployementURI, getF().createURI(HAS_TITLE), getF().createLiteral(fe.getTitle()));
			if(fe.getEmployer()!=null)
				getRDFSstore().add(freebaseEmployementURI, getF().createURI(HAS_EMPLOYER), getF().createLiteral(fe.getEmployer()));
			if(fe.getFrom()!=null)
				getRDFSstore().add(freebaseEmployementURI, getF().createURI(HAS_START_PEOPLE_SEARCH), getF().createLiteral(fe.getFrom()));
			if(fe.getTo()!=null)
					getRDFSstore().add(freebaseEmployementURI, getF().createURI(HAS_END_PEOPLE_SEARCH), getF().createLiteral(fe.getTo()));
		}
		return freebaseEmployementURI;
	}
	
	/**
	 * Add a FreebaseEducation if it dosen't be already in sesame. And return URI
	 * @param fpIRU: uri of people have this education
	 * 				 fe object FreebaseEmployement to add
	 * @return URI of education
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addFreebaseEducation(URI fpURI, FreebaseEducation fe) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI freebaseEducationURI = getF().createURI(NAMESPACE_ONTOLOGY_PEOPLE_SEARCH+fe.getId());
		if(!RDFSStorePeopleSearch.isStored(freebaseEducationURI)){
			getRDFSstore().add(freebaseEducationURI, RDF.TYPE, getF().createURI(EDUCATION));
			getRDFSstore().add(freebaseEducationURI, getF().createURI(HAS_INSTITUTION), getF().createLiteral(fe.getInstitution()));
			if(fe.getDegree()!=null)
				getRDFSstore().add(freebaseEducationURI, getF().createURI(HAS_DEGREE), getF().createLiteral(fe.getDegree()));
			if(fe.getEnd_date()!=null)
				getRDFSstore().add(freebaseEducationURI, getF().createURI(HAS_END_PEOPLE_SEARCH), getF().createLiteral(fe.getEnd_date()));
			if(fe.getStart_date()!=null)
					getRDFSstore().add(freebaseEducationURI, getF().createURI(HAS_START_PEOPLE_SEARCH), getF().createLiteral(fe.getStart_date()));
			if(fe.getMajor_field_of_study()!=null)
				getRDFSstore().add(freebaseEducationURI, getF().createURI(HAS_MAJOR_FIELD_OF_STUDY), getF().createLiteral(fe.getMajor_field_of_study()));
			if(fe.getSpecialization()!=null)
				getRDFSstore().add(freebaseEducationURI, getF().createURI(HAS_SPECIALIZATION), getF().createLiteral(fe.getSpecialization()));
		}
		return freebaseEducationURI;
	}
	
	/**
	 * Add a LinkedIn Person if it dosen't be already in sesame. And return URI
	 * @param fp object LinkedInPerson to add
	 * @return URI of person
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addLinkedInPerson(LinkedInPerson lip) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		//For country ID, remove space and special characters
		URI linkedInPersonURI = getF().createURI(NAMESPACE_ONTOLOGY_PEOPLE_SEARCH+lip.getId());
		if(!RDFSStorePeopleSearch.isStored(linkedInPersonURI)){
			//Put person in DB
			getRDFSstore().add(linkedInPersonURI, RDF.TYPE, getF().createURI(LINKEDIN_PERSON));
			getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_URL), getF().createLiteral(lip.getUrl()));
			getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_NAME_PEOPLE_SEARCH), getF().createLiteral(lip.getFormattedName()));
			
			if(lip.getPicture()!=null)
				getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_PICTURE), getF().createLiteral(lip.getPicture()));
			if(lip.getCountry()!=null)
				getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_COUNTRY), getF().createLiteral(lip.getCountry()));
			if(lip.getIndustry()!=null)
				getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_INDUSTRY), getF().createLiteral(lip.getIndustry()));
			if(lip.getHeadline()!=null)
				getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_HEADLINE), getF().createLiteral(lip.getHeadline()));
			if(lip.getSpecialties()!=null){
				String[] specialitiesArray = lip.getSpecialties().split(",");
				for(String special : specialitiesArray)
					getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_SPECIALTIES), getF().createLiteral(special));
			}
			if(lip.getSummary()!=null)
				getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_SUMMARY), getF().createLiteral(lip.getSummary()));
			 
			//Call add linkedin position method for each position
			if(lip.getPositions().size()>0)
				for(LinkedInPosition lpos : lip.getPositions())
					getRDFSstore().add(linkedInPersonURI, getF().createURI(HAS_EMPLOYMENT), addLinkedInPosition(linkedInPersonURI, lpos));
		}
		return linkedInPersonURI;
	}
	
	
	/**
	 * Add a FreebaseEmployement if it dosen't be already in sesame. And return URI
	 * @param fpIRU: uri of people have this employement
	 * 				 fe object FreebaseEmployement to add
	 * @return URI of employement
	 * @throws RepositoryException
	 * @throws QueryEvaluationException
	 * @throws MalformedQueryException
	 */
	public static URI addLinkedInPosition(URI fpURI, LinkedInPosition lpos) throws RepositoryException, QueryEvaluationException, MalformedQueryException{
		URI linkedInPositionURI = getF().createURI(NAMESPACE_ONTOLOGY_PEOPLE_SEARCH+lpos.getId());
		if(!RDFSStorePeopleSearch.isStored(linkedInPositionURI)){
			getRDFSstore().add(linkedInPositionURI, RDF.TYPE, getF().createURI(EMPLOYMENT));
			getRDFSstore().add(linkedInPositionURI, getF().createURI(HAS_TITLE), getF().createLiteral(lpos.getTitle()));
			if(lpos.getCompany()!=null&&lpos.getCompany().getName()!=null)
				getRDFSstore().add(linkedInPositionURI, getF().createURI(HAS_EMPLOYER), getF().createLiteral(lpos.getCompany().getName()));
			if(lpos.getStartYear()!=-1)
				getRDFSstore().add(linkedInPositionURI, getF().createURI(HAS_START_PEOPLE_SEARCH), getF().createLiteral(lpos.getStartYear()));
			if(lpos.getEndYear()!=-1)
				getRDFSstore().add(linkedInPositionURI, getF().createURI(HAS_END_PEOPLE_SEARCH), getF().createLiteral(lpos.getEndYear()));
		}
		return linkedInPositionURI;
	}
	
	/**
	 * Search all people by a name and put result in a ArrayList on SesamePerson (what can contains SesameFreebasePerson and SesameLinkedInPerson)
	 * @param name name of person to search
	 * @return ArrayList on SesamePerson
	 */
	public static ArrayList<SesamePerson> getByName(String name){
		String request = "SELECT ?uri WHERE{?uri <"+Cnst.HAS_NAME_PEOPLE_SEARCH+"> \""+name+"\"}";
		ArrayList<SesamePerson> persons = new ArrayList<>();
		try{
			TupleQueryResult res = getRDFSstore().execSelectQuery(request);
			while(res.hasNext()){
				String uri = res.next().getBinding("uri").getValue().toString();
				URI uriP = getF().createURI(uri);
				String request2 = "SELECT ?type WHERE{<"+uri+"> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ?type}";
				TupleQueryResult res2 = getRDFSstore().execSelectQuery(request2);
				while(res2.hasNext()){
					String type = res2.next().getBinding("type").getValue().stringValue();
					if(type.equals(Cnst.FREEBASE_PERSON)){
						SesameFreebasePerson p = new SesameFreebasePerson();
						p.setId(uri);
						p.setName(getValue(uriP, HAS_NAME_PEOPLE_SEARCH));
						p.setUrl(getValue(uriP, HAS_URL));
						p.setPicture(getValue(uriP, HAS_PICTURE));
						p.setBirthDate(getValue(uriP, HAS_BIRTH_DATE));
						p.setBirthPlace(getValue(uriP, HAS_BIRTH_PLACE));
						p.setGender(getValue(uriP, HAS_GENDER));
						p.setNationalities(getValues(uriP, HAS_NATIONALITY));
						p.setReligions(getValues(uriP, HAS_RELIGION));
						p.setProfessions(getValues(uriP, HAS_PROFESSION));
						p.setEthnicities(getValues(uriP, HAS_ETHNICITY));
						p.setQuotations(getValues(uriP, HAS_QUOTATION));
						
						p.setEmployements(getEmployements(uriP));
						
						p.setEducations(getEducations(uriP));
						
						persons.add(p);
					}
					if(type.equals(Cnst.LINKEDIN_PERSON)){
						 SesameLinkedInPerson p = new SesameLinkedInPerson();
						 p.setId(uri);
						 p.setName(getValue(uriP, HAS_NAME_PEOPLE_SEARCH));
						 p.setUrl(getValue(uriP, HAS_URL));
						 p.setPicture(getValue(uriP, HAS_PICTURE));
						 p.setCountry(getValue(uriP, HAS_COUNTRY));
						 p.setIndustry(getValue(uriP, HAS_INDUSTRY));
						 p.setHeadline(getValue(uriP, HAS_HEADLINE));
						 p.setSummary(getValue(uriP, HAS_SUMMARY));
						 p.setSpecialities(getValues(uriP, HAS_SPECIALTIES));
						 
						 p.setEmployements(getEmployements(uriP));
						 
						 persons.add(p);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return persons;
	}
	
	/**
	 * Search all employment for a person
	 * @param uriP URI of people for who search employment
	 * @return a ArrayList of SesameEmployment
	 */
	private static ArrayList<SesameEmployment> getEmployements(URI uriP) {
		ArrayList<SesameEmployment> listSE = new ArrayList<>();
		try {
			ArrayList<String> uriList = getValues(uriP,HAS_EMPLOYMENT);
			for(String uri : uriList){
				URI uriEmp = getF().createURI(uri);
				SesameEmployment se = new SesameEmployment();
				se.setId(uri);
				se.setEmployer(getValue(uriEmp, HAS_EMPLOYER));
				se.setStart(getValue(uriEmp, HAS_START_PEOPLE_SEARCH));
				se.setEnd(getValue(uriEmp, HAS_END_PEOPLE_SEARCH));
				se.setTitle(getValue(uriEmp, HAS_TITLE));
				listSE.add(se);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return listSE;
	}
	
	/**
	 * Search all education for a person
	 * @param uriP URI of people for who search educations
	 * @return a ArrayList of SesameEducation
	 */
	private static ArrayList<SesameEducation> getEducations(URI uriP) {
		ArrayList<SesameEducation> listSE = new ArrayList<>();
		try {
			ArrayList<String> uriList = getValues(uriP,HAS_EDUCATION);
			for(String uri : uriList){
				URI uriEdu = getF().createURI(uri);
				SesameEducation se = new SesameEducation();
				se.setId(uri);
				se.setDegree(getValue(uriEdu, HAS_DEGREE));
				se.setStart(getValue(uriEdu, HAS_START_PEOPLE_SEARCH));
				se.setEnd(getValue(uriEdu, HAS_END_PEOPLE_SEARCH));
				se.setInstitution(getValue(uriEdu, HAS_INSTITUTION));
				se.setMajorFieldOfStudy(getValue(uriEdu, HAS_MAJOR_FIELD_OF_STUDY));
				se.setSpecialization(getValue(uriEdu, HAS_SPECIALIZATION));
				listSE.add(se);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return listSE;
	}
	
	/**
	 * Get fisrt value for a URI and a predicat
	 * @param uri URI of object in which search value
	 * @param predicat String URI of predicat to get value
	 * @return String with value
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static String getValue(URI uri, String predicat) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		String request = "SELECT ?object WHERE{<"+uri+"> <"+ predicat +"> ?object}";
		TupleQueryResult res = RDFSStorePeopleSearch.execSelectQuery(request);
		String s = null;
		if(res.hasNext())
		{
			BindingSet bs = res.next();
			s=(bs.getBinding("object").getValue().stringValue());
		}
		return s;
	}
	
	/**
	 * Get all values for a URI and a predicat
	 * @param uri URI of object in which search value
	 * @param predicat String URI of predicat to get value
	 * @return ArrayList of String with value
	 * @throws RepositoryException
	 * @throws MalformedQueryException
	 * @throws QueryEvaluationException
	 */
	public static ArrayList<String> getValues(URI uri, String predicat) throws RepositoryException, MalformedQueryException, QueryEvaluationException{
		String request = "SELECT ?object WHERE{<"+uri+"> <"+ predicat +"> ?object}";
		TupleQueryResult res = RDFSStorePeopleSearch.execSelectQuery(request);
		ArrayList<String> list = new ArrayList<String>();
		while(res.hasNext())
		{
			BindingSet bs = res.next();
			list.add((bs.getBinding("object").getValue().stringValue()));
		}
		return list;
	}
}
