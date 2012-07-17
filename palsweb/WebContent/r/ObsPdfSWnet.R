# ObsPdfSWnet.R
# Variable requirements: SWnet
#
# This script plots s probability density function
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Pdf:
varname=SWnetNames
units=SWnetUnits
xtext=expression("Net shortwave radiation W/"~m^{2})
legendtext=c('Observed')

ObsPdf(analysisType,varname,units,xtext,legendtext)