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
	
	<s:action name="Top" executeResult="true"></s:action>
	 
	<script>
	document.getElementById("mbModels").setAttribute("class","mbON");
	</script> 
	
	 <div class="main">
	 
	 <s:if test="message!=null">
	     <p><s:property value="message" /></p>
	 </s:if>
	 
	 <h2><s:if test="myModels">My</s:if><s:else>All</s:else> Models</h2>
	
	<a class="pbut-link"href='<s:url action="NewModelAction_show.action"/>'>Create New Model</a>
	
	<p>&nbsp;</p>
	
    <s:set name="admin" value="%{user.isAdmin()}"/>
   
   <table class="t2">
	<tr>
		<th class="mo"><b><s:text name="name.text"/></b></th>
		<th class="mo">&nbsp;</th>
		<th class="mo"><b><s:text name="version.text"/></b></th>
		<th class="mo"><b>Created By</b></th>
		<th class="mo"><b><s:text name="createdDate.text"/></b></th>
		<s:if test="#admin">
		    <th class="mo">Actions</th>
		</s:if>
	</tr>
	
	<s:iterator value="models">
		<tr>
		<td class="mo"><a href='<%=request.getContextPath()%>/User/ModelAction_view.action?modelId=<s:property value="id" />'><s:property value="modelName"/></a></td>
		<td class="mo"><a class="moItalic" href='ListModelOutputPlots.action?filterModelId=<s:property value="id" />'>View Model Output Plots</a></td>
		<td class="mo"><s:property value="version"/></td>
		<td class="mo"><s:property value="user.fullName"/></td>
		<td class="mo"><s:date name="createdDate" format="dd MMM yyyy hh:mm"/></td>
		<s:if test="#admin">
		    <td class="mo"><a href='javascript:void(0)' onclick="confirmDelete(<s:property value="id" />);return false;">Delete</a></th>
		</s:if>
		</tr>
	</s:iterator>
	
	</table>
	

   
   </div>
   <script>

       var contextRoot = '<%=request.getContextPath()%>';
   
       function confirmDelete(id)
       {
    	   var answer = confirm("Are you sure? This will delete the model and any associated model outputs.");
    		if (answer)
        	{
    			var url = contextRoot + '/User/ModelAction_delete.action?modelId=' + id;
    			window.location = url;
    		}
    		else
        	{
    		}
    		return false;
       }
   </script>
   <s:include value="../Footer.jsp"></s:include>
	</body>
	
</html>
