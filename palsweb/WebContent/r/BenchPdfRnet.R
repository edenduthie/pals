# BenchPdfRnet.R
# Variable requirements: Rnet
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Pdf:
varname=RnetNames
units=RnetUnits
xtext=expression("Net absorbed radiation W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchPdf(analysisType,varname,units,xtext,legendtext)