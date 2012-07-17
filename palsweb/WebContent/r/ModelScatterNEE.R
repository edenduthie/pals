# ModelScatterNEE.R
# Variable requirements: NEE
#
# This script plots a scatterplot of model vs obs NEE 
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Scatter:
varname=NEENames
units=NEEUnits
ttext='NEE'
legendtext=c('Observed','Modelled')

ModelScatter(analysisType,varname,units,ttext,legendtext)