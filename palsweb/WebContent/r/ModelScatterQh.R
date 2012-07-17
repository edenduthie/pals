# ModelScatterQh.R
# Variable requirements: Qh
#
# This script plots a scatterplot of model vs obs Qh 
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Scatter:
varname=QhNames
units=QhUnits
ttext='Sensible heat flux'
legendtext=c('Observed','Modelled')

ModelScatter(analysisType,varname,units,ttext,legendtext)