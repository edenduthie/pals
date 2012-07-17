# ModelScatterRnet.R
# Variable requirements: Rnet
#
# This script plots a scatterplot of model vs obs Rnet 
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Scatter:
varname=RnetNames
units=RnetUnits
ttext='Net radiation'
legendtext=c('Observed','Modelled')

ModelScatter(analysisType,varname,units,ttext,legendtext)