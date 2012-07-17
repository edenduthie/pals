# ObsTimeseriesQh.R
# Variable requirements: Qh
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Timeseries:
varname=QhNames
units=QhUnits
ytext=expression("Smoothed sensible heat flux W/"~m^{2})
legendtext=c('Observed')

ObsTimeseries(analysisType,varname,units,ytext,legendtext)