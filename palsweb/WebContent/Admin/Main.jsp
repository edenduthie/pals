<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="../LSMEval.css" rel="stylesheet" />
	</head>

	<body>
	
	<h4>PALS Admin</h4>
	
	<ul>
	<li><a href="NewUser.action">Create New User</a></li>
	<li><a href="StartScheduler.action">Start Scheduler</a></li>
	<li><a href="StopScheduler.action">Stop Scheduler</a></li>
	<li><a href="ReRun.action">ReRun Analysis</a></li>
	<li><a href="GenerateBenchmarks.action">Generate Benchmarks</a></li>
	</ul>
	
	
	</body>
	
</html>