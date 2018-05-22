<%@page import="constant.Cnst"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Result - People Search</title>
<script type="text/javascript">
	      function checkAll() {
	    	   var arrLinkedin = document.getElementsByName("linkedin");
		   	   for (var i = 0; i < arrLinkedin.length; i++) {
		   	      arrLinkedin[i].checked = true;
		   	   }
		 	   var arrFreebase = document.getElementsByName("freebase");
		 	   for (var i = 0; i < arrFreebase.length; i++) {
		 	      arrFreebase[i].checked = true;
		 	   }
	      }
	      function uncheckAll() {
	    	  var arrLinkedin = document.getElementsByName("linkedin");
		   	   for (var i = 0; i < arrLinkedin.length; i++) {
		   	      arrLinkedin[i].checked = false;
		   	   }
		 	   var arrFreebase = document.getElementsByName("freebase");
		 	   for (var i = 0; i < arrFreebase.length; i++) {
		 	      arrFreebase[i].checked = false;
		 	   }
	   		}
    </script>
</head>
<body>
	<button type="button" onclick="checkAll();">Check All</button><button type="button" onclick="uncheckAll();">Uncheck All</button>
	<form action="addIntoSesame.jsp" method="post">
	 	<% if(request.getAttribute(Cnst.ATTR_LINKEDIN_CONTENT)!=null&&!request.getAttribute(Cnst.ATTR_LINKEDIN_CONTENT).equals("")){ %>
		<h4><u>Data from LinkedIn:</u></h4>
		<% out.println(request.getAttribute(Cnst.ATTR_LINKEDIN_CONTENT)); %>
		<br/>
		<br/>
		<%} if(request.getAttribute(Cnst.ATTR_FREEBASE_CONTENT)!=null&&!request.getAttribute(Cnst.ATTR_FREEBASE_CONTENT).equals("")){ %>
		<h4><u>Data from Freebase:</u></h4>
		<% out.println(request.getAttribute(Cnst.ATTR_FREEBASE_CONTENT)); }%>
		<br/>
		<br/>
		
		<% if(request.getAttribute(Cnst. ATTR_ARNETMINER_CONTENT)!=null&&!request.getAttribute(Cnst. ATTR_ARNETMINER_CONTENT).equals("")){ %>
		<h4><u>Data from Arnetminer:</u></h4>
		<% out.println(request.getAttribute(Cnst.ATTR_ARNETMINER_CONTENT)); }%>
		<br/>
		<br/>
		<% if(request.getAttribute(Cnst. ATTR_DBLP_CONTENT)!=null&&!request.getAttribute(Cnst. ATTR_DBLP_CONTENT).equals("")){ %>
		<h4><u>Data from DBLP:</u></h4>
		<% out.println(request.getAttribute(Cnst.ATTR_DBLP_CONTENT)); }%>
		<br/>
		<br/>
		<% if(request.getAttribute(Cnst. ATTR_MAS_CONTENT)!=null&&!request.getAttribute(Cnst. ATTR_MAS_CONTENT).equals("")){ %>
		<h4><u>Clustering Results:</u></h4>
		<% out.println(request.getAttribute(Cnst.ATTR_MAS_CONTENT)); }%>
		
		 <br/>
		<br/>
		<center><input type="submit" value="Add into Sesame"/></center>
	</form>
	<button onclick="window.location.href='searchForm.html'">Back</button>
</body>
</html>