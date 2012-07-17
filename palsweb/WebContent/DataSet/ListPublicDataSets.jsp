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
	
	<div id="num-items-select">
	    <s:form action="ListPublicDataSets">
	       <s:select name="limit" label="Results Per Page" list="itemsPerPageOptions" />
	    </s:form>
	</div>
	
	<h2 class="list-heading" >Public Data Sets</h2>
	
	<table class="mo"	>
	<tr>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/ListPublicDataSets.action?sortColumn=<s:property value="@pals.service.DataSetVersionService@COLUMN_NAME" />&asc=<s:property value="!asc" />">Data Set</a></b></th>
		
		<th class="mo">Type</th>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/ListPublicDataSets.action?sortColumn=<s:property value="@pals.service.DataSetVersionService@COLUMN_COUNTRY" />&asc=<s:property value="!asc" />">Country</a></b></th>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/ListPublicDataSets.action?sortColumn=<s:property value="@pals.service.DataSetVersionService@COLUMN_VEG_TYPE" />&asc=<s:property value="!asc" />">Vegetation Type</a></b></th>
		<th class="mo"><b>Years</b></th>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/ListPublicDataSets.action?sortColumn=<s:property value="@pals.service.DataSetVersionService@COLUMN_CREATED_BY" />&asc=<s:property value="!asc" />">Created By</a></b></th>
		<th class="mo"><b>Plots</b></th>
		<th class="mo"><b>QC</b></th>
		<th class="mo"><b>Met</b></th>
		<th class="mo"><b>Flux</b></th>
		<th class="mo"><b>Original</b></th>
		<s:if test="user.isAdmin()">
		    <th class="mo">Actions</th>
		</s:if>
	</tr>
	<s:set name="username" value="user.username" />
	<s:iterator value="latestDataSetVersions">
		
		<tr>
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
	        <a class="moItalic" href='QCPlots.action?id=<s:property value="id" />'>View</a>
	    </td>
	    <td class="mo">
			<a class="moItalic" href='../DownloadDataSet_met.action?dataSetVersionId=<s:property value="id" />'>.nc</a>
		</td>
		<td class="mo">
			<a class="moItalic" href='../DownloadDataSet_flux.action?dataSetVersionId=<s:property value="id" />'>.nc</a>
		</td>
		<td class="mo">
			<a class="moItalic" href='../DownloadDataSet_original.action?dataSetVersionId=<s:property value="id" />'>.csv</a>
		</td>
		<s:if test="user.isAdmin()">
		    <td class="mo">
		        <a href='javascript:void(0)' onclick='confirmDelete(<s:property value="dataSet.id" />);return false;'>Delete</a>
		    </td>
		</s:if>
		
		
		</tr>
	</s:iterator>
	
	
	</table>
	
	<s:if test="hasNextPage">
	    <a alt="next page" title="next page" class="right-link link-content" href='<%=request.getContextPath()%>/DataSet/ListPublicDataSets.action?offset=<s:property value="nextOffset" />&sortColumn=<s:property value="sortColumn" />&asc=<s:property value="asc" />'><span id="next" class="top-pad">Next <s:property value="limit"/> Data Sets</span> <div id="right-arrow" class="right-arrow" alt="next page" title="next page"></div></a>
	</s:if>
	<s:if test="offset > 0">
	    <a class="left-link link-content" href='<%=request.getContextPath()%>/DataSet/ListPublicDataSets.action?offset=<s:property value="previousOffset" />&sortColumn=<s:property value="sortColumn" />&asc=<s:property value="asc" />'><div id="left-arrow" class="left-arrow" alt="previous page" title="previous page"></div> <span id="previous" class="top-pad">Previous <s:property value="limit"/> Data Sets</span></a>
	</s:if>
	
	<br />
	
	</div> 

	<s:include value="../Footer.jsp"></s:include>
	
	<script>

       var contextRoot = '<%=request.getContextPath()%>';
   
       function confirmDelete(id)
       {
    	   var answer = confirm("Are you sure? This will delete the data set, all its versions, and any associated model outputs.");
    		if (answer)
        	{
    			var url = contextRoot + '/DataSet/DataSetAction_delete.action?requestId=' + id;
    			window.location = url;
    		}
    		else
        	{
    		}
    		return false;
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
