# BenchPdfQle.R
# Variable requirements: Qle
#
# This script plots probability density functions.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)

library(pals)

analysisType = 'BenchAnalysis'

# Other arguments to Pdf:
varname=QleNames
units=QleUnits
xtext=expression("Latent heat flux W/"~m^{2})
legendtext=c('Observed','Modelled',getUserBenchNames())

BenchPdf(analysisType,varname,units,xtext,legendtext)