# ObsPdfQg.R
# Variable requirements: Qg
#
# This script plots a probability density function
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Pdf:
varname=QgNames
units=QgUnits
xtext=expression("Ground heat flux W/"~m^{2})
legendtext=c('Observed')

ObsPdf(analysisType,varname,units,xtext,legendtext)