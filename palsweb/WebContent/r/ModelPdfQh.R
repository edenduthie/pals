# ModelPdfQh.R
# Variable requirements: Qh
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Pdf:
varname=QhNames
units=QhUnits
xtext=expression("Sensible heat flux W/"~m^{2})
legendtext=c('Observed','Modelled')

ModelPdf(analysisType,varname,units,xtext,legendtext)