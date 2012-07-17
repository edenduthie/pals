TrainPathsMet <- c('D:\\code\\webappdata\\username\\ds10.11_met.nc','D:\\code\\webappdata\\username\\ds7.8_met.nc')
TrainPathsFlux <- c('D:\\code\\webappdata\\username\\ds10.11_flux.nc','D:\\code\\webappdata\\username\\ds7.8_flux.nc')
PredictPathMet='D:\\code\\webappdata\\username\\ds4.5_met.nc'
DataSetName='Data Set Name'
DataSetVersion='First Version'
PredictNcPath='D:\\code\\webappdata\\username\\ds4.5_bench.nc'
SiteLat=101.0
SiteLon=102.0
library(pals)
analysisType = 'BenchmarkGeneration'
removeflagged = TRUE # only use non-gapfilled data?
varnames=c(QleNames[1],QhNames[1],NEENames[1],QgNames[1])
varsunits=c(QleUnits$name[1],QhUnits$name[1],NEEUnits$name[1],QgUnits$name[1])
GenerateBenchmarkSet(TrainPathsMet,TrainPathsFlux,PredictPathMet,DataSetName,DataSetVersion,PredictNcPath,SiteLat,SiteLon,varnames,varsunits,removeflagged)
