# ModelPdfSWnet.R
# Variable requirements: SWnet
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Pdf:
varname=SWnetNames
units=SWnetUnits
xtext=expression("Net shortwave radiation W/"~m^{2})
legendtext=c('Observed','Modelled')

ModelPdf(analysisType,varname,units,xtext,legendtext)