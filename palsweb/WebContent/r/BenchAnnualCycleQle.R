# BenchAnnualCycleQle.R
# Variable requirements: Qle
#
# This script plots the average annual cycle 
# (that is, monthly averages) of Qle for
# both a model output and observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to AnnualCycle:
varname=QleNames
units=QleUnits
ytext=expression("Average latent heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchAnnualCycle(analysisType,varname,units,ytext,legendtext)