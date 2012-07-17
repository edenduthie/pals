# BenchAnnualCycleRnet.R
# Variable requirements: Rnet
#
# This script plots the average annual cycle 
# (that is, monthly averages) of Rnet for
# both a model output and observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to AnnualCycle:
varname=RnetNames
units=RnetUnits
ytext=expression("Net absorbed radiation W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchAnnualCycle(analysisType,varname,units,ytext,legendtext)