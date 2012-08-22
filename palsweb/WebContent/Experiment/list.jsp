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
    <div class="experiments-list">

	<a id="create-experiment-link" class="pbut-link" href="/pals/Experiment/Experiment_form.action">
	    Create a new workspace   
	</a>	
	<h2 id="my-experiments-title">My Workspaces</h2>
	
	<s:if test="myExperiments.size > 0">
	<s:set name="username" value="user.username" />
	
	<table>
	    <tr>
	        <th class="workspace-table-name">Name</th>
	        <th class="workspace-table-actions">Actions</th>
	        <th class="workspace-table-owner">Owner</th>
	        <th>Users</th>
	        <th>Data Sets</th>
	        <th>Created</th>
	        <th>Last Activity</th>
	    </tr>
	    <s:iterator value="myExperiments">
	        <tr class="experiment-item <s:if test="(user.currentExperiment != null) && (user.currentExperiment.id==id)">selected-experiment</s:if>">
	            <td class="workspace-table-name"><!--name-->
	                <div class="experiment-name" id='experiment-name-<s:property value="id"/>'><div class="experiment-edit-button"><a href="javascript:void(0)" onclick="editName(<s:property value="id"/>,'<s:property value="name"/>')"><img alt="Edit" title="Edit" src="../images/edit.jpg"></img></a></div><s:property value="name"/></div>
	            </td>
	            <td class="workspace-table-actions"><!--actions-->
	                    <a href="Experiment_load?experimentId=<s:property value="id"/>" title="Load">Enter</a>, 
                        <a href="ShareExperiment_list?experimentId=<s:property value="id"/>" title="Share">Share</a>, 
                        <a href="javascript:void(0)" onclick="deleteExp(<s:property value="id"/>,'<s:property value="name"/>')" title="Delete">Delete</a>
	            </td>
	            <td class="workspace-table-owner">
	                <a href="<%=request.getContextPath()%>/Account/Profile.action?username=<s:property value='owner.username' />"><s:property value="owner.fullName" /></a>
	            </td>
	            <td><s:property value="sharedList.size()" /></td>
	            <td><s:property value="numDataSets" /></td>
	            <td><s:date name="creationDate" format="E dd MMMM yyyy  h:mm:ss z" /></td>
	            <td><s:date name="lastActivity" format="E dd MMMM yyyy  h:mm:ss z" /></td>
	        </tr>
	    </s:iterator>
	</table>
	
	</s:if>
	
	<br />
	
	<h2>Shared Workspaces</h2>
	
	<s:if test="sharedExperiments.size > 0">
	<s:set name="username" value="user.username" />
	
	<table>
	    <tr>
	        <th class="workspace-table-name">Name</th>
	        <th class="workspace-table-actions">Actions</th>
	        <th class="workspace-table-owner">Owner</th>
	        <th>Users</th>
	        <th>Data Sets</th>
	        <th>Created</th>
	        <th>Last Activity</th>
	    </tr>
	    <s:iterator value="sharedExperiments">
	        <tr class="experiment-item <s:if test="(user.currentExperiment != null) && (user.currentExperiment.id==id)">selected-experiment</s:if>">
	            <td class="workspace-table-name"><!--name-->
	                <div class="experiment-name" id='experiment-name-<s:property value="id"/>'><s:property value="name"/></div>
	            </td>
	            <td class="workspace-table-actions"><!--actions-->
	                    <a href="Experiment_load?experimentId=<s:property value="id"/>" title="Load">Enter</a>
	            </td>
	            <td class="workspace-table-owner">
	                <a href="<%=request.getContextPath()%>/Account/Profile.action?username=<s:property value='owner.username' />"><s:property value="owner.fullName" /></a>
	            </td>
	            <td><s:property value="sharedList.size()" /></td>
	            <td><s:property value="numDataSets" /></td>
	            <td><s:date name="creationDate" format="E dd MMMM yyyy  h:mm:ss z" /></td>
	            <td><s:date name="lastActivity" format="E dd MMMM yyyy  h:mm:ss z" /></td>
	        </tr>
	    </s:iterator>
	</table>
	
	</s:if>
	<br />
	</div>
	</div>  
<script language="JavaScript">
function editName(experimentId,experimentName)
{
	var html = '<form action="Experiment_update.action"><input type="text" name="experiment.name" value="';
	html += experimentName;
	html += '"></input>';
	html += '<input type="hidden" name="experimentId" value="';
	html += experimentId;
	html += '"></input>';
	html += '</form>';
	$('#experiment-name-'+experimentId).html(html);
};
function deleteExp(experimentId,experimentName)
{
	if(confirm('Delete workspace '+experimentName+'? All Data Sets, Models, and Model Outputs will be deleted.'))
	{
		url = "/pals/Experiment/Experiment_delete.action?experimentId="+experimentId;
		window.location = url;
	}
};
</script>
	</body>
	
</html>
