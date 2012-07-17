# ModelScatterSWnet.R
# Variable requirements: SWnet
#
# This script plots a scatterplot of model vs obs SWnet 
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to Scatter:
varname=SWnetNames
units=SWnetUnits
ttext='Net shortwave radiation'
legendtext=c('Observed','Modelled')

ModelScatter(analysisType,varname,units,ttext,legendtext)