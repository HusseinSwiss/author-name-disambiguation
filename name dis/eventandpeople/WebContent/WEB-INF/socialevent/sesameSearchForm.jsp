<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Sesame search</title>
	<script type="text/javascript" src="js/autocomplete.js"></script>
	<script language="javascript">
		function loadCountry() {
	        var xhr = new XMLHttpRequest();
	        xhr.open('GET', './getCountries');
	        xhr.onreadystatechange = function() {
	            if (xhr.readyState == 4 && xhr.status == 200) {
	            	showCountry(xhr.responseText);
	            	loadCity();
	            }
	        };
	        xhr.send(null);
	    }
		function loadCity() {
			var list = document.getElementById('countryList');
	        var xhr = new XMLHttpRequest();
	        xhr.open('GET', './getCities?country='+list.options[list.selectedIndex].value);
	        xhr.onreadystatechange = function() {
	            if (xhr.readyState == 4 && xhr.status == 200) {
	            	showCity(xhr.responseText);
	            }
	        };
	        xhr.send(null);
	    }
		function showCountry(response){
			response = response.split('|');
            var responseLen = response.length/2;
            var list = document.getElementById('countryList');
            list.innerHTML = ''; // On vide les résultats
 
            for (var i = 0; i < responseLen ; i++) {
                option = list.appendChild(document.createElement('option'));
                option.value = response[i*2];
                option.innerHTML = response[i*2+1];
            }
		}
		function showCity(response){
			response = response.split('|');
            var list = document.getElementById('cityList');
            list.innerHTML = ''; // On vide les résultats
 
            for (var i = 0; i < response.length ; i++) {
                option = list.appendChild(document.createElement('option'));
                option.value = response[i];
                option.innerHTML = response[i];
            }
		}
		function getAutocomplete(){
			var list = document.getElementById('cityList');
			var location = document.getElementById("location");
			getResults(location.value,list.options[list.selectedIndex].value);
		}
		function load(){
			loadAutocomplete();
			loadCountry();
		}
	</script>
</head>
<body onload="load();">
	<form method="post" action="sesameResult.jsp">
		<table>
			<tr><td>Country:</td><td><select id="countryList" name="country" onchange="loadCity()"></select></td></tr>
			<tr><td>City:</td><td><select id="cityList" name="city" onchange=""></select></td></tr>
			<tr><td>Location from which calculate distance:</td><td><input type="text" id="location" name="location" autocomplete="off" onkeypress="getAutocomplete();" /><div id="autocomplete_result"></div></td></tr>
			<tr><td colspan="2"><input type="submit" value="Search places and events"/></td></tr>
		</table>
	</form>
	<button onclick="window.location.href='index.html'">Back</button>
</body>
</html>