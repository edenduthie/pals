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
	
	<h4><s:text name="modeloutputs.message"/>:</h4>
	
	<table width="600" border="1" cellpadding="6" cellspacing="3">
	<tr>
		<td class="top"><b><s:text name="id.text"/></b></td>
		<td class="top"><b><s:text name="name.text"/></b></td>
		<td class="top"><b><s:text name="model.text"/></b></td>
		<td class="top"><b><s:text name="dataset.text"/></b></td>
		<td class="top"><b><s:text name="date.text"/></b></td>
	</tr>
	
	<s:iterator value="modelOutputs">
		<tr>
		<td><s:property value="modelOutputId"/></td>
		<td><a href='ViewModelOutput.action?modelOutputId=<s:property value="modelOutputId" />'><s:property value="modelOutputName"/></a></td>
		<td><s:property value="model.modelName"/></td>
		<td><s:property value="dataSet.dataSetNameUnique"/></td>
		<td><s:date name="uploadDate" format="dd MMM yyyy"/></td>
		</tr>
	</s:iterator>
	
	</table>
	
	</div>  
   
	</body>
	
</html>
