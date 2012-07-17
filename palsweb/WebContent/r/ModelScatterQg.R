# ModelScatterQg.R
# Variable requirements: Qg
#
# This script plots a scatterplot of model vs obs Qg 
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Scatter:
varname=QgNames
units=QgUnits
ttext='Ground heat flux'
legendtext=c('Observed','Modelled')

ModelScatter(analysisType,varname,units,ttext,legendtext)