<%@page import="constant.Cnst;"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>SocialEvent</title>
<script type="text/javascript" src="js/autocomplete.js"></script>
<script type="text/javascript">
function getAutocomplete(){
	var list = document.getElementById('city');
	var location = document.getElementById("location");
	getResults(location.value,list.value);
}
</script>
</head>

<body onload="loadAutocomplete()">
	<form method="post" action="result.jsp">
		<table>
			<tr>
				<td>City:</td><td><input  type="text" id="city" name="city" /></td>
			</tr>
			<tr>
				<td>Location from which calculate distance:</td><td>
					<input type="text" id="location" name="location" autocomplete="off" onkeypress="getAutocomplete();" />
					<div id="autocomplete_result"></div>
				</td>
			</tr>
			<tr>
				<td colspan="2"><center><input type="submit" value="Search places and events"/></center></td>
			<tr>
		</table>
	</form>
	<button onclick="window.location.href='index.html'">Back</button>
</body>
</html>