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
	
	<h4>Login was a success.</h4>      
	  
   </div>
   
	</body>
	
</html>
