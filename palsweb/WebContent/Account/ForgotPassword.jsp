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
<div class="forgot-password-width">
<h3>Forgot Your Password?</h3>
<s:form action="ForgotPassword_reset">
<p>Enter your username, and we will send a new temporary password to your email address on file.</p>
<p><s:property value="message" /></p>
<s:textfield name="username" label="username"></s:textfield>
<div id="reset-password-submit"><s:submit value="Reset Password"></s:submit></div>
</s:form>
</div>
</div>
</body>
</html>