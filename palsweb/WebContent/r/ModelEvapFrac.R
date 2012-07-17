# ModelEvapFrac.R
# Variable requirements: Qle and Qh
#
# This script produces a time series of the evaporative fraction
# [latent heat/(latent heat+sensible heat)] using a moving window
# of weekly averages of these two variables.
# 
# Gab Abramowitz CCRC, UNSW 2012 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'
checkUsage(analysisType)
setOutput(analysisType)

winsize = 30 # averaging window size in days
units=QleUnits
ytext=paste(winsize,'-day mean E/(E+H) for E,H > 0',sep="")
legendtext=c('Observed','Modelled')

# Load model and obs latent heat data:
varname=QleNames
obs_qle = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
model_qle = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Load model and obs sensible heat data:
varname=QhNames
obs_qh = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
model_qh = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Check compatibility between model and obs (same dataset):
CheckTiming(model_qh$timing,obs_qh$timing)

# Create data matrices for function:
qledata=matrix(NA,length(model_qle$data),2)
qledata[,1] = obs_qle$data
qledata[,2] = model_qle$data
qhdata=matrix(NA,length(model_qh$data),2)
qhdata[,1] = obs_qh$data
qhdata[,2] = model_qh$data

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

