# BenchPdfQg.R
# Variable requirements: Qg
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Pdf:
varname=QgNames
units=QgUnits
xtext=expression("Ground heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchPdf(analysisType,varname,units,xtext,legendtext)