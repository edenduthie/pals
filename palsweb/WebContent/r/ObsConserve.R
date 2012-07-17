# ObsConserve.R
# Variable requirements: Qle, Qh, Qg and Rnet
#
# This script produces a scatter plot of
# Qle+Qh vs. Rnet-Qg
# 
# Gab Abramowitz CCRC, UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'
checkUsage(analysisType)
setOutput(analysisType)

units=QleUnits
legendtext = c('Rnet - Qg','Qle + Qh')

# Load obs latent heat data:
varname=QleNames
obs_qle = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)

# Load obs sensible heat data:
varname=QhNames
obs_qh = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)

# Load obs net radiation data:
varname=RnetNames
obs_rnet = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)

# Load obs ground heat flux data:
varname=QgNames
obs_qg = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)

# Create data vectors for scatter function:
qleqh = obs_qle$data + obs_qh$data
rnetqg = obs_rnet$data - obs_qg$data

# Call PALSScatter plotting function:
PALSScatter(getObsLabel(analysisType),qleqh,rnetqg,
	'Energy density','energy conservation',legendtext,obs_qg$timing$tstepsize,
	obs_qg$timing$whole,ebal=TRUE)

