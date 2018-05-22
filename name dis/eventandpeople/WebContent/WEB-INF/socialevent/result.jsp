<%@page import="beans.SocialLocation"%>
<%@page import="constant.Cnst"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SocialEvent</title>
	<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript">
    	var map;
	    var markersPlaces = [];
	    var markersEvents = [];
    	function addMarker(array,lat,lng,title,i) {
	    	var marker = new google.maps.Marker({
	    	    position: new google.maps.LatLng(lat,lng),
	    	    map: map,
	    	    title: title
			});
	    	array[i] = marker;
	   	 }
    	function modification(){
    		var arrPlaces = document.getElementsByName("places");
		   	for (var i = 0; i < arrPlaces.length; i++) {
		   		if(arrPlaces[i].checked){
		   			markersPlaces[i].setMap(map);
		   		}else{
		   			markersPlaces[i].setMap(null);
		   		}
			}
		   	var arrEvents = document.getElementsByName("events");
		   	for (var i = 0; i < arrEvents.length; i++) {
		   		if(arrEvents[i].checked){
		   			markersEvents[i].setMap(map);
		   		}else{
		   			markersEvents[i].setMap(null);
		   		}
			}
    	}
    	function initialize() {
    		var eventLatlng = new google.maps.LatLng(<%SocialLocation loc = (SocialLocation) request.getSession().getAttribute(Cnst.ATTR_GEOLOC); out.print(loc.getLatitude()+","+loc.getLongitude()); %>);
        	var mapOptions = {
          		center: eventLatlng,
				zoom: 12,
				mapTypeId: google.maps.MapTypeId.ROADMAP
        	};
	        map = new google.maps.Map(document.getElementById("map-canvas"),
	            mapOptions);
	        var marker = new google.maps.Marker({
	  		  position: eventLatlng,
	  		  title:"Event Location",
	  		  map: map
	  		});
	        <% out.println(request.getAttribute(Cnst.ATTR_JS_ADD_MARKERS)); %>
	        
     	 }
	     google.maps.event.addDomListener(window, 'load', initialize);
     </script>
     <script type="text/javascript">
	      function checkAll() {
	    	   var arrPlaces = document.getElementsByName("places");
		   	   for (var i = 0; i < arrPlaces.length; i++) {
		   	      arrPlaces[i].checked = true;
		   	   }
		 	   var arrEvents = document.getElementsByName("events");
		 	   for (var i = 0; i < arrEvents.length; i++) {
		 	      arrEvents[i].checked = true;
		 	   }
		 	  modification();
	      }
	      function uncheckAll() {
	   	   var arrplaces = document.getElementsByName("places");
			   var arrFsPlaces = document.getElementsByName("places");
		 	   for (var i = 0; i < arrFsPlaces.length; i++) {
		 	      arrFsPlaces[i].checked = false;
		 	   }
		 	   var arrEvents = document.getElementsByName("events");
		 	   for (var i = 0; i < arrEvents.length; i++) {
		 	      arrEvents[i].checked = false;
		 	   }
		 	   modification();
	   		}
    </script>
    <script language="javascript">
	   function ouvre_popup(page,nom) {
	       window.open(page,nom,"menubar=no, status=no, scrollbars=no, menubar=no, width=300, height=400");
	   }
	</script>
    <style type="text/css">
      #map-canvas { height: 600px; width: 800px }
    </style>
</head>
<body>

	<button type="button" onclick="checkAll();">Check All</button><button type="button" onclick="uncheckAll();">Uncheck All</button>
	<form method="post" action="addIntoSesame.jsp">
		<h3>Touristic site:</h3>
		
			<%-- Show list contents touristic site create in Result.java --%>
			<% out.println(request.getAttribute(Cnst.ATTR_PLACES_TABLE)); %>
		
		<button type="button" onclick="javascript:ouvre_popup('edit.jsp?t=<% out.print(Cnst.TYPE_PLACE); %>&id=-1','add_edit');">Add</button>
		<br/>
		<h3>Events:</h3>
		
			<%-- Show list contents events create in Result.java --%>
			<% out.println(request.getAttribute(Cnst.ATTR_EVENTS_TABLE)); %>
		
		<button type="button" onclick="javascript:ouvre_popup('edit.jsp?t=<% out.print(Cnst.TYPE_EVENT); %>&id=-1','add_edit');">Add</button><br/>
		<center><input type="submit" value="Add into Sesame"/></center>
	</form>
	<center><div id="map-canvas"></div></center>
	<button onclick="window.location.href='searchForm.jsp'">Back</button>
</body></html>