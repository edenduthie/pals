<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../pals.css" rel="stylesheet" />
	
	<link href="../swfupload/simpledemo/css/only_demo.css" rel="stylesheet" type="text/css" />
	<script type="text/javascript" src="../swfupload/swfupload.js"></script>
	<script type="text/javascript" src="../swfupload/simpledemo/js/swfupload.queue.js"></script>
	<script type="text/javascript" src="../swfupload/simpledemo/js/fileprogress.js"></script>
	<script type="text/javascript" src="../swfupload/simpledemo/js/handlers.js"></script>
	<script type="text/javascript">
		var swfu;

		window.onload = function() {
			var username = '<s:property value="user.username" />';
			var settings = {
				flash_url : "/pals/swfupload/Flash/swfupload.swf",
				upload_url: "/pals/Upload/Upload.action",
				post_params: {"username" : username},
				file_size_limit : "100 MB",
				file_types : "*.csv;*.txt",
				file_types_description : "CSV or TXT",
				file_upload_limit : 100,
				file_queue_limit : 0,
				custom_settings : {
					progressTarget : "fsUploadProgress",
					cancelButtonId : "btnCancel"
				},
				debug: false,

				// Button settings
				button_image_url: "/pals/swfupload/simpledemo/images/Button_180_27.png",
				button_width: "160",
				button_height: "27",
				button_placeholder_id: "spanButtonPlaceHolder",
				button_text: '<span class="theFont">Select Data Set File</span>',
				button_text_style: ".theFont { font-size: 16; }",
				button_text_left_padding: 12,
				button_text_top_padding: 3,
				
				// The event handler functions are defined in handlers.js
				file_queued_handler : fileQueued,
				file_queue_error_handler : fileQueueError,
				file_dialog_complete_handler : fileDialogComplete,
				upload_start_handler : uploadStart,
				upload_progress_handler : uploadProgress,
				upload_error_handler : uploadError,
				upload_success_handler : uploadSuccess,
				upload_complete_handler : uploadComplete,
				queue_complete_handler : queueComplete	// Queue plugin event
			};

			swfu = new SWFUpload(settings);
	     };
	</script>
	</head>

	<body>
	
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 
	
	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	document.getElementById("smbDataSets").setAttribute("class","smbON");
	
 	function displayUploadingMsg() {
		document.getElementById("uploadingMsg").style.visibility = "visible";
		document.getElementById("uploadingOverlay").style.visibility = "visible";
 	}
 	
 	function doUpload() {
		displayUploadingMsg();
		document.getElementById("uploadForm").submit();
 	}
	</script>

	<div class="main">
	
   <div class="overlayMsg" id="uploadingMsg">
   <h2>Uploading... &nbsp; <img src="../images/wait28.gif" border=0 /></h2>
   <p>Please wait.</p> 
   <p>PALS is processing your upload, and this may take up to a minute.</p>
   <p>This message will disappear when the process is complete.</p>
   </div> 
   
   <div class="overlay" id="uploadingOverlay">&nbsp;</div>

	<h2>Upload a New Version of a Data Set</h2>
	
	<form>
	<a class="pbut-link" href="../DataSetTemplates/PALSFluxTowerTemplate1.0.2.xls">Download FluxTower Data Template</a>
	</form>
	<br/>
	
	<table cellpadding="5" class="shade"><tr><td class="shade">
	
	<s:if test="message != null " >
	<div id="version-message"><s:property value="message" /></div>
	</s:if>
	
	<s:label name="dataSetName" label="Data Set" value="%{dataSet.name}" />
	
	<form id="form1" action="index.php" method="post" enctype="multipart/form-data">
			<div>
				<span id="spanButtonPlaceHolder"></span>
				<input id="btnCancel" type="button" value="Cancel All Uploads" onclick="swfu.cancelQueue();" disabled="disabled" style="margin-left: 2px; font-size: 8pt; height: 29px;" />
			</div>
			<div id="file-upload-progress" style="display:none;">
			<div class="fieldset flash" id="fsUploadProgress">
			<!--<span class="legend">Upload Queue</span>-->
			</div>
		    <div id="divStatus">0 Files Uploaded</div>
		    </div>
	</form>
	
	<s:form id="uploadForm" action="UploadDataSetVersion_verify" method="post" enctype="multipart/form-data">
	
	<s:hidden name="dataSetId" value="%{dataSet.id}"/>
	
	<input type="hidden" id="filename" name="filename" />
	
	<!--<s:file name="dataSetRaw" label="Data Set File"/>-->
	<s:textfield name="versionName" label="Version Name"/>
	<div id="versionNameValidation" class="error-text"></div>
	<s:textarea name="description" label="Description of changes" rows="3" cols="50" />
	
	<div id="ancillary-files">
	    <a href="javascript:void(0)" onclick="addFile()" class="pbut-link">Add an Ancillary File</a> (50MB limit per file)
	    <!--<s:file name="upload" label="file1"/>-->
	</div>
	
	</s:form>
	<div class="submit-buttons">
	<a class="pbut-link" href="javascript:void(0)" onclick="submit()">Upload Data Set</a>
	<a class="pbut-link" href="<s:url action="DataSetAction_cancel"/>">Cancel</a>
	</div>
	
   
   </td></tr></table>
   
   </div>
   
      <script language="javascript">
       $(document).ready(function () {
           $("#uploadForm_versionName").change(function(){
               result = runValidationOnName();
               if( result ) checkNameExists();
           });
       });

       function runValidationOnName()
       {
           value = $("#uploadForm_versionName").val();
           result = validateName(value);
           $("#versionNameValidation").html('');
           if( result != 0 ) 
           {
               $("#versionNameValidation").html(result);
               return false;
           }
           return true;
       }

       function validateName(name)
       {
           if( name == null || name.length <= 0 )
           {
               return "Required field";
           }
           if( name.length > 15 )
           {
               return "Max lenth is 15 characters";
           }
           else 
           {
               re = new RegExp('^[a-zA-Z0-9_.]*$');
               if( !name.match(re) )
               {
                   return "Only alphanum, underscores, or periods";
               }
           }
           return 0;
       }

       function checkNameExists()
       {
           name = $("#uploadForm_versionName").val();
   	       $.getJSON(
   	 	   	       '../JSON/ValidationAction_versionName.action?text='+name+'&dataSetId='+<s:property value="dataSet.id"/>,
   	 	   	       function(data) {
   	   	 	   	       if( data != null )
   	   	 	   	       {
   	 	   	   	           if( data.trim() != 'null' ) 
   	 	   	   	           {
   	 	   	   	                $("#versionNameValidation").html(data);
   	 	   	   	           }
   	   	 	   	       }
   	 	   	       }
   	 	   	   );
       }

       $("#uploadForm").submit(function(){
           var result = true;
    	   if( !runValidationOnName() ) result = false;
    	   if( result == true ) displayUploadingMsg();
    	   return result;
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

       function submit()
       {
           $('#uploadForm').submit();
       }
   </script>
   
	</body>
	
</html>
