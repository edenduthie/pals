<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %><html>
<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../pals.css" rel="stylesheet" />
</head>

	<body>

	<s:include value="Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<div class="main">

	<p>Test action</p>
	
	
		<s:if test="filterPublic == null">YES</s:if>
		<s:else> NO </s:else>
		
		
	<p>Turkey phase</p>
	
	<s:set name="frogg" value="frogName" />	
	
	<s:set name="newt" value="%{'yellow'}" />
	
	<s:property value="#newt" />	
	
	</div>

</body>
</html>