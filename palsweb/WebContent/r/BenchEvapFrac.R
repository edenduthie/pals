# BenchEvapFrac.R
# Variable requirements: Qle and Qh
#
# This script produces a time series of the evaporative fraction
# [latent heat/(latent heat+sensible heat)] using a moving window
# of weekly averages of these two variables.
# 
# Gab Abramowitz CCRC, UNSW 2012 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'
checkUsage(analysisType)
setOutput(analysisType)

winsize = 30 # averaging window size in days
units=QleUnits
ytext=paste(winsize,'-day mean E/(E+H) for E,H > 0',sep="")
legendtext=c('Observed','Modelled',getUserBenchNames())

# Load benchmark names and paths:
UserBenchPaths = getUserBenchPaths()
UserBenchNames = getUserBenchNames()

nbench = length(UserBenchNames) # number of benchmarks

# Load obs and model latent heat data:
varname=QleNames
obs_qle = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
model_qle = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Load obs and model sensible heat data:
varname=QhNames
obs_qh = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
model_qh = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Check compatibility between model and obs (same dataset):
CheckTiming(model_qh$timing,obs_qh$timing)

# Load benchmark data:
benchQle = matrix(NA,nbench,length(obs_qle$data))
benchQh = matrix(NA,nbench,length(obs_qle$data))
for(b in 1:nbench){
	if(substr(UserBenchNames[b],1,5)=='B_Emp'){ 
		# This is an empirical benchmark.
		# Get emprirical benchmark variable names:
		bvarname_qle = paste(QleNames[1],'_',
			substr(UserBenchNames[b],6,nchar(UserBenchNames[b])),sep='')
		bvarname_qh = paste(QhNames[1],'_',
			substr(UserBenchNames[b],6,nchar(UserBenchNames[b])),sep='')
		# Get empirical benchmark data:
		tmp_qle = GetBenchmarkVariable(bvarname_qle,UserBenchPaths[b])
		tmp_qh = GetBenchmarkVariable(bvarname_qh,UserBenchPaths[b])
		# Check emp benchmark is based on same data set and version as obs:
		if(b==1){
			CheckVersionCompatibility(UserBenchPaths[b],
				getObservedFluxDataFilePath(analysisType))
		}
	}else{
		# This benchmark is a model simulation.
		tmp_qle = GetModelOutput(QleNames,UserBenchPaths[b],units)
		tmp_qh = GetModelOutput(QhNames,UserBenchPaths[b],units)
	}
	# Check benchmark data timing is compatible with obs:
	if(b==1){
		CheckTiming(tmp_qle$timing,obs_qle$timing)
	}
	# For now, if requested benchmark variable doesn't exist in benchmark
	# file, stop script:
	if(is.null(tmp_qle)){
		CheckError(paste('B4: Could not find benchmark variable "',bvarname_qle,
			'" in benchmark netcdf file.',sep=''))
	}else if(is.null(tmp_qh)){
		CheckError(paste('B4: Could not find benchmark variable "',bvarname_qh,
			'" in benchmark netcdf file.',sep=''))
	}
	benchQle[b,] = tmp_qle$data
	benchQh[b,] = tmp_qh$data
}

# Create data matrices for function:
qledata=matrix(NA,length(model_qle$data),(2+nbench))
qledata[,1] = obs_qle$data
qledata[,2] = model_qle$data
qhdata=matrix(NA,length(model_qh$data),(2+nbench))
qhdata[,1] = obs_qh$data
qhdata[,2] = model_qh$data
for(b in 1:nbench){
	qledata[,(b+2)] = benchQle[b,]
	qhdata[,(b+2)] = benchQh[b,]
}
if(obs_qle$qcexists & obs_qh$qcexists){
	qleqcdata = matrix(NA,length(obs_qle$data),1)
	qleqcdata[,1] = obs_qle$qc
	qhqcdata = matrix(NA,length(obs_qh$data),1)
	qhqcdata[,1] = obs_qh$qc
	# Call Evaporative fraction function with qc data:
	EvapFrac(getObsLabel(analysisType),qledata,qhdata,ytext,legendtext,
		obs$timing$tstepsize,winsize=winsize,modlabel=getModLabel(analysisType),
		qleqcdata=qleqcdata,qhqcdata=qhqcdata)	
}else{
	# Call Evaporative fraction function without qc data:
	EvapFrac(getObsLabel(analysisType),qledata,qhdata,ytext,legendtext,
		obs$timing$tstepsize,winsize=winsize,modlabel=getModLabel(analysisType))
	
}