# ModelConserve.R
# Variable requirements: Qle, Qh, Qg and Rnet
#
# This script produces a scatter plot of
# Qle+Qh vs. Rnet-Qg
# 
# Gab Abramowitz CCRC, UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'
checkUsage(analysisType)
setOutput(analysisType)

units=QleUnits
legendtext = c('Rnet - Qg','Qle + Qh')

# Load modelled latent heat data:
varname=QleNames
mod_qle = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Load modelled sensible heat data:
varname=QhNames
mod_qh = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Load modelled net radiation data:
varname=RnetNames
mod_rnet = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Load modelled ground heat flux data:
varname=QgNames
mod_qg = GetModelOutput(varname,getModelOutputFilePath(analysisType),units)

# Create data vectors for scatter function:
qleqh = mod_qle$data + mod_qh$data
rnetqg = mod_rnet$data - mod_qg$data

# Call PALSScatter plotting function:
PALSScatter(getObsLabel(analysisType),qleqh,rnetqg,
	'Energy density','energy conservation',legendtext,
	mod_qg$timing$tstepsize,mod_qg$timing$whole,ebal=TRUE,
	getModLabel(analysisType))
