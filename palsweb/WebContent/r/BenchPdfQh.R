# BenchPdfQh.R
# Variable requirements: Qh
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Pdf:
varname=QhNames
units=QhUnits
xtext=expression("Sensible heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchPdf(analysisType,varname,units,xtext,legendtext)