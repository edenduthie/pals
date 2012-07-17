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
	document.getElementById("smbDataSets").setAttribute("class","smbON");
	</script>
	
	<div class="main">
	
	<s:if test="message!=null">
	    <p><s:property value="message" /></p>
	</s:if>
	
	<h2>My Data Sets</h2>
	
	<div id="num-items-select">
	    <s:form action="DataSetAction_list">
	       <s:select name="limit" label="Results Per Page" list="itemsPerPageOptions" />
	    </s:form>
	</div>
	
	<form>
	<table><tr>
	<td>
	<a class="pbut-link" href="<s:url action="../DataSet/DataSetAction_add.action"/>">Create New Data Set</a>
	</td><td>
	<div ><a class="pbut-link"href="<s:url action="ListDataSetTemplates"/>">Download Data Set Templates</a></div>
	</td>
	<td>&nbsp;
	<i><a class="blue" href="../User/Help/Help.action?page=help-data-uploading-instructions">How to upload your data...</a></i>
	</td>
	</tr></table>
	</form>
	
	<br />
	
	<s:if test="myDataSets.size > 0">
	
	<table class="mo"	>
	<tr>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/DataSetAction_list.action?sortColumn=<s:property value="@pals.service.DataSetService@COLUMN_NAME" />&asc=<s:property value="!asc" />">Data Set</a></b></th>
		<th class="mo"><b>Type</b></th>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/DataSetAction_list.action?sortColumn=<s:property value="@pals.service.DataSetService@COLUMN_COUNTRY" />&asc=<s:property value="!asc" />">Country</a></b></th>
		<th class="mo"><b><a href="<%=request.getContextPath()%>/DataSet/DataSetAction_list.action?sortColumn=<s:property value="@pals.service.DataSetService@COLUMN_VEG_TYPE" />&asc=<s:property value="!asc" />">Vegetation Type</a></b></th>
		<th class="mo"><b>Years</b></th>
		<th class="mo"><b>Latest Version</b></th>
		<th class="mo"><b>Downloads</b></th>
		<th class="mo"><b>Plots</b></th>
		<th class="mo"><b>QC</b></th>
		<th class="mo"><b>&nbsp;</b></th>
	</tr>
	<s:set name="username" value="user.username" />
	<s:iterator value="myDataSets">
		
		<tr>
		<td class="mo"><a href='ViewDataSet_view.action?id=<s:property value="id"/>' ><s:property value="name"/></a></td>
		<td class="mo"><s:property value="dataSetType"/></td>
		<td class="mo"><s:property value="country.name"/></td>
		<td class="mo"><s:property value="vegType.vegetationType" /></td>
		<td class="mo"><s:date name="latestVersion.startDate" format="yy" />-<s:date name="latestVersion.endDate" format="yy" /></td>
		<td class="mo"><s:property value="latestVersion.name" /></td>
		<td class="mo"><s:property value="downloadCount" /></td>
		<td class="mo">
		    <s:if test="latestVersion != null"><a class="moItalic" href='ListDataSetPlots.action?filterDataSetId=<s:property value="id"/>&filterDataSetVersionId=<s:property value="latestVersion.id" />'>View Plots</a></s:if>
	    </td>
	    <td class="mo">
	        <s:if test="latestVersion != null"><a class="moItalic" href='QCPlots.action?id=<s:property value="latestVersion.id" />'>View</a></s:if>
	    </td>
		<td class="mo">
			<a href='UploadDataSetVersion_upload.action?dataSetId=<s:property value="id" />'>Upload New Version</a> |
			<a href='DataSetAction_edit.action?requestId=<s:property value="id" />'>Edit</a>
			<s:if test="user.isAdmin()">
			    | <a href='javascript:void(0)' onclick="confirmDelete(<s:property value="id" />);return false;">Delete</a>
			</s:if>
		<s:else> &nbsp; </s:else>
		</td>
		
		</tr>
	</s:iterator>
	
	</table>
	
	<s:if test="hasNextPage">
	    <a alt="next page" title="next page" class="right-link link-content" href='<%=request.getContextPath()%>/DataSet/DataSetAction_list.action?offset=<s:property value="nextOffset" />&sortColumn=<s:property value="sortColumn" />&asc=<s:property value="asc" />'><span id="next" class="top-pad">Next <s:property value="limit"/> Data Sets</span> <div id="right-arrow" class="right-arrow" alt="next page" title="next page"></div></a>
	</s:if>
	<s:if test="offset > 0">
	    <a class="left-link link-content" href='<%=request.getContextPath()%>/DataSet/DataSetAction_list.action?offset=<s:property value="previousOffset" />&sortColumn=<s:property value="sortColumn" />&asc=<s:property value="asc" />'><div id="left-arrow" class="left-arrow" alt="previous page" title="previous page"></div> <span id="previous" class="top-pad">Previous <s:property value="limit"/> Data Sets</span></a>
	</s:if>
	
	</s:if>
	
	<br />
	
	
	</div>
	
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
   
   <s:include value="../Footer.jsp"></s:include>
   
	</body>
	
</html>
