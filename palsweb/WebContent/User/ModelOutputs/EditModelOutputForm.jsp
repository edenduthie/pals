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
	<script type="text/javascript" src="../js/validation.js"></script>
	</head>

	<body>
	
	<s:include value="../Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<script language="Javascript1.2">
	document.getElementById("mbModelOutputs").setAttribute("class","mbON");
	</script>

	<div class="main">
	
	<h2>Edit Model Output</h2>
	
	<table class="shade upload-model-output" width="450"><tr><td class="shade">
	
	<s:form action="EditModelOutputForm" method="post" validate="true" method="post" enctype="multipart/form-data">
	<s:hidden name="editTask" value="modify" id="editTaskInput" />
	<s:hidden name="modelOutputId" value="%{modelOutput.id}" />
	<s:textfield name="modelOutputName" label="Name" value="%{modelOutput.name}"/>
	<s:select name="modelId" label="Model" list="models" listKey="id" listValue="identifier" value="%{modelOutput.modelId}"/>
	<s:select name="dataSetVersionId" label="DataSet" list="dataSetVersions" listKey="id" listValue="displayName" value="modelOutput.dataSetVersionId"/>
	<s:select name="stateSelection" list="stateSelections" emptyOption="true"
	    label="State Selection" value="modelOutput.stateSelection"/>
	<s:select name="parameterSelection" list="parameterSelections" emptyOption="true"
	    label="Parameter Selection" value="modelOutput.parameterSelection"/>
	<s:textarea name="userComments" label="Please add any comments that would aid reproducing this simulation" rows="5" cols="50" />
	<s:checkbox name="allowDownload" value="modelOutput.allowDownload">Allow public users to download the uploaded model output file</s:checkbox>
	<br>
    <s:if test="modelOutput.files.size() > 0" >
	<i>Existing Ancillary Files:</i>
	<table class="existing-files-table">
	<s:iterator value="modelOutput.files">
	    <tr>
	    <td>
	    <div id='existing-file-<s:property value="id" />'>
	        <a class="ancillary-file-dl" href='<%=request.getContextPath()%>/DownloadFile?id=<s:property value="id" />'><s:property value="name" /></a>
	    </div>
	    </td>
	    <td><a  href="javascript:void(0)" onclick='removeExistingFile(<s:property value="id" />)'>remove</a></td>
	    </tr>
	</s:iterator>
	</table>
	<br>
	</s:if>
	<s:hidden name="filesToRemove" id="files-to-remove" />
	<div id="ancillary-files">
	    <a href="javascript:void(0)" onclick="addFile()" class="pbut-link">Add an Ancillary File</a> (50MB limit per file)
	    <!--<s:file name="upload" label="file1"/>-->
	</div>
	<br>
	<s:submit cssClass="pbut" value="Update Model Output Attributes"/>
	</s:form>
	
	</td></tr></table>
	
	</div>
<script language="javascript">
$(document).ready(function() {
    $("#EditModelOutputForm_userComments").html('<s:property value="modelOutput.userComments"/>');
	var validation = new Validation();
	validation.textLength('modelOutputName',15,'Model Output Name');
});

var numFiles = 0;

function addFile()
{
    fileNum = numFiles+1;
    if( fileNum > 4 )
    {
        alert("Maximum of 4 Ancillary Files");
        return;
    }
    content =
 	   '<div class="wwgrp" id="file' +
 	   numFiles +
 	   '">' +
        '<div id="wwlbl_uploadForm_upload" class="wwlbl">' +
        '<label for="uploadForm_upload" class="label">Ancillary File ' +
        fileNum + 
        '</label></div> <br /><div class="wwctrl">' +
        ' <input type="file" name="upload" value="[]" />'+
        '<a class="pbut-link" href="javascript:void(0)" onclick="removeFile('+
        numFiles+
        ')">remove</a>'+
        '</div> </div>';
    $('#ancillary-files').append(content);
    ++numFiles;
}

function removeFile(id)
{
    $('#file'+id).remove();
    --numFiles;
}

function removeExistingFile(id)
{
    var current = $('#files-to-remove').val();
    if( current.length > 0 ) current += ",";
    current += id;
    $('#files-to-remove').val(current);
    $('#existing-file-'+id).remove();
}

</script>
<s:include value="../../Footer.jsp"></s:include>
	</body>
	
</html>
