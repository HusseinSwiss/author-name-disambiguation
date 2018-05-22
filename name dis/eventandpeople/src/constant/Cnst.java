package constant;

import java.text.SimpleDateFormat;

public class Cnst {

	/*
	 * mysql jdbc driver
	 */
	public static final String DRIVER = "com.mysql.jdbc.Driver";

	/**
	 * mysql database url
	 */
	public static final String DB_URL = "jdbc:mysql://localhost/mas";

	/**
	 * mysql username
	 */
	public static final String USER_NAME = "root";

	/**
	 * mysql password
	 */
	public static final String PASSWORD = "";

	/*
	 * Eventful Constants
	 */
	/**
	 * Eventful Api_Key
	 * */
	public static final String EVENTFUL_KEY = "S9hthwHBkmfmp2xd";
	/**
	 * Eventful api username
	 * */
	public static final String EVENTFUL_USERNAME = "husseinhazimeh";
	/**
	 * Eventful api password
	 * */
	public static final String EVENTFUL_PASSWORD = "center";
	/**
	 * Eventful api base url
	 * */
	public static final String EVENTFUL_BASEURL = "http://api.evdb.com/rest/";
	/*
	 * Facebook Constants
	 */
	/**
	 * Facebook Application Key
	 */
	public static final String FB_KEY = "695665877119503";
	/**
	 * Facebook Application Secret
	 */
	public static final String FB_SECRET = "5747c3e87ed39400b0befc6be87c5aa5";
	/**
	 * URL to receive callback from Facebook with the code to get AccessToken
	 */
	public static final String FB_CALLBACK = "http://localhost:8080/eventandpeople/socialevent/connect.jsp";
	/*
	 * Google places Application Key
	 */
	public static final String GP_KEY = "AIzaSyDrpThD44CH7UPkVCejJKL9qzvl5b2Cklc";

	/*
	 * Foursquare Constant
	 */
	/**
	 * Foursqure Application Key
	 */
	public final static String FS_KEY = "ELI1Q1KRWGDCDSZZWDHMEE1K4P5GHZT3FRQBYTRU455EEMJ4";
	/**
	 * Foursquare Application Secret
	 */
	public static final String FS_SECRET = "KQUA2F51RCNAIB1DMZUTVNCB3ABIMAA5ZVZZW0AKGAOIVE4P";
	/**
	 * Foursquare Date Version
	 */
	public static final String FS_VERSION = "20130719";
	/**
	 * Foursquare hotel category for search venues
	 */
	public static final String CATEGORY_HOTEL = "4bf58dd8d48988d1fa931735";
	/**
	 * Foursquare food category for search venues
	 */
	public static final String CATEGORY_FOOD = "4d4b7105d754a06374d81259";
	/**
	 * Foursquare food category for search venues
	 */
	public static final String CATEGORY_TOURISTIC = "4d4b7104d754a06370d81259,4d4b7105d754a06375d81259,4d4b7105d754a06376d81259,4d4b7105d754a06377d81259,4d4b7105d754a06378d81259";
	/**
	 * range in meters to search
	 */
	public static final int FS_RANGE = 10000;

	/*
	 * Global Constants
	 */
	/**
	 * Number of result
	 */
	public static final int NB_RESULT = 12;

	/**
	 * Set that a place is from Facebook
	 */
	public static final String SITE_FACEBOOK = "Facebook";
	/**
	 * Set that an event is from Eventful
	 */
	public static final String SITE_EVENTFUL = "Eventful";
	/**
	 * Set that a place is from Google places
	 */
	public static final String SITE_GP = "Google places";
	/**
	 * Set that a place is from Foursquare
	 */
	public static final String SITE_FOURSQUARE = "Foursquare";
	/**
	 * Set that a place is set manually by a user
	 */
	public static final String SITE_CUSTOM = "Custom";

	/**
	 * Set top level category to Hotel
	 */
	public static final String TYPE_HOTEL = "Hotel";
	/**
	 * Set top level category to Restaurant
	 */
	public static final String TYPE_RESTAURANT = "Restaurant";
	/**
	 * Set top level category to Touristic Site (others)
	 */
	public static final String TYPE_TOURISTIC_SITE = "Touristic site";
	/**
	 * Set the type (for edit of show pic) is a event
	 */
	public static final String TYPE_EVENT = "event";
	/**
	 * Set the type (for edit of show pic) is a place
	 */
	public static final String TYPE_PLACE = "place";

	/*
	 * GoogleMaps Constans
	 */
	/**
	 * Google Maps API Key
	 */
	public static final String GOOGLE_KEY = "AIzaSyAZ1vEAuhQnDZFgMLgPWwSrFkZ3z9VH42g";
	/**
	 * Mode walking for distance search
	 */
	public static final String MODE_WALKING = "walking";
	/**
	 * Mode driving for distance search
	 */
	public static final String MODE_DRIVING = "driving";
	/**
	 * Mode bicycling for distance search
	 */
	public static final String MODE_BICYCLING = "bicycling";
	/**
	 * Distance: Index of array
	 */
	public static final int DISTANCE = 0;
	/**
	 * Duration: Index of array
	 */
	public static final int DURATION = 1;
	/**
	 * Latitude and Longitude error
	 */
	public static final double ERROR = -1;

	/*
	 * Attribute JSP Constants (use for pass data between servlets and jsp)
	 */
	/**
	 * Attribute for ArrayList of SocialPlace
	 */
	public static final String ATTR_PLACES_LIST = "FbPlaceList";
	/**
	 * Attribute for ArrayList of SocialDistanceDuration (for places)
	 */
	public static final String ATTR_PLACES_DISTANCES = "FbPlaceDistances";
	/**
	 * Attribute for ArrayList of SocialEvent
	 */
	public static final String ATTR_EVENTS_LIST = "FbEventList";
	/**
	 * Attribute for ArrayList of SocialDistanceDuration (for events)
	 */
	public static final String ATTR_EVENTS_DISTANCES = "FbEventDistances";
	/**
	 * Attribute for url of facebook connection
	 */
	public static final String ATTR_URL = "url";
	/**
	 * Attribute for geolocation of from where calculate the distance and
	 * duration (and center of map)
	 */
	public static final String ATTR_GEOLOC = "geoloc";
	/**
	 * SesameLocation from where calculate the distance and duration (and center
	 * of map)
	 */
	public static final String ATTR_SESAME_LOC = "sesame_loc";
	/**
	 * Attribute for GetInfoFacebook object
	 */
	public static final String ATTR_FB_OBJ = "fb_conn";
	/**
	 * Attribute for SesameCity object
	 */
	public static final String ATTR_SESAME_CITY = "sesame_city";
	/**
	 * Attribute containing the js code for adding markers to the map (with
	 * title and latitude/longitude)
	 */
	public static final String ATTR_JS_ADD_MARKERS = "js_add_markers";
	/**
	 * Attribute containing the html code of the table of places
	 */
	public static final String ATTR_PLACES_TABLE = "places_table";
	/**
	 * Attribute containing the html code of the table of events
	 */
	public static final String ATTR_EVENTS_TABLE = "events_table";
	/**
	 * Attribute containing the js code add bigPictures in a array
	 */
	public static final String ATTR_JS_PICS = "js_pics";
	/**
	 * Attribute for ArrayList of FreebasePerson
	 */
	public static final String ATTR_FREEBASE_LIST = "FreebaseList";
	/**
	 * Attribute for ArrayList of ArnetminerPerson
	 */
	public static final String ATTR_ARNETMINER_LIST = "ArnetminerList";
	/**
	 * Attribute for ArrayList of LinkedInPerson
	 */
	public static final String ATTR_LINKEDIN_LIST = "LinkedInList";
	/**
	 * Attribute for ArrayList of DBLPPerson
	 */
	public static final String ATTR_DBLP_LIST = "DBLPList";
	/**
	 * Attribute for ArrayList of Microsoft Academic Search Person
	 */
	public static final String ATTR_MAS_LIST = "MASList";
	/**
	 * Attribute for ArrayList of SesamePerson
	 */
	public static final String ATTR_SESAME_LIST = "SesameList";
	/**
	 * Attribute for Content of FreebasePerson
	 */
	public static final String ATTR_FREEBASE_CONTENT = "FreebaseContent";
	/**
	 * Attribute for Content of Arnetminer
	 */
	public static final String ATTR_ARNETMINER_CONTENT = "ArnetminerList";
	/**
	 * Attribute for Content of DBLP
	 */
	public static final String ATTR_DBLP_CONTENT = "DBLPContent";
	/**
	 * Attribute for Content of Microsoft Academic Search
	 */
	public static final String ATTR_MAS_CONTENT = "MASContent";
	/**
	 * Attribute for Content of LinkedInPerson
	 */
	public static final String ATTR_LINKEDIN_CONTENT = "LinkedInContent";
	/**
	 * Attribute for Content of Sesame Persons
	 */
	public static final String ATTR_SESAME_CONTENT = "SesameContent";

	/*
	 * Folders and files Constants
	 */
	/**
	 * Attribute for cache of locations
	 */
	public static final String CACHE_LOCATIONS = "locations.cache";
	/**
	 * Attribute for cache of geolocations
	 */
	public static final String CACHE_GEOLOCATIONS = "geolocations.cache";
	/**
	 * Attribute for cache of distance
	 */
	public static final String CACHE_DISTANCES = "distances.cache";
	/**
	 * Attribute for cache of autocomplete
	 */
	public static final String CACHE_AUTOCOMPLETE = "autocomplete.cache";
	/**
	 * The PATH to the folder in witch save the caches (WEB-INF/res on the
	 * server)
	 */
	public static String PATH;

	/*
	 * Object Constants
	 */
	/**
	 * DateFormat for convert String to Date and Date to String
	 */
	public static final SimpleDateFormat FORMATER = new SimpleDateFormat(
			"MM-dd-yyyy HH:mm");

	/*
	 * Ontology Social Event
	 */
	/**
	 * NameSpace
	 */
	public static final String NAMESPACE_ONTOLOGY_SOCIAL_EVENT = "http://www.semanticweb.org/eiafr/ontologies/2013/7/socialevent/";
	/**
	 * Entities
	 */
	public static final String COUNTRY = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#Country";
	public static final String CITY = NAMESPACE_ONTOLOGY_SOCIAL_EVENT + "#City";
	public static final String LOCATION = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#Location";
	public static final String EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#Event";
	public static final String FACEBOOK_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#FacebookEvent";
	public static final String EVENTFUL_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#EventfulEvent";
	public static final String PLACE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#Place";
	public static final String FACEBOOK_PLACE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#FacebookPlace";
	public static final String FOURSQUARE_PLACE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#FoursquarePlace";
	public static final String GOOGLEPLACE_PLACE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#GooglePlace";
	public static final String DISTANCE_DURATION = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#DistanceDuration";
	/**
	 * Object properties
	 */
	public static final String CONTAINS_DATA_DURATION_FROM = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#containsDataDurationFrom";
	public static final String CONTAINS_DATA_DURATION_TO = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#containsDataDurationTo";
	public static final String CONTAINS_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#containsEvent";
	public static final String CONTAINS_PLACE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#containsPlace";
	public static final String IS_CITY_FROM = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#isCityFrom";
	public static final String CONTAINS_LOCATION = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#containsLocation";
	/**
	 * Data properties
	 */
	public static final String HAS_ATTENDING_COUNT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasAttendingCount";
	public static final String HAS_BIG_PIC = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasBigPic";
	public static final String HAS_DESCRIPTION = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasDescription";
	public static final String HAS_DRIVE_DISTANCE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasDriveDistance";
	public static final String HAS_DRIVE_DURATION = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasDriveDuration";
	public static final String HAS_END_SOCIAL_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasEnd";
	public static final String HAS_ID_SOCIAL_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasId";
	public static final String HAS_INVITES_COUNT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasInvitesCount";
	public static final String HAS_LATITUDE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasLatitude";
	public static final String HAS_LIKE_COUNT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasLikeCount";
	public static final String HAS_LINK = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasLink";
	public static final String HAS_LONGITUDE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasLongitude";
	public static final String HAS_NAME_SOCIAL_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasName";
	public static final String HAS_SMALL_PIC = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasSmallPic";
	public static final String HAS_START_SOCIAL_EVENT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasStart";
	public static final String HAS_STREET = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasStreet";
	public static final String HAS_TOP_LEVEL_CAT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasTopLevelCat";
	public static final String HAS_TYPE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasType";
	public static final String HAS_WALK_DISTANCE = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasWalkDistance";
	public static final String HAS_WALK_DURATION = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasWalkDuration";
	public static final String HAS_WERE_HERE_COUNT = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasWereHereCount";
	public static final String HAS_ZIP = NAMESPACE_ONTOLOGY_SOCIAL_EVENT
			+ "#hasZip";

	/*
	 * Ontology/Repository
	 */
	/**
	 * NameSpace
	 */
	public static final String NAMESPACE_ONTOLOGY_PEOPLE_SEARCH = "http://www.semanticweb.org/eiafr/ontologies/2013/9/peoplesearch/";
	/**
	 * Entities
	 */
	public static final String EDUCATION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#Education";
	public static final String EMPLOYMENT = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#Employment";
	public static final String PERSON = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#Person";
	public static final String LINKEDIN_PERSON = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#LinkedInPerson";
	public static final String FREEBASE_PERSON = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#FreebasePerson";
	public static final String ARNETMINER_PERSON = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#ArnetminerPerson";

	/**
	 * Object properties
	 */
	public static final String HAS_EDUCATION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasEducation";
	public static final String HAS_EMPLOYMENT = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasEmployment";
	/**
	 * Data properties
	 */
	public static final String HAS_BIRTH_DATE = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasBirthDate";
	public static final String HAS_BIRTH_PLACE = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasBirthPlace";
	public static final String HAS_COUNTRY = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasCountry";
	public static final String HAS_DEGREE = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasDegree";
	public static final String HAS_EMPLOYER = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasEmployer";
	public static final String HAS_END_PEOPLE_SEARCH = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasEnd";
	public static final String HAS_ETHNICITY = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasEthnicity";
	public static final String HAS_GENDER = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasGender";
	public static final String HAS_HEADLINE = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasHeadline";
	public static final String HAS_ID_PEOPLE_SEARCH = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasId";
	public static final String HAS_INDUSTRY = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasIndustry";
	public static final String HAS_INSTITUTION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasInstitution";
	public static final String HAS_MAJOR_FIELD_OF_STUDY = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasMajorFieldOfStudy";
	public static final String HAS_NAME_PEOPLE_SEARCH = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasName";
	public static final String HAS_NATIONALITY = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasNationality";
	public static final String HAS_PICTURE = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasPicture";
	public static final String HAS_PROFESSION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasProfession";
	public static final String HAS_QUOTATION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasQuotation";
	public static final String HAS_RELIGION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasReligion";
	public static final String HAS_SPECIALIZATION = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasSpecialization";
	public static final String HAS_SPECIALTIES = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasSpecialties";
	public static final String HAS_START_PEOPLE_SEARCH = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasStart";
	public static final String HAS_SUMMARY = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasSummary";
	public static final String HAS_TITLE = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasTitle";
	public static final String HAS_URL = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasUrl";
	public static final String HAS_HINDEX = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasHindex";
	public static final String HAS_EMAIL = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasEmail";
	public static final String HAS_PHONE_NUMBER = NAMESPACE_ONTOLOGY_PEOPLE_SEARCH
			+ "#hasPhoneNumber";

	/**
	 * Repository
	 */
	public static final String REPOSITORY_HOSTNAME = "localhost";
	public static final int REPOSITORY_PORT = 8080;
	public static final String REPOSITORY_ID_SOCIAL_EVENT = "socialevent";
	public static final String REPOSITORY_ID_PEOPLE_SEARCH = "peoplepearch";

}
