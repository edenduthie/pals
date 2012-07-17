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
	
	<script language="javascript">
	document.getElementById("mbExperiments").setAttribute("class","mbON");
	</script>
	
	<div class="main">
	
	<h2>Shared Workspaces</h2>
	
	<s:if test="sharedExperiments.size > 0">
	<s:set name="username" value="user.username" />
	
	<div class="experiments-title">Workspaces</div>
	
	<s:iterator value="sharedExperiments">
	
	<div class="experiment-item <s:if test="(user.currentExperiment != null) && (user.currentExperiment.id==id)">selected-experiment</s:if>">	
        <div class="experiment-name"><s:property value="name"/></div>
        <a href="Experiment_load?experimentId=<s:property value="id"/>" title="Load"><img src="../images/load.png" alt="Load" title="Load"></img></a>
        <!--<a href="ShareExperiment_list?experimentId=<s:property value="id"/>" title="Share"><img src="../images/share.png" alt="Share" title="Share"></img></a>-->
    </div>

	</s:iterator>
	</s:if>
	
	<br />
	
	
	</div>
   
	</body>
	
</html>
