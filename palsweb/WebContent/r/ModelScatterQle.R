# ModelScatterQle.R
# Variable requirements: Qle
#
# This script plots a scatterplot of model vs obs Qle 
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Scatter:
varname=QleNames
units=QleUnits
ttext='Latent heat flux'
legendtext=c('Observed','Modelled')

ModelScatter(analysisType,varname,units,ttext,legendtext)