# ObsTimeseriesSWnet.R
# Variable requirements: SWnet
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Timeseries:
varname=SWnetNames
units=SWnetUnits
ytext=expression("Smoothed net shortwave radiation W/"~m^{2})
legendtext=c('Observed')

ObsTimeseries(analysisType,varname,units,ytext,legendtext)