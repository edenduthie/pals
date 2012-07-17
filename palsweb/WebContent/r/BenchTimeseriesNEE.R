# BenchTimeseriesNEE.R
# Variable requirements: NEE
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Timeseries:
varname=NEENames
units=NEEUnits
ytext=expression("Smoothed NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchTimeseries(analysisType,varname,units,ytext,legendtext)