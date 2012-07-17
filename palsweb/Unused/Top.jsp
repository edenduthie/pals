<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="top">

<div class=toptitle><s:text name="modcheck.name"/></div>

<% String loggedInAs = request.getParameter("loggedInAs"); %>

<div class="topR"><i>
<% if (loggedInAs != null) { %>

	Logged in as <%=loggedInAs %>. <a href="<s:url action="User/Logout"/>">[Log out]</a>

<% }else { %>

	You are not logged in. <a href="<s:url action="Login!input"/>">[Log in]</a>

<% } %>
</i></div>

<table class=navbar BORDER=0 CELLSPACING=0 CELLPADDING=0 width=100%><tr>
	<td width=20  class="mb"> &nbsp; </td>
	<td width=100  class="mb"><a class="mb" href="<s:url action="Welcome"/>">Home</a></td>

<% if (loggedInAs != null) { %>

	<td width=100 class="mb"><a class="mb" href="<s:url action="User/ListDataSets"/>">Data Sets</a></td>

	<td width=100  class="mb"><a class="mb" href="<s:url action="User/ListModels"/>">Models</a></td>

	<td width=160 class="mb"><a class="mb" href="<s:url action="User/ListModelOutputs"/>">Model Outputs</a></td>

	<td width=100  class="mb"><a class="mb" href="<s:url action="User/Upload!input"/>">Upload</a></td>
<% } else { %>



<% } %>

	<td            class="mb"> &nbsp; </td>

</tr>
<tr height=2px><td colspan=30 class=white></td></tr>
</table>

</div>
