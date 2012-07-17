# ModelTaylorQle.R
# Variable requirements: Qle
#
# This script plots Taylor diagram of Qle
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to TaylorDiagram:
varname=QleNames
units=QleUnits
xtext=expression("Latent heat ( W /"~m^{2}~")")

ModelTaylorDiagram(analysisType,varname,units,xtext)