<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script language="javascript" src="/pals/js/jquery.min.js"></script>
<script language="javascript" src="/pals/pals.js"></script>
<script langauge="javscript" src="/pals/js/sessvars.js"></script>

<s:if test="user != null">
<s:set name="DataSetsTabURL" value="%{webAppURLBase+'DataSet/ListPublicDataSets.action'}" />	
<s:set name="ModelsTabURL" value="%{webAppURLBase+'User/NewModelAction_list.action'}" />
<s:set name="ModelOutputsTabURL" value="%{webAppURLBase+'User/ListModelOutputs.action'}" />
<s:set name="ExperimentsTabURL" value="%{webAppURLBase+'Experiment/Experiment_list.action'}" />
<s:set name="HelpTabURL" value="%{webAppURLBase+'User/Help/Help.action'}" />
<s:set name="AnalysisTabURL" value="%{webAppURLBase+'Analysis/DefaultPlots.action'}" />
</s:if>

<div id="full-header">
<div class='top <s:if test="user.currentExperiment != null">top-experiment</s:if>'>
<div class="block-display">
<% String loggedInAs = request.getParameter("loggedInAs"); %>
<table border=0 cellspacing=0 cellpadding=0 width="100%"><tr>
<td NOWRAP>
	
	<div class="toptitle">
	<font class="PALS">PALS</font>: Protocol for the Analysis of Land Surface models
	</div>

</td><td align="right" valign="top">

	<div class="loggedin">
	
	<s:if test="user != null">
	    Welcome <a href="/pals/Account/AccountForm.action"><s:property value="user.username" /></a>&nbsp;
	    <a href="/pals/User/Logout.action">[Log out]</a>
	</s:if>
	<s:else>
	    <a href="/pals/Welcome.action">[Log in]</a>
	    &nbsp;<a href="/pals/Account/RegisterForm.action">[Register]</a>
	</s:else>
	&nbsp;<a href="/pals/Welcome.action">[PALS Home]</a>
	<s:if test="user != null">
	    &nbsp;<a href="/pals/User/Help/Help.action">[Help]</a>
	</s:if>
	<s:if test="user == null ">
	    <br><div id="forgot-password"><a href="/pals/Account/ForgotPassword_form.action">Forgot your password?</a></div>
	</s:if>
	<!--<a id="pals-users-link" href="/pals/Account/ListUsers.action"><img alt="PALS Users" title="PALS Users" src="/pals/images/elvis.png"></img></a>
	-->
	</div>
	
	</td>
</tr>
<s:if test="user != null">
    <s:if test="user.currentExperiment != null">
    	<tr id="experiment-row">
		    <td>Currently showing workspace: <a href='<s:property value="#ExperimentsTabURL" />'><strong><s:property value="user.currentExperiment.name"></s:property></strong></a></td>
		    <td id="exit-experiment"><a href="/pals/Experiment/Experiment_close.action">[exit this workspace]</a></td>
		</tr>
    </s:if>
    <s:else>
		<tr id="experiment-row">
		    <td>Currently showing all public data. Alternatively enter a PALS <a href='<s:property value="#ExperimentsTabURL" />'>workspace.</a></td>
		</tr>
	</s:else>
</s:if>
</table>
</div>

</div>
<s:if test="user != null">
<div id="menu">
<ul id="jsddm">
    <li>
        <a id="mbDataSets"  class="mb" href="<s:property value="#DataSetsTabURL" />">Data Sets</a>
        <ul>
            <li><a id="smbPublicDataSets" href="/pals/DataSet/ListPublicDataSets.action">Public Data Sets</a></li>
            <li><a id="smbDataSets" href="/pals/DataSet/DataSetAction_list.action">My Data Sets</a></li>
            <li><a id="smbDataSets" href="/pals/DataSet/DataSetAction_add.action">Create</a></li>
            <s:if test="user.currentExperiment.owner.username == user.username">
                <li><a id="smbDataSets" href="/pals/DataSet/Import_form.action">Import</a></li>
            </s:if>
            <!-- 
            <li><a id="smbDataSets" href="/pals/DataSet/ListDataSetPlots.action">Plots</a></li>
            <li><a id="smbDataSets" href="/pals/DataSet/DynamicPlotsSingleChart.action">Dynamic Plots</a></li>
             -->
        </ul>
    </li>
    <li>
        <a id="mbModels"  class="mb" href="<s:property value="#ModelsTabURL" />">Models</a>
        <ul>
            <li><a id="smbPublicDataSets" href="<s:property value="#ModelsTabURL" />">All Models</a></li>
            <li><a id="smbPublicDataSets" href="<%=request.getContextPath()%>/User/NewModelAction_my.action">My Models</a></li>
            <li><a id="smbDataSets" href="/pals/User/NewModelAction_show.action">Create</a></li>
        </ul>
    </li>
    <li>
        <a id="mbModelOutputs" class="mb" href="<s:property value="#ModelOutputsTabURL" />">Model Outputs</a>
        <ul>
            <li><a id="smbPublicModelOutputs" class="mb" href="/pals/User/ListPublicModelOutputs.action">Public Model Outputs</a></li>
            <li><a id="smbModelOutputs" class="mb" href="/pals/User/ListModelOutputs.action">My Model Outputs</a></li>
            <li><a id="smbModelOutputs" class="mb" href="/pals/User/ListDataSetOwnerModelOutputs.action">Using My Data Sets</a></li>
            <li><a id="smbModelOutputs" class="mb" href="/pals/User/UploadModelOutput!input.action">Upload</a></li>
            <!-- 
            <li><a id="smbModelOutputPlots" class="mb" href="/pals/User/ListModelOutputPlots.action">Plots</a></li>  
            <li><a id="smbModelOutputs" href="/pals/User/ModelOutputDynamicPlots.action">Dynamic Plots</a></li>      
             -->
        </ul>
    </li>
    <li>
        <a id="mbAnalysis" class="mb" href="<s:property value="#AnalysisTabURL" />">Analysis</a>
        <ul>
            <li><a id="smbDataSets" href="/pals/DataSet/ListDataSetPlots.action">Of Data Sets</a></li>
            <li><a id="smbDefaultPlots" class="mb" href="/pals/Analysis/DefaultPlots.action">Of Model Outputs</a></li>
            <!-- <li><a id="smbDynamicPlots" class="mb" href="/pals/Analysis/DynamicPlots.action">Dynamic Plots</a></li> -->   
        </ul>
    </li>
    <!--  
    <li>
        <a id="mbExperiments" class="mb" href="<s:property value="#ExperimentsTabURL" />">Experiments</a>
        <ul>
            <li><a class="smbExperiments" href="<s:property value="#ExperimentsTabURL" />">My Experiments</a></li>
            <li><a class="smbExperiments" href="/pals/Experiment/Experiment_list_shared.action">Shared Experiments</a></li>
            <li><a class="smbExperiments" href="/pals/Experiment/Experiment_form.action">Create</a></li>
            <li><a class="smbExperiments" href="/pals/Experiment/Experiment_close.action">Close</a></li>
        </ul>
    </li>
    -->
    <!--
    <li>
        <a id="mbHelp" class="mb" href="<s:property value="#HelpTabURL" />">Help</a>
    </li>
    -->
    <!--  
    <li>
        <s:if test="user.currentExperiment != null">
            <div id="current-experiment">Current Experiment: <span id="experiment-name">
                <s:property value="user.currentExperiment.name"></s:property></span>
            </div>
        </s:if>
    </li>
    -->
</ul>
</div>
<div class="float-clear"></div>
</s:if>
</div>

