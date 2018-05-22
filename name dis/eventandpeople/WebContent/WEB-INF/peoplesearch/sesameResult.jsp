<%@page import="constant.Cnst"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sesame Result - People Search</title>
</head>
<body>
	<% if(request.getAttribute(Cnst.ATTR_SESAME_CONTENT)!=null&&!request.getAttribute(Cnst.ATTR_SESAME_CONTENT).equals("")){ 
		out.println(request.getAttribute(Cnst.ATTR_SESAME_CONTENT)); 
	}%>
	<button onclick="window.location.href='sesameForm.html'">Back</button>
</body>
</html>