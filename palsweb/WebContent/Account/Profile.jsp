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

<s:include value="/User/Top.jsp"></s:include>

<div class="main-width">
<div class="register-form-width">
<h3>PALS User Profile : <s:property  value="profileUser.fullName"/></h3>
    <div>
    <div id="profile-picture">
        <img src='/pals/User/PhotoDisplay.action?photoId=<s:property value="profileUser.photo.id" />' alt="Profile Picture" title="Profile Picture"></img>
    </div>
    <table>
    <tr><td><b>Username:</b></td><td><s:property value="profileUser.username" /></td></tr>
    <tr><td><b>Name:</b></td><td><s:property  value="profileUser.fullName"/></td></tr>
    <tr><td><b>Institution:</b></td><td><s:property value="profileUser.institution.name" /></td></tr>
    <s:if test="profileUser.showEmail">
        <tr><td><b>Email:</b></td><td><s:property  value="profileUser.email" /></td></tr>
    </s:if>
    </table>
    <div class="clear"></div>
    </div>
        <b>Research Interests:</b><br><s:property value="profileUser.researchInterest" />
</div><!--register-form-width -->
</div><!--main-width-->
</body>
</html>