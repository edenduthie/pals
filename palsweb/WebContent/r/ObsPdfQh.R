# ObsPdfQh.R
# Variable requirements: Qh
#
# This script plots a probability density function
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Pdf:
varname=QhNames
units=QhUnits
xtext=expression("Sensible heat flux W/"~m^{2})
legendtext=c('Observed')

ObsPdf(analysisType,varname,units,xtext,legendtext)