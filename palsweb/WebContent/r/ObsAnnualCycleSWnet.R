# ObsAnnualCycleSWnet.R
# Variable requirements: SWnet
#
# This script plots the average annual cycle 
# (that is, monthly averages) of SWnet for
# an observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to AnnualCycle:
varname=SWnetNames
units=SWnetUnits
ytext=expression("Average net shortwave W/"~m^{2})
legendtext=c('Observed')

ObsAnnualCycle(analysisType,varname,units,ytext,legendtext)