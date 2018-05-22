package gate;

import gate.annotation.AnnotationImpl;
import gate.annotation.AnnotationSetImpl;
import gate.corpora.DocumentContentImpl;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.creole.annic.apache.lucene.store.InputStream;
import gate.creole.annic.apache.lucene.document.*;
import gate.gui.LogArea;
import gate.gui.TabHighlighter;
import gate.persist.PersistenceException;
import gate.swing.XJTabbedPane;
import gate.util.Err;
import gate.util.GateException;
import gate.util.GateRuntimeException;
import gate.util.InvalidOffsetException;
import gate.util.LanguageAnalyserDocumentProcessor;
import gate.util.Out;
import gate.util.ThreadWarningSystem;
import gate.util.persistence.PersistenceManager;

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.management.ThreadInfo;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JTabbedPane;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.tools.ant.taskdefs.Get.DownloadProgress;

import beans.CRFObject;

import com.aliasi.crf.SimplePosTag;
import com.aliasi.crf.SimplePosTrain;
import com.aliasi.crf.URLReader;
import com.aliasi.crf.iTextRead;

public class Profiling_prof {

	private static String download_path = "C:\\Users\\Hazimeh\\Dropbox\\name dis\\eventandpeople\\src\\Author\\";
	private static String jape_path = "C:\\Users\\Hazimeh\\Dropbox\\name dis\\coref\\";
	CorpusController annieController;

	public void initAnnie() throws GateException, IOException {
		Out.prln("Initialising ANNIE...");

		// load the ANNIE application from the saved state in plugins/ANNIE
		File pluginsHome = Gate.getPluginsHome();
		File anniePlugin = new File(pluginsHome, "ANNIE");
		File annieGapp = new File(anniePlugin, "ANNIE_with_defaults.gapp");
		annieController = (CorpusController) PersistenceManager
				.loadObjectFromFile(annieGapp);

		Out.prln("...ANNIE loaded");
	} // initAnnie()

	/** Tell ANNIE's controller about the corpus you want to run on */
	public void setCorpus(Corpus corpus) {
		annieController.setCorpus(corpus);
	} // setCorpus

	/** Run ANNIE */
	public void execute() throws GateException {
		Out.prln("Running ANNIE...");
		annieController.execute();
		Out.prln("...ANNIE complete");
	} // execute()

	/**
	 * Run from the command-line, with a list of URLs as argument.
	 * <P>
	 * <B>NOTE:</B><BR>
	 * This code will run with all the documents in memory - if you want to
	 * unload each from memory after use, add code to store the corpus in a
	 * DataStore.
	 */
	// initialise the GATE library
	public static void initialising() {
		Out.prln("Initialising GATE...");
		try {
			Gate.init();
		} catch (GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Out.prln("...GATE initialised");
	}

	// download article
	public static void download(String u, String name) {
		System.out.println("opening connection");
		URL url = null;
		try {
			url = new URL(u);
			java.io.InputStream in = url.openStream();
			FileOutputStream fos = new FileOutputStream(new File(download_path
					+ name + ".pdf"));
			System.out.println("reading file...");
			int length = -1;
			byte[] buffer = new byte[1024];// buffer for portion of data from
			// connection
			while ((length = in.read(buffer)) > -1) {
				fos.write(buffer, 0, length);
			}
			fos.close();
			in.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("file was downloaded");
	}

	// ANNIE
	public static SerialAnalyserController ANNIE() {
		// load ANNIE plugin - you must do this before you can create
		// tokeniser
		// or JAPE transducer resources.
		try {
			Gate.getCreoleRegister().registerDirectories(
					new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
		} catch (MalformedURLException | GateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Build the pipeline
		SerialAnalyserController pipeline = null;
		try {
			pipeline = (SerialAnalyserController) Factory
					.createResource("gate.creole.SerialAnalyserController");
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LanguageAnalyser tokeniser = null;
		try {
			tokeniser = (LanguageAnalyser) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser");
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LanguageAnalyser jape = null, jape1 = null, jape2 = null;
		try {
			jape = (LanguageAnalyser) Factory.createResource(
					"gate.creole.Transducer", gate.Utils.featureMap(
							"grammarURL", new File(jape_path
									+ "ace-pleonasm-print.jape").toURI()
									.toURL(), "encoding", "windows-1256"));
			jape1 = (LanguageAnalyser) Factory.createResource(
					"gate.creole.Transducer", gate.Utils.featureMap(
							"grammarURL", new File(jape_path
									+ "ace-pronouns-print.jape").toURI()
									.toURL(), "encoding", "windows-1256"));
			jape2 = (LanguageAnalyser) Factory.createResource(
					"gate.creole.Transducer", gate.Utils.featureMap(
							"grammarURL", new File(jape_path
									+ "ace-pronouns-print.jape").toURI()
									.toURL(), "encoding", "windows-1256"));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // ensure this matches the file
		pipeline.add(tokeniser);
		pipeline.add(jape);
		pipeline.add(jape1);
		pipeline.add(jape2);
		return pipeline;
	}

	public static Document Corpus(SerialAnalyserController pipeline, String path) {
		/**
		 * create document and corpus create a GATE corpus and add a document
		 * for each command-line argument
		 **/
		Corpus corpus = null;
		try {
			corpus = Factory.newCorpus("JAPE corpus");
		} catch (ResourceInstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		URL u = null;
		try {
			u = new URL(path);
			// "file:\\D:\\desktop2015\\stage%20hassan%20noureddine\\03-20-2015\\chercheur\\Jouni%20Simila\\30750418.pdf");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FeatureMap params = Factory.newFeatureMap();
		params.put("sourceUrl", u);
		params.put("preserveOriginalContent", new Boolean(true));
		params.put("collectRepositioningInfo", new Boolean(true));
		Out.prln("Creating doc for " + u);
		Document doc = null;
		doc = (Document) Factory.createResource("gate.corpora.DocumentImpl",
				params);
		corpus.add(doc);
		pipeline.setCorpus(corpus);
		try {
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.annotdelete.AnnotationDeletePR"));
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.tokeniser.DefaultTokeniser"));
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.gazetteer.DefaultGazetteer"));
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.splitter.RegexSentenceSplitter"));
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.POSTagger"));
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.ANNIETransducer"));
			pipeline.add((ProcessingResource) Factory
					.createResource("gate.creole.orthomatcher.OrthoMatcher"));

			FeatureMap coRefParams = Factory.newFeatureMap();
			coRefParams.put("resolveIt", "true");

			pipeline.add((ProcessingResource) Factory.createResource(
					"gate.creole.coref.Coreferencer", coRefParams));

		} catch (ResourceInstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		return doc;
	}

	// public static void Profil(Document doc, String name) {
	// String document = doc.getContent().toString();
	// document = document.toLowerCase();
	// document = document.replaceAll("\n", " ");
	// name = name.toLowerCase();
	// int index_author = document.indexOf(name);
	// String indice = document.substring(index_author + name.length(),
	// index_author + name.length() + 1);
	// System.out.println(indice);
	// }

	// Profiling the gate document
	public static TreeMap<String, Profil> Profil(Document doc, String author) {
		// the document in gate
		String document = doc.getContent().toString();
		int resume = document.indexOf("abstract");

		/** extraction of the names of authors **/
		int endIndex = document.toLowerCase().indexOf("abstract");
		String header = "";
		if (endIndex > -1)
			header = document.substring(0, endIndex);
		else if (document.toLowerCase().indexOf("introduction") > -1)
			header = document.substring(0,
					document.toLowerCase().indexOf("introduction"));
		else
			header = document;
		int nbPerson = 0;
		AnnotationSet person = doc.getAnnotations().get("Person");

		// ind = first character after name
		Iterator iperson = person.iterator();
		TreeMap<String, ArrayList<String>> per1 = new TreeMap<String, ArrayList<String>>();
		ArrayList<String> perso = new ArrayList<String>();
		String personne = null;
		try {
			while (iperson.hasNext()) {
				nbPerson++;
				Annotation a = (Annotation) iperson.next();
				long start = a.getStartNode().getOffset();
				if (start < endIndex) {
					long end = a.getEndNode().getOffset();
					personne = header.substring((int) start, (int) end);
					if (personne.contains(" ")) {
						if (author.toLowerCase().contains(
								personne.toLowerCase()))
							personne = author;
						int start_ind = (int) start + personne.length();
						perso.add(personne);
						String ind = header.substring(start_ind, start_ind + 1);
						if (!ind.equals(" ") && !ind.equals(".")
								&& !ind.equals("\n") && !ind.equals(",")
								&& !ind.equals("(")) {
							if (!per1.containsKey(ind)) {
								ArrayList<String> persons = new ArrayList<String>();
								persons.add(personne);
								per1.put(ind, persons);
							} else {
								ArrayList<String> persons = per1.get(ind);
								if (!persons.contains(personne))
									persons.add(personne);
								per1.put(ind, persons);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ind = second character after name
		TreeMap<String, ArrayList<String>> per2 = new TreeMap<String, ArrayList<String>>();
		personne = null;
		iperson = person.iterator();
		try {
			while (iperson.hasNext()) {
				nbPerson++;
				Annotation a = (Annotation) iperson.next();
				long start = a.getStartNode().getOffset();
				if (start < endIndex) {
					long end = a.getEndNode().getOffset();
					personne = header.substring((int) start, (int) end);
					if (personne.contains(" ")) {
						if (author.toLowerCase().contains(
								personne.toLowerCase()))
							personne = author;
						int start_ind = (int) start + personne.length();
						String ind = header.substring(start_ind, start_ind + 2);
						if (!ind.equals(" ") && !ind.equals(".")
								&& !ind.equals("\n") && !ind.equals(",")
								&& !ind.equals("(")) {
							if (!per2.containsKey(ind)) {
								ArrayList<String> persons = new ArrayList<String>();
								persons.add(personne);
								per2.put(ind, persons);
							} else {
								ArrayList<String> persons = per2.get(ind);
								if (!persons.contains(personne))
									persons.add(personne);
								per2.put(ind, persons);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ind = first character before name
		TreeMap<String, ArrayList<String>> per_1 = new TreeMap<String, ArrayList<String>>();
		personne = null;
		iperson = person.iterator();
		try {
			while (iperson.hasNext()) {
				nbPerson++;
				Annotation a = (Annotation) iperson.next();
				long start = a.getStartNode().getOffset();
				if (start < endIndex) {
					long end = a.getEndNode().getOffset();
					personne = header.substring((int) start, (int) end);
					if (personne.contains(" ")) {
						if (author.toLowerCase().contains(
								personne.toLowerCase()))
							personne = author;
						String ind = header.substring((int) start - 1,
								(int) start);
						if (!ind.equals(".") && !ind.equals("(")) {
							if (!per_1.containsKey(ind)) {
								ArrayList<String> persons = new ArrayList<String>();
								persons.add(personne);
								per_1.put(ind, persons);
							} else {
								ArrayList<String> persons = per_1.get(ind);
								if (!persons.contains(personne))
									persons.add(personne);
								per_1.put(ind, persons);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ind = number of a first character directly after name
		TreeMap<String, ArrayList<String>> per11 = new TreeMap<String, ArrayList<String>>();
		personne = null;
		iperson = person.iterator();
		try {
			while (iperson.hasNext()) {
				nbPerson++;
				Annotation a = (Annotation) iperson.next();
				long start = a.getStartNode().getOffset();
				if (start < endIndex) {
					long end = a.getEndNode().getOffset();
					personne = header.substring((int) start, (int) end);
					if (personne.contains(" ")) {
						if (author.toLowerCase().contains(
								personne.toLowerCase()))
							personne = author;
						int start_ind = (int) start + personne.length();
						int index = start_ind + 1;
						String ind = "" + index;
						if (!ind.equals(" ") && !ind.equals(".")
								&& !ind.equals("\n") && !ind.equals(",")
								&& !ind.equals("(")) {
							if (!per11.containsKey(ind)) {
								ArrayList<String> persons = new ArrayList<String>();
								persons.add(personne);
								per11.put(ind, persons);
							} else {
								ArrayList<String> persons = per11.get(ind);
								if (!persons.contains(personne))
									persons.add(personne);
								per11.put(ind, persons);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ind = number of a second character after name
		TreeMap<String, ArrayList<String>> per12 = new TreeMap<String, ArrayList<String>>();
		personne = null;
		iperson = person.iterator();
		try {
			while (iperson.hasNext()) {
				nbPerson++;
				Annotation a = (Annotation) iperson.next();
				long start = a.getStartNode().getOffset();
				long end = a.getEndNode().getOffset();
				if ((int) end <= endIndex) {
					personne = header.substring((int) start, (int) end);
					if (author.toLowerCase().contains(personne.toLowerCase()))
						personne = author;
					int start_ind = (int) start + personne.length();
					int index = start_ind + 2;
					String ind = "" + index;
					ArrayList<String> persons = new ArrayList<String>();
					persons.add(personne);
					per12.put(ind, persons);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// end extraction names authors

		/** extraction of organizations **/
		// ind = first character before organization
		int nbOrg = 0;
		AnnotationSet organization = doc.getAnnotations().get("Organization");
		ArrayList<String> headOrg = new ArrayList<String>();
		Iterator iorg = organization.iterator();
		TreeMap<String, ArrayList<String>> org1 = new TreeMap<String, ArrayList<String>>();
		String orga = null;
		while (iorg.hasNext()) {
			Annotation a = (Annotation) iorg.next();
			long start = a.getStartNode().getOffset();
			long end = a.getEndNode().getOffset();
			orga = document.substring((int) start, (int) end);
			if ((int) end <= endIndex) {
				headOrg.add(orga);
				nbOrg++;
			}
			if ((int) start - 1 >= 0) {
				String ind = document.substring((int) start - 1, (int) start);
				if (!ind.equals(" ") && !ind.equals(".") && !ind.equals(",")
						&& !ind.equals("(")) {
					if (!org1.containsKey(ind)) {
						ArrayList<String> organ = new ArrayList<String>();
						organ.add(orga);
						org1.put(ind, organ);
					} else {
						ArrayList<String> organ = org1.get(ind);
						if (!organ.contains(orga))
							organ.add(orga);
						org1.put(ind, organ);
					}
				}
			}
		}

		// ind = extract the second character before organization
		iorg = organization.iterator();
		TreeMap<String, ArrayList<String>> org2 = new TreeMap<String, ArrayList<String>>();
		orga = null;
		while (iorg.hasNext()) {
			Annotation a = (Annotation) iorg.next();
			long start = a.getStartNode().getOffset();
			long end = a.getEndNode().getOffset();
			orga = document.substring((int) start, (int) end);
			if ((int) start - 2 >= 0) {
				String ind = document.substring((int) start - 2,
						(int) start - 1);
				if (!ind.equals(" ") && !ind.equals(".") && !ind.equals(",")
						&& !ind.equals("(")) {
					if (!org2.containsKey(ind)) {
						ArrayList<String> organ = new ArrayList<String>();
						organ.add(orga);
						org2.put(ind, organ);
					} else {
						ArrayList<String> organ = org2.get(ind);
						if (!organ.contains(orga))
							organ.add(orga);
						org2.put(ind, organ);
					}
				}
			}
		}

		// ind = number of character before organization
		iorg = organization.iterator();
		TreeMap<String, ArrayList<String>> org_1 = new TreeMap<String, ArrayList<String>>();
		orga = null;
		while (iorg.hasNext()) {
			Annotation a = (Annotation) iorg.next();
			long start = a.getStartNode().getOffset();
			long end = a.getEndNode().getOffset();
			orga = document.substring((int) start, (int) end);
			int index = (int) start;
			String ind = "" + index;
			ArrayList<String> organ = new ArrayList<String>();
			organ.add(orga);
			org_1.put(ind, organ);

		}
		// end extraction organizations

		/*** person -> organization **/

		Set<String> perKey = (Set<String>) per1.keySet();
		// style 1 : index after name author; index directly before
		// organization
		Iterator perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg1 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org1.containsKey(key)) {
				ArrayList<String> pers = per1.get(key);
				ArrayList<String> orgList = org1.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg1.put(persKey, orgList);
				}
			}
		}

		// style 2 : index after name author; space between index and
		// organization
		perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg2 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org2.containsKey(key)) {
				ArrayList<String> pers = per1.get(key);
				ArrayList<String> orgList = org2.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg2.put(persKey, orgList);
				}
			}
		}

		// style 3 : index name author after 2 character of name ; index
		// directly before organization
		perKey = (Set<String>) per2.keySet();
		perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg3 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org1.containsKey(key)) {
				ArrayList<String> pers = per2.get(key);
				ArrayList<String> orgList = org1.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg3.put(persKey, orgList);
				}
			}
		}

		// style 4 : index before name author; index directly before
		// organization
		perKey = (Set<String>) per_1.keySet();
		perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg4 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org1.containsKey(key)) {
				ArrayList<String> pers = per_1.get(key);
				ArrayList<String> orgList = org1.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg4.put(persKey, orgList);
				}
			}
		}

		// style 5 : one organization for all authors
		TreeMap<String, ArrayList<String>> perOrg5 = new TreeMap<String, ArrayList<String>>();
		if (headOrg.size() == 1) {
			// ArrayList<String> organi =
			// org1.get(org1.keySet().iterator().next());
			Iterator i = perso.iterator();
			while (i.hasNext()) {
				String p = (String) i.next();
				perOrg5.put(p, headOrg);
			}
		}

		// style 6 : author \n organization
		perKey = (Set<String>) per11.keySet();
		perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg6 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org_1.containsKey(key)) {
				ArrayList<String> pers = per11.get(key);
				ArrayList<String> orgList = org_1.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg6.put(persKey, orgList);
				}
			}
		}

		// style 7 : author, organization
		perKey = (Set<String>) per12.keySet();
		perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg7 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org_1.containsKey(key)) {
				ArrayList<String> pers = per12.get(key);
				ArrayList<String> orgList = org_1.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg7.put(persKey, orgList);
				}
			}
		}

		// style 8 : author space index; index space organization
		perKey = (Set<String>) per2.keySet();
		perIterator = perKey.iterator();
		TreeMap<String, ArrayList<String>> perOrg8 = new TreeMap<String, ArrayList<String>>();
		while (perIterator.hasNext()) {
			String key = (String) perIterator.next();
			if (org2.containsKey(key)) {
				ArrayList<String> pers = per2.get(key);
				ArrayList<String> orgList = org2.get(key);
				Iterator persIterator = pers.iterator();
				while (persIterator.hasNext()) {
					String persKey = (String) persIterator.next();
					perOrg8.put(persKey, orgList);
				}
			}
		}

		/** extraction location **/
		AnnotationSet location = doc.getAnnotations().get("Location");
		// extraction of id of organizations
		int[] idOragnazation = new int[nbOrg];
		String[] NameOrganization = new String[nbOrg];
		int i = 0;
		iorg = organization.iterator();
		while (iorg.hasNext()) {
			Annotation a = (Annotation) iorg.next();
			long start = a.getStartNode().getOffset();
			long end = a.getEndNode().getOffset();
			if ((int) end <= endIndex) {
				orga = document.substring((int) start, (int) end);
				int id = a.getId();
				idOragnazation[i] = id;
				NameOrganization[i] = orga;
				i++;
			}
		}
		/** extraction organization -> location **/
		String loc = null;
		Iterator iloc = location.iterator();
		TreeMap<String, String> OrgLoc = new TreeMap<String, String>();
		while (iloc.hasNext()) {
			Annotation a = (Annotation) iloc.next();
			int id = a.getId();
			for (int j = 0; j < nbOrg; j++) {
				if (id == idOragnazation[j] + 1) {
					long start = a.getStartNode().getOffset();
					long end = a.getEndNode().getOffset();
					loc = document.substring((int) start, (int) end);
					OrgLoc.put(NameOrganization[j], loc);
				}
			}
		}

		/** extraction email **/
		AnnotationSet EMail = doc.getAnnotations().get("Address");
		String[] NamePerson = new String[nbPerson];
		String[] LastName = new String[nbPerson];
		i = 0;
		// extract name author
		iperson = person.iterator();
		while (iperson.hasNext()) {
			Annotation a = (Annotation) iperson.next();
			long start = a.getStartNode().getOffset();
			long end = a.getEndNode().getOffset();
			String name = document.substring((int) start, (int) end);
			NamePerson[i] = name;
			int part = name.lastIndexOf(" ");
			String last = null;
			if (part != -1)
				last = FrenchLetters(name.substring(part + 1));
			LastName[i] = last;
			i++;
		}
		Iterator imail = EMail.iterator();
		TreeMap<String, String> perMail = new TreeMap<String, String>();
		while (imail.hasNext()) {
			Annotation a = (Annotation) imail.next();
			long start = a.getStartNode().getOffset();
			long end = a.getEndNode().getOffset();
			String email = document.substring((int) start, (int) end);
			for (int j = 0; j < nbPerson; j++) {
				if (LastName[j] != null) {
					if (email.contains(LastName[j]))
						perMail.put(NamePerson[j], email);
				}
			}
		}
		TreeMap<String, String> EMail1 = Email(document, NamePerson, LastName);
		for (int j = 0; j < NamePerson.length; j++) {
			if (perMail.size() != 0)
				if (NamePerson[j] != null
						&& !perMail.containsKey(NamePerson[j]))
					perMail.put(NamePerson[j], EMail1.get(NamePerson[j]));
		}

		TreeMap<String, ArrayList<String>> perOrg1_2 = compare(perso, perOrg1,
				perOrg2);
		TreeMap<String, ArrayList<String>> perOrg3_4 = compare(perso, perOrg3,
				perOrg4);
		TreeMap<String, ArrayList<String>> perOrg5_6 = compare(perso, perOrg5,
				perOrg6);
		TreeMap<String, ArrayList<String>> perOrg7_8 = compare(perso, perOrg7,
				perOrg8);
		TreeMap<String, ArrayList<String>> perOrg12_78 = compare(perso,
				perOrg1_2, perOrg7_8);
		TreeMap<String, ArrayList<String>> perOrg34_56 = compare(perso,
				perOrg3_4, perOrg5_6);
		TreeMap<String, ArrayList<String>> perOrg = compare(perso, perOrg12_78,
				perOrg34_56);
		/** profiling **/
		TreeMap<String, Profil> profil = new TreeMap<String, Profil>();
		Set<String> namePerson = perOrg.keySet();
		Iterator iPerOrg = namePerson.iterator();
		while (iPerOrg.hasNext()) {
			String name = (String) iPerOrg.next();
			String organizat = null;
			String Locat = null;
			String mail = perMail.get(name);
			boolean tloc = false;
			ArrayList<String> Organ = perOrg.get(name);
			if (Organ != null) {
				Iterator iOrg = Organ.iterator();
				while (iOrg.hasNext() && !tloc) {
					organizat = (String) iOrg.next();
					Locat = OrgLoc.get(organizat);
					if (Locat != null)
						tloc = true;
				}
			}
			Profil prof = new Profil(name, mail, organizat, Locat);
			profil.put(name, prof);
		}
		return profil;
	}// profil

	public static Profil extract_author(String name,
			TreeMap<String, Profil> profil, Document doc) {
		Profil prof = new Profil();
		if (profil.containsKey(name))
			prof = profil.get(name);
		else {
			Set<String> names = profil.keySet();
			Iterator i = names.iterator();
			while (i.hasNext()) {
				String n = (String) i.next();
				String m = n.toLowerCase();
				if (m.equals(name))
					name = n;
				// System.out.println(name + "\n" + n + "\n" + m);
				if (name.contains(n) || n.contains(name) || name.contains(m)
						|| m.contains(name))
					name = n;
			}

			if (profil.containsKey(name))
				prof = profil.get(name);
			else
				prof = Profil1(doc, name);
		}
		return prof;
	}// extract author

	public static Profil Profil1(Document doc, String name) {
		Profil prof = new Profil();
		String document = doc.getContent().toString();
		document = document.toLowerCase();
		// document = document.replaceAll("\n", " ");
		name = name.toLowerCase();
		String last_name = name.substring(name.lastIndexOf(" ") + 1);
		int index_author = document.indexOf(last_name);
		// System.out.println(document);
		String indice = document.substring(index_author + last_name.length(),
				index_author + last_name.length() + 1);
		// System.out.println("indice " + indice);
		String aff = "", mail = "";
		if (!indice.equals(" ") && !indice.equals("\n")) {
			int begin_aff = document.substring(
					index_author + last_name.length() + 1).indexOf(indice)
					+ index_author + last_name.length() + 1;
			int end_aff = document.substring(begin_aff).indexOf("\n")
					+ begin_aff;
			aff = document.substring(begin_aff + 1, end_aff);

		} else {
			if (indice.equals("\n")) {
				int end_aff = document.substring(
						index_author + last_name.length() + 1).indexOf("\n")
						+ index_author + last_name.length() + 1;
				aff = document.substring(index_author + last_name.length() + 1,
						end_aff);
			} else {
				indice = document.substring(index_author + last_name.length()
						+ 1, index_author + last_name.length() + 2);
				if (!indice.equals(" ") && !indice.equals("\n")) {
					int begin_aff = document.substring(
							index_author + last_name.length() + 1).indexOf(
							indice)
							+ index_author + last_name.length() + 1;
					int end_aff = document.substring(begin_aff).indexOf("\n")
							+ begin_aff;
					aff = document.substring(begin_aff + 1, end_aff);

				} else {
					indice = document.substring(index_author - 1, index_author);
					if (!indice.equals(" ") && !indice.equals("\n")) {
						int begin_aff = document.substring(
								index_author + last_name.length() + 1).indexOf(
								indice)
								+ index_author + last_name.length() + 1;
						int end_aff = document.substring(begin_aff).indexOf(
								"\n")
								+ begin_aff;
						aff = document.substring(begin_aff + 1, end_aff);

					}
				}
			}
		}
		if (aff.contains("@")) {
			int i = aff.indexOf("@");
			int begin_mail = aff.substring(0, i).lastIndexOf(" ");
			mail = aff.substring(begin_mail + 1);
			aff = aff.substring(0, begin_mail);
			// System.out.println(mail);
		}
		// System.out.println(aff);
		int ind_mail;
		if (mail.equals("")) {
			ind_mail = document.indexOf(last_name + "@");
			if (ind_mail > -1) {
				int begin_mail = Integer.max(document.substring(0, ind_mail)
						.lastIndexOf(" "), document.substring(0, ind_mail)
						.lastIndexOf("\n"));
				int end_mail = document.substring(begin_mail + 1).indexOf(" ")
						+ begin_mail;
				mail = document.substring(begin_mail + 1, end_mail);
			}
		}
		prof.setName(name);
		prof.setEMail(mail);
		prof.setOrganization(aff);
		return prof;
	}

	// main function
	public static ArrayList<Profil> MAIN(ArrayList<String> paths, String n,
			int in) {
		// initialise the GATE library
		initialising();
		TreeMap<String, Profil> profil = null;
		ArrayList<Profil> prof_name = new ArrayList<Profil>();
		// int in = 0;
		Iterator ipaths = paths.iterator();
		while (ipaths.hasNext()) {
			// download a article
			String namepaper = n + " " + in;
			in++;
			String su = (String) ipaths.next();
			download(su, namepaper);
			// ANNIE plugin
			SerialAnalyserController pipeline = ANNIE();
			String path = "file:\\" + download_path + namepaper + ".pdf";
			// path = path.replaceAll(" ", "%20");
			String new_path = download_path + namepaper + ".pdf";
			File f = new File(new_path);
			System.out.println(f.isAbsolute() + " " + f.length());
			if (f.length() > 480000 && f.length() < 3005528) {// f.listFiles()
																// != null) {
				Document doc = Corpus(pipeline, path);

				// run it
				try {
					pipeline.execute();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				profil = Profil(doc, n);
				System.out.println(profil);
				Profil prof = extract_author(n, profil, doc);
				System.out.println("prof: " + prof);
				prof_name.add(prof);
			}
		}
		return prof_name;
	}// end MAIN

	public static void main(String args[]) throws GateException, IOException {
		String n = "Omar Abou Khaled";// "Anna Rohunen";// "Imran Zualkernan";//

		// ArrayList<String> path = new ArrayList<String>();
		// path.add("omar%20abou%20khaled%200");
		// path.add("omar%20abou%20khaled%201");
		// path.add("omar%20abou%20khaled%204");
		// path.add("omar%20abou%20khaled%2055");
		// path.add("omar%20abou%20khaled%2056");

		ArrayList<String> paths = new ArrayList<String>();
		paths.add(download_path + "omar abou khaled 0.pdf");
		paths.add(download_path + "omar abou khaled 1.pdf");
		paths.add(download_path + "omar abou khaled 4.pdf");
		paths.add(download_path + "omar abou khaled 55.pdf");
		paths.add(download_path + "omar abou khaled 56.pdf");

		// paths.add("yourFile");
		// paths.add("omar%20abou%20khaled%2057");

		// System.out.println("gate result :\n" + MAIN(path, n, 0));

	}// main

	/** compare 2 TreeMap **/
	public static TreeMap<String, ArrayList<String>> compare(
			ArrayList<String> names, TreeMap<String, ArrayList<String>> map1,
			TreeMap<String, ArrayList<String>> map2) {
		TreeMap<String, ArrayList<String>> result = new TreeMap<String, ArrayList<String>>();
		Iterator i = names.iterator();
		while (i.hasNext()) {
			String person = (String) i.next();
			if (map1.containsKey(person)) {
				if (!map2.containsKey(person))
					result.put(person, map1.get(person));
				else {
					ArrayList<String> org1 = map1.get(person);
					ArrayList<String> org2 = map2.get(person);
					if (org1 != null) {
						Iterator io = org1.iterator();
						while (io.hasNext()) {
							String org = (String) io.next();
							if (org2 != null && !org2.contains(org))
								org2.add(org);
						}
					}
					if (org2 != null)
						result.put(person, org2);
					else
						result.put(person, org1);
				}
			} else {
				result.put(person, map2.get(person));
			}
		}
		return result;
	}// end compare

	/** convert the french letters **/
	public static String FrenchLetters(String s) {
		s = s.toLowerCase();
		s = s.replace("أ¢", "a");
		s = s.replace("أ ", "a");
		s = s.replace("أ¤", "a");
		s = s.replace("أ§", "c");
		s = s.replace("أھ", "e");
		s = s.replace("أ«", "e");
		s = s.replace("أ¨", "e");
		s = s.replace("أ©", "e");
		s = s.replace("أ®", "i");
		s = s.replace("أ¯", "i");
		s = s.replace("أ´", "o");
		s = s.replace("أ¹", "u");
		s = s.replace("أ¼", "u");
		s = s.replace("أ»", "u");
		s = s.replace("أ؟", "y");
		s = s.replace("إ“", "oe");
		s = s.replace("أ¦", "ae");
		return s;
	}// FrenchLetters

	/** E-Mail **/
	public static TreeMap<String, String> Email(String doc, String[] name,
			String[] lastName) {
		TreeMap<String, String> mails = new TreeMap<String, String>();
		for (int i = 0; i < lastName.length; i++) {
			if (lastName[i] != null) {
				String lastname = FrenchLetters(lastName[i]);
				doc = doc.toLowerCase();
				if (doc.contains(lastname)) {
					int k = 0;
					boolean trouverMail = false;
					while (doc.substring(k).indexOf(lastname) != -1
							&& !trouverMail) {
						int index = doc.substring(k).indexOf(lastname) + k;
						k = index + 1;
						int lastspace = doc.substring(0, index)
								.lastIndexOf(" ");
						int lastline = doc.substring(0, index)
								.lastIndexOf("\n");
						int departmail = Integer.max(lastspace, lastline);
						departmail = Integer.max(0, departmail);
						int firstspace = doc.substring(index).indexOf(" ");
						int firstline = doc.substring(index).indexOf("\n");
						int firstcam = doc.substring(index).indexOf(",");
						int finMail;
						String mail = null;
						if (firstline != -1 && firstspace != -1) {
							finMail = Integer.min(firstspace, firstline);
							if (firstcam > -1)
								finMail = Integer.min(finMail, firstcam);
							finMail = finMail + index;
							mail = doc.substring(departmail + 1, finMail);
						} else if (firstline == -1 && firstspace == -1) {
							mail = doc.substring(departmail + 1);
						} else {
							finMail = Integer.max(firstline, firstspace);
							mail = doc.substring(departmail + 1, finMail);
						}
						if (!mail.contains("@")) {
							int at = doc.substring(index).indexOf("@");
							if (at > -1) {
								at = at + index;
								int fin;
								if (doc.substring(at).indexOf(" ") > -1
										&& doc.substring(at).indexOf("\n") > -1) {
									fin = Integer.min(doc.substring(at)
											.indexOf(" ") + at,
											doc.substring(at).indexOf("\n")
													+ at);
									mail = mail + doc.substring(at, fin);
									mails.put(name[i], mail);
									trouverMail = true;
								} else {
									if (doc.substring(at).indexOf(" ") > -1
											|| doc.substring(at).indexOf("\n") > -1) {
										fin = Integer.max(doc.substring(at)
												.indexOf(" ") + at, doc
												.substring(at).indexOf("\n")
												+ at);
										mail = mail + doc.substring(at, fin);
										mails.put(name[i], mail);
										trouverMail = true;
									} else {
										mail = mail + doc.substring(at + 1);
										mails.put(name[i], mail);
										trouverMail = true;
									}
								}
							}
						} else {
							mails.put(name[i], mail);
							trouverMail = true;
						}
					}
				}
			}
		}
		return mails;

	}// Email

	/** filter an AnnotationSet **/
	public static ArrayList<Annotation> filter(AnnotationSet a, int index) {
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		Iterator i = a.iterator();
		while (i.hasNext()) {
			Annotation an = (Annotation) i.next();
			long end = an.getEndNode().getOffset();
			if ((int) end < index) {
				annotations.add(an);
			}
		}
		return annotations;
	}// filter AnnotationSet

	/** filter an ArrayList **/
	public static ArrayList<Annotation> filter(ArrayList a, int index) {
		ArrayList<Annotation> annotations = new ArrayList<Annotation>();
		Iterator i = a.iterator();
		while (i.hasNext()) {
			Annotation an = (Annotation) i.next();
			long end = an.getEndNode().getOffset();
			if ((int) end < index) {
				annotations.add(an);
			}
		}
		return annotations;
	}// filter ArrayList
}
