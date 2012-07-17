# ModelTaylorQg.R
# Variable requirements: Qg
#
# This script plots Taylor diagram of Qg
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to TaylorDiagram:
varname=QgNames
units=QgUnits
xtext=expression("Ground heat flux ( W /"~m^{2}~")")

ModelTaylorDiagram(analysisType,varname,units,xtext)