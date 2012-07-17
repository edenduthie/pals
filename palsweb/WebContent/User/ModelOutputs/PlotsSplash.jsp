<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<html>

	<head>

    <title><s:text name="modcheck.name"/></title>

    <s:head />

	<link type="text/css" href="../pals.css" rel="stylesheet" />

	</head>

	<body>
	
	<s:include value="../Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include>

	<script language="Javascript1.2">
	document.getElementById("mbModelOutputs").setAttribute("class","mbON");
	document.getElementById("smbModelOutputPlots").setAttribute("class","smbON");
	</script>

	<div class="main">
	
	<p class="nav">/ Model Outputs / Plots /</p>
	
	<script type="text/javascript" src="../pals.js"></script>
	
	&nbsp;<br />
	
	<!-- Analysis List -->
	<table>
	
	<tr><th class="shade">Please select an analysis type to display plots...</th></tr>
	
	<tr><td class="shade" width="50%">
			<a href='ListModelOutputPlots.action?filterAnalysisName=Diurnal Cycle'><img src='../images/DiurnalCycle.png' border="0" /></a>
			<br />
			<a href='ListModelOutputPlots.action?filterAnalysisName=Diurnal Cycle'>Diurnal Cycle</a> 
			<a class='small' href='#'>(About...)</a>
			
			
	
			<td class="shade" width="50%">

			<a href='ListModelOutputPlots.action?filterAnalysisName=Averaging Window'><img src='../images/AveragingWindow.png' border="0" /></a>
			<br />
			<a href='ListModelOutputPlots.action?filterAnalysisName=Averaging Window'>Averaging Window</a> 
			<a class='small' href='#'>(About...)</a>
			</tr>
			
	
			<tr><td class="shade">
			<a href='ListModelOutputPlots.action?filterAnalysisName=Smoothed Evaporative Fraction'><img src='../images/SmoothedEvaporativeFraction.png' border="0" /></a>
			<br />

			<a href='ListModelOutputPlots.action?filterAnalysisName=Smoothed Evaporative Fraction'>Smoothed Evaporative Fraction</a> 
			<a class='small' href='#'>(About...)</a>
			
			
	
			<td class="shade">
			<a href='ListModelOutputPlots.action?filterAnalysisName=Taylor'><img src='../images/Taylor.png' border="0" /></a>
			<br />
			<a href='ListModelOutputPlots.action?filterAnalysisName=Taylor'>Taylor</a> 
			<a class='small' href='#'>(About...)</a>

			</tr>
			
	
			<tr><td class="shade">
			<a href='ListModelOutputPlots.action?filterAnalysisName=Annual Cycle'><img src='../images/AnnualCycle.png' border="0" /></a>
			<br />
			<a href='ListModelOutputPlots.action?filterAnalysisName=Annual Cycle'>Annual Cycle</a> 
			<a class='small' href='#'>(About...)</a>
			
			<td class="shade">
			<a href='ListModelOutputPlots.action'><img src='../images/DiurnalCycle.png' border="0" width="200" height="111" /></a>
			<br />
			<a href='ListModelOutputPlots.action'>All Plots</a> 
			
			</tr>
			
	

	
	</table>	
	
	
	
	
	

	
	  
   	</div>
   
	</body>
	
</html>
