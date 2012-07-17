# ModelAvWindowQle.R
# Variable requirements: Qle
#
# This script plots linear regression gradient, 
# rsq, rmse and stdev for a temporally averaged model output.
# Four plot panels are produced.
#
# Gab Abramowitz CCRC, UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to AveragingWindow:
varname=QleNames
units=QleUnits
ytext=expression("Average Qle flux W"~m^{2})

ModelAveragingWindow(analysisType,varname,units,ytext)