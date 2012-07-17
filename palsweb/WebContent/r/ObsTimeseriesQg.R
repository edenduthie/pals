# ObsTimeseriesQg.R
# Variable requirements: Qg
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Timeseries:
varname=QgNames
units=QgUnits
ytext=expression("Smoothed ground heat flux W/"~m^{2})
legendtext=c('Observed')

ObsTimeseries(analysisType,varname,units,ytext,legendtext)