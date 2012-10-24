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
	
<!--
	<script language="javascript">
	document.getElementById("mbExperiments").setAttribute("class","mbON");
	</script>
-->
	
	<div class="main">
	
	<s:form action="ShareExperiment_input">
	
	<h2>Workspace Sharing Settings: <strong><s:property value="experiment.name" /></strong></h2>
	
	<div id="experiment-user-list">
	<!-- 
	<div id="share-with-all">
	    <div class="user-checkbox">
	        <s:checkbox name="shareWithAll" fieldValue="true" value="%{experiment.shareWithAll}"/>
	    </div>
	    Share With All
	</div>
	 -->
	<div id="save-button-experiment-users">
	    <s:submit value="Save" cssClass="pbut"></s:submit>
	</div>
	<div class="experiments-title">
	    PALS Users
	</div>
	
	<s:iterator value="allUsers">
	<div class="experiment-item">
	    <a href='../Account/Profile.action?username=<s:property value="username"></s:property>'><s:property value="fullName" /></a> (<s:property value="username" />)
	    <div class="user-checkbox">
	        <s:checkbox name="selected" fieldValue="%{getUsername()}" value="shared"/>
	    </div>
	</div>
	</s:iterator>
	
	</div><!--experiment-user-list -->
	
	<s:hidden name="experimentId" value="%{experiment.id}" />
	
	</s:form>
	
	</div><!--main-->
   
	</body>
	
</html>
