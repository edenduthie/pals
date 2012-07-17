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
 
   	<h4><s:text name="login.message"/></h4>
   	
   	<s:form action="Login">
   		<s:textfield name="username" label="UserName"/>
    	<s:password  name="password" label="Password"/>
    	<s:submit    name="Log in"/>
   	</s:form>
   
   	</div>
   
	</body>
	
</html>
