<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %><html>
<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../pals.css" rel="stylesheet" />
	<script type="text/javascript" src="../js/validation.js"></script>
</head>

	<body>

	<s:include value="Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<script>
	document.getElementById("mbModels").setAttribute("class","mbON");
	</script> 

	<div class="main">

    <h2>Create a New Model</h2>	
	
	<table class="shade create-model"><tr><td class="shade">
	
	<s:form action="NewModelAction_add" method="post" enctype="multipart/form-data" id="submit-form">
	<s:textfield name="modelName" label="Model Name"/>
	<s:textfield name="version" label="Version"/>
    <s:textfield name="urlM" label="URL" />
    <s:textarea name="referencesM" label="References" rows="5" cols="100"/>
	<s:textarea name="commentsM" label="Comments" rows="5" cols="100"/>
    <div id="ancillary-files">
	    <a href="javascript:void(0)" onclick="addFile()" class="pbut-link">Add an Ancillary File</a> (50MB limit per file)
	    <!--<s:file name="upload" label="file1"/>-->
	</div>
	</s:form> 
	<div class="submit-buttons">
	    <a class="pbut-link" href="javascript:void(0)" onclick="submit()">Create New Model</a>
	    <a class='pbut-link' href='NewModelAction_list.action'>Cancel</a>
	</div>
	
	</td></tr></table>
	
	</div>
<script>
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

$(document).ready(function(){
	var validation = new Validation();
	validation.textLength('modelName',15,'Model Name');
	validation.textLength('version',15,'Version');
	validation.textLength('urlM',255,'URL');
});

function submit()
{
    $('#submit_form').submit();
}
</script>
<s:include value="../Footer.jsp"></s:include>
</body>
</html>