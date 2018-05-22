<%@page import="beans.SocialEvent"%>
<%@page import="constant.Cnst"%>
<%@page import="beans.SocialPlace"%>
<%@page import="java.util.ArrayList;"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script language="javascript">
	var i=0;
	var arr = new Array();
	function resize(){
		var height = document.getElementById('img').height;
		var width = document.getElementById('img').width;
		window.resizeTo(width+145,height+85);
	}
	function next(){
		i++;
		if(i>arr.length-1)i=0;
		document.getElementById("img").src=arr[i];
	}
	function prev(){
		i--;
		if(i<0)i=arr.length-1;
		document.getElementById("img").src=arr[i];
	}
	function init(){
		<% out.print(request.getAttribute(Cnst.ATTR_JS_PICS)); %>
		document.getElementById("img").src=arr[i];
	}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Picture</title>
</head>
<body onLoad="init();">
	<center> 
		<a href="javascript:prev();"><img src="button_prev.png"/></a><img id="img" onclick="self.close();" onload="resize();"/><a href="javascript:next();"><img src="button_next.png"/></a>
	</center>
</body>
</html>