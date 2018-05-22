package model.sesame;


import org.openrdf.model.Resource;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import constant.Cnst;

/**
 * Class to get and add information in sesame
 * @author Julien
 * @author Damien Goetschi
 */
public class RDFSStoreSocialEvent {
	/**
	 * Repository connected to sesame
	 */
	private static Repository repository;
	/**
     * Constructor
     * @param hostname hostname of sesame(tomcat)
     * @param port port of sesame(tomcat)
     * @param repositoryID 
     * @throws RepositoryException
     */
    public RDFSStoreSocialEvent(String hostname, int port, String repositoryID) throws RepositoryException {
        String sesameServer = "http://" + hostname + ":" + port + "/openrdf-sesame/";
        System.out.println("Connecting to : " + sesameServer);
        
        repository = new HTTPRepository(sesameServer, repositoryID);
        repository.initialize();
        
    }
    
    public static Repository getRepository() {
        return repository;
    }

    /**
     * Add a tuple
     * @param subject
     * @param predicate
     * @param object
     * @throws RepositoryException
     */
    public void add(Resource subject, URI predicate, Value object) throws RepositoryException 
    {
        getRepository().getConnection().add(subject, predicate, object);
    }

    /**
     * Get result for a query in sparql
     * @param sparqlSelectQuery query
     * @return TupleQueryResult
     * @throws RepositoryException
     * @throws MalformedQueryException
     * @throws QueryEvaluationException
     */
    public static TupleQueryResult execSelectQuery(String sparqlSelectQuery) throws RepositoryException, MalformedQueryException, QueryEvaluationException {
    	TupleQuery selectQuery = getRepository().getConnection().prepareTupleQuery(QueryLanguage.SPARQL, sparqlSelectQuery);
        TupleQueryResult selectQueryResult = selectQuery.evaluate();
        return selectQueryResult;
    }


    public static GraphQueryResult execGraphQuery(String sparqlGraphQuery)
            throws QueryEvaluationException, RepositoryException,
            MalformedQueryException {

        GraphQuery constructQuery = getRepository().getConnection().prepareGraphQuery(QueryLanguage.SPARQL, sparqlGraphQuery);

        GraphQueryResult constructQueryResult = constructQuery.evaluate();
        return constructQueryResult;
    }

    /**
     * Get Factory to create URI and literal
     * @return ValueFactory
     * @throws RepositoryException
     */
    public static ValueFactory getFactory() throws RepositoryException
    {
        return getRepository().getConnection().getValueFactory();
    }
    
    /**
     * Vérifie si un objet est stocké dans la base
     * @param id : id de l'objet dont on vérifie s'il est stocké dans la base
     * @return true if an object with the id "id" is already stored in the repository, else false
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     */
    public static boolean isStored(String id) throws RepositoryException, QueryEvaluationException, MalformedQueryException
    {
    	ValueFactory f = RDFSStoreSocialEvent.getFactory();
    	URI testedIdURI = f.createURI(Cnst.NAMESPACE_ONTOLOGY_SOCIAL_EVENT + id);
		// Check if
		String request ="SELECT ?subject WHERE{<"+ testedIdURI +"> ?predicate ?object}";
		TupleQueryResult reponse = RDFSStoreSocialEvent.execSelectQuery(request);
		return reponse.hasNext();
    }
    
    /**
     * Vérifie si un objet est stocké dans la base
     * @param uri : l'uri a chercher dans la base de donnée
     * @return true if an object with the id "id" is already stored in the repository, else false
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     */
    public static boolean isStored(URI uri) throws RepositoryException, QueryEvaluationException, MalformedQueryException
    {
    	// Check if
		String request ="SELECT ?subject WHERE{<"+ uri +"> ?predicate ?object}";
		TupleQueryResult reponse = RDFSStoreSocialEvent.execSelectQuery(request);
		return reponse.hasNext();
    }
    
    /**
     * Vérifie si un objet d'un certain type est stocké dans la base
     * @param uri : l'uri à chercher dans la base de donnée
     * @param type : l'uri (en string) du type à chercher
     * @return true if an object with the id "id" is already stored in the repository, else false
     * @throws RepositoryException
     * @throws QueryEvaluationException
     * @throws MalformedQueryException
     */
    public static boolean isStored(URI uri,String type) throws RepositoryException, QueryEvaluationException, MalformedQueryException
    {
    	// Check if
		String request ="SELECT ?predicate WHERE{<"+ uri +"> ?predicate <"+type+">}";
		TupleQueryResult reponse = RDFSStoreSocialEvent.execSelectQuery(request);
		return reponse.hasNext();
    }
}

/*
Julien Tscherrig â–ª PhD Student â–ª Scientific collaborator â–ª Member of MISG group
College of Engineering and Architecture â–ª Perolles 80 â–ª PO BOX 32 â–ª CH-1705 Fribourg
Member of University of Applied Sciences of Western Switzerland
https://eia-fr.ch<https://eia-fr.ch/> â–ª +41 (0)26 429 69 64
*/