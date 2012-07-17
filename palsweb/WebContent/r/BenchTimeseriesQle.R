# BenchTimeseriesQle.R
# Variable requirements: Qle
#
# This script plots a smoothed timeseries.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Timeseries:
varname=QleNames
units=QleUnits
ytext=expression("Smoothed latent heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchTimeseries(analysisType,varname,units,ytext,legendtext)