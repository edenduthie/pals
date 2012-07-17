<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
	<head>
    <title><s:text name="modcheck.name"/></title>
    <s:head />
	<link type="text/css" href="pals.css" rel="stylesheet" />
	</head>

	<body>
  
	<div class="top">


	</div>

	<div class="mainWelcome">   
	
	<table border=0 cellspacing=0 cellpadding=8 width="100%" ><tr>
	<td align="center">
	<div class="toptitle">
	Protocol for the Analysis of Land Surface Models
	</div>
	
	<img class="logo" src="images/Logo7.jpg" />
	</td>
	</tr>


	<tr><td>

	<hr size=1 />

	<a class="blue" href="Welcome.action">< < Back</a>

	<h3>About PALS</h3>

	<p>PALS was originally designed to bridge the land surface modelling and ecological measurement 
	communities (e.g. Fluxnet), although experiments structured around other observational data set are 
	welcomed (please contact help at pals.unsw.edu.au). We hope that free model output analysis, access to 
	current observational datasets and comparison with other modelling groups will motivate land surface modellers 
	to participate. Similarly we hope that free analyses of uploaded observational data, access to model 
	simulations that use these data and uptake by many modelling groups will motivate the measurement 
	community to maintain their data on the PALS site.
	</p>

	<p>We strongly encourage both data providers and model users to give as much information about their data 
	and simulations as possible.
  	</p>

	<h3>Using PALS with your model:</h3>

	<p>The PALS evaluation system is intended both for model comparison and for model development. 
	You can upload and receive analyses on as many model simulations as you like. 
	Each uploaded model output file can be assigned <b>access privileges</b> by you, to determine who you
	will share it with. This can be either "no-one", "data owner" or "all PALS participants".

	</p>
	
	<ul>
	<li>The 'no-one' shared label, as the name suggests, only allows the file owner to view analyses 
	derived from it. This is intended for model development, when results may be very unpredictable.
	</li>
	
	<li>The 'data owner' shared label means that the data provider for the experiment can see your plots 
	as well as your model and username, but other users will not have any access. 
	These simulations will also contribute to any PILPS-style model comparisons at the front 
	of the PALS website (when this feature is implemented), but individual models will not be identified. 
	</li>
	
	<li>The 'all PALS participants' shared label means that any PALS user can see the plots made from your model output, 
	with your model and username identified. 
	</li>
	
	</ul>


	<p>By default, all uploaded model outputs will shared with 'all PALS participants'. 
	While the access privileges for any model output file can be changed at any time, 
	for each dataset/experiment you choose to use, at least one of your uploaded model outputs must be 
	shared. This means, for example, that if you have only one model simulation 
	for a given dataset/experiment, you will not be able to change its sharded status to 'private'.
	</p>

	<p>We strongly encourage users to share their runs with all PALS users. 
	This will maximise constructive discussion between users and ultimately the drive the development of models. 
	Please avoid making your model outputs 'private' unless you are genuinely engaged in model development or 
	have been informed of legal restraints on your participation.
	</p>

	<p>We also encourage users to upload the parameter sets, initial conditions datasets and even model code 
	that generated their model output file (within reasonable file size constraints). 
	These files are never shared with other users, regardless of the access level of the model output file. 
	They are for the reference of the submitting user alone. The interface is designed to ensure reproducibility of experiments.
	</p>

	<h3>Using PALS with your observational data:</h3>

	<p>For flux tower data:
	<br />
	In the In the first instance, your uploaded data will be used to generate a set of diagnostic plots. 
	This is partly intended to give you feedback on the data you've uploaded and partly to check that the PALS 
	interface has interpreted your data correctly. A Netcdf file for use with land surface models will be created from it, 
	and checking these plots is in part quality control for its production.
	</p>

	<p>By uploading your flux tower to the PALS site, you allow PALS land surface model users to use your
	 (quality-controlled, converted) data, and have access to at least one simulation from every model that 
	 uses your data, as well as ancillary data about the model simulation and the contact information of the model user.
	 </p>

	<p>For other types of data:
	<br />
	At the moment, a structured interface is only in place for flux tower data. 
	If you have suggestions for other experiment/data types we'd love to hear about it. 
	Please read the flux tower data upload description above to get a rough idea of how your interface may be structured.
	</p>


	<p>If you have suggestions about how PALS might be improved, we'd love to hear about it 
	(bearing in mind the limited resources we have). In particular, if you would like to fund or 
	personally build additional functionality for PALS we will try to facilitate it. 
	PALS analysis scripts are written in R and all are [soon to be] available as an R package.
	</p>

	<a class="blue" href="Welcome.action">< < Back</a>
   
     </td></tr>
     
	</table>

	<br />
	
	<div class="small">
	Last updated Feb 2010.
	</div>
	
	<p>&nbsp;</p>
   
   </div>
   
   </body>
	
</html>
