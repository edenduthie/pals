# ModelAvWindowNEE.R
# Variable requirements: NEE
#
# This script plots linear regression gradient, 
# rsq, rmse and stdev for a temporally averaged model output.
# Four plot panels are produced.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to AveragingWindow:
varname=NEENames
units=NEEUnits
ytext=expression("Average NEE flux "~mu~"mol/"~m^{2}~"/s")

ModelAveragingWindow(analysisType,varname,units,ytext)