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
	
	
	<s:include value="../User/Top.jsp"><s:param name="loggedInAs" value="user.username"/></s:include>
	
	<script>
	document.getElementById("mbDataSets").setAttribute("class","mbON");
	document.getElementById("smbPublicDataSets").setAttribute("class","smbON");
	</script>
	
	<div class="main">
	
	<h2 class="list-heading" >Import a Data Set</h2>
	<p>
	    You can copy Data Sets from the main PALS database into the current workspace. Select one or more
	    Data Sets from the list below and click on <a href="javascript:void(0)" onClick="submit();">Import</a> to copy these into the current 
	    workspace <s:property value="user.currentExperiment.name"/>.
	</p>
	<div id="num-items-select">
	    <s:form action="Import_form">
	       <s:select name="limit" label="Results Per Page" list="itemsPerPageOptions" />
	    </s:form>
	</div>
	<div id="import-button"><a class="pbut-link" href="javascript:void(0)" onClick="submit();">Import</a></div>
	<table class="mo"	>
	<tr>
	    <th class="mo"></th>
		<th class="mo"><b>Data Set</b></th>
		<th class="mo">Type</th>
		<th class="mo"><b>Country</b></th>
		<th class="mo"><b>Vegetation Type</b></th>
		<th class="mo"><b>Years</b></th>
		<th class="mo"><b>Created By</b></th>
		<th class="mo"><b>Plots</b></th>
		<th class="mo"><b>Met</b></th>
		<th class="mo"><b>Flux</b></th>
		<th class="mo"><b>Original</b></th>
	</tr>
	<s:set name="username" value="user.username" />
	<s:iterator value="latestDataSetVersions">
		
		<tr>
		<td class="mo"><input type="checkbox" id='<s:property value="dataSet.id" />' value='<s:property value="dataSet.id" />'></td>
		<td class="mo"><a class="moItalic" href='ViewDataSet_view.action?id=<s:property value="dataSet.id"/>'><s:property value="displayName"/></a></td>
		<td class="mo"><s:property value="dataSet.dataSetType"/></td>
		<td class="mo"><s:property value="dataSet.country.name"/></td>
		<td class="mo"><s:property value="dataSet.vegType.vegetationType" /></td>
		<td class="mo"><s:date name="dataSet.latestVersion.startDate" format="yy" />-<s:date name="dataSet.latestVersion.endDate" format="yy" /></td>
		<td class="mo"><a href='../Account/Profile.action?username=<s:property value="dataSet.owner.username"></s:property>'><s:property value="dataSet.owner.fullName"/></a></td>
		<td class="mo">
		    <a class="moItalic" href='ListDataSetPlots.action?filterDataSetVersionId=<s:property value="id" />&filterDataSetId=<s:property value="dataSet.id"/>'>View Plots</a>
	    </td>
	    <td class="mo">
			<a class="moItalic" href='../DownloadDataSet_met.action?dataSetVersionId=<s:property value="id" />'>Met</a>
		</td>
		<td class="mo">
			<a class="moItalic" href='../DownloadDataSet_flux.action?dataSetVersionId=<s:property value="id" />'>Flux</a>
		</td>
		<td class="mo">
			<a class="moItalic" href='../DownloadDataSet_original.action?dataSetVersionId=<s:property value="id" />'>Original</a>
		</td>
		
		
		</tr>
	</s:iterator>
	
	
	</table>
	
	<s:if test="hasNextPage">
	    <a alt="next page" title="next page" class="right-link link-content" href='<%=request.getContextPath()%>/DataSet/Import_form.action?offset=<s:property value="nextOffset" />'><span id="next" class="top-pad">Next <s:property value="limit"/> Data Sets</span> <div id="right-arrow" class="right-arrow" alt="next page" title="next page"></div></a>
	</s:if>
	<s:if test="offset > 0">
	    <a class="left-link link-content" href='<%=request.getContextPath()%>/DataSet/Import_form.action?offset=<s:property value="previousOffset" />'><div id="left-arrow" class="left-arrow" alt="previous page" title="previous page"></div> <span id="previous" class="top-pad">Previous <s:property value="limit"/> Data Sets</span></a>
	</s:if>
	
	<br />
	
	</div> 
<form id="submit-form" action="Import_submit.action">
    <input type="hidden" id="dataSetIdList" name="dataSetIdList" />
</form>
	
<script language="JavaScript">
function submit()
{
	var list = "";
    $("input").each(function(){
        if( $(this).attr('checked') )
        {
        	value = $(this).val();
        	if( list.length > 0 ) list += ",";
        	list += value;
        }
    });
    if( list.length <= 0 )
    {
        alert("Please select one or more Data Sets to import");
    }
    else
    {
        $('#dataSetIdList').val(list);
        $('#submit-form').submit();
    }
}
$(document).ready(function(){
	$('#num-items-select form div br').remove();
	$('#num-items-select select').change(function(){
		$('#num-items-select form').submit();
	});
});
</script>
	</body>
	
</html>
