# BenchTimeseriesQg.R
# Variable requirements: Qg
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Timeseries:
varname=QgNames
units=QgUnits
ytext=expression("Smoothed ground heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchTimeseries(analysisType,varname,units,ytext,legendtext)