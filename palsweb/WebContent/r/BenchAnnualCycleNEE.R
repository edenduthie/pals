# BenchAnnualCycleNEE.R
# Variable requirements: NEE
#
# This script plots the average annual cycle 
# (that is, monthly averages) of NEE for
# both a model output and observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to AnnualCycle:
varname=NEENames
units=NEEUnits
ytext=expression("Average NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchAnnualCycle(analysisType,varname,units,ytext,legendtext)