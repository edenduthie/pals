# ObsAnnualCycleNEE.R
# Variable requirements: NEE
#
# This script plots the average annual cycle 
# (that is, monthly averages) of NEE for
# an observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz CCRC, UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to AnnualCycle:
varname=NEENames
units=NEEUnits
ytext=expression("Average NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed')

ObsAnnualCycle(analysisType,varname,units,ytext,legendtext)