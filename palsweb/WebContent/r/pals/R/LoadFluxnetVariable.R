# GetFluxnetVariable.R
#
# Reads a variable from an internally converted netcdf
# file of a Fluxnet site's observed data.
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)
#
GetFluxnetVariable = function(varname,obsfile,units){
	library(ncdf) # load netcdf library	
	errtext = 'ok' # initialise
	exists = FALSE
	ofid=open.ncdf(obsfile,readunlim=FALSE) # open observed data file
	# Check variable exists in netcdf file:
	for (v in 1:ofid$nvars){ # Search through all dimensions in netcdf file
		if(ofid$var[[v]]$name==varname[1]){
			exists=TRUE
			break	
		}
	}
	# If not, return:
	if(! exists){
		errtext=paste('DS2: Cannot find',varname[1],'in',stripFilename(obsfile))
		obs=list(errtext=errtext)
		return(obs)
	}
	timing = GetTimingNcfile(ofid)
	data=get.var.ncdf(ofid,varname[1])   # read observed variable data
	close.ncdf(ofid) # close netcdf file
	obs=list(data=data,timing=timing,errtext=errtext)
	return(obs)
}