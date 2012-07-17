# ObsPdfRnet.R
# Variable requirements: Rnet
#
# This script plots a probability density function
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Pdf:
varname=RnetNames
units=RnetUnits
xtext=expression("Net radiation W/"~m^{2})
legendtext=c('Observed')

ObsPdf(analysisType,varname,units,xtext,legendtext)