# ModelTaylorRnet.R
# Variable requirements: Rnet
#
# This script plots Taylor diagram of Rnet
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to TaylorDiagram:
varname=RnetNames
units=RnetUnits
xtext=expression("Net radiation ( W /"~m^{2}~")")

ModelTaylorDiagram(analysisType,varname,units,xtext)