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
</head>
<body>
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	document.getElementById("smbDataSets").setAttribute("class","smbON");
	</script>

<div class="main">
<!-- saved from url=(0013)about:internet -->
<!-- amstock script-->
<h2>Dynamic Plots for Data Set <s:property value="dataSet.name"/></h2>
<div class="chart-left">
  <script type="text/javascript" src="../amstock/amstock/swfobject.js"></script>
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
			so.addVariable("settings_file", encodeURIComponent("amstock_settings.xml"));
			so.addVariable("preloader_color", "#999999");
			so.addVariable("chart_id", "chart1");
			so.write("flashcontent");
		}

		displayChart();

		var flashMovie;
		var dataSetId = '<s:property value="retrieveId()" />';
		var fileName = '/DataSet/DataSetAsFile.action?id=' + dataSetId;
		var settings = '';

		function amChartInited(chart_id)
		{
	        flashMovie = document.getElementById(chart_id);
	        flashMovie.setParam('data_sets.data_set[0].file_name', fileName);
	        //flashMovie.setParam('data_sets.data_set[1].file_name', fileName);
	        //flashMovie.setParam('charts.chart[1].hidden','true');
	        //flashMovie.removeChart(1);
	        //$('input').change(changeSelections);
	        removedUncheckedCharts();
	        flashMovie.showAll();
		}

		function removedUncheckedCharts()
		{
			var i = 0;
			if( !$('#sw-down').attr('checked') ) flashMovie.removeChart(0);
	        if( !$('#air-temp').attr('checked') ) flashMovie.removeChart(1);
	        if( !$('#rel-humidity').attr('checked') ) flashMovie.removeChart(2);
	        if( !$('#windspeed').attr('checked') ) flashMovie.removeChart(3);
	        if( !$('#rainfall').attr('checked') ) flashMovie.removeChart(4);
	        if( !$('#snowfall').attr('checked') ) flashMovie.removeChart(5);
	        if( !$('#air-pressure').attr('checked') ) flashMovie.removeChart(6);
	        if( !$('#co2').attr('checked') ) flashMovie.removeChart(7);
	        if( !$('#rnet').attr('checked') ) flashMovie.removeChart(8);
	        if( !$('#sw-up').attr('checked') ) flashMovie.removeChart(9);
	        if( !$('#latent-heat').attr('checked') ) flashMovie.removeChart(10);
	        if( !$('#sensible-heat').attr('checked') ) flashMovie.removeChart(11);
	        if( !$('#nee').attr('checked') ) flashMovie.removeChart(12);
	        if( !$('#sh').attr('checked') ) flashMovie.removeChart(13);
		}

		function resize()
		{
			var i = 0;
			if( $('#sw-down').attr('checked') ) ++i;
	        if( $('#air-temp').attr('checked') ) ++i;
	        if( $('#rel-humidity').attr('checked') ) ++i;
	        if( $('#windspeed').attr('checked') ) ++i;
	        if( $('#rainfall').attr('checked') ) ++i;
	        if( $('#snowfall').attr('checked') ) ++i;
	        if( $('#air-pressure').attr('checked') ) ++i;
	        if( $('#co2').attr('checked') ) ++i;
	        if( $('#rnet').attr('checked') ) ++i;
	        if( $('#sw-up').attr('checked') ) ++i;
	        if( $('#latent-heat').attr('checked') ) ++i;
	        if( $('#sensible-heat').attr('checked') ) ++i;
	        if( $('#nee').attr('checked') ) ++i;
	        if( $('#sh').attr('checked') ) ++i;
	        var size = i*200;
	        if( size < 500 ) size = 500;
	        $('#flashcontent').css('height',size);
		}

		function updateChart()
		{
            resize();
            changeSelections();
		}

		function changeSelections()
		{
			flashMovie.reloadSettings();
		}

		function amProcessCompleted(chart_id,process_name)
		{
			if( process_name == 'init' )
			{
				flashMovie.setParam('data_sets.data_set[0].file_name', fileName);
		        removedUncheckedCharts();
		        flashMovie.showAll();
			}
		}

		function showDataSet()
		{
	        var id = $('#data-set-id').val();
	        window.location.href = "DynamicPlots.action?id=" + id;
		}
		
		// ]]>
	</script>
<!-- end of amstock script -->
</div>
<div class="chart-right">
<div id="data-set-selector">
<s:select id="data-set-id" name="id" list="dataSets" listKey="id" listValue="name"/>
<a class="pbut-link" href="javascript:void(0)" onclick="showDataSet()">Show Data Set</a>
</div>
<input type="checkbox" name="sw-down" id="sw-down" checked>SWDown W/m2<br>
<input type="checkbox" name="air-temp" id="air-temp">Air Temp C<br>
<input type="checkbox" name="rel-humidity" id="rel-humidity">Relative Humidity %<br>
<input type="checkbox" name="windspeed" id="windspeed" >Windspeed m/s<br>
<input type="checkbox" name="rainfall" id="rainfall" >Rainfall mm<br>
<input type="checkbox" name="snowfall" id="snowfall" >Snowfall mm<br>
<input type="checkbox" name="air-pressure" id="air-pressure" >Air Pressure mb<br>
<input type="checkbox" name="co2" id="co2" >CO2 ppmv<br>
<input type="checkbox" name="rnet" id="rnet" >RNet W/m2<br>
<input type="checkbox" name="sw-up" id="sw-up" >SWUp W/m2<br>
<input type="checkbox" name="latent-heat" id="latent-heat" >Latent Heat W/m2<br>
<input type="checkbox" name="sensible-heat" id="sensible-heat" >Sensible Heat W/m2<br>
<input type="checkbox" name="nee" id="nee" >NEE umol m-2 s-2<br>
<input type="checkbox" name="sh" id="sh" >SH W/m2<br><br>
<a class="pbut-link" href="javascript:void(0)" onclick="updateChart();">Update Displayed Variables</a>
</div>
</div>
</body>
</html>
