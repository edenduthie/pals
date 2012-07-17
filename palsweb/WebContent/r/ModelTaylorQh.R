# ModelTaylorQh.R
# Variable requirements: Qh
#
# This script plots Taylor diagram of Qh
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to TaylorDiagram:
varname=QhNames
units=QhUnits
xtext=expression("Sensible heat ( W /"~m^{2}~")")

ModelTaylorDiagram(analysisType,varname,units,xtext)