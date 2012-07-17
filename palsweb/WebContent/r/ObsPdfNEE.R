# ObsPdfNEE.R
# Variable requirements: NEE
#
# This script plots a probability density function
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ObsAnalysis'

# Other arguments to Pdf:
varname=NEENames
units=NEEUnits
ytext=expression("NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed')

ObsPdf(analysisType,varname,units,ytext,legendtext)