<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>

	<head>
	
    <title><s:text name="modcheck.name"/></title>
    
    <s:head />
    
	<link type="text/css" href="../pals.css" rel="stylesheet" />
	
	</head>

	<body>
	
	<s:include value="../Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include>
	
	<div class="main">
	
	<s:text name="userErrorMessage" />
	
	</div>
   
	</body>
	
</html>