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

	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<script>
	document.getElementById("mbExperiments").setAttribute("class","mbON");
	</script> 

	<div class="main">

    <h2>Create a New Workspace</h2>	
	
	<table class="shade create-model"><tr><td class="shade">
	
	<s:form action="Experiment_add">
	<s:textfield name="experiment.name" label="Name"/>
	<br/>
	<s:submit cssClass="pbut" value="Create New Workspace"/>
	<br/>
	</s:form> 
	<div align="right">
	<a class='pbut-link' href='Experiment_list.action'>Cancel</a>
	</div>
	
	</td></tr></table>
	
	</div>

</body>
</html>