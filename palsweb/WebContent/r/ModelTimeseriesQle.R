# ModelTimeseriesQle.R
# Variable requirements: Qle
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Timeseries:
varname=QleNames
units=QleUnits
ytext=expression("Smoothed latent heat flux W/"~m^{2})
legendtext=c('Observed','Modelled')

ModelTimeseries(analysisType,varname,units,ytext,legendtext)