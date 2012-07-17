<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="LSMEval.css" rel="stylesheet" />
	</head>

	<body>
	
	<s:if test="loggedIn">
	<s:include value="Top.jsp"><s:param name="loggedInAs" value="user.shortName"/></s:include>
	</s:if>
	<s:else>
	<s:include value="Top.jsp"/>
	</s:else> 

	<div class="main">
	
	<h4><s:text name="datasets.message"/></h4>
	
	<table border=1 cellpadding=6 cellspacing=3	>
	<tr>
		<td class="top"><b><s:text name="dataset.text"/></b></td>
		<td class="top"><b><s:text name="version.text"/></b></td>
		<td class="top"><b><s:text name="date.text"/></b></td>
		<td class="top"><b><s:text name="description.text"/></b></td>
		<td class="top"><b><s:text name="owner.text"/></b></td>
		<td class="top"><b><s:text name="download.text"/></b></td>
	</tr>
	
	<s:iterator value="dataSets">
		<tr>
		<td><s:property value="dataSetName"/></td>
		<td><s:property value="versionId"/></td>
		<td><s:date name="uploadDate" format="dd MMM yyyy"/></td>
		<td><s:property value="description"/></td>
		<td><s:property value="user.fullName"/></td>
		<td><a href='DownloadDataSet.action?dataSetId=<s:property value="dataSetId" />'>Download</a></td>
		
		</tr>
	</s:iterator>

	</table>
	
	</div>
   
	</body>
	
</html>
