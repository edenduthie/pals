# ModelPdfQg.R
# Variable requirements: Qg
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Pdf:
varname=QgNames
units=QgUnits
xtext=expression("Ground heat flux W/"~m^{2})
legendtext=c('Observed','Modelled')

ModelPdf(analysisType,varname,units,xtext,legendtext)