# ModelAnnualCycleNEE.R
# Variable requirements: NEE
#
# This script plots the average annual cycle 
# (that is, monthly averages) of NEE for
# both a model output and observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to AnnualCycle:
varname=NEENames
units=NEEUnits
ytext=expression("Average NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed','Modelled')

ModelAnnualCycle(analysisType,varname,units,ytext,legendtext)