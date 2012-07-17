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
	
	<!-- Google maps stuff -->
	
	<script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=<s:property value="gmapsKey" />&sensor=false"
            type="text/javascript"></script>
    <script type="text/javascript">

    
    function initialize() {
      if (GBrowserIsCompatible()) {
        var map = new GMap2(document.getElementById("map_canvas"));
        var center = new GLatLng(<s:property value="dataSet.latitude" />,<s:property value="dataSet.longitude" />);
        map.setCenter(center, 11);
        map.addOverlay(new GMarker(center));
        map.setMapType(G_PHYSICAL_MAP);
        
        //map.setUIToDefault();
        var customUI = map.getDefaultUI();
        //customUI.controls.scalecontrol = false;
       	customUI.controls.largemapcontrol3d = false;
       	customUI.controls.smallzoomcontrol3d = true;
       	customUI.controls.maptypecontrol = false;
        map.setUI(customUI);
                
      }
    }

  

    
    </script>
	
	<!--  End Google Maps stuff -->
	
	</head>

	<body onload="initialize()" onunload="GUnload()">
	
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 
	
	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	</script>

    <s:set name="url" value="dataSet.fullUrl" scope="request"/>

	<div class="main">
	
	<div id="message"><s:property value="message"/></div>
	
	<s:if test="dataSet.owner.username == user.username">
	<div id="data-set-edit-button">
	    <a class="pbut-link" href='DataSetAction_edit?requestId=<s:property value="dataSet.id" />'>
	        Edit
	    </a>
	    <a class="pbut-link" href='UploadDataSetVersion_upload.action?dataSetId=<s:property value="dataSet.id" />'>Upload New Version</a>
	    <a href="ListDataSetTemplates.action" class="pbut-link">Download Data Set Templates</a>
	</div>
	</s:if>
	
	<h2>Data Set: <s:property value="dataSet.name"/></h2>

	<hr size=1 />

	
	<div id="map_canvas" ></div>
	
	
	<table>
	
	<tr><td><b>Data type:</b></td><td> <s:property value="dataSet.dataSetType"/>
	</td></tr>
	<tr><td><b>Created by:</b></td><td> <s:property value="dataSet.owner.fullName" /> 
	</td></tr>
	<tr><td><b>Site contact:</b></td><td> <s:property value="dataSet.siteContact" /> 
	</td></tr>
	<tr><td><b>URL:</b></td><td> <a  href="<s:property value="dataSet.fullUrl" />">
	    <s:property value="wrappedUrl" escape="false" />
	</a>
	</td></tr>
	<tr><td><b>Latitude:</b></td><td> <s:property value="dataSet.latitude" /> 
	</td></tr>
	<tr><td><b>Longitude:</b></td><td><s:property value="dataSet.longitude" /> 
	</td></tr>
	<tr><td><b>Elevation:</b></td><td> <s:property value="dataSet.elevation" /> m
	</td></tr>
	<tr><td><b>Maximum Vegetation Height:</b></td><td> <s:property value="dataSet.maxVegHeight" /> m
	</td></tr>
	<tr><td><b>Measurement Height:</b></td><td> <s:property value="dataSet.towerHeight" /> m
	</td></tr>
	<tr><td><b>UTC offset:</b></td><td> <s:property value="dataSet.timeZoneOffsetHours" /> h
	</td></tr>
	<tr><td><b>Vegetation Type:</b></td><td> <s:property value="dataSet.vegType.vegetationType" />
	</td></tr>
	<tr><td><b>Soil Type:</b></td><td> <s:property value="dataSet.soilType" />
	</td></tr>
	<tr><td><b>Country:</b></td><td> <s:property value="dataSet.country.name" />
	</td></tr>
	<tr><td><b>Date at timestamp represents:</b></td><td> <s:property value="dataSet.measurementAggregation" />
	</td></tr>
	</table>
	
    <s:if test="dataSet.copiedFrom != null ">
	    <p>This Data Set has been copied from the main PALS database</p>
	</s:if>
	<s:else>
	    <br />
	</s:else>
	
	<b>References:</b> 
	
	<s:iterator value="refsParas">
		<p><s:property/></p>
	</s:iterator>
	
	<br />&nbsp;
	<br />
	
	<b>Comments:</b> 
	
	<s:iterator value="commentsParas">
		<p><s:property/></p>
	</s:iterator>
	
	<br />&nbsp;
	<br />
	
	<s:if test="dataSetVersions.size > 0" >

	<!--  Data Set Versions -->

<style>

div.abrev { overflow: hidden; width: 50em; height: 1.5em; margin: 0px; float: left; }


</style>

<script>


function toggle(dsvId) {
	var descDiv = document.getElementById("desc"+dsvId);
	if (descDiv.className == "") {
		descDiv.className = "abrev";
		$("#ellipsis"+dsvId).show();
	} else {
		descDiv.className = "";
		$("#ellipsis"+dsvId).hide();
	}
}

</script>
    <div id="key">
        <div id="latest-version-key-color"></div>
        <div id="latest-version-key-text">Latest Version</div>
    </div>
	<table border="1" cellpadding="6" cellspacing="3"class="mo">
	<tr>
		
		<th class="mo"><b>Version name</b></th>
		<th class="mo" >Upload date</th>
		<th class="mo" >Description of changes</th>
		<th class="mo" >QC</th>
		<th class="mo" >Met</th>
		<th class="mo" >Flux</th>
		<th class="mo" >Original</th>
		<th class="mo" >Public</th>
		<th class="mo" ></th>
	</tr>
	
	<s:iterator value="dataSetVersions">
		<tr class="
		    <s:if test="dataSet.latestVersion.id == id">latest-version-row</s:if>
		    ">
			
			<td class="mo"><b><s:property value="displayName"/></b></td>
			
			<td class="mo date"><s:date name="uploadDate" format="dd MMM yyyy h:mm"/>&nbsp;</td>
			
			<td class="mo">
			
			<div class="abrev" id='desc<s:property value="id" />'>
			<a class="noDec" href="javascript:toggle('<s:property value="id" />')">
			    <s:property value="description" />
			    <s:if test="files.size() > 0">
			        <h3 class="ancillary-files-heading">Ancillary Files:</h3>
			    </s:if>
			    <s:iterator value="files">
			        <p class="ancillary-files-p"><a href='/pals/DownloadFile.action?id=<s:property value="id" />'><s:property value="name" /></a></p>
			    </s:iterator>
			</a></div>
			
			<div style="float: left;" id='ellipsis<s:property value="id" />'>....</div>
			</td>
			
		    <td class="mo">
	            <a class="moItalic" href='QCPlots.action?id=<s:property value="id" />'>View</a>
	        </td>
			
			<td class="mo" >
				<a class="moItalic" href='../DownloadDataSet_met.action?dataSetVersionId=<s:property value="id" />'>.nc</a>
			</td>
			<td class="mo">
				<a class="moItalic" href='../DownloadDataSet_flux.action?dataSetVersionId=<s:property value="id" />'>.nc</a>
			</td>
			<td class="mo">
				<a class="moItalic" href='../DownloadDataSet_original.action?dataSetVersionId=<s:property value="id" />'>.csv</a>
			</td>
			<td class="mo">
			    <s:property value="isPublic"/>
			</td>
            <td class="mo">
                <s:if test="dataSet.owner.username == user.username">
                <s:if test="dataSet.latestVersion.id != id">
                <a class="latest-version-select" href='ViewDataSet_latest.action?id=<s:property value="dataSet.id"/>&dataSetVersionId=<s:property value="id"/>'>
                    <img src="/pals/images/latestVersion.png" title="Set as Latest Version" alt="Set as Latest Version"></img>
                </a>
                </s:if>
                </s:if>
            </td>
		</tr>
	</s:iterator>

	</table>
	
	</s:if>
	
	<br />
	
	<s:if test="dataSet.copiedTo.size() > 0">
	<h3>Workspaces this Data Set has been copied to:</h3>
    <table>
        <tr>
            <th class="mo">Workspace Name</th>
            <th class="mo">Workspace Owner</th>
        </tr>
	<s:iterator value="dataSet.copiedTo">
	    <tr>
	        <td class="mo"><s:property value="name" /></td>
	        <td class="mo"><a href='<%=request.getContextPath()%>/Account/Profile.action?username=<s:property value="owner.username" />'><s:property value="owner.fullName" /></a></td>
	    </tr>
	</s:iterator>
	</table>
	<br/>
	<br/>
	</s:if>
	
	<!--
	<div id="copy-to-experiment-form">
	<s:form action='ViewDataSet_experiment?id=%{dataSet.id}'>
        <s:select name="experimentId" list="experiments" listKey="id" listValue="name" label="Workspace" />
        <s:submit cssClass="pbut" value="Copy To Workspace"></s:submit>
    </s:form>
    </div>
    -->
    
    
	
	</div>
	<script language="javascript">
	//    $(".latest-version-select").hover(function(){
	//    });
	</script>
   
	</body>
	
</html>
