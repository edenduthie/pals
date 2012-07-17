# ObsDiurnalCycleSWnet.R
# Variable requirements: SWnet
#
# This script plots the average diurnal cycle of SWnet for
# an observation time series.
# Four plots are produced, one for each season, over an 
# entire, integer-year single-site data set. Dataset 
# **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to DiurnalCycle:
varname=SWnetNames
units=SWnetUnits
ytext = expression("Net shortwave radiation  W/"~m^{2})
legendtext=c('Observed')

ObsDiurnalCycle(analysisType,varname,units,ytext,legendtext)