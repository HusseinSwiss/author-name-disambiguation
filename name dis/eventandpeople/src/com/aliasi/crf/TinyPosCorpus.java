package com.aliasi.crf;

import com.aliasi.corpus.Corpus;
import com.aliasi.corpus.ObjectHandler;
import com.aliasi.tag.Tagging;

import java.util.Arrays;

public class TinyPosCorpus
    extends Corpus<ObjectHandler<Tagging<String>>> {

    public void visitTrain(ObjectHandler<Tagging<String>> handler) {
        for (String[][] wordsTags : WORDS_TAGSS) {
            String[] words = wordsTags[0];
            String[] tags = wordsTags[1];
            Tagging<String> tagging
                = new Tagging<String>(Arrays.asList(words),
                                      Arrays.asList(tags));
            handler.handle(tagging);
        }
    }

    public void visitTest(ObjectHandler<Tagging<String>> handler) {
        /* no op */
    }
	/**
		received his M.S. degree and Ph.D. degree both in computer science from the University of Technology of Compiègne. He is now a professor at the University of Applied Sciences, Fribourg. Since 1996, he has been working as a research assistant in the MEDIA group of the Theoretical Computer Computer Science Laboratory of EPFL in the field of educational technologies and web based training research field. He is an international advisor at HES-SO. He is responsible of several projects in the field of document engineering, multimodal interfaces, context awareness, ambient intelligence, blended learning, and content-based multimedia retrieval. 
	*/
    static final String[][][] WORDS_TAGSS = new String[][][] {
        { { "Ph.D", "in", "computer", "science" },                                                            		 	 { "PHDDegree", "PRP", "DOMAIN", "DOMAIN" } },
        { { "Ph.D.", "in", "computer", "science", "from", "university", "of", "beirut" },                       		 { "PHDDegree", "PRP", "DOMAIN", "DOMAIN", "PRP", "UNIV", "UNIV", "UNIV" } },
        { { "PhD.", "in", "computer", "science", "from", "university", "of", "beirut" },                        		 { "PHDDegree", "PRP", "DOMAIN", "DOMAIN", "PRP", "UNIV", "UNIV", "UNIV" } },
		{ { "M.S.", "both", "in", "computer", "science", "from", "university", "of", "beirut" },                		 { "MasterDegree", "PRP", "PRP", "DOMAIN", "DOMAIN", "PRP", "UNIV", "UNIV", "UNIV" } },
        { { "master", "candidate", "of", "automation", "department", "tsinghua", "university" }, 						 { "MasterDegree", "MasterDegree", "PRP", "UNIV", "UNIV", "UNIV", "UNIV" } },
        { { "include", "logistics" ,"simulation", ",", "simulation", "software" },									 	 { "DOMAIN", "DOMAIN", "DOMAIN", "PRP", "DOMAIN", "DOMAIN" } },
        { { "researcher", "university", "of", "oulu", "finland", "since", "then" },										 { "Position", "UNIV", "UNIV", "UNIV", "Location", "Date", "Date" } },
        { { "m.sc.", "process", "engineering", "automation", "information", "technology" },								 { "MasterDegree", "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN" } },
        { { "projects", "oulu", "southern", "institute", "process", "engineering" },									 { "UNIV", "UNIV", "UNIV", "UNIV", "DOMAIN", "DOMAIN" } },
        { { "doctoral", "dissertation", "on", "data", "privacy", "protection", "leader" },								 { "PHDDegree", "PHDDegree", "PRP", "DOMAIN", "DOMAIN", "DOMAIN", "Position" } },
        { { "phd", "student", "at", "university", "of", "applied", "sciences","of","western","switzerland","eia-fr" },	 { "PHDDegree", "PHDDegree", "PRP", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV" } },
        { { "multimedia", "information", "system", "group", "saint", "joseph", "university", "esib" },					 { "UNIV", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV", "UNIV" } },
        { { "multimedia", "production", "and", "creation", "multimedia", "information", "retrieval", "misg" },			 { "DOMAIN", "DOMAIN", "PRP", "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN", "UNIV" } },
        { { "knowledge", "management", "modeling", "and", "visualization", "university", "of", "florence" },			 { "DOMAIN", "DOMAIN", "DOMAIN", "PRP", "DOMAIN", "UNIV", "UNIV", "UNIV" } },
        { { "head", "the", "technological", "center", "professor", "," },												 { "Position", "PRP", "UNIV", "UNIV", "Position", "PRP" } },
        { { "measurement", "of", "individuals", "personal", "data", "privacy", "concerns" },							 {  "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN", "DOMAIN"} },
        { { "department", "of", "information", "processing", "science" },												 { "UNIV", "PRP", "UNIV", "UNIV", "UNIV" } }
    };
	
    
//	static final String[][][] WORDS_TAGSS = new String[][][] {
//        { { "Ph.D", "in", "computer", "science" },                                                               { "PHDDegree", "PRP", "DOMAIN", "DOMAIN" } },
//        { { "Ph.D.", "in", "computer", "science", "from", "university", "of", "beirut" },                        { "PHDDegree", "PRP", "DOMAIN", "DOMAIN", "PRP", "UNIV", "UNIV", "UNIV" } },
//        { { "PhD.", "in", "computer", "science", "from", "university", "of", "beirut" },                        { "PHDDegree", "PRP", "DOMAIN", "DOMAIN", "PRP", "UNIV", "UNIV", "UNIV" } },
//		{ { "M.S.", "both", "in", "computer", "science", "from", "university", "of", "beirut" },                 { "MasterDegree", "PRP", "PRP", "DOMAIN", "DOMAIN", "PRP", "UNIV", "UNIV", "UNIV" } }
//    };

	
	/*static final String[][][] WORDS_TAGSS = new String[][][] {
        { { "John", "ran", "." },                 { "NAME", "IV", "EOS" } },
        { { "Mary", "ran", "." },                 { "NAME", "IV", "EOS" } },
        { { "John", "jumped", "!" },              { "NAME", "IV", "EOS" } },
        { { "The", "dog", "jumped", "!" },        { "DET", "N", "IV", "EOS" } },
        { { "The", "dog", "sat", "." },           { "DET", "N", "IV", "EOS" } },
        { { "Mary", "sat", "!" },                 { "NAME", "IV", "EOS" } },
        { { "Mary", "likes", "John", "." },       { "NAME", "TV", "NAME", "EOS" } },
        { { "The", "dog", "likes", "Mary", "." }, { "DET", "N", "TV", "NAME", "EOS" } },
        { { "John", "likes", "the", "dog", "." }, { "NAME", "TV", "DET", "N", "EOS" } },
		{ { "John", "likes", "his", "dog", "." }, { "NAME", "TV", "DET", "N", "EOS" } },
        { { "The", "dog", "ran", "." },           { "DET", "N", "IV", "EOS" } },
        { { "The", "dog", "ran", "." },           { "DET", "N", "IV", "EOS" } }
    };*/
}

