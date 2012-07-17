# ObsTimeseriesRnet.R
# Variable requirements: Rnet
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Timeseries:
varname=RnetNames
units=RnetUnits
ytext=expression("Smoothed net radiation W/"~m^{2})
legendtext=c('Observed')

ObsTimeseries(analysisType,varname,units,ytext,legendtext)