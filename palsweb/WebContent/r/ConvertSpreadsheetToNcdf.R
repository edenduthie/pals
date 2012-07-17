# ConvertSpreadsheetToNcdf.R
#
# Converts data from a PALS formatted spreadhseet to
# netcdf.
#
# Gab Abramowitz UNSW 2012 (palshelp at gmail dot com)

library(pals)

analysisType='ConvertSpreadsheetToNcdf'
defaultLWsynthesis = 'Abramowitz (2012)' # 'Brutsaert (1975)' or 'Swinbank (1963)' or 'Abramowitz (2012)'

checkUsage(analysisType)

if(length(commandArgs()) != 4){
	# i.e. if we're using the old command line call for spreadsheet conversion:
	fileinname = commandArgs()[4]
	metfilename = commandArgs()[5]
	fluxfilename = commandArgs()[6]
	latitude = as.numeric(commandArgs()[7])
	longitude = as.numeric(commandArgs()[8])
	elevation = as.numeric(commandArgs()[9])
	measurementheight = as.numeric(commandArgs()[10])
	datasetname =  commandArgs()[11]
	datasetversion = commandArgs()[12]
	canopyheight=NA
	vegetationtype=NA
	utcoffset=NA
	avprecip=NA
	avtemp=NA
}else{ # we're using the newer, 4 argument command line call:
	source(commandArgs()[4])
}

# Read text file containing flux data:
DataFromText = ReadTextFluxData(fileinname)

# Make sure whole number of days in dataset:
CheckSpreadsheetTiming(DataFromText)

# Check which variables are actually present:
found = CheckTextDataVars(DataFromText)

if(!found$LWdown){ # no LWdown found
	DataFromText$data$LWdown = SynthesizeLWdown((DataFromText$data$Tair+zeroC),
		DataFromText$data$Qair,defaultLWsynthesis)
	DataFromText$data$LWdownFlag = 0
	found$LWdown_qc=TRUE
}else if(found$LWdown & !found$LWdown_all){ # only some LWdown found
	filledLWdown = gapfillLWdown(DataFromText$data$LWdown,
		(DataFromText$data$Tair+zeroC),DataFromText$data$Qair,
		defaultLWsynthesis)
	DataFromText$data$LWdown = filledLWdown$data
	DataFromText$data$LWdownFlag = filledLWdown$flag
	found$LWdown_qc=TRUE
}

# Change units:
ConvertedData = ChangeMetUnits(DataFromText,found,elevation)

# Run preliminary tests on data:
CheckTextDataRanges(ConvertedData,found)

# Create netcdf met driving file:
CreateMetNcFile(metfilename,ConvertedData,latitude,longitude,
	DataFromText$timestepsize,datasetname,datasetversion,defaultLWsynthesis,
	found,DataFromText$starttime,DataFromText$templateVersion,
	elevation=elevation,measurementheight=measurementheight,
	canopyheight=canopyheight,vegetationtype=vegetationtype,
	utcoffset=utcoffset,avprecip=avprecip,avtemp=avtemp)

# Create netcdf flux data file:
CreateFluxNcFile(fluxfilename, ConvertedData,latitude,longitude,
	DataFromText$timestepsize,datasetname,datasetversion,
	found,DataFromText$starttime,DataFromText$templateVersion,
	elevation=elevation,measurementheight=measurementheight,
	canopyheight=canopyheight,vegetationtype=vegetationtype,
	utcoffset=utcoffset,avprecip=avprecip,avtemp=avtemp)