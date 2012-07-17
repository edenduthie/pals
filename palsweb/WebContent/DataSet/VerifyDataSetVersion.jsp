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
<div class="main">
	<h2>Data Upload Quality Control</h2>
	<p>
	    Please carefully check the units, ranges, and appearance of variables (approval required below):
	</p>
    <img src='<s:property value="imagePath"/>' alt="verificationPlots.png" />
    
    <div class="verify-dsv-buttons">
	    <a class="pbut but-link" href="UploadDataSetVersion_back?dataSetId=<s:property value='dataSetId'/>&dataSetVersionId=<s:property value='dataSetVersion.id'/>">
	        Upload a Different File
	    </a>
	    &nbsp;&nbsp;
	    <a class="pbut but-link" href="UploadDataSetVersion_accept?dataSetId=<s:property value='dataSetId'/>&dataSetVersionId=<s:property value='dataSetVersion.id'/>">
	         Accept This Version
	    </a>
	    &nbsp;&nbsp;
	    <a class="pbut but-link" href="UploadDataSetVersion_cancel?dataSetVersionId=<s:property value='dataSetVersion.id'/>">
	         Cancel Upload and Exit
	    </a>
    </div>
    <br><br>
</div>
</body>
</html>