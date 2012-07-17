<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %><html>
<head>
    <link href="../jquery-ui.css" rel="stylesheet" type="text/css"/>
    <script src="../js/jquery.min.js"></script>
    <script src="../js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="../js/pals-forms.js"></script>
	<script type="text/javascript" src="../js/validation.js"></script>
	
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../pals.css" rel="stylesheet" />
</head>

	<body>

	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 
	
	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	document.getElementById("smbDataSets").setAttribute("class","smbON");
	</script>

	<div class="main">

	<h2>Edit Data Set</h2>
	<form>
	<div class="margin-bottom">
	  <a class="noTextDec" href="../DataSetTemplates/FluxTowerDataTemplate1.xls"><input type="button" class="pbut" value="Download FluxTower Data Template"/></a>
	</div>
	</form>
	
	<table class="shade create-data-set"><tr><td class="shade">
	
	<s:form action="DataSetAction_update" id="form-to-submit">

<s:textfield name="name" label="Data Set Name"/>
	
	<s:select name="dataSetType" label="Data Set Type" list="{'flux tower','other'}" />
	
	<s:select name="countryIdSet" list="countries" listKey="id" listValue="name" label="Country" />
	<div id="vegetationTypeSelect" class="left-form-field right-space">
	    <s:select name="vegTypeSet" list="vegetationTypes" listKey="vegetationType" listValue="vegetationType" label="IGBP Vegetation Type" />
	</div>
	<div id="vegetationTypeNew" class="left-form-field right-space">
	    <s:textfield name="vegTypeNew" label="Or a new Vegetation Type"/>
	</div>
	<div id="soilType">
	    <s:textfield name="dataSet.soilType" label="Soil Type"/>
	</div>
		
	<s:textfield name="url" label="Site Description URL" />
	<div id="latString" class="left-form-field">
	    <s:textfield name="latString" label="Latitude (decimal)" cssClass="exclusive-1-a"/>
	</div>
	<div class="middle-or">OR</div>
    <div class="right-form-field">
        <div class="right-form-item">
	        <s:textfield name="latDeg" label="degrees" cssClass="exclusive-1-b"/>
	    </div>
	    <div class="right-form-item">
	        <s:textfield name="latMin" label="minutes" cssClass="exclusive-1-b"/>
	    </div>
	    <div class="right-form-item">
	        <s:textfield name="latSec" label="seconds" cssClass="exclusive-1-b"/>
	    </div>
	</div>
	<div class="float-clear"></div>
	<br>
	<div id="lonString" class="left-form-field">
	    <s:textfield name="lonString" label="Longitude (decimal)" cssClass="exclusive-2-a"/>
	</div>
	<div class="middle-or">OR</div>
	<div class="right-form-field">
	    <div class="right-form-item">
	        <s:textfield name="lonDeg" label="degrees" cssClass="exclusive-2-b"/>
	    </div>
	    <div class="right-form-item">
	        <s:textfield name="lonMin" label="minutes" cssClass="exclusive-2-b"/>
	    </div>
	    <div class="right-form-item">
	        <s:textfield name="lonSec" label="seconds" cssClass="exclusive-2-b"/>
	    </div>
	</div>
	<div class="float-clear"></div>
	<s:textfield name="elevationString" label="Elevation (m)"/>
	<s:textfield name="maxVegHeight" label="Max Vegetation Height (m)" />
	<s:textfield name="towerHeight" label="Tower Height (m)" />
	<!--<s:textfield name="timeStepSizeSeconds" label="Time Step Size (s)" />-->
	<s:textfield name="utcOffsetHoursString" label="UTC Offset (hours)" />
	<s:select name="measurementAggregation" list="measurementAggregations" label="Date at timestamp represents" />
	<s:textfield name="dataSet.siteContact" label="Site Contact"/>
	<s:textarea name="refs" label="References" rows="5" cols="100"/>
	<s:textarea name="comments" label="Comments" rows="5" cols="100"/>	
	
	<s:hidden name="id"/>
	</s:form> 
	
	<div class="submit-buttons">
	<a class='pbut-link' href='javascript:void(0)' onclick='submit()'>Save Changes</a>
	<a class='pbut-link' href='DataSetAction_list.action'>Cancel</a>
	</div>
	
	</div>
<script>
$(document).ready(function(){
	var validation = new Validation();
	validation.textLength('name',15,'Data Set Name');
	validation.textLength('url',255,'The URL');
});

function submit()
{
    $('#form_to_submit').submit();
}
</script>
</td>
</tr>
</table>
</div>
<s:include value="../Footer.jsp"></s:include>
</body>
</html>