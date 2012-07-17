# ObsConserveTimeseries.R
# Variable requirements: SWdown, Qle, Qh, Qg and Rnet
#
# This script produces separate day and night time series plots of
# (Qle+Qh) / (Rnet-Qg)
# 
# Gab Abramowitz CCRC, UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'
checkUsage(analysisType)
setOutput(analysisType)

units=QleUnits
legendtext = c('(Qle+Qh)/(Rn-Qg)')

# Load obs SW down data:
varname=SWdownNames
obs_swdown = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
# Check obs data loading is okay:
CheckError(obs_swdown$errtext)

# Load obs latent heat data:
varname=QleNames
obs_qle = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
# Check obs data loading is okay:
CheckError(obs_qle$errtext)

# Load obs sensible heat data:
varname=QhNames
obs_qh = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
# Check obs data loading is okay:
CheckError(obs_qh$errtext)

# Load obs net radiation data:
varname=RnetNames
obs_rnet = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
# Check obs data loading is okay:
CheckError(obs_rnet$errtext)

# Load obs ground heat flux data:
varname=QgNames
obs_qg = GetFluxnetVariable(varname,getObservedFluxDataFilePath(analysisType),units)
# Check obs data loading is okay:
CheckError(obs_qg$errtext)

# Create data vectors for scatter function:
qleqh = obs_qle$data + obs_qh$data
rnetqg = obs_rnet$data - obs_qg$data

daynight = DayNight(obs_swdown,5)

# Call PALSScatter plotting function:
errtext=PALSScatter(getObsLabel(analysisType),qleqh,rnetqg,
	'Energy density','energy conservation',legendtext,obs_qg$timing$tstepsize,
	obs_qg$timing$whole,ebal=TRUE)
CheckError(errtext)

