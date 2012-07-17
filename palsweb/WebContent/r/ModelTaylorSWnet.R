# ModelTaylorSWnet.R
# Variable requirements: SWnet
#
# This script plots Taylor diagram of SWnet
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to TaylorDiagram:
varname=SWnetNames
units=SWnetUnits
xtext=expression("Net shortwave radiation ( W /"~m^{2}~")")

ModelTaylorDiagram(analysisType,varname,units,xtext)