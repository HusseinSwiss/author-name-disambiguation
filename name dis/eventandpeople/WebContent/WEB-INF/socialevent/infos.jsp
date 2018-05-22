<%@page  import="constant.Cnst"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	You must log into Facebook to use this service.<br/>
	Without that, we can't access to Facebook to search places and events.<br/>
	<a href="<% out.print(request.getAttribute(Cnst.ATTR_URL)); %>"><img src="connect_fb.png"/></a>
</body>
</html>