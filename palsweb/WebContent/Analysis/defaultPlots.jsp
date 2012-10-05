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

</head>

<body>
<input type="hidden" id="filterModelOutputId" value='<s:property value="filterModelOutputId" />' ></input>
<input type="hidden" id="filterModelId" value='<s:property value="filterModelId" />' ></input>

<script language="javascript">

var analysisList;
var modelList;

var modelOutputFilterId = -1;
var analysisTypeFilterId = -1;
var variableFilterId = -1;
var modelFilterId = -1;

var filterModel = '';
var filterDataSet = '';
var filterVersion = '';
var filterAccess = '';

var haveInfo = false;

$(document).ready(function(){ 
	
    $.getJSON(
            '../JSON/ModelDataAction.action',
        	function(data) {
            	modelList = data;
            	if( modelList.length > 0 ) modelFilterId = modelList[0]['id'];
    	        strutsModelId = $("#filterModelId").val();
    	        if( strutsModelId != -1 ) modelFilterId = strutsModelId;
            	displayModelSelect();
            	retrieveAnalysisList();
        	}
    );

    $("#modelSelect").change(function(){
    	$("#wait").show();
        modelFilterId = $(this).val();
        retrieveAnalysisList(true);
    });

    $("#modelOutputSelect").change(function(){
        modelOutputFilterId = $(this).val();
        //variableFilterId = -1;
        //analysisTypeFilterId = -1;
        displaySelectOptions();
        refilterDisplay();
    });

    $("#analysisTypeSelect").change(function(){
    	analysisTypeFilterId = $(this).val();
    	//variableFilterId = -1;
    	displaySelectOptions();
    	refilterDisplay();
    	showInfo();
    });

    $("#variableSelect").change(function(){
        variableFilterId = $(this).val();
        refilterDisplay();
    });

    $("#dataSetSelect").change(function(){
        filterDataSet = $(this).val();
        modelOutputFilterId = -1;
        variableFilterId = -1;
        analysisTypeFilterId = -1;
        modelFilterId = -1;
        filterVersion = '';
        if( filterDataSet.length > 0 ) 
        {
            displayVersionSelect(filterDataSet);
        }
        else
        {
            $("#versionSelect").hide();
        }
        displaySelectOptions(null,true);
        refilterDisplay();
        showInfo();
    });

    $("#versionSelect").change(function(){
        filterVersion = $(this).val();
        //modelOutputFilterId = -1;
        //variableFilterId = -1;
        //analysisTypeFilterId = -1;
        displaySelectOptions();
        refilterDisplay();
    });

    $("#accessSelect").change(function(){
        filterAccess = $(this).val();
        //modelOutputFilterId = -1;
        //variableFilterId = -1;
        //analysisTypeFilterId = -1;
        displaySelectOptions();
        refilterDisplay();
    });

    $("#fullscreen").click(function(){fullScreen();});
    
    $("#filters-label").click(function(){
        filtersContent = $("#filters-content");
        if( filtersContent.css('display') == 'none' ) filtersContent.show();
        else filtersContent.hide();
    });

    $('#info-link').click(function(){
        infoLinkClick();
    });

    $('#bench-choice-check').change(function(){
        refilterDisplay();
    });
});

function retrieveAnalysisList(resetModelOutputId)
{
    commandString = '../JSON/ModelOutputPlotDataAction.action?modelId=' + modelFilterId;
    $.getJSON(
            commandString,
        	function(data) {
            	analysisList = data;
        	    // set the selected options to the first option
        	    if( data.length > 0 )
        	    {
        	    	//modelOutputFilterId = data[0].analysable.id;
        	        strutsModelOutputId = $("#filterModelOutputId").val();
        	        if( resetModelOutputId == null )
        	        {
        	            if( strutsModelOutputId != -1 ) modelOutputFilterId = strutsModelOutputId;
        	        }
        	    	//analysisTypeFilterId = data[0].analysisType.analysisTypeName;
        	    	//variableFilterId = data[0].analysisType.variableName;
            	    
                    displaySelectOptions(resetModelOutputId);
                    refilterDisplay();
                    displayFilters();
                    $("#wait").hide();
                    showInfo();
        	    }
        	}
    );	
}

function displayModelSelect()
{
	if( modelFilterId != -1 )
	{
	    for( i=0; i < modelList.length; ++i)
	    {
		    if( modelList[i].id == modelFilterId )
		    {
			    modelList[i].selected = 'true';
		    }
	    }
	}
	fillSelectOptions('modelSelect',modelList,'identifier','id');
}

fullscreen = false;

function fullScreen()
{
    if( fullscreen )
    {
        $("#full-header").show();
        $("#fullscreen img").attr('src',"../images/fullscreen.png");
        fullscreen = false;
    }	
    else
    {
        $("#full-header").hide();
        $("#fullscreen img").attr('src',"../images/fullscreen_back.png");
        fullscreen = true;
    }
}

function displaySelectOptions(resetModelOutput,old)
{	
	modelOutputs = new Array();
    analysisTypes = new Array();
    variables = new Array();

    var haveSelectedModelOutput = false;
    var haveSelectedAnalysisType = false;
    var haveSelectedVariable = false;

    for( i=0; i < analysisList.length; ++i )
    {
        privacyCorrect = false;
        if( filterAccess.length <= 0 ) 
        {
            privacyCorrect = true;
        }
        else
        {
        	if( filterAccess == 'public' )
        	{
            	if(analysisList[i].analysable.accessLevel == 'PUBLIC') privacyCorrect = true;
        	}
        	else if( filterAccess == 'private' )
        	{
            	if(analysisList[i].analysable.accessLevel == 'PRIVATE') privacyCorrect = true;
        	}
        	else if( filterAccess == 'provider' )
        	{
            	if(analysisList[i].analysable.accessLevel == 'DATA_SET_OWNER') privacyCorrect = true;
        	}
        }
        
        if( (filterModel.length <= 0 ||  analysisList[i].analysable.modelId == filterModel) &&
        	(filterDataSet.length <= 0 ||  analysisList[i].analysable.dataSetVersion.dataSetId == filterDataSet) &&
        	(filterVersion.length <= 0 ||  analysisList[i].analysable.dataSetVersion.id == filterVersion) &&
        	privacyCorrect
        )
        {
	    	if( resetModelOutput )
	    	{
		    	modelOutputFilterId = analysisList[i].analysable.id;
		    	resetModelOutput = false;
	    	}
	        if( modelOutputFilterId == analysisList[i].analysable.id )
	        {
	        	haveSelectedModelOutput = true;
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
        }
    }

    for( i=0; i < analysisList.length; ++i )
    {
        privacyCorrect = false;
        if( filterAccess.length <= 0 ) 
        {
            privacyCorrect = true;
        }
        else
        {
        	if( filterAccess == 'public' )
        	{
            	if(analysisList[i].analysable.accessLevel == 'PUBLIC') privacyCorrect = true;
        	}
        	else if( filterAccess == 'private' )
        	{
            	if(analysisList[i].analysable.accessLevel == 'PRIVATE') privacyCorrect = true;
        	}
        	else if( filterAccess == 'provider' )
        	{
            	if(analysisList[i].analysable.accessLevel == 'DATA_SET_OWNER') privacyCorrect = true;
        	}
        }
        
        if( (filterModel.length <= 0 ||  analysisList[i].analysable.modelId == filterModel) &&
        	(filterDataSet.length <= 0 ||  analysisList[i].analysable.dataSetVersion.dataSetId == filterDataSet) &&
        	(filterVersion.length <= 0 ||  analysisList[i].analysable.dataSetVersion.id == filterVersion) &&
        	privacyCorrect
        )
        {
	    	modelOutputs[analysisList[i].analysable.id] = analysisList[i].analysable;
	    	if( !haveSelectedModelOutput )
	    	{
	    		modelOutputFilterId = analysisList[i].analysable.id;
	    		haveSelectedModelOutput = true;
	    	}
	        if( modelOutputFilterId == analysisList[i].analysable.id )
	        {
	        	modelOutputs[analysisList[i].analysable.id]['selected'] = 'true';
	            key = analysisList[i].analysisType.analysisTypeName;
	            analysisTypes[key] = new Array();
	            analysisTypes[key].analysisTypeName = key;
	            if( !haveSelectedAnalysisType )
	            {
	                analysisTypeFilterId = key;
	                haveSelectedAnalysisType = true;
	            }
	            if( analysisTypeFilterId == key )
	            {
	            	if(analysisTypeFilterId != -1) analysisTypes[key]['selected'] = 'true';
	            	variableKey = analysisList[i].analysisType.variableName;
	            	variables[variableKey] = new Array();
	            	variables[variableKey].variableName =  variableKey;
	            	if( !haveSelectedVariable )
	            	{
	            		variableFilterId = variableKey;
	            		haveSelectedVariable = true;
	            	}
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
	        	modelOutputs[analysisList[i].analysable.id]['selected'] = 'false';
	        }
        }
    }

    if( old )
    {
	    fillSelectOptionsOld("modelOutputSelect",modelOutputs);
	    fillSelectOptionsOld("analysisTypeSelect",analysisTypes,'analysisTypeName');
	    fillSelectOptionsOld("variableSelect",variables,'variableName');
    }
    else
    {
        fillSelectOptions("modelOutputSelect",modelOutputs);
        fillSelectOptions("analysisTypeSelect",analysisTypes,'analysisTypeName');
        fillSelectOptions("variableSelect",variables,'variableName');
    }
}

// only needs to be run on load
function displayFilters()
{
    dataSets = new Array();
    
    for( i=0; i < analysisList.length; ++i )
    {
        // load data for the extra filters
        dataSets[analysisList[i].analysable.dataSetVersion.dataSet.id] = analysisList[i].analysable.dataSetVersion.dataSet;
    }
    fillSelectOptionsOld("dataSetSelect",dataSets,'name','id',"All Data Sets");
}

function displayVersionSelect(dataSetId)
{
	versions = new Array();
    for( i=0; i < analysisList.length; ++i )
    {
    	if(analysisList[i].analysable.dataSetVersion.dataSet.id == dataSetId)
    	{
    	    versions[analysisList[i].analysable.dataSetVersion.id] = analysisList[i].analysable.dataSetVersion;
    	}
    }
    fillSelectOptionsOld("versionSelect",versions,'name','id',"--Version--");
    $("#versionSelect").show();
}

function refilterDisplay()
{
    if(haveInfo)
    {
        showInfo();
    }

    var showBenchmarks = false;
    if ($('#bench-choice-check').attr('checked')) showBenchmarks = true;
	
    content = "";
    pdfLink = "";
    //alert("mo: " + modelOutputFilterId + " at: " + analysisTypeFilterId + " vf: " + variableFilterId);
    for( i=0; i < analysisList.length; ++i )
    {
        if( modelOutputFilterId < 0 || modelOutputFilterId == analysisList[i].analysable.id )
        {
            if( analysisTypeFilterId < 0 || analysisTypeFilterId == analysisList[i].analysisType.analysisTypeName )
            {
                if( variableFilterId < 0 || variableFilterId == analysisList[i].analysisType.variableName )
                {
                    content += "<image src='../User/AnalysisRunPNG.action?analysisRunId=" + 
                        analysisList[i].id;
                    if( showBenchmarks ) content += "&bench=true";
                    content +=  "'></image>";
                    pdfLink = "../User/AnalysisRunPDF.action?analysisRunId=" + 
                        analysisList[i].id;
                    if( showBenchmarks ) pdfLink += "&bench=true";
                    break;
                }
            }
        }
    }
	$("#plots").html(content);
	$("#pdf-link").attr('href',pdfLink);
}

function fillSelectOptionsOld(elementId,arrayWithNames,valueToFill,idValue,emptyValue)
{
	if( valueToFill == null )
	{
		valueToFill = 'name';
	}
    options = "";
	if( emptyValue != null )
	{
		options += "<option value=''>"+emptyValue+"</option>";
	}
    for( var id in arrayWithNames )
    {
        theValue = id;
        if( idValue != null ) theValue = arrayWithNames[id][idValue];
        name = arrayWithNames[id][valueToFill];
        selected = arrayWithNames[id].selected;
        options += '<option value="' + theValue +'"';
        if( selected == 'true' ) options += " selected = 'true' ";
        options += ' >' + name + '</option>';
    }
    $("#"+elementId).html(options);    
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
            var emptyOption = "<option id='emptyValue' value=''>"+emptyValue+"</option>";
		    $("#"+elementId).prepend(emptyOption);
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

function infoLinkClick()
{
    if(haveInfo) 
    {
        clearInfo();
        haveInfo = false;
    }
    else
    {
        showInfo();
        haveInfo = true;
    }   
}

function showInfo()
{
	clearInfo();
	$('#info' + analysisTypeFilterId).show();
}

function clearInfo()
{
    $('#info-content').children().each(function(){
        $(this).hide();
    });
}

</script>

<s:include value="../User/Top.jsp">
	<s:param name="loggedInAs" value="user.username" />
</s:include>

<script language="Javascript1.2">
document.getElementById("mbAnalysis").setAttribute("class","mbON");
</script>

<div id="plot-filter">
    <div id="wait"><img src="../images/wait28.gif""/></div>
    <div id="choices">
                <div id="bench-choice">Display Benchmarks&nbsp;<input type="checkbox" id="bench-choice-check"></input></div>
	    <!-- <a id="fullscreen" href="javascript:void(0)"><img src="../images/fullscreen.png" alt="fullscreen" title="fullscreen"/></a> -->
	    <!-- &nbsp;<a id="filters-label" href="javascript:void(0)"><img src="../images/filter.png" alt="filters" title="filters"></a>  -->
	    Data Set
	    <select id="dataSetSelect"></select>
	    <select id="versionSelect" style="display:none;"></select>&nbsp;
	    Model
	    <select id="modelSelect">
	    </select>
	    <!-- Model Output  -->
	    <select id="modelOutputSelect">
	    </select>
	    &nbsp;&nbsp;Variable
	    <select id="variableSelect">
	    </select>
	    &nbsp;Analysis Type
	    <select id="analysisTypeSelect">
	    </select>
	    &nbsp;&nbsp;
	    <a id="pdf-link" href=""><img src="../images/pdf.gif" alt="pdf version" title="pdf version"/></a>
	    <div id="filters">
                <div id="filters-content">
                Data Set &nbsp;
	            <select id="dataSetSelect"></select>
	            <select id="versionSelect" style="display:none;"></select>&nbsp;
	            Access &nbsp;
	            <select id="accessSelect">
	                <option value='' selected="true">--access--</option>
	                <option value="public">public</option>
	                 <option value="provider">provider</option>
	                <option value="private">private</option>
	            </select>
            </div>
        </div>
    </div>
</div>
<div id="plots">
</div>
<!-- 
<div id="info-link">
More information about<br>
this analysis type...
</div>
-->
<div id="info-content">
<div id="infoEvapFrac">
<h3>Smoothed evaporative fraction</h3>
<p>
This plot is a smoothed time series of evaporative fraction - latent heat flux divided by the sum of latent and sensible heat fluxes (Qle/(Qle+Qh)). It shows the moving average of a 30-day window, and only includes times when both fluxes are positive (daytime). Red sections of the greyed lines labelled E and H at the bottom of the plot show when the Fluxdata.org quality control flag was used, usually meaning data was gap-filled for that period. The scalar score for each model or benchmark plot is Normalised Mean Error (NME). Values greater than 1 suggest the mean of the observations would have been a better estimate of the dynamics of the variable than the model time series.
</p>
<p>
<b>Interpretation</b>: this plot is commonly interpreted as a proxy for soil moisture - it gives an indication of how closely model soil moisture dynamics track those of the site. In very wet densely forested sites, it may also provide information about a model's canopy interception parametrisation. Both top layer (through evaporation) and below surface soil moisture (through transpiration) contribute to Qle, as well as direct canopy evaporation in the time immediately following rainfall events.
</p>
<!--
<p>
<b>Temporal Requirements</b>: latent (Qle) and sensible heat flux (Qh) time series
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoDiurnalCycle">
<h3>Diurnal Cycle</h3>
<p>
This plot shows the average diurnal cycle of a particular variable. Four separate panels are shown, one for each season, and results include all years of the dataset. Where information is available, gap-filled time steps will be removed from plots and scalar scores (with the percentage of removed data shown with the skill score). The scalar score used here for each model or benchmark is Normalised Mean Error (NME). Values greater than 1 suggest the mean of the observations would have been a better estimate of the dynamics of the variable than the model time series. A value of NME is shown for each season (top right of each panel) and for an all-year diurnal cycle (top left of the DJF plot).
</p>
<p>
<b>Interpretation</b>: this plot allows identification of the particular times of day that a model may under-perform. It may also allow consideration of model performance, for example, without reference to night time flux data, if this data is believed to be less reliable. By having four panels, it also allows the identification of errors associated with seasonal phenomena, such as vegetation growth phases or water availability.
</p>
<!--
<p>
<b>Requirements</b>: any variable which varies on hourly timescales 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoAnnualCycle">
<h3>Annual Cycle</h3>
<p>
This graph shows a line plot of the twelve average monthly values of a variable, averaging over all years of a data set. The scalar score for each model or benchmark plot is Normalised Mean Error (NME). Values greater than 1 suggest the mean of the observations would have been a better estimate of the dynamics of the variable than the model time series.
</p>
<p>
<b>Interpretation</b>: by identifying which times of year a model may under-perform, this plot can point to seasonal issues such as representation of vegetation phenology or soil drying after wet seasons. 
</p>
<!--
<p>
<b>Requirements</b>: any variable which varies on monthly timescales 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoAvWindow">
<h3>Averaging window</h3>
<p>
This plot has four panels corresponding to four performance measures for a single model output variable (clockwise on plot): root mean square error (RMSE); model and observed correlation; both model and observed standard deviation, and; model versus observed linear regression gradient. The horizontal axis shows the value of a performance measure for a given averaging window size - '1' for example gives the value of the performance measure (say RMSE) for daily average values of the variable; '7' for weekly averages; and '30' approximately for monthly averages. The left-most value (closest to zero) is currently set to be double the time step size of the model simulation (e.g. hourly averages for half hourly time step size).
</p>
<p>
<b>Interpretation</b>: This plot gives some indication of frequency domain performance. It may indicate, for example, that a model has been fitted or calibrated to data at a particular temporal resolution at the expense of performance at other time scales.
</p>
<!--
<p>
<b>Requirements</b>: any variable which varies at each model time step 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoTaylor">
<h3>Taylor diagram</h3>
<p>
This plot shows RMSE, model-observed correlation and standard deviation of both model and observed data on a single diagram. Radial lines represent lines of constant correlation and arcs about the origin are lines of constant standard deviation. On the correlation 1 line (the horizontal axis), lies the value of the standard deviation of the observational dataset, marked by a small hollow blue circle. The solid black arc from this point is a line which shows all points on the diagram with the same standard deviation. The approximately circular arcs with this blue circle as its centre are lines of equal root mean square error. The model standard deviation, RMSE and correlation with observations is shown by the solid blue dot. The equivalent values of these performance measures for daily averages (rather than all time steps) are shown by red circles, and for monthly averages by green circles.</p>
<p>
<b>Interpretation</b>: This plot provides a way to see the relationship between these three performance measures, as well as how they change from per-time-step to daily to monthly averages, giving an indication of how performance is different at different time scales.</p>
<!--
<p>
<b>Requirements</b>: any variable which varies at each model time step 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoConserve">
<h3>Conserve</h3>
<p>
This shows two panels. Both show (latent heat flux + sensible heat flux) as the ordinate (y-axis) and (net radiation - ground heat flux) on their abscissa (x-axis). In most cases, these two should be precisely equal to each other, so that all values should lie on the 1:1 line. Where this is not the case, these plots show the linear regression coefficients for the per-timestep (left) and daily average (right) cases.
</p>
<p>
<b>Interpretation</b>: This is usually an indication that a model does not conserve energy and reveals the extent to which that is the case.
</p>
<!--
<p>
<b>Requirements</b>: latent (Qle), sensible (Qh) and ground heat fluxes (Qg) as well as net radiation (Rnet).
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoPdf">
<h3>Pdf</h3>
<p>
This shows the approximate density of values of a particular variable across its entire range. Where modelled or benchmark data are shown, the overlap (shaded in grey if there is just one model) is reported as a percentage that constitutes the scalar score for this analysis type. Where gap-filling information is available, a second observational PDF may be plotted to indicate the extent to which gap-filling may affect the percentage overlap metric.
</p>
<p>
<b>Interpretation</b>: gives an indication of which variable values are most common / likely in a particular environment, whether the distribution of modelled values is similar to observations and whether the range of modelled values is appropriate. In particular is useful for identifying cases where a model may be getting mean values correct for the wrong reasons.
</p>
<!--
<p>
<b>Requirements</b>: any variable which varies at each model time step 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
<div id="infoScatter">
<h3>Scatter</h3>
<p>
Shows a scatter plot of modelled vs. observed values of a variable including regression coefficients.
</p>
<p>
<b>Interpretation</b>: gives a general indication of significant model biases and prediction accuracy.
</p>
<!--
<p>
<b>Requirements</b>: any variable which varies at each model time step 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
 -->
</div>
<div id="infoTimeseries">
<h3>Timeseries</h3>
<p>
This simply shows a smoothed time series of a variable (14-day running mean) across the entire data set. The red sections of the grey line at the bottom of the graph show when the Fluxdata.org quality control flag was used, usually meaning data was gap-filled for that period (the gap-filled percentage of the time series shown immediately above the grey line). At the top of the graph in the centre, the minimum, maximum, mean and standard deviation of the original (unsmoothed) time series are shown. Values inside the brackets follow the same order as the plot legend (e.g. observed, modelled, benchmark time series). Two scalar scores are also shown: the Normalised Mean Error (NME) of the smoothed time series for each model or benchmark, and the NME of the original time series for each benchmark or model (labelled "Score_all"). Values greater than 1 suggest the mean of the observations would have been a better estimate of the dynamics of the variable than the model time series.
</p>
<p>
<b>Interpretation</b>: gives an indication a model's temporal divergence from observations. Good, for example, for looking at dry-down after rainfall events (by looking at latent heat, Qle) or temporal variation in carbon uptake.
</p>
<!--
<p>
<b>Requirements</b>: any variable which varies at each model time step 
</p>
<p>
<b>Spatial Requirements</b>: single site.
</p>
-->
</div>
</div><!-- info-content -->
<s:include value="../Footer.jsp"></s:include>
</body>

</html>
