<%@page import="beans.sesame.SesameLocation"%>
<%@page import="constant.Cnst"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	SesameLocation loc = (SesameLocation) request.getSession().getAttribute(Cnst.ATTR_SESAME_LOC);
%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result</title>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
<script type="text/javascript" src="js/jshashtable-3.0.js"></script>
<script type="text/javascript">
	var map;
	var markersArray = [];
	function addMarker(lat,lng,title,id) {
    	var marker = new google.maps.Marker({
    	    position: new google.maps.LatLng(lat,lng),
    	    map: map,
    	    title: title
		});
    	markersArray[id]=marker;
    	
   	 }
	function checkAll() {
		var checkDict = document.getElementsByName("checkDict");
		for (check in checkDict) {
			checkDict[check].checked = true;
		}
		for (marker in markersArray)
			markersArray[marker].setMap(map);
	}
	function uncheckAll() {
		var checkDict = document.getElementsByName("checkDict");
		for (check in checkDict) {
			checkDict[check].checked = false;
		}
		for (marker in markersArray)
			markersArray[marker].setMap(null);	
	}
	function modification(id,bool) {
		if(bool)
			markersArray[id].setMap(map);
		else
			markersArray[id].setMap(null);
	}
	function initialize() {
		var eventLatlng = new google.maps.LatLng(<% out.print(loc.getLatitude()+","+loc.getLongitude()); %>);
    	var mapOptions = {
      		center: eventLatlng,
			zoom: 12,
			mapTypeId: google.maps.MapTypeId.ROADMAP
    	};
        map = new google.maps.Map(document.getElementById("map-canvas"),
            mapOptions);
        markersDict=new Hashtable();
        var marker = new google.maps.Marker({
  		  position: eventLatlng,
  		  title:"Event Location",
  		  map: map
  		});
        <% out.println(request.getAttribute(Cnst.ATTR_JS_ADD_MARKERS)); %>
 	 }
     google.maps.event.addDomListener(window, 'load', initialize);
</script>
<script language="javascript">
	function ouvre_popup(page, nom) {
		window
				.open(page, nom,
						"menubar=no, status=no, scrollbars=no, menubar=no, width=300, height=400");
	}
</script>
<style type="text/css">
#map-canvas {
	height: 600px;
	width: 800px
}
</style>
</head>
<body>
	<button type="button" onclick="checkAll();">Check All</button>
	<button type="button" onclick="uncheckAll();">Uncheck All</button>
	<h3>Touristic site:</h3>
	
		<%-- Show list contents touristic site create in SesameResult.java --%>
		<% out.println(request.getAttribute(Cnst.ATTR_PLACES_TABLE)); %>
		
	<br />
	<h3>Events:</h3>
	
		<%-- Show list contents events create in Result.java --%>
		<% out.println(request.getAttribute(Cnst.ATTR_EVENTS_TABLE)); %>
		
	<center>
		<div id="map-canvas"></div>
	</center>
	<button onclick="window.location.href='sesameSearchForm.jsp'">Back</button>
</body>
</html>