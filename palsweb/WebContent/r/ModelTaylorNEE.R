# ModelTaylorNEE.R
# Variable requirements: NEE
#
# This script plots a Taylor diagram of NEE
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to TaylorDiagram:
varname=NEENames
units=NEEUnits
xtext=expression("NEE ("~mu~"mol/"~m^{2}~"/s )")

ModelTaylorDiagram(analysisType,varname,units,xtext)