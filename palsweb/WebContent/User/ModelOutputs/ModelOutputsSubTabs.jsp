<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="subtabs">

<table id="submenubar" class="submenubar" border="0" cellspacing="0" cellpadding="0" width="100%"><tr>
	<td width="50"  class="mb"> &nbsp; </td>

	<td id="smbPublicModelOutputs" width="160"  class="mb" onClick="document.location.href='ListPublicModelOutputs.action';"><a class="smb" href="<s:url action="ListPublicModelOutputs"/>">Public Model Outputs</a></td>

	<td id="smbModelOutputs" width="130" class="mb" onClick="document.location.href='ListModelOutputs.action';" ><a class="smb" href="<s:url action="ListModelOutputs"/>">My Model Outputs</a></td>
	
	<td id="smbModelOutputPlots" width="80" class="mb" onClick="document.location.href='ListModelOutputPlots.action';" ><a class="smb" href="<s:url action="ListModelOutputPlots"/>">Plots</a></td>
	

	<td            class="mbLast">   &nbsp; </td>

</tr>

</table>

</div>