<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../pals.css" rel="stylesheet" />
	<script language="javascript" src="../js/jquery.min.js"></script>
	</head>
<body>	
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 
	
	<script>
	document.getElementById("mbModels").setAttribute("class","mbON");
	</script> 

    <s:set name="url" value="model.url" scope="request"/>

	<div class="main">
	
	<div id="message"><s:property value="message"/></div>
	
	<s:if test="model.ownerUserName == user.username">
	<div id="data-set-edit-button">
	    <a class="pbut-link" href='ModelAction_edit?modelId=<s:property value="model.id" />'>
	        Edit
	    </a>
	</div>
	</s:if>
	
	<h2>Model: <s:property value="model.modelName"/></h2>

	<hr size=1 />
	
	
	<table>
	
	<tr><td><b>Name:</b></td><td> <s:property value="model.modelName"/>
	</td></tr>
	<tr><td><b>Version:</b></td><td> <s:property value="model.version" /> 
	</td></tr>
	<tr><td><b>Owner:</b></td><td> <s:property value="model.user.fullName" /> 
	</td></tr>
	<tr><td><b>Created On:</b></td><td> <s:date name="model.createdDate" /> 
	</td></tr>
	<tr><td><b>URL:</b></td><td> <a  href="<s:property value="model.urlM" />">
	    <s:property value="model.urlM" escape="false" />
	</a>
	</td></tr>
	</table>
	

	
	<br />
	
	<b>References:</b> 
	<p><s:property value="references" escape="false"/></p>
	
	<b>Comments:</b> 
	<p><s:property value="comments" escape="false"/></p>
	
	<table>
    <tr><td colspan="2">
	<hr size=1 />
	</td></tr>
	
	<s:if test="modelOwner">
	<tr><td  colspan="2">
	<b>Ancillary Files:</b><br>
	<s:iterator value="model.files">
	<div><a class="ancillary-file-dl" href='<%=request.getContextPath()%>/DownloadFile?id=<s:property value="id" />'><s:property value="name" /></a></div>
	</s:iterator>
	<br>
	</td></tr>
	</s:if>
	
	
	</table>
    <s:include value="../Footer.jsp"></s:include>
	</body>
	
</html>
