<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>

	<head>
	
    <title><s:text name="modcheck.name"/></title>
    
    <s:head />
    
	<link type="text/css" href="../pals.css" rel="stylesheet" />
	
	</head>

	<body>
	
	<s:set name="fullAccessLevel" value="accessLevel"/>
	
	<s:include value="../Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include>

	<s:if test="accessLevel == 'PUBLIC'">
		<script language="Javascript1.2">
		document.getElementById("mbModelOutputs").setAttribute("class","mbON");
		document.getElementById("smbPublicModelOutputs").setAttribute("class","smbON");
		</script>
	</s:if>
	<s:else>
		<script language="Javascript1.2">
		document.getElementById("mbModelOutputs").setAttribute("class","mbON");
		document.getElementById("smbModelOutputs").setAttribute("class","smbON");
		</script>
	</s:else>

	<script type="text/javascript" src="../pals.js"></script>

	<script language="Javascript1.2">
	function submitEditAction(editAction) {
		if (checkSomethingIsSelected(document.getElementById("editModelOutput"))) {
			document.getElementById("editTaskInput").value = editAction;
			if (editAction == "edit") document.getElementById("editModelOutput").action = "EditModelOutputForm!input.action";
			$('#username').remove();
			$('#modelId').remove();
			$('#dsvId').remove();
			$('#access').remove();
			document.getElementById("editModelOutput").submit();
		} else {
			alert("Please select a Model Output.");
		}
	}
	</script>

	<div class="main">
	
	<s:if test="accessLevel == 'PRIVATE'">
	<h2>My Model Outputs</h2>
	
	<div id="num-items-select">
	    <s:form action="ListModelOutputs">
	       <s:select name="limit" label="Results Per Page" list="itemsPerPageOptions" />
	       <s:hidden name="dsvId" />
	    </s:form>
	</div>
	
	<form>
	<table><tr>
	
	<td><a class="pbut-link" href="javascript:void(0)" onclick="submitEditAction('public')">Share with all PALS</a></td>
	<td><a class="pbut-link" href="javascript:void(0)" onclick="submitEditAction('sharedataprovider')">Share with data provider</a></td>
	<td><a class="pbut-link" href="javascript:void(0)" onclick="submitEditAction('private')">Make Private</a></td>
	<td>&nbsp;</td><td>&nbsp;</td>
	<td><a class="pbut-link" href="javascript:void(0)" onclick="submitEditAction('edit')" >Edit</a></td>
	<td><a class="pbut-link" href="javascript:void(0)" onclick="submitEditAction('delete')" >Delete</a></td>
	<td>&nbsp;</td><td>&nbsp;</td>
	<td><a class="pbut-link" href="<s:url action="UploadModelOutput!input"/>">Upload new model output</a></td>
	
	</tr></table>
	
   </form>
	<br/>
	</s:if>
	<s:elseif test="accessLevel == 'DATA_SET_OWNER'">
		<div id="num-items-select">
	        <s:form action="ListDataSetOwnerModelOutputs">
	           <s:select name="limit" label="Results Per Page" list="itemsPerPageOptions" />
	        </s:form>
	    </div>
	    <h2 class="list-heading">Model Outputs using My Data Sets</h2>
	</s:elseif>
	<s:elseif test="accessLevel == 'PUBLIC'">
	    <div id="num-items-select">
	        <s:form action="ListPublicModelOutputs">
	           <s:select name="limit" label="Results Per Page" list="itemsPerPageOptions" />
	        </s:form>
	    </div>
	    <h2 class="list-heading">Public Model Outputs</h2>
	</s:elseif>
	
	<form id="editModelOutput" action="EditModelOutput.action">
	
	<input id="editTaskInput" type="hidden" name="editTask" />
	
	<table border="1" cellpadding="6" cellspacing="3" class="mo">
	
	<!-- 
	<tr>
	
		<th class="mo"><input type="checkbox" onChange='selectAll(document.getElementById("editModelOutput"),this.checked)'></th>
		
		<th class="mo"><b>Model Output Name</b></th>
		
		<th class="mo">&nbsp;</th>
		
		<s:if test="accessLevel != 'PRIVATE'">
            <th class="mo"><b>Owner</b></th>
		</s:if>
		
		<th class="mo"><b>Model</b></th>
		
		<th class="mo"><b>Data Set</b></th>
		
		<th class="mo"><b>Uploaded</b></th>
		
		<th class="mo"><b>Status</b></th>
		
		<th class="mo"><b>Access</b></th>
		
	</tr>
	 -->
	
		<tr>
		<th class="mo"><input type="checkbox" onChange='selectAll(document.getElementById("editModelOutput"),this.checked)'></th>
		
		<th class="mo">
		    <b>Name</b>
		    <a id="modelOutputNameASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('modelOutputName','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="modelOutputNameSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		    <a id="modelOutputNameDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('modelOutputName','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="modelOutputNameSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>
		</th>
		
		<th class="mo">&nbsp;</th>
		
		<s:if test="accessLevel != 'PRIVATE'">
            <th class="mo">
                <s:select name="username" headerKey="null" headerValue="-- Owner --" listKey="username" listValue="fullName" list="users"></s:select>
                <a id="ownerASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('owner','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="ownerSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		        <a id="ownerDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('owner','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="ownerSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>
            </th>
		</s:if>
		
		<th class="mo">
		    <s:select name="modelId" headerKey="null" headerValue="-- Model --" listKey="id" listValue="fullName" list="models"></s:select>
            <a id="modelASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('model','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="modelSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		    <a id="modelDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('model','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="modelSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>		
		</th>
		
		<th class="mo">
		    <s:select name="dsvId" headerKey="null" headerValue="-- DataSet --" listKey="id" listValue="displayName" list="dsvs"></s:select>
		    <a id="dataSetASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('dataSet','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="dataSetSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		    <a id="dataSetDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('dataSet','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="dataSetSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>	
		</th>
		
		<th class="mo">
		    <b>Uploaded</b>
		    <a id="uploadedASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('uploaded','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="uploadedSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		    <a id="uploadedDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('uploaded','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="uploadedSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>
		</th>
		
		<th class="mo">
		    <b>Status</b>
		    <a id="statusASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('status','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="statusSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		    <a id="statusDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('status','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="statusSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>
		</th>
		
		<th class="mo">
		    <s:select name="access" headerKey="null" headerValue="-- Access --" list="accessVersions"></s:select>
		    <a id="accessASC" class="sort-arrow" href="javascript:void(0)" onclick="sort('access','ASC');return false;"><img src="<%=request.getContextPath()%>/images/ASC_<s:if test="accessSort == 'ASC'">active</s:if><s:else>inactive</s:else>.png" alt="sort asc" title ="sort asc"></img></a>
		    <a id="accessDESC" class="sort-arrow" href="javascript:void(0)" onclick="sort('access','DESC');return false;"><img src="<%=request.getContextPath()%>/images/DESC_<s:if test="accessSort == 'DESC'">active</s:if><s:else>inactive</s:else>.png" alt="sort desc" title ="sort desc"></img></a>
		 </th>
	</tr>
	
	<s:iterator value="modelOutputs">
	
		<tr>
		
		<td class="mo"><input type="checkbox" name="modelOutputId" value="<s:property value="id" />"/></td>
		
		<td class="mo"><a class="moLarge" href='ViewModelOutput.action?modelOutputId=<s:property value="id" />'><s:property value="name"/></a></td>
		
		<td class="mo"><a class="moItalic" href='ListModelOutputPlots.action?filterModelOutputId=<s:property value="id" />&filterModelId=<s:property value="model.id" />'>View Plots</a></td>
		
		<s:if test="#fullAccessLevel != 'PRIVATE'">
		    <td class="mo"><a href='../Account/Profile.action?username=<s:property value="owner.username"/>'><s:property value="owner.fullName"></s:property></a></td>
		</s:if>
		
		<td class="mo"><s:property value="model.modelName"/>.<s:property value="model.version"/></td>
		
		<td class="mo"><s:property value="dataSetVersion.dataSet.name"/>.<s:property value="dataSetVersion.name"/></td>
		
		<td class="mo"><s:date name="uploadDate" format="dd MMM yyyy h:mm"/></td>
		
		<td class="mo">
		<s:if test="status == 'e'">
			<a class='status<s:property value="status"/>' href='ViewModelOutput.action?modelOutputId=<s:property value="id" />'><i><s:property value="getText('status.'+status)"/></i></a>
		</s:if>
		<s:else>
			<i><font class='status<s:property value="status"/>'><s:property value="getText('status.'+status)"/></font></i>
		</s:else>
		</td>
		
		<td class="mo">
		<s:if test="accessLevel == 'PUBLIC'">
		<font class="public">Public</font>
		</s:if>
		<s:elseif test="accessLevel == 'DATA_SET_OWNER'">
		<font class="private">Provider</font>
		</s:elseif>
		<s:else>
		<font class="private">Private</font>
		</s:else>
		</td>
		
		</tr>
		
	</s:iterator>
	
	</table>
	
	<s:if test="hasNextPage">
	    <a alt="next page" title="next page" class="right-link link-content" href='javascript:void(0)' onclick='filter("<%=request.getContextPath()%>/User/<s:property value="actionName" />?offset=<s:property value="nextOffset" />"); return false;'><span id="next" class="top-pad">Next <s:property value="limit"/> Model Outputs</span> <div id="right-arrow" class="right-arrow" alt="next page" title="next page"></div></a>
	</s:if>
	<s:if test="offset > 0">
	    <a class="left-link link-content" href='javascript:void(0)' onclick='filter("<%=request.getContextPath()%>/User/<s:property value="actionName" />?offset=<s:property value="previousOffset" />")'><div id="left-arrow" class="left-arrow" alt="previous page" title="previous page"></div> <span id="previous" class="top-pad">Previous <s:property value="limit"/> Model Outputs</span></a>
	</s:if>
	
	</form>
	  
   	</div>
<script>

var modelId;
var username;
var dsvId;
var access;

var columnDirections = new Array();
columnDirections['modelOutputName'] = '<s:property value="modelOutputNameSort"/>';
columnDirections['owner'] = '<s:property value="ownerSort"/>';
columnDirections['model'] = '<s:property value="modelSort"/>';
columnDirections['dataSet'] = '<s:property value="dataSetSort"/>';
columnDirections['uploaded'] = '<s:property value="uploadedSort"/>';
columnDirections['status'] = '<s:property value="statusSort"/>';
columnDirections['access'] = '<s:property value="accessSort"/>';

window.firstPref = '<s:property value="firstPref"/>';
window.secondPref = '<s:property value="secondPref"/>';

function filter(url)
{	
	//$('#num-items-select form input[name=dsvId]').value(dsvId);
    modelId = $("#modelId").val();
    username = $("#username").val();
    dsvId = $("#dsvId").val();
    access = $("#access").val();
    //alert(modelId + " " + username + " " + dsvId);
    var have = false;
    if( url == null )
    {
	    url = '<%=request.getContextPath()%>/User/';
		<s:if test="accessLevel == 'PUBLIC'">
		    url += 'ListPublicModelOutputs.action';
		</s:if>
		<s:elseif test="accessLevel == 'DATA_SET_OWNER'">
		    url += 'ListDataSetOwnerModelOutputs.action';
		</s:elseif>
		<s:else>
		    url += 'ListModelOutputs.action';
		</s:else>
		have = false;
    }
    else
    {
        have = true;
    }
        
    if( modelId != -1 && modelId != 'null'  )
    {
        if( !have )
        {
            url += '?';
            have = true;
        }
        else url += '&';
        url += "modelId=" + modelId;
    }
<s:if test="accessLevel != 'PRIVATE'">
    if( username != 'null' && username.length > 0 )
    {
        if( !have )
        {
            url += '?';
            have = true;
        }
        else url += '&';
        url += "username=" + username;
    }
</s:if>
    if( dsvId != -1 && dsvId != 'null' )
    {
        if( !have )
        {
            url += '?';
            have = true;
        }
        else url += '&';
        url += "dsvId=" + dsvId;
    }
    if( access != 'null' )
    {
        if( !have )
        {
            url += '?';
            have = true;
        }
        else url += '&';
        url += "access=" + access;
    }

    // set sort orders
    for( var key in columnDirections )
    {
        if( have ) url += '&';
        else
        {
            url += '?';
            have = true;
        }
        url += key + 'Sort' + '=' + columnDirections[key];
    }

    if( window.firstPref )
    {
        url += "&firstPref=" + window.firstPref;
    }
    if( window.secondPref )
    {
        url += "&secondPref=" + window.secondPref;
    }
    
    window.location.href = url;
}
$(document).ready(function(){
	$("#modelId").change(function(){
		filter();
	});
	$("#username").change(function(){
		filter();
	});
	$("#dsvId").change(function(){
		filter();
	});
	$("#access").change(function(){
		filter();
	});

	$('#num-items-select form div br').remove();
	$('#num-items-select select').change(function(){
		$('#num-items-select form').submit();
	});
});

function sort(columnName,direction)
{
	if( columnDirections[columnName] == direction )
	{
		direction = 'NONE';
	}
	columnDirections[columnName] = direction;

	if( window.firstPref != columnName )
	{
	    window.secondPref = window.firstPref;
	}
	window.firstPref = columnName;
	
    filter();
}
</script>
<s:include value="../../Footer.jsp"></s:include>
</body>
	
</html>
