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
	
	<form><input onclick="javascript:history.back()" type="button" class="pbut" value="< < Back"/></form>
	
	
	<table width="100%" cellspacing="4"><tr>
	
	<s:iterator value="analysisRuns" status="itStatus">
	
	<td width="50%" class="pm">
	<i>&nbsp; <s:property value="#itStatus.count" />
	<b>Model:</b> <s:property value="modelOutput.model.modelName" />,
	<b>Model Output:</b> <s:property value="modelOutput.modelOutputName" />,
	<b>Analysis:</b> <s:property value="analysis.analysisName" /> <s:property value="analysis.analysisVariableName" />
	</i>
	<br>
	<img border="0" src='AnalysisRunPNG.action?analysisRunId=<s:property value="analysisRunId" />'   width="100%" />
	</td>
	
	<s:if test="#itStatus.count % 2 == 0">
	</tr><tr>
	</s:if>

	</s:iterator>
	
		</tr></table>
	

<br>&nbsp;
<br>&nbsp;
<br>&nbsp;
<br>&nbsp;
<br>&nbsp;


   
	</body>
	
</html>