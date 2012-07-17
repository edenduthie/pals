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
	
	<div class="main">
	
	<h2>PALS Users</h2>
	
	<div id="experiment-user-list">
	
	<table class="mo">
	<tr>
	    <th class="mo">Full Name</th>
	    <th class="mo">Username</th>
	    <th class="mo">Institution</th>
	    <th class="mo">Email</th>
	</tr>
	<s:iterator value="allUsers">
	    <tr>
	        <td class="mo">
	            <a href='../Account/Profile.action?username=<s:property value="username"></s:property>'><s:property value="fullName" /></a>
	        </td>
	        <td class="mo"><s:property value="username" /></td>
	        <td class="mo"><s:property value="institution.name" /></td>
	        <td class="mo">
	            <s:if test="showEmail">
	                <a href='mailto:<s:property value="email" />'><s:property value="email" /></a>
	            </s:if>
	        </td>
	    </tr>
	</s:iterator>
	</table>
	
	</div><!--experiment-user-list -->
	</div><!--main-->
   
	</body>
	
</html>
