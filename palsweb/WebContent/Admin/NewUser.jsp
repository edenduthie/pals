<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../LSMEval.css" rel="stylesheet" />
	</head>

	<body>
	
	<h4>Create New User</h4>
	
	<table cellpadding="5" class="shade"><tr><td class="shade">
	
	<s:form action="NewUser.action" method="post" validation="true">
	<s:textfield name="username" key="User Name"/>
	<s:textfield name="password" key="Password"/>
	<s:textfield name="fullName" key="Full Name"/>
	<s:textfield name="shortName" key="Short Name"/>
	<s:textfield name="email" key="Email Address"/>
	<s:submit value="Create New User"/>
	</s:form>
   
   </td></tr></table>
	
	</body>
	
</html>