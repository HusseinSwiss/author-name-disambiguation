<%@page import="beans.SocialEvent"%>
<%@page import="java.util.ArrayList"%>
<%@page import="constant.Cnst"%>
<%@page import="beans.SocialPlace;"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Add/Edit</title>
	<script type="text/javascript" src="js/edit_verify.js"></script>
</head>
<body>
<% 
	String type = request.getParameter("t");
	int i =	Integer.valueOf(request.getParameter("id"));
	if(type.equals(Cnst.TYPE_PLACE)){
		SocialPlace p=null;
		if(i!=-1) p=((ArrayList<SocialPlace>)request.getSession().getAttribute(Cnst.ATTR_PLACES_LIST)).get(i);
		%>
		<form name="editForm"  method="post" onsubmit="return validateEditForm();" action="edit.jsp">
		<input type="hidden" name="id" value="<% out.print(i);%>"/>
		<input type="hidden" name="t" value="<% out.print(type);%>"/>
			<table>
				<tr><td>Name:</td><td><input type="text" name="name" value="<%if(i!=-1)out.print(p.getName());%>"/></td></tr>
				<tr><td>Url:</td><td><input type="text" name="url" value="<%if(i!=-1)out.print(p.getUrl());%>"/></td></tr>
				<tr><td>Top level category:</td><td><select name="topLevelCat">
					<option value="<% out.print(Cnst.TYPE_TOURISTIC_SITE);%>"><% out.print(Cnst.TYPE_TOURISTIC_SITE);%></option>
					<option value="<% out.print(Cnst.TYPE_HOTEL);%>" <%if(i!=-1 && p.getTopLevelCat().equals(Cnst.TYPE_HOTEL)) out.print("selected");%>><% out.print(Cnst.TYPE_HOTEL);%></option>
					<option value="<% out.print(Cnst.TYPE_RESTAURANT);%>" <%if(i!=-1 && p.getTopLevelCat().equals(Cnst.TYPE_RESTAURANT)) out.print("selected");%>><% out.print(Cnst.TYPE_RESTAURANT);%></option>
				</select></td></tr>
				<tr><td>Type:</td><td><input type="text" name="type" value="<%if(i!=-1)out.print(p.getType());%>"/></td></tr>
				<tr><td>Street:</td><td><input type="text" name="street" value="<%if(i!=-1)out.print(p.getLocation().getStreet());%>"/></td></tr>
				<tr><td>ZIP:</td><td><input type="text" name="zip" value="<%if(i!=-1)out.print(p.getLocation().getZip());%>"/></td></tr>
				<tr><td>City:</td><td><input type="text" name="city" value="<%if(i!=-1)out.print(p.getLocation().getCity());%>"/></td></tr>
				<tr><td>Country:</td><td><input type="text" name="country" value="<%if(i!=-1)out.print(p.getLocation().getCountry());%>"/></td></tr>
				<tr><td colspan="2"><input type="submit" value="Submit"/></td></tr>
			</table>
		</form>
		<%
	}else if(type.equals(Cnst.TYPE_EVENT)){
		SocialEvent e=null;
		if(i!=-1) e=((ArrayList<SocialEvent>)request.getSession().getAttribute(Cnst.ATTR_EVENTS_LIST)).get(i);
		%>
		<form name="editForm"  method="post" onsubmit="return validateEditForm();" action="edit.jsp">
		<input type="hidden" name="id" value="<% out.print(i);%>"/>
		<input type="hidden" name="t" value="<% out.print(type);%>"/>
			<table>
				<tr><td>Name:</td><td><input type="text" name="name" value="<%if(i!=-1)out.print(e.getName());%>"/></td></tr>
				<tr><td>Url:</td><td><input type="text" name="url" value="<%if(i!=-1)out.print(e.getUrl());%>"/></td></tr>
				<tr><td>Description:</td><td><textarea name="description"><%if(i!=-1)out.print(e.getDescription());%></textarea></td></tr>
				<tr><td>Start date:</td><td><input type="text" name="start" value="<%if(i!=-1) if(e.getStart() != null)out.print(Cnst.FORMATER.format(e.getStart()));%>"/></td></tr>
				<tr><td>End date:</td><td><input type="text" name="end" value="<% if(i!=-1) if(e.getStart() != null)out.print(Cnst.FORMATER.format(e.getEnd()));%>"/></td></tr>
				<tr><td>Street:</td><td><input type="text" name="street" value="<%if(i!=-1) out.print(e.getLocation().getStreet());%>"/></td></tr>
				<tr><td>ZIP:</td><td><input type="text" name="zip" value="<%if(i!=-1) out.print(e.getLocation().getZip());%>"/></td></tr>
				<tr><td>City:</td><td><input type="text" name="city" value="<%if(i!=-1) out.print(e.getLocation().getCity());%>"/></td></tr>
				<tr><td>Country:</td><td><input type="text" name="country" value="<%if(i!=-1) out.print(e.getLocation().getCountry());%>"/></td></tr>
				<tr><td colspan="2"><input type="submit" value="Submit"/></td></tr>
			</table>
		</form>
		<%
	}
%>
</body>
</html>