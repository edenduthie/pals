# ObsAnnualCycleRnet.R
# Variable requirements: Rnet
#
# This script plots the average annual cycle 
# (that is, monthly averages) of Rnet for
# an observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to AnnualCycle:
varname=RnetNames
units=RnetUnits
ytext=expression("Average net radiation W/"~m^{2})
legendtext=c('Observed')

ObsAnnualCycle(analysisType,varname,units,ytext,legendtext)