<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="subtabs">

<table id="submenubar" class="submenubar" border="0" cellspacing="0" cellpadding="0" width="100%"><tr>
	<td width=50  class="mb"> &nbsp; </td>

	<td id="smbPublicDataSets" width=160  class="mb" onClick="document.location.href='ListPublicDataSets.action';"><a class="smb" href="<s:url action="ListPublicDataSets"/>">Public Data Sets</a></td>

	<td id="smbDataSets" width=120 class="mb" onClick="document.location.href='DataSetAction_list.action';" ><a class="smb" href="<s:url action="DataSetAction_list"/>">My Data Sets</a></td>
	
	<td id="smbDataSets" width=120 class="mb" onClick="document.location.href='ListDataSetPlots.action';" ><a class="smb" href="<s:url action="ListDataSetPlots"/>">Plots</a></td>
	

	<td            class="mbLast">   &nbsp; </td>

</tr>

</table>

</div>