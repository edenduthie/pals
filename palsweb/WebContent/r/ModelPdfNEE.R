# ModelPdfNEE.R
# Variable requirements: NEE
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Pdf:
varname=NEENames
units=NEEUnits
xtext=expression("NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed','Modelled')

ModelPdf(analysisType,varname,units,xtext,legendtext)