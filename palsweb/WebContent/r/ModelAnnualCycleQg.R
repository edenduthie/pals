# ModelAnnualCycleQg.R
# Variable requirements: Qg
#
# This script plots the average annual cycle 
# (that is, monthly averages) of Qg for
# both a model output and observation time series.
# Dataset **MUST START AT JAN 1**
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to AnnualCycle:
varname=QgNames
units=QgUnits
ytext=expression("Ground heat flux W/"~m^{2})
legendtext=c('Observed','Modelled')

ModelAnnualCycle(analysisType,varname,units,ytext,legendtext)