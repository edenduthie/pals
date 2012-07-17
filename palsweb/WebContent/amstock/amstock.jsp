<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>amStock chart</title>
<script language="javascript" src="../js/jquery.min.js"></script>
</head>
<body>
<!-- saved from url=(0013)about:internet -->
<!-- amstock script-->
  <script type="text/javascript" src="../amstock/amstock/swfobject.js"></script>
	<div id="flashcontent">
		<strong>You need to upgrade your Flash Player</strong>
	</div>

	<script type="text/javascript">
		// <![CDATA[		
		var so = new SWFObject("../amstock/amstock/amstock.swf", "chart1", "800", "500", "8", "#FFFFFF");
		//so.addVariable("path", "/pals/amstock/amstock/");
		so.addVariable("path", "/pals");
		so.addVariable("settings_file", encodeURIComponent("../amstock/amstock/amstock_settings.xml"));
		so.addVariable("preloader_color", "#999999");
		so.addVariable("chart_id", "chart1");
//  so.addVariable("chart_settings", "");
//  so.addVariable("additional_chart_settings", "");
//  so.addVariable("loading_settings", "Loading settings");
//  so.addVariable("error_loading_file", "ERROR LOADING FILE: ");
		so.write("flashcontent");

		var flashMovie;
		var dataSetId = '<s:property value="id" />';
		var fileName = '/DataSet/DataSetAsFile.action?id=' + dataSetId;
		var settings = '';

		function amChartInited(chart_id)
		{
	        flashMovie = document.getElementById(chart_id);
	        flashMovie.setParam('data_sets.data_set[0].file_name', fileName);
	        //flashMovie.setParam('data_sets.data_set[1].file_name', fileName);
	        //flashMovie.setParam('charts.chart[1].hidden','true');
	        //flashMovie.removeChart(1);
		}
		// ]]>
	</script>
<!-- end of amstock script -->
</body>
</html>
