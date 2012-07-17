TrainPathsMet <- c('testdata\\dsvServiceTest\\username\\ds596.597_met.nc','testdata\\dsvServiceTest\\username\\ds590.591_met.nc')
TrainPathsFlux <- c('testdata\\dsvServiceTest\\username\\ds596.597_flux.nc','testdata\\dsvServiceTest\\username\\ds590.591_flux.nc')
PredictPathMet='testdata\\dsvServiceTest\\username\\ds593.594_met.nc'
DataSetName='Data Set Name'
DataSetVersion='First Version'
PredictNcPath='testdata\\dsvServiceTest\\username\\ds593.594_bench.nc'
SiteLat=101.0
SiteLon=102.0
library(pals)
analysisType = 'BenchmarkGeneration'
removeflagged = TRUE # only use non-gapfilled data?
varnames=c(QleNames[1],QhNames[1],NEENames[1],QgNames[1])
varsunits=c(QleUnits$name[1],QhUnits$name[1],NEEUnits$name[1],QgUnits$name[1])
GenerateBenchmarkSet(TrainPathsMet,TrainPathsFlux,PredictPathMet,DataSetName,DataSetVersion,PredictNcPath,SiteLat,SiteLon,varnames,varsunits,removeflagged)
