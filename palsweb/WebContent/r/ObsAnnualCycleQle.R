# ObsAnnualCycleQle.R
# Variable requirements: Qle
#
# This script plots the average annual cycle 
# (that is, monthly averages) of Qle for
# an observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz CCRC, UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to AnnualCycle:
varname=QleNames
units=QleUnits
ytext=expression("Average latent heat flux W/"~m^{2})
legendtext=c('Observed')

ObsAnnualCycle(analysisType,varname,units,ytext,legendtext)