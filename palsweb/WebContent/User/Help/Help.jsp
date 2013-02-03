<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%><html>
<head>
<title><s:text name="modcheck.name" /></title>
<s:head />
<link type="text/css" href="../../pals.css" rel="stylesheet" />
<script language="javascript" src="../js/jquery.min.js"></script>
</head>

<body>
<input type="hidden" id="page" value='<s:property value="page" />' ></input>

<s:action name="Top" executeResult="true"></s:action>

<!-- 
<script>
	document.getElementById("mbHelp").setAttribute("class","mbON");
</script>
 -->
<div class="main">

<div id="left-help-column">
<h2>Help</h2>

<ul>
    <li><a href="javascript:void(0);" onclick="select('help-contact')">Contact</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-disclaimer')">Terms and Conditions</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-suggestions')">Suggestions / Improvements</a></li>
    <li><a href="<%=request.getContextPath()%>/Account/ListUsers.action">List of PALS users</a></li>
</ul>
<ul>
    <li><a href="javascript:void(0);" onclick="select('help-experiments')">What is a PALS Workspace?</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-benchmarking')">What is a PALS Benchmark?</a></li>
</ul>
<h3>Uploading model output</h3>
<ul>
    <li><a href="javascript:void(0);" onclick="select('help-lsm-format')">Model output file format</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-who-lsm')">Who has access to my data?</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-why-lsm')">Why use PALS with my LSM?</a></li>
</ul>
<h3>Uploading flux data:</h3>
<ul>
    <li><a href="javascript:void(0);" onclick="select('help-why-contribute')">Why contribute my data?</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-data-req')">Data requirements</a></li>
    <li><a href="javascript:void(0);" onclick="select('help-who-access')">Who has access to my data?</a></li>
	<li><a href="javascript:void(0);" onclick="select('help-data-uploading-instructions')">Data uploading instructions</a></li>
</ul>

</div>
<div id="help-contents">

<div id="help-contact" style="display:none;">
<h3>Contact</h3>
<p>
PALS is currently maintained at the <a href="http://www.ccrc.unsw.edu.au/">Climate Change Research Centre</a> at the University of New South Wales, Sydney, by <a href="http://web.science.unsw.edu.au/~gabrielabramowitz/homepage/Gab_Abramowitz_home_page.html">Gab Abramowitz</a> and <a href="http://www.gatorlogic.com">Eden Duthie</a>.
Contact us at <a href="mailto:palshelp@gmail.com">palshelp@gmail.com</a>.
</p>
<p>
Abramowitz, G. (2012) Towards a public, standardized, diagnostic benchmarking system for land surface models, <i>Geoscientific Model Development</i>, 5, 819-827, doi:10.5194/gmd-5-819-2012.
</p>
</div>


<div id="help-disclaimer" style="display:none;">
<h2>Terms and Conditions</h2>

<p>
Many data sets have been made available through the PALS site for use by users of the PALS site.  These data sets are only available for use within PALS unless otherwise agreed by the data set owner. The data set owner can be identified in the information pages for PALS Data Sets, Models and Model Outputs, usually accessed by clicking on the Data Set, Model or Model Output name where it is listed in PALS. Please also check these information pages for any additional data policies or licencing agreements associated with each data set that have been nominated by the data set owner, you are bound to adhere by them.
</p>
<p>
In uploading your own data to the PALS site, you are able to prescribe the policies that govern it, and we encourage you to do so. Please add this information to the Data Set, Model or Model Ouput "Comments" fields when you create or upload them. You can supplement comments with ancillary files, usually uploadable in the same form. It is your responsibility to ensure that you have appropriate rights to grant such a licence (your  organisation/institution/employer may have policies governing this).
</p>
<p>
It is the responsibility of individual users to ensure they adhere to any institutional guidelines by which they are bound regarding the dissemination of data, model code or experimental results.
</p>
<p>
For information regarding the use of PALS scripts or graphs for publications, please email palshelp at gmail dot com. 
</p>
<p>
Finally, while the administrators of PALS aim to make PALS as reliable and secure as possible, we take no responsibility whatsoever for any loss incurred as a result of the use of PALS, availability of the PALS site, faults in the structure or operation of PALS or inappropriate use of the PALS site by its users.
</p>
</div>
<div id="help-suggestions" style="display:none;">
<h2>Improvements and suggestions</h2>

<p>If you have suggestions about how PALS might be improved, please let us know. Please also bear in mind that we have very limited resources. In particular, if you would like to fund or personally build additional functionality for PALS we would love to try to facilitate it. </p>
<p>All analysis scripts in PALS are currently written in R (although some may eventually be in Python as well). The entire PALS code base including these is avilable as an open source project on <a href="https://github.com/edenduthie/pals">GitHub</a>. If you would like to know more or just get a copy of the analysis scripts please send us an <a href="mailto:palshelp@gmail.com">email</a>. </p>
</div>
<div id="help-why-contribute" style="display:none;">
<h2>Why would I contribute my flux data to PALS? 
</h2>
<p>
<strong>Free analysis</strong>. Each time you upload a version of your data set, PALS will run a set of analysis scripts on your data, producing a collection of graphs that you can view on PALS or download. This collection of analyses is expanding all the time, in fact we encourage you to submit additional types of analyses to run.
</p>
<p>
<strong>Access to model simulations</strong>. As a data provider for PALS, you will have access to at least one model simulation from each model that uses your data as a driver on PALS. You also of course also have access to all the PALS analyses that compare these model simulations to your flux data. As with analyses on data sets, all users are encouraged to submit additional analysis scripts.
</p>
<p>
<strong>Free data management</strong>. You are welcome to maintain as many versions of your data set as you'd like on PALS  - analyses will run on all uploaded versions. You can choose which of your data set versions will be available to other PALS users to drive model simulations. PALS will automatically version control all the data set versions you upload.
</p>
<p>
<strong>Raise your site's profile in the modelling community</strong>. PALS gives you the opportunity to have your data widely used in the land surface modelling community. It converts your flux data from a spreadsheet to a standardised netcdf format that is widely used in the modelling community. This aids collaboration both within PALS and externally.
</p>
<p>
<strong>Collaborate with modellers</strong>. PALS aims to both give model users a better understanding of the flux data they use and give data providers a better understanding of land surface modelling.
</p>
</div>
<div id="help-data-req" style="display:none;">
<h2>
Data requirements for PALS flux data
</h2>
<p>
<strong>PALS requires gap-filled meteorological and flux variables</strong>. PALS is aimed at land surface model simulation and evaluation. Land surface models require gap-filled meteorological data as driving data and many of the analyses on PALS requires a continuous time series of flux data for evaluation.
</p>
<p>
<strong>PALS will only accept data sets that are a whole number of years in length</strong>. Many land surface models rely on a repeated data set spin up period to stabilise their states. Part-year datasets mean that model states using this technique are less likely to be realistic.
</p>
<p>
<strong>PALS has a minimum required set of variables</strong> that will allow most land surface models to simulate a site. These are shaded in green in the PALS flux data template (see uploading instructions).
</p>
</div>
<div id="help-who-access" style="display:none;">
<h2>Who has access to my data?</h2>

<p>
Each version of your site data has one of two possible 'access labels':
</p>
<ul>
<li>The 'public' access label means that any PALS user will be able to download your data and can see the plots made from your data. No more than one version of your site data can public at once, and one version must always be public.</li>
<li>The 'private' access label, as the name suggests, only allows the data owner to view analyses derived from it or download the data. The private label is intended for the testing of dataset processing techniques, when results may be unpredictable.</li>
</ul>
</div>
<div id="help-data-uploading-instructions" style="display:none;">
<h2>How to upload your gap-filled flux tower data</h2>
<ol id="help-upload-list">
<li><strong>Create a data set profile</strong>. To do this, go to <span class="blue">Data Sets > Create</span> and fill out the online form detailing the characteristics of your site, references to it, and any additional meta data you feel might be important for modellers simulating the site. Please ensure the site coordinates are accurate, as these are used to download map data. Once this is done, click the "Create New Data Set" button. Your "My Data Sets" table in the "Data Sets" tab should now show this data set, but there will be no actual data associated with it yet.</li>
<li><strong>Download the flux tower data template file</strong>, available through <span class="blue">Data Sets > My Data Sets > "Download Data Set Templates"</span>.</li>
<li>
<strong>Populate this spreadsheet template</strong> with your data:
<ul id="help-upload-sublist">
	<li>Do not modify anything in the header section of the spreadsheet. PALS assumes that the data follows the variable order and units that are in this template.</li>
	<li>Variables in green shaded boxes are essential for use in land surface models (although only one of Rnet, latent heat flux, sensible heat flux, NEE or soil heat flux are required to qualify); variables in blue shaded boxes are optional (allowing a greater number of experiments to be performed using the site).</li>
	<li>All variables that you include need to be completely gap-filled, otherwise they will register as not being present.</li>
	<li>The missing value is '-9999' for integer variables and '-9999.0' for real variables. If this value is not used a variable may register as included in your data set and fail a test of expected ranges.</li>
	<li>Associated with each time-dependent variable is a FLAG variable that is intended to provide information about which time steps are gap-filled. Where the information is available, we ask that data providers use:  "0" => datum is original / measured / higher confidence;  "1" => datum is synthesised / gap-filled / lower confidence; "-9999" => source of datum is unknown.</li>
</ul>
</li>
<li><strong>Save the spreadsheet as a comma separated text file (.csv)</strong>. If you try to upload an OpenOffice or MSExcel formatted spreadsheet, PALS will reject the file.</li>
<li><strong>Upload your data filled template</strong> by clicking on the "Upload New Version" link in the "Upload" column of the My Data Sets table entry. For data set version updates, please describe the changes to your dataset and why they were necessary.</li>
<li><strong>Approve Quality Control graphs</strong>. After uploading, PALS will produce a set of quality control graphs. Please check that the ranges, maximum and minimum values, units and time series are as you would expect. This both tells PALS it has interpreted your data correctly and gives you an opportunity to eyeball your data for any obvious problems before submitting it as a version of your site data. If everything looks okay, scroll to the bottom of the page and approve your uploaded version.</li>
</ol>
<p><a class="blue" href="Help.action?page=help-data-uploading-instructions" target="_blank">Open these instructions in a new window</a></p>

<p>Feel free to suggest changes to the spreadsheet template structure or site characteristics web form by emailing <a href="mailto:palshelp@gmail.com">palshelp@gmail.com</a>.</p>

</div>
<div id="help-why-lsm" style="display:none;">
<h2>Why use PALS with my model output?</h2>
<p>
<strong>Free analysis</strong>. PALS automatically runs a wide variety of analyses on uploaded model output data and produces graphs that can be viewed on PALS or downloaded.This collection of analyses is expanding all the time, in fact we encourage you to submit additional types of analyses to run.
</p>
<p>
<strong>Access to datasets</strong>. PALS provides access to a variety datasets that useful for diagnostic evaluation of alined surface models. Many are actually maintained on PALS by the data providers, and include meta data to aid meaningful comparison.
</p>
<p>
<strong>Standardisation of model evaluation</strong>. PALS provides the ability for different research gropes to use the same standardised testing environment for their land surface models. It facilitates model comparison experiments in a wide range performance diagnostics.
</p>
<p>
<strong>A model development tool</strong>. As you develop your land surface model, it requires constant testing. PALS enables a free, fast and comprehensive testing procedure as you develop your model. You can maintain as many model simulations as you like on PALS - analyses will be performed immediately on upload.
</p>
<p>
<strong>Free experimental management to aid reproducibility</strong>. We also encourage users to upload the parameter sets, initial conditions datasets and even model code that generated their model output file (within reasonable file size constraints). These files are never shared with other users, regardless of the access level of the model output file. They are for the reference of the submitting user alone. The interface is designed to aid reproducibility of experiments.
</p>
<p>
<strong>Collaborate with the measurement community</strong>. PALS aims to both give model users a better understanding of the flux data they use and give data providers a better understanding of land surface modelling.
</p>
<p>
<strong>Raise your model's profile in the measurement community</strong>. PALS gives you the opportunity to have your model output data used in the measurement community, for example in exploring ecosystem dynamics or gap-filling data.
</p>
</div>
<div id="help-who-lsm" style="display:none;">
<h2>
Who has access to my model output data?
</h2>
<p>
Each uploaded model output file has one of three possible 'access labels':
</p>
<ul>
<li>The 'public' access label means that any PALS user can see the plots made from your model output, with your model and username identified. Only the experiment data provider will be able to download your actual model output data. Public model simulations will contribute to any PILPS-style model analysis at the front of the PALS website (when this feature is implemented), but individual models will not be identified.</li> 
<li>The 'available to data provider' (or 'provider') access label means that the experiment data provider will be able to download your model output data, can see your plots, as well as your model and username, but other users will not have any access. 'Provider' labelled model simulations will also contribute to any PILPS-style model analysis at the front of the PALS website (when this feature is implemented). </li>
<li>The 'private' access label, as the name suggests, only allows the model output owner to view analyses derived from it. The private label is intended for model development, when results may be unpredictable.</li>
</ul>
<p>
By default, all uploaded model outputs will have public access. While the access label for any model output file can be changed at any time, for each dataset/experiment you choose to use at least one of your uploaded model outputs must be flagged as having 'provider' or 'public' access. This means that if you have only one model simulation for a given dataset/experiment, you will not be able to change its access label to 'private'.
</p>
<p>
We strongly encourage users to label runs at the highest level of availability to other users - 'public' access. This will maximise constructive discussion between users and ultimately the drive the development of models. Please avoid using the 'private' access label unless you are genuinely engaged in model development or have been informed of legal restraints on your participation.
</p>
</div>

<div id="help-lsm-format" style="display:none;">
<h2>Which model output format is PALS expecting?</h2>
<p>
Currently PALS uses <a href="http://www.lmd.jussieu.fr/~polcher/ALMA/convention_output_3.html">ALMA format netcdf</a>, although we will endeavor to ensure CF compliant netcdf is read in future. PALS is also currently expecting that model output will be at the same time step as the provided driving data, although this too will change as soon as distributed experiments are launched. 
</p>
</div>

<div id="help-experiments" style="display:none">
<h2>What is a PALS Workspace?</h2>
<p>
A PALS Workspace is effectively a separate copy of PALS. All data associated with a Workspace is available only to users associated with that Workspace. 
</p>
<p>
One might use a Workspace, for example, as a model development benchmarking environment. A user might create a Workspace and only invite members of their model development group to the Workspace. In that case, only these users will be able to view the Data Sets, Experiments, Models, Model Outputs and Analyses that are part of this Workspace. The Workspace owner can choose import existing PALS Data Sets and Experiments from the main PALS database to that Workspace to establish a set of benchmark tests for their development program. All the usual Analyses associated with an Experiment will run privately within the Workspace.
</p>
<p>
Alternatively, a Workspace may be used as a way of partitioning all the Data Sets, Models and Model Outputs associated with a model comparison experiment. The Workspace could be made available to only a subset of PALS users (if, for example, some portion of the associated data sets could not be made public), or to all PALS users (the workspace is then effectively just a way of filtering the content of the main PALS database).
</p>
<p>
As a Workspace owner you can invite whichever PALS users you like to have access to your Workspace, and implicitly exclude others. We encourage anyone setting up a Workspace that isn't used for model development to use the "Share with all PALS users" option - this creates a separate database of all data associated with the Workspace, while still making this data, and participation in the Workspace, open to all PALS users. As a Workspace owner you are also the only Workspace participant who can import Data Sets from the main PALS database.
</p>
<p>
You can enter an existing Workspace or manage your own Workspaces by clicking on the highlighted "workspace" link in the PALS banner at the top of your screen.
</p>
</div>

<div id="help-benchmarking" style="display:none;">
<h2>What is a PALS Benchmark?</h2>
<p>
A benchmark in PALS is a time series that is used to compare with your model output. By default (and in the first stages of benchmark implementation) these benchmarks are generated using empirical models.
</p>
<p>
Empirical benchmark time series are predictions made by empirical models using out-of-sample data, at the same time step as a land surface model. For example, for a flux tower site simulation at site A, all other flux tower sites are used to define the parameters of a linear regression of latent heat flux against downward shortwave radiation. The parameters are then used to predict latent heist flux at site A, based solely on downward shortwave radiation. This particular empirical benchmark is labelled Emp1lin - a linear regression with 1 input. The three default empirical benchmarks are:
</p>
<ol id="help-benchmark-list">
<li>
Emp1lin - a linear regression of a particular flux against downward shortwave
</li>
<li>
Emp2lin - a multiple linear regression of a particular flux against downward shortwave and temperature.
</li>
<li>
Emp3km27 - an empirical model describing a particular flux as a function of 3 inputs:  downward shortwave, temperature and relative humidity.
</li>
</ol>
<p>
Where data is available, these empirical benchmarks are applied to Qle (latent heat flux), Qh (sensible heat flux), NEE (net ecosystem exchange of CO2) and Qg (ground heat flux). The Rnet empirical time series is defined as the sum of empirical predictions of Qle, Qh and Qg. These empirical benchmarks provide a model-like time series, which means that they can be used as a benchmark in any chosen metric.
</p>
<p>
An empirical benchmark is a valuable tool for quantitatively judging model performance. A model's apparently good correlation with observations may represent nothing more sophisticated than a linear response to sunlight - something that comparison with the Emp1lin benchmark would highlight. The three default empirical benchmarks of increasing complexity above therefore give a way of ranking model performance - they provide the ability to equate model performance to a level of empirical model sophistication.
</p>
<p>
The three default empirical benchmarks of increasing complexity above should show increasing performance at representative sites. Sites that do not show this behaviour can be interpreted as atypical - the nature of fluxes at these sites is somehow uncommon and is driven by other complicating factors. It is at sites like these where we hope that the benefits of a complex land surface model will be apparent.
</p>
<p>
All scalar skill scores are shown for both models and benchmarks.
</p>
<p>
In later releases of PALS, other model outputs (whether your own or those of other users) can be nominated as benchmark time series.
</p>
</div>

</div>

<script langauge="JavaScript">

currentSelected = "help-contact";

function select(id)
{
    $("#"+currentSelected).hide();
    $("#"+id).show();
    currentSelected = id;
}

$(document).ready(function(){ 
	strutsPage = $("#page").val();
	if( strutsPage && strutsPage.length > 0 ) currentSelected = strutsPage;
	select(currentSelected);
});

</script>
</body>
</html>
