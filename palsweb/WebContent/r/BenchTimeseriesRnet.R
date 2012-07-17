# BenchTimeseriesRnet.R
# Variable requirements: Rnet
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Timeseries:
varname=RnetNames
units=RnetUnits
ytext=expression("Smoothed net radiation W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchTimeseries(analysisType,varname,units,ytext,legendtext)