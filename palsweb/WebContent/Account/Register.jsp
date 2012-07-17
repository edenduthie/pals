<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<head>
   <title><s:text name="modcheck.name"/></title>
   <s:head />
   <link type="text/css" href="../pals.css" rel="stylesheet" />

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/autocomplete.css">
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.8.2r1/build/button/assets/skins/sam/button.css" />
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/yuiloader/yuiloader-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/dom/dom-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/event/event-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/animation/animation-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/datasource/datasource-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/autocomplete/autocomplete-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/element/element-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/button/button-min.js"></script>
<script type="text/javascript" src="http://yui.yahooapis.com/2.8.2r1/build/get/get-min.js"></script>
</head>

<body class="yui-skin-sam">
 
<s:include value="/User/Top.jsp"></s:include>
<div class="main-width">
<div class="register-form-width">
<h3>Create a PALS Account</h3>
<s:form id="Register" action="Register" validate="true" enctype="multipart/form-data" method="post">
    <div id="profile-picture-upload">
        <s:file label="Profile Picture" name="upload"></s:file>
    </div>
    <s:textfield  name="userToCreate.fullName" label="Name"/>
    <s:textfield name="userToCreate.institution.name" label="Institution"/>
    <div class="input-long-auto" id="institution-empty-results-container"></div>
   	<s:textfield name="userToCreate.username" label="username"/>
    <s:password  name="userToCreate.password" label="Password"/>
    <s:password  name="passwordConfirm" label="Confirm Password"/>
    <s:textfield  name="userToCreate.email" label="Email"/>
    <s:checkbox name="userToCreate.showEmail" fieldValue="true" label="Display email to PALS users" />
    <div class="clear"></div>
    <br>
    <s:textarea name="userToCreate.researchInterest" label="What is your research interest related to PALS" rows="5" cols="70"/>
    <div id="disclaimer">By clicking the 'Register' button below you are confirming that you accept the <a href="Terms.jsp">Terms and Conditions</a></div>
    <s:submit value='Register' />
</s:form>
</div><!-- register-form-width-->
</div><!-- main-width -->
<script type="text/javascript">

YAHOO.example.Data = {
        institutions: [
            {name: "UNSW", id:1 }
        ]
};

function displayInstitutions()
{
    YAHOO.example.ItemSelectHandler = function() {
        // Use a LocalDataSource
        var oDS = new YAHOO.util.LocalDataSource(YAHOO.example.Data.institutions);
        oDS.responseSchema = {fields : ["name", "id"]};

        // Instantiate the AutoComplete
        var oAC = new YAHOO.widget.AutoComplete("Register_userToCreate_institution_name","institution-empty-results-container", oDS);
        oAC.resultTypeList = false;
        
        var myHandler = function(sType, aArgs) {
            var myAC = aArgs[0]; // reference back to the AC instance
            var elLI = aArgs[1]; // reference to the selected LI element
            var oData = aArgs[2]; // object literal of selected item's result data
            
            // update hidden form field with the selected item's ID
            //countryId = oData.id;
            //country = oData;
        };
        oAC.itemSelectEvent.subscribe(myHandler);
        oAC.forceSelection = false;
        oAC.typeAhead = true;

        return {
            oDS: oDS,
            oAC: oAC
        };
    }();
}

$(document).ready(function() {
	$.getJSON('/pals/JSON/AutocompleteAction_institution.action', function(data) {
		YAHOO.example.Data.institutions = data.institutions;
		displayInstitutions();
	});
	$('#Register_userToCreate_showEmail').attr('checked','checked');
});
</script>
</body>
</html>