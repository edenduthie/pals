# GetModelOutput.R
#
# This function is the model output read interface.
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)
#
# Inputs: variable name; model file name; variable units.
GetModelOutput = function(varname,modelfile,units){
	library(ncdf) # load netcdf library
	errtext = 'ok' # initialise
	mfid=open.ncdf(modelfile,readunlim=FALSE) # open model file
	# Find appropriate variable name for this model output,
	# and any units conversion required:
	modelvar = GetModelVariableName(mfid,varname,units)
	if(modelvar$errtext != 'ok'){ # i.e. there was a problem
		# Return to parent function with error message: 
		model=list(errtext=modelvar$errtext)
		return(model)
	}		
	# Get time step size:
	modeltiming = GetTimingNcfile(mfid)
	if(modeltiming$errtext != 'ok'){ # i.e. there was a problem
		# Return to parent function with error message: 
		model=list(errtext=modeltiming$errtext)
		return(model)
	}	
	# Get model data:	
	# Check for special cases first:
	if(modelvar$name=='FCEV'){ # lat heat in CLM has 3 components
		data1=get.var.ncdf(mfid,'FCEV') # read canopy evap
		data2=get.var.ncdf(mfid,'FCTR') # read canopy transp
		data3=get.var.ncdf(mfid,'FGEV') # read ground evap
		data = data1 + data2 + data3
	}else{ # otherwise just fetch variable data:
		data=get.var.ncdf(mfid,modelvar$name) # read model output data
	}
	# Apply any units changes:
	data = data*modelvar$multiplier + modelvar$addition
	close.ncdf(mfid) # close netcdf file
	model=list(data=data,timing = modeltiming,errtext=errtext)
	return(model)
}
