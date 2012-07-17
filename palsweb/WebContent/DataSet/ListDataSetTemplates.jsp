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
	
	
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include>
	
	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	document.getElementById("smbDataSets").setAttribute("class","smbON");
	</script>
	
	<div class="main">
	
	<p class="nav">/ Data Sets / My Data Sets / Data Set Templates</p>
	
	<p><a href="../DataSetTemplates/PALSFluxTowerTemplate1.0.1.xls">PALSFluxTowerTemplate1.0.1.xls</a></p>
	<p><a href="../DataSetTemplates/PALSFluxTowerTemplate1.0.2.xls">PALSFluxTowerTemplate1.0.2.xls</a></p>
	
	</div>
   
	</body>
	
</html>
