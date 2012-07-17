# ModelAvWindowQh.R
# Variable requirements: Qh
#
# This script plots linear regression gradient, 
# rsq, rmse and stdev for a temporally averaged model output.
# Four plot panels are produced.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to AveragingWindow:
varname=QhNames
units=QhUnits
ytext=expression("Average Qh flux W"~m^{2})

ModelAveragingWindow(analysisType,varname,units,ytext)