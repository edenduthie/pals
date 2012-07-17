# BenchDiurnalCycleQle.R
# Variable requirements: Qle
#
# This script plots the average diurnal cycle of Qle for
# both a model output and observation time series.
# Four plots are produced, one for each season, over an 
# entire, integer-year single-site data set. Dataset 
# **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to DiurnalCycle:
varname=QleNames
units=QleUnits
ytext=expression("Average latent heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchDiurnalCycle(analysisType,varname,units,ytext,legendtext)