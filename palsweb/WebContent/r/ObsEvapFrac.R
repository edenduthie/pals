# ObsEvapFrac.R
# Variable requirements: Qle and Qh
#
# This script produces a time series of the evaporative fraction
# [latent heat/(latent heat+sensible heat)] using a moving window
# of weekly averages of these two variables.
# 
# Gab Abramowitz CCRC, UNSW 2012 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'
checkUsage(analysisType)
setOutput(analysisType)

winsize = 30 # averaging window size in days
units=QleUnits
ytext=paste(winsize,'-day averaged E/(E+H) for E,H > 0',sep="")
legendtext=c('Observed')

# Load obs latent heat data:
varname=QleNames
obs_qle = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)

# Load obs sensible heat data:
varname=QhNames
obs_qh = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)

# Create data matrices for function:
qledata=matrix(NA,length(obs_qle$data),1)
qledata[,1] = obs_qle$data
qhdata=matrix(NA,length(obs_qh$data),1)
qhdata[,1] = obs_qh$data

if(obs_qle$qcexists & obs_qh$qcexists){
	qleqcdata = matrix(NA,length(obs_qle$data),1)
	qleqcdata[,1] = obs_qle$qc
	qhqcdata = matrix(NA,length(obs_qh$data),1)
	qhqcdata[,1] = obs_qh$qc
	# Call Evaporative fraction function with qc data:
	EvapFrac(getObsLabel(analysisType),qledata,qhdata,ytext,legendtext,
		obs$timing$tstepsize,winsize=winsize,
		qleqcdata=qleqcdata,qhqcdata=qhqcdata)	
}else{
	# Call Evaporative fraction function without qc data:
	EvapFrac(getObsLabel(analysisType),qledata,qhdata,ytext,legendtext,
		obs$timing$tstepsize,winsize=winsize)
	
}