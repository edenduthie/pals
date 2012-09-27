<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    
    <s:if test="refresh()">
        <meta http-equiv="refresh" content="10">
    </s:if>
    
    <s:head />
	<link type="text/css" href="../pals.css" rel="stylesheet" />
	</head>

	<body>
	
	<s:include value="../Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include> 

	<script language="Javascript1.2">
	document.getElementById("mbModelOutputs").setAttribute("class","mbON");
	</script>

	<div class="main">
	
	<s:if test="message!=null" >
	<s:if test="complete()">
	    <div id="message">The plots have been generated successfully.</div>
	</s:if>
	<s:else>
	    <div id="message"><s:property value="message" /></div>
	</s:else>
	</s:if>

	<h2>Model Output: <s:property value="modelOutput.name"/></h2>

	<table cellpadding="1" width="100%">

	<a class="pbut-link" href='<%=request.getContextPath()%>/Analysis/DefaultPlots.action?filterModelOutputId=<s:property value="modelOutputId" />&filterModelId=<s:property value="modelOutput.model.id" />'>View Plots</a>
	
	</td>
	</tr>
	
	<tr><td colspan="2">
	<hr size=1 />
	</td></tr>
	
	
	<tr><td width="180"><b>Model:</b></td><td><s:property value="modelOutput.model.modelName" />.<s:property value="modelOutput.model.version" /></td></tr>
	
	<tr><td><b>Data Set:</b></td><td><s:property value="modelOutput.dataSetVersion.displayName" />
	</td>
	</tr><tr>
	<td><b>Date:</b></td><td><s:date name="modelOutput.uploadDate" format="dd MMM yyyy h:mm" />
	</td>
	</tr><tr>
	<td><b>Status:</b></td><td><s:property value="getText('status.'+modelOutput.status)" />
	</td>
	</tr><tr>
	<td><b>PALS id:</b></td><td><s:property value="modelOutputId" />
	</td>
	</tr><tr>
		<td><b>Owner:</b></td><td><s:property value="modelOutput.owner.fullName" />
	</td>
	</tr><tr>
	<td valign="top"><b>Parameter selection:</b></td><td><s:property value="modelOutput.parameterSelection" />
	</td>
	</tr><tr>
	<td valign="top"><b>State selection:</b></td><td><s:property value="modelOutput.stateSelection" />
	</td>
	</tr><tr>
	<td valign="top"><b>Comments:</b></td><td><s:property value="modelOutput.userComments" />
	</td>
	</tr>


	<tr><td colspan="2">
	<hr size=1 />
	</td></tr>

	
	<!-- netcdf file info extracted -->
	
	
	<tr><td  colspan="2">
	<b>NetCDF file global attributes:</b><br>
	<pre><s:property value="topOfFile"/></pre>
	</td></tr>
	
	<s:if test="modelOutput.owner.username == user.username">
	
	<tr><td colspan="2">
	<hr size=1 />
	</td></tr>
	
	<tr><td  colspan="2">
	<b>Ancillary Files:</b><br>
	<s:iterator value="modelOutput.files">
	<div><a class="ancillary-file-dl" href='<%=request.getContextPath()%>/DownloadFile?id=<s:property value="id" />'><s:property value="name" /></a></div>
	</s:iterator>
	<br>
	</td></tr>
	
	</s:if>
	
	&nbsp<br />
	</table>
	
	<!--  Analysis Run Errors	 -->
	
	<table border="1" cellpadding="6" cellspacing="3" class="mo" width="100%">
	
	<tr>
	<th class="mo">Analysis Type</th>
	<th class="mo">Status</th>
	</tr>
	
	<s:iterator value="analysisRuns">
		<tr>
			
			<td class="mo" width="30%"><b><s:property value="analysisType.name"/></b></td>

			<s:if test="hasError">
			<td class="mo" width="30%" colspan="3"><i><font
				class='status<s:property value="status"/>'> <s:property
				value="getText('status.'+status)" /> <s:property
				value="errorMessage" /> </font></i></td>
			</s:if>
			
			<s:else>
			<td class="mo" width="30%"><i><font
				class='status<s:property value="status"/>'> <s:property
				value="getText('status.'+status)" /> <s:property
				value="errorMessage" /> </font></i></td>
			
			</s:else>
			

		</tr>
	</s:iterator>

	</table>
	
	<br />
	
	
	</div>
	<s:include value="../../Footer.jsp"></s:include>
	</body>
	
</html>
