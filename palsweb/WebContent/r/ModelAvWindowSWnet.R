# ModelAvWindowSWnet.R
# Variable requirements: SWnet
#
# This script plots linear regression gradient, 
# rsq, rmse and stdev for a temporally averaged model output.
# Four plot panels are produced.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'ModelAnalysis'

# Other arguments to AveragingWindow:
varname=SWnetNames
units=SWnetUnits
ytext=expression("Average net shortwave radiation W"~m^{2})

ModelAveragingWindow(analysisType,varname,units,ytext)