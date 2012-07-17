# BenchPdfNEE.R
# Variable requirements: NEE
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Pdf:
varname=NEENames
units=NEEUnits
xtext=expression("NEE flux "~mu~"mol/"~m^{2}~"/s")
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchPdf(analysisType,varname,units,xtext,legendtext)