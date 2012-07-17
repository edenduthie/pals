# BenchmarkGeneration.R
#
# Runs empirical benchmark simulations for a single site.
#
# Requires that the following variables be available through the
# sourced R declarations file:
# TrainPathsMet; TrainPathsFlux; PredictPathMet; DataSetName; DataSetVersion; 
# PredictNcPath; SiteLat; SiteLon;
#
# Gab Abramowitz CCRC, UNSW 2011 (palshelp at gmail dot com)
#
library(pals)
analysisType = 'BenchmarkGeneration'
checkUsage(analysisType)

removeflagged = TRUE # only use non-gapfilled data?

varnames=c(QleNames[1],QhNames[1],NEENames[1],QgNames[1])
varsunits=c(QleUnits$name[1],QhUnits$name[1],
	NEEUnits$name[1],QgUnits$name[1])
# Load benchmark training and simulation file locations: 
source(commandArgs()[4])

GenerateBenchmarkSet(TrainPathsMet,TrainPathsFlux,PredictPathMet,
	DatSetName,DataSetVersion,PredictNcPath,SiteLat,SiteLon,
	varnames,varsunits,removeflagged)