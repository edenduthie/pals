<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../LSMEval.css" rel="stylesheet" />
	</head>

	<body>

	<h4>Scheduler Started</h4>
	
	<a href='<s:url action="StopScheduler"/>'>Stop</a>
	
	</body>
	
	
</html>