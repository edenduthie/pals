<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Data Set Dynamic Plots</title>
<script language="javascript" src="../js/jquery.min.js"></script>
<link type="text/css" href="../pals.css" rel="stylesheet" />

<!--CSS file (default YUI Sam Skin) -->
<link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/2.9.0/build/calendar/assets/skins/sam/calendar.css">
 
<!-- Dependencies -->
<script src="http://yui.yahooapis.com/2.9.0/build/yahoo-dom-event/yahoo-dom-event.js"></script>
 
<!-- Source file -->
<script src="http://yui.yahooapis.com/2.9.0/build/calendar/calendar-min.js"></script>
</head>
<body class="yui-skin-sam">
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	document.getElementById("smbDataSets").setAttribute("class","smbON");
	</script>

<div class="main">
<!-- saved from url=(0013)about:internet -->
<!-- amstock script-->
<div id="cal1Container"></div>
<div id="dynamic-top">
<h2>Dynamic Plots for Data Set <s:property value="dataSet.name"/></h2>
<div class="line-height jump-text">Jump to start date > </div>
<div class="line-height"><a href="javascript:void(0)" onclick="showCalendar()"><div id="calendar-image" alt="Jump to start date" title="Jump to start date"></div></a></div>
<div class="line-height"><div id="date-field"></div></div>
</div>
<div class="chart-left">
  <script type="text/javascript" src="../amstock/amstock/swfobject.js"></script>
  <!-- 
  <div id="slider">
      <div id="slider-key"><s:property value="firstDate"/> <span id="last-date"><s:property value="lastDate"/></span></div>
      <div id="slider-scale">
          <div id="shutter"></div>
      </div>
  </div>
   -->
	<div id="flashcontent">
		<strong>You need to upgrade your Flash Player</strong>
	</div>

	<script type="text/javascript">
		// <![CDATA[		
		
		var so;

		function displayChart()
		{
			so = new SWFObject("../amstock/amstock/amstock.swf", "chart1", "1000", "100%", "8", "#FFFFFF");
			so.addVariable("path", "/pals");
			so.addVariable("settings_file", encodeURIComponent("amstock_settings_single_chart.xml"));
			so.addVariable("preloader_color", "#999999");
			so.addVariable("chart_id", "chart1");
			so.addParam("wmode", "opaque");
			so.write("flashcontent");
		}

		displayChart();

		var flashMovie;
		var dataSetId = '<s:property value="retrieveId()" />';
		var firstDate = '<s:property value="firstDate"/>';
		var lastDate = '<s:property value="lastDate"/>';
		var fileName = '/DataSet/DataSetAsFile.action?id=' + dataSetId;
		var settings = '';

		function amChartInited(chart_id)
		{
	        flashMovie = document.getElementById(chart_id);
	        flashMovie.setParam('data_sets.data_set[0].file_name', fileName);
	        flashMovie.showAll();
		}
		
		function amProcessCompleted(chart_id,process_name)
		{
			if( process_name == 'init' )
			{
			}
		}
		
		function amRolledOver(chart_id, date, period, data_object)
		{
			$('#date-field').html(date);
			for( key in data_object[0].values ) 
		    {
				$('#'+key+'-value').html(Math.round((data_object[0].values[key]['close'])*100)/100);
			}
		}

		function showDataSet()
		{
	        var id = $('#data-set-id').val();
	        window.location.href = "DynamicPlotsSingleChart.action?id=" + id;
		}

		var graphVisible0 = new Array();
		graphVisible0[0] = '<s:property value="dataAvailableForColumn(2)"/>'; // SWDown
		graphVisible0[1] = '<s:property value="dataAvailableForColumn(6)"/>';  // AirTemp
		//graphVisible[1] = true;
		var graphVisible1 = new Array();
		graphVisible1[9] = '<s:property value="dataAvailableForColumn(22)"/>'; // SWUp
		graphVisible1[12] = '<s:property value="dataAvailableForColumn(28)"/>'; // NEE

		function showGraph(chartId,gid)
		{
			if( chartId == 0 )
			{
				if( graphVisible0[gid] )
				{
	                hideGraph(0,gid);
	                graphVisible0[gid] = false;
	                $('.'+'value-'+chartId+'-'+gid).hide();
				}
				else
				{
	                displayGraph(0,gid);
	                graphVisible0[gid]=true;
	                $('.'+'value-'+chartId+'-'+gid).show();
				}
			}
			else if( chartId == 1 )
			{
				if( graphVisible1[gid] )
				{
	                hideGraph(1,gid);
	                graphVisible1[gid] = false;
	                $('.'+'value-'+chartId+'-'+gid).hide();
				}
				else
				{
	                displayGraph(1,gid);
	                graphVisible1[gid]=true;
	                $('.'+'value-'+chartId+'-'+gid).show();
				}
			}
		}

		function hideGraph(chartId,gid)
		{
			var cssId = "#show-button-" + gid;
            flashMovie.hideGraph(chartId,gid);
            $(cssId).addClass("white-background");
		}

		function displayGraph(chartId,gid)
		{
			var cssId = "#show-button-" + gid;
			flashMovie.showGraph(chartId,gid);
			$(cssId).removeClass("white-background");
		}

		var down = false;

		$(document).ready(function(){
			var pageDate = firstDate.substring(0,3) + firstDate.substring(6);
			var cal1 = new YAHOO.widget.Calendar("cal1Container",
					{
				        navigator: true,
				        pagedate: pageDate,
				        mindate: firstDate,
				        maxdate: lastDate
				    }
			);
			cal1.selectEvent.subscribe(dateSelected);
			cal1.render();

			//$('#slider-scale').click(function(e){
			//	$('#shutter').css('left',e.pageX-80);
			//});
		});

        var calendarVisible = false;
		
		function showCalendar()
		{
            if( calendarVisible ) 
            {
                $('#cal1Container').hide();
                calendarVisible = false;
            }
            else
            {
                $('#cal1Container').show();
                calendarVisible = true;
            }
		}

		function dateSelected(type,args,me)
		{
            //alert(args[0]);
            fileName = '/DataSet/DataSetAsFile.action?id=' + dataSetId +
                "&startDate=" + args[0];
            //alert(fileName);
	        flashMovie.setParam('data_sets.data_set[0].file_name', fileName);
	        flashMovie.showAll();
            showCalendar();
		}
		
		// ]]>
	</script>
<!-- end of amstock script -->
</div>
<div class="chart-right">
<div id="data-set-selector">
<s:select id="data-set-id" name="id" list="dataSets" listKey="id" listValue="name"/>
<a class="pbut-link" href="javascript:void(0)" onclick="showDataSet()">Go</a>
</div>

<hr />
<h3>Chart 1 left y-axis</h3>
<s:if test="dataAvailableForColumn(2)"><div class="graph-selector"><div class="show-button show-button-0" id="show-button-0" onclick="showGraph(0,0);"></div><div class="show-button-label">SWDown W/m2</div><div id="close-value" class="value-0-0 hover-value"></div></div></s:if>
<s:if test="dataAvailableForColumn(4)"><div class="graph-selector"><div class="show-button show-button-14 white-background" id="show-button-14" onclick="showGraph(0,14);"></div><div class="show-button-label">LWDown</div><div id="LWDown-value" class="value-0-14 hover-value hide"></div></div></s:if>
<s:if test="dataAvailableForColumn(20)"><div class="graph-selector"><div class="show-button show-button-8 white-background" id="show-button-8" onclick="showGraph(0,8);"></div><div class="show-button-label">RNet W/m2</div><div id="RNet-value" class="value-0-8 hover-value hide"></div></div></s:if>

<hr />
<h3>Chart 1 right y-axis</h3>
<s:if test="dataAvailableForColumn(6)"><div class="graph-selector"><div class="show-button show-button-1" id="show-button-1" onclick="showGraph(0,1);"></div><div class="show-button-label">Air Temp C</div><div id="AirTemp-value" class="value-0-1 hover-value"></div></div></s:if>
<s:if test="dataAvailableForColumn(8)"><div class="graph-selector"><div class="show-button show-button-2 white-background" id="show-button-2" onclick="showGraph(0,2);"></div><div class="show-button-label">Relative Humidity</div><div id="RelHumidity-value" class="value-0-2 hover-value hide"></div></div></s:if>
<s:if test="dataAvailableForColumn(10)"><div class="graph-selector"><div class="show-button show-button-3 white-background" id="show-button-3" onclick="showGraph(0,3);"></div><div class="show-button-label">Windspeed m/s</div><div id="Windspeed-value" class="value-0-3 hover-value hide"></div></div></s:if>
<s:if test="dataAvailableForColumn(12)"><div class="graph-selector"><div class="show-button show-button-4 white-background" id="show-button-4" onclick="showGraph(0,4);"></div><div class="show-button-label">Rainfall mm</div><div id="Rainfall-value" class="value-0-4 hover-value hide"></div></div></s:if>
<s:if test="dataAvailableForColumn(14)"><div class="graph-selector"><div class="show-button show-button-5 white-background" id="show-button-5" onclick="showGraph(0,5);"></div><div class="show-button-label">Snowfall mm</div><div id="Snowfall-value" class="value-0-5 hover-value hide"></div></div></s:if>

<hr />
<h3>Chart 2 left y-axis</h3>
<s:if test="dataAvailableForColumn(22)"><div class="graph-selector"><div class="show-button show-button-9" id="show-button-9" onclick="showGraph(1,9);"></div><div class="show-button-label">SWUp W/m2</div><div id="SWUp-value" class="value-1-9 hover-value"></div></div></s:if>
<s:if test="dataAvailableForColumn(24)"><div class="graph-selector"><div class="show-button show-button-10 white-background" id="show-button-10" onclick="showGraph(1,10);"></div><div class="show-button-label">Latent Heat W/m2</div><div id="LatentHeat-value" class="value-1-10 hover-value hide"></div></div></s:if>
<s:if test="dataAvailableForColumn(26)"><div class="graph-selector"><div class="show-button show-button-11 white-background" id="show-button-11" onclick="showGraph(1,11);"></div><div class="show-button-label">Sensible Heat W/m2</div><div id="SensibleHeat-value" class="value-1-11 hover-value hide"></div></div></s:if>
<s:if test="dataAvailableForColumn(30)"><div class="graph-selector"><div class="show-button show-button-13 white-background" id="show-button-13" onclick="showGraph(1,13);"></div><div class="show-button-label">Ground Heat W/m2</div><div id="SH-value" class="value-1-13 hover-value hide"></div></div></s:if>
<!-- 
<div class="graph-selector"><div class="show-button show-button-6 white-background" id="show-button-6" onclick="showGraph(6);"></div>Air Pressure mb</div>
<div class="graph-selector"><div class="show-button show-button-7 white-background" id="show-button-7" onclick="showGraph(7);"></div>CO2 ppmv</div>
 -->
 
<hr />
<h3>Chart 2 right y-axis</h3>
<s:if test="dataAvailableForColumn(28)"><div class="graph-selector"><div class="show-button show-button-12" id="show-button-12" onclick="showGraph(1,12);"></div><div class="show-button-label">NEE umol m-2 s-2</div><div id="NEE-value" class="value-1-12 hover-value"></div></s:if>
 
</div>
</div>
<s:include value="../Footer.jsp"></s:include>
</body>
</html>
