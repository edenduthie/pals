<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<title><s:text name="modcheck.name" /></title>
<s:head />
<link type="text/css" href="../pals.css" rel="stylesheet" />
<script language="javascript" src="../js/jquery.min.js"></script>

<!--[if lt IE 8]>
	<link rel="stylesheet" type="text/css" href="../css/ie.css" />
<![endif]-->
</head>

<body>

<input type="hidden" id="filterDataSetVersionId" value='<s:property value="filterDataSetVersionId" />' ></input>
<input type="hidden" id="filterDataSetId" value='<s:property value="filterDataSetId" />' ></input>
<script language="javascript">

var analysisList;

var dataSetFilterId = -1;
var versionFilterId = -1;
var analysisTypeFilterId = -1;
var variableFilterId = -1;

$(document).ready(function(){ 
    $.getJSON(
        '../JSON/PlotDataAction.action',
    	function(data) {
        	analysisList = data;
    	    // set the selected options to the first option
    	    if( data.length > 0 )
    	    {
    	        dataSetFilterId = data[0].analysable.dataSet.id;
    	        strutsDataSetId = $("#filterDataSetId").val();
    	        if( strutsDataSetId != -1 ) dataSetFilterId = strutsDataSetId;
    	        versionFilterId = data[0].analysable.id;
    	        strutsDataSetVersionId = $("#filterDataSetVersionId").val();
    	        if( strutsDataSetVersionId != -1 ) versionFilterId = strutsDataSetVersionId;
    	        analysisTypeFilterId = data[0].analysisType.analysisTypeName;
    	        variableFilterId = data[0].analysisType.variableName;
    	    }
    	    displaySelectOptions();
    	    refilterDisplay();
    	    $("#wait").hide();
    	}
    );

    $("#dataSetSelect").change(function(){
        dataSetFilterId = $(this).val();
        displaySelectOptionsDataSetChanged(true);
        redraw();
    });

    $("#versionSelect").change(function(){
        versionFilterId = $(this).val();
        displaySelectOptionsDataSetChanged(false);
        redraw();
    });

    $("#analysisTypeSelect").change(function(){
    	analysisTypeFilterId = $(this).val();
    	displaySelectOptionsDataSetChanged(false);
    	redraw();
    });

    $("#variableSelect").change(function(){
        variableFilterId = $(this).val();
        displaySelectOptionsDataSetChanged(false);
        redraw();
    });

    $("#fullscreen").click(function(){fullScreen();});
});

function redraw()
{
	refilterDisplay();
}

function refilterDisplay()
{
    content = "";
    pdfLink = "";
    for( i=0; i < analysisList.length; ++i )
    {
        if( dataSetFilterId == analysisList[i].analysable.dataSet.id )
        {
            if( versionFilterId == analysisList[i].analysable.id )
            {
                if( analysisTypeFilterId == analysisList[i].analysisType.analysisTypeName )
                {
                	if( variableFilterId == analysisList[i].analysisType.variableName )
                    {
                    content += "<image src='../User/AnalysisRunPNG.action?analysisRunId=" + 
                        analysisList[i].id +"'></image>";
                    pdfLink = "../User/AnalysisRunPDF.action?analysisRunId=" + 
                        analysisList[i].id;
                    break;
                    }
                }
            }
        }
    }
	$("#plots").html(content);
	$("#pdf-link").attr('href',pdfLink);
}

function displaySelectOptions()
{
    dataSets = new Array();
    dataSetVersions = new Array();
    analysisTypes = new Array();
    variables = new Array();
    
    for( i=0; i < analysisList.length; ++i )
    {
        dataSets[analysisList[i].analysable.dataSet.id] = analysisList[i].analysable.dataSet;
        if( dataSetFilterId < 0 || dataSetFilterId == analysisList[i].analysable.dataSet.id )
        {
        	dataSets[analysisList[i].analysable.dataSet.id]['selected'] = 'true';
            dataSetVersions[analysisList[i].analysable.id] = analysisList[i].analysable;
            if( versionFilterId < 0 || versionFilterId == analysisList[i].analysable.id )
            {
            	dataSetVersions[analysisList[i].analysable.id]['selected'] = 'true';
            	key = analysisList[i].analysisType.analysisTypeName;
	            analysisTypes[key] = new Array();
	            analysisTypes[key].analysisTypeName = key.substring(3,key.length);  // remove 'Obs' from the front
	            if( analysisTypeFilterId < 0 || analysisTypeFilterId == key )
	            {
	            	if(analysisTypeFilterId != -1) analysisTypes[key]['selected'] = 'true';
	            	variableKey = analysisList[i].analysisType.variableName;
	            	variables[variableKey] = new Array();
	            	variables[variableKey].variableName =  variableKey;
	            	if( variableFilterId == variableKey )
	            	{
	                	variables[variableKey]['selected'] = true;
	            	}
	            	else
	            	{
	            		variables[variableKey]['selected'] = false;
	            	}
	            }
	            else
	            {
	            	analysisTypes[key]['selected'] = 'false';
	            }
            }
            else
            {
            	dataSetVersions[analysisList[i].analysable.id]['selected'] = 'false';
            }
        }
        else
        {
        	dataSets[analysisList[i].analysable.dataSet.id]['selected'] = 'false';
        }
    }

    fillSelectOptions("dataSetSelect",dataSets);
    fillSelectOptions("versionSelect",dataSetVersions);
    fillSelectOptions("analysisTypeSelect",analysisTypes,'analysisTypeName');
    fillSelectOptions("variableSelect",variables,'variableName');
}

function displaySelectOptionsDataSetChanged(resetVersion)
{
    dataSets = new Array();
    dataSetVersions = new Array();
    analysisTypes = new Array();
    variables = new Array();

    var haveSelectedAnalysisType = false;
    var haveSelectedVariable = false;
       
    for( i=0; i < analysisList.length; ++i )
    {
        dataSets[analysisList[i].analysable.dataSet.id] = analysisList[i].analysable.dataSet;
        if( dataSetFilterId == analysisList[i].analysable.dataSet.id )
        {
        	dataSets[analysisList[i].analysable.dataSet.id]['selected'] = 'true';
            dataSetVersions[analysisList[i].analysable.id] = analysisList[i].analysable;
            if( resetVersion )
            {
                versionFilterId = analysisList[i].analysable.id;
                resetVersion = false;
            }
            if( versionFilterId == analysisList[i].analysable.id )
            {
            	dataSetVersions[analysisList[i].analysable.id]['selected'] = 'true';
            	key = analysisList[i].analysisType.analysisTypeName;
	            if( analysisTypeFilterId == key )
	            {
	            	haveSelectedAnalysisType = true;
	            	variableKey = analysisList[i].analysisType.variableName;
	            	if( variableFilterId == variableKey )
	            	{
	                	haveSelectedVariable = true;
	            	}
	            }
            }
            else
            {
            	dataSetVersions[analysisList[i].analysable.id]['selected'] = 'false';
            }
        }
        else
        {
        	dataSets[analysisList[i].analysable.dataSet.id]['selected'] = 'false';
        }
    }

    var alreadySelectedFirstAnalysisType = false;
    var alreadySelectedFirstVariable = false;
    
    for( i=0; i < analysisList.length; ++i )
    {
        if( dataSetFilterId == analysisList[i].analysable.dataSet.id )
        {
            if( versionFilterId == analysisList[i].analysable.id )
            {
            	key = analysisList[i].analysisType.analysisTypeName;
	            analysisTypes[key] = new Array();
	            //analysisTypes[key].analysisTypeName = key;
	            analysisTypes[key].analysisTypeName = key.substring(3,key.length);  // remove 'Obs' from the front
	            if( analysisTypeFilterId == key || 
	    	        (!haveSelectedAnalysisType && !alreadySelectedFirstAnalysisType))
	            {
	            	analysisTypeFilterId = key;
	            	alreadySelectedFirstAnalysisType = true;
	            	analysisTypes[key]['selected'] = 'true';
	            	variableKey = analysisList[i].analysisType.variableName;
	            	variables[variableKey] = new Array();
	            	variables[variableKey].variableName =  variableKey;
	            	if( variableFilterId == variableKey ||
	    	            (!haveSelectedVariable && !alreadySelectedFirstVariable) )
	            	{
	            		variableFilterId = variableKey;
	                	variables[variableKey]['selected'] = true;
	                	alreadySelectedFirstVariable = true;
	            	}
	            	else
	            	{
	            		variables[variableKey]['selected'] = false;
	            	}
	            }
	            else
	            {
	            	analysisTypes[key]['selected'] = 'false';
	            }
            }
            else
            {
            	dataSetVersions[analysisList[i].analysable.id]['selected'] = 'false';
            }
        }
        else
        {
        	dataSets[analysisList[i].analysable.dataSet.id]['selected'] = 'false';
        }
    }

    fillSelectOptions("dataSetSelect",dataSets);
    fillSelectOptions("versionSelect",dataSetVersions);
    fillSelectOptions("analysisTypeSelect",analysisTypes,'analysisTypeName');
    fillSelectOptions("variableSelect",variables,'variableName');
}

fullscreen = false;

function fullScreen()
{
    if( fullscreen )
    {
        $(".top").show();
        $(".subtabs").show();
        $("#full-header").show();
        $("#fullscreen img").attr('src',"../images/fullscreen.png");
        fullscreen = false;
    }	
    else
    {
        $(".top").hide();
        $(".subtabs").hide();
        $("#full-header").hide();
        $("#fullscreen img").attr('src',"../images/fullscreen_back.png");
        fullscreen = true;
    }
}

function fillSelectOptions(elementId,arrayWithNames,valueToFill,idValue,emptyValue)
{
	if( valueToFill == null )
	{
		valueToFill = 'name';
	}
    options = "";
	if( emptyValue != null )
	{
		if($("#"+elementId+'option[id="emptyValue"]').length == 0 )
		{
		    $("#"+elementId).prepend("<option if='emptyValue' value=''>"+emptyValue+"</option>");
		}
	}
	// add any options which are missing
    for( var id in arrayWithNames )
    {
        theValue = id;
        if( idValue != null ) theValue = arrayWithNames[id][idValue];
        name = arrayWithNames[id][valueToFill];
        selected = arrayWithNames[id].selected;
        optionHtml = '<option value="' + theValue +'"';
        if( selected == 'true' ) optionHtml += " selected = 'true' ";
        optionHtml += ' >' + name + '</option>';
        if( $("#"+elementId+' option[value="'+theValue+'"]').length <= 0 )
        {
		    $("#"+elementId).append(optionHtml);
        }
    }
    // remove any options which are no longer available
    $("#"+elementId).children().each(function() {
        var child = $(this);
        var value = child.attr("value");
        if( value != null && value.length > 0 )
        {
	        var have = false;
	        for( var id in arrayWithNames )
	        {
	        	theValue = id;
	            if( idValue != null ) theValue = arrayWithNames[id][idValue];
	            if( theValue == value ) have = true;
	        }
	        if( !have ) child.remove();
        }
    });   
}
</script>


<s:include value="../User/Top.jsp">
	<s:param name="loggedInAs" value="user.username" />
</s:include>

<script>
document.getElementById("mbAnalysis").setAttribute("class","mbON");
//document.getElementById("mbDataSets").setAttribute("class","mbON");
//document.getElementById("smbPublicDataSets").setAttribute("class","smbON");
</script>

<div id="wait"><img src="../images/wait28.gif""/></div>
<div id="plot-filter">
    <div class="float-left small-menu-item"><a  id="fullscreen" href="javascript:void(0)"><img alt="fullscreen" title="fullscreen" src="../images/fullscreen.png" /></a></div>
    <div class="float-left large-menu-item pad">Data Set</div>
    <div class="float-left large-menu-item">
    <select  id="dataSetSelect">
    </select>
    </div>
    <div class="float-left medium-menu-item pad">Version</div>
    <div class="float-left large-menu-item">
    <select class="float-left large-menu-item" id="versionSelect" name="versionSelect">
    </select>
    </div>
    <div class="float-left large-menu-item pad">Analysis Type</div>
    <div class="float-left large-menu-item"><select id="analysisTypeSelect"></select></div>
    <div class="float-left large-menu-item pad">Variable</div>
	<div class="float-left large-menu-item"><select id="variableSelect"></select></div>
    <div id="pdf-link-containter" class="float-left small-menu-item"><a id="pdf-link" class="left-margin" href=""><img src="../images/pdf.gif" alt="pdf version" title="pdf version"/></a></div>
</div>
<div id="plots">
</div>

<s:include value="../Footer.jsp"></s:include> 

</body>

</html>
