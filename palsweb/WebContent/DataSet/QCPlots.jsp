<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><s:text name="modcheck.name" /></title>
<s:head />
<link type="text/css" href="../pals.css" rel="stylesheet" />
<script language="javascript" src="../js/jquery.min.js"></script>

<s:include value="../User/Top.jsp">
	<s:param name="loggedInAs" value="user.username" />
</s:include>

<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
</script>

<div class="main">

<div id="message"><s:property value="message" /></div>

<h2>Data Set <s:property value="dataSetVersion.dataSet.name" />.<s:property value="dataSetVersion.name" /> Quality Control Plots</h2>
<img src='<s:property value="imagePath"/>' alt="verificationPlots.png" />
</div>
<!-- main -->

</body>

</html>
