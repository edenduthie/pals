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

	<h4>New model output successfully uploaded.</h4>
	
	<table>
	<tr><td><b>id</b></td>			<td><s:property value="modelOutputId"/></td></tr>
	<tr><td><b>Model</b></td>		<td><s:property value="modelId"/></td></tr>
	<tr><td><b>DataSet</b></td>		<td><s:property value="dataSetId"/></td></tr>
	<tr><td><b>Date</b></td>		<td><s:property value="date"/></td></tr>
	<tr><td><b>Username</b></td>	<td><s:property value="userName"/></td></tr>
	<tr><td><b>Filepath</b></td>	<td><s:property value="filePath"/></td></tr>
	</table>
   
   	</div>
   
	</body>
	
</html>
