# Fluxnet2PALS.R
#
# Converts data from several Fluxnet formatted spreadhseets
# a single PALS template spreadsheet.
#
# Gab Abramowitz UNSW 2011 (palshelp at gmail dot com)
rm(list=ls()) # clear all variables
datasetname='wallaby'
fluxnetdir = '~/data/fluxnet/LaThuile_open/AU-Wac'

FluxTemplateVersion = '1.0.1'

qcScript = paste('~/Documents/admin/PALS/datasets/ObsUpload/Fluxnet/',
	datasetname,'QC.R',sep='')
outfile = paste('~/Documents/admin/PALS/datasets/ObsUpload/Fluxnet',
	'/',datasetname,'Conversion',FluxTemplateVersion,'.csv',sep='')


# Save all Fluxnet file names and list to screen:
fnet = list.files(path = fluxnetdir,full.names=TRUE)
cat('Found',length(fnet),'files. \n')

library(gdata)
library(pals)
library(stringr)

# Load PALS template spreadsheet:
PALSinit = as.matrix(read.xls(
	paste('~/Documents/admin/PALS/datasets/ObsUpload/PALSFluxTowerTemplate',
	FluxTemplateVersion,'.xls',sep=''),skip=0,header=FALSE,quote="\"",sep=','))

if(FluxTemplateVersion=='1.0.1'){
	ncols = 32
}else{
	ncols = 34	
}

# Deal with a few template version peculiarities:
if(FluxTemplateVersion=='1.0.1'){	
	# Add quotes to appropriate 3rd header line entries,
	# i.e. those with commas in their text:
	PALSinit[3,32] = str_trim(PALSinit[3,32])
	centries = c(1:16) * 2 # entries with commas in their text on template1.0.1
	PALSinit[3,centries] = paste('\"',PALSinit[3,centries],'\"',sep='')
}

# Initialise time step counter, knowing header has to replicate PALS template
tctr = 4 

for(f in 1:length(fnet)){
	cat('Loading:',fnet[f],'\n')
	# Read Fluxnet file:	
	fdata = as.matrix(read.csv(fnet[f]))	
	if(f == 1){ 
		# find starting year and allocate output array
		tlength = 0 # init
		for (ff in 1:length(fnet)){
			if(is.leap(fdata[4,1]+ff-1)){
				tlength = tlength + 17568
			}else{
				tlength = tlength + 17520
			}	
		}
		PALSt = matrix(NA,(tlength+3),ncols) # init
		PALSt[1:3,1:ncols] = PALSinit[1:3,1:ncols]
		
		# Start of data:
		sdate = doydate(fdata[1,2],leap=is.leap(as.integer(fdata[1,1])))
		tstepsize = as.double(fdata[2,3]) - as.double(fdata[1,3])
		starttime = list(time=as.double(fdata[1,3]),day=sdate$day,
		month=sdate$month,year=as.integer(fdata[1,1]),timestepsize=tstepsize)
	}
	tsteps = length(fdata[,1])
	cat('Found',tsteps,'time steps for',length(fdata[1,]),'variables. \n')
	# Account for odd date on last time step entry in Fluxnet data
	if(fdata[tsteps,1] != fdata[(tsteps-1),1]){ # if year changes at last time step
		fdata[tsteps,2] = 1	# reset day-of-year counter
	}
	# Write date data:
	for(t in 1:tsteps){
		dd = doydate(fdata[t,2],leap=is.leap(fdata[t,1]))
		PALSt[(t+tctr-1),1] = paste(as.character(dd$day),'/',as.character(dd$month),
			'/',as.character(fdata[t,1]),sep='')
	}
	PALSt[tctr:(tctr+tsteps-1),2] = fdata[,3] # Hour-of-day data:
	PALSt[tctr:(tctr+tsteps-1),3] = fdata[,19] # SWdown
	PALSt[tctr:(tctr+tsteps-1),4] = fdata[,73] # SWdownFLAG
	PALSt[tctr:(tctr+tsteps-1),5] = fdata[,29] # LWdown
	PALSt[tctr:(tctr+tsteps-1),6] = -9999 # LWdownFLAG
	PALSt[tctr:(tctr+tsteps-1),7] = fdata[,11] # Tair
	PALSt[tctr:(tctr+tsteps-1),8] = fdata[,65] # TairFLAG
	PALSt[tctr:(tctr+tsteps-1),9] = fdata[,36] # Rel H
	PALSt[tctr:(tctr+tsteps-1),10] = fdata[,68] # RHFLAG
	PALSt[tctr:(tctr+tsteps-1),11] = fdata[,18] # Windspeed
	PALSt[tctr:(tctr+tsteps-1),12] = fdata[,72] # WSFLAG
	PALSt[tctr:(tctr+tsteps-1),13] = fdata[,15] # Rainfall
	PALSt[tctr:(tctr+tsteps-1),14] = fdata[,69] # RainfFLAG
	PALSt[tctr:(tctr+tsteps-1),15] = -9999 # snowf
	PALSt[tctr:(tctr+tsteps-1),16] = -9999 # snowfFLAG
	PALSt[tctr:(tctr+tsteps-1),17] = -9999 # pressure
	PALSt[tctr:(tctr+tsteps-1),18] = -9999 # pressureFLAG
	PALSt[tctr:(tctr+tsteps-1),19] = fdata[,37] # CO2
	PALSt[tctr:(tctr+tsteps-1),20] = -9999 # CO2flag
	PALSt[tctr:(tctr+tsteps-1),21] = fdata[,21] # Rnet
	PALSt[tctr:(tctr+tsteps-1),22] = fdata[,75] # RnetFLAG
	PALSt[tctr:(tctr+tsteps-1),23] = fdata[,32] # SWup
	PALSt[tctr:(tctr+tsteps-1),24] = -9999 # SWupFLAG
	PALSt[tctr:(tctr+tsteps-1),25] = fdata[,8] # LE
	PALSt[tctr:(tctr+tsteps-1),26] = fdata[,62] # LEFLAG
	PALSt[tctr:(tctr+tsteps-1),27] = fdata[,9] # H
	PALSt[tctr:(tctr+tsteps-1),28] = fdata[,63] # HFLAG
	PALSt[tctr:(tctr+tsteps-1),29] = fdata[,5] # NEE
	PALSt[tctr:(tctr+tsteps-1),30] = fdata[,61] # NEEFLAG
	if(FluxTemplateVersion=='1.0.1'){
		PALSt[tctr:(tctr+tsteps-1),31] = fdata[,10] # G
		PALSt[tctr:(tctr+tsteps-1),32] = fdata[,64] # GFLAG
	}else{
		PALSt[tctr:(tctr+tsteps-1),31] = fdata[,6] # GPP
		PALSt[tctr:(tctr+tsteps-1),32] = fdata[,61] # GPP/NEEFLAG
		PALSt[tctr:(tctr+tsteps-1),33] = fdata[,10] # G
		PALSt[tctr:(tctr+tsteps-1),34] = fdata[,64] # GFLAG
	}
	# Increment time step counter
	tctr = tctr + tsteps
}

# Perform QC for this data set if required:
if(qcScript != ''){
	cat('Calling QC for this dataset... \n')
	source(qcScript)	
	PALSt = datasetqc(PALSt,FluxTemplateVersion,starttime)
}

# Write csv file:
cat('Writing Fluxnet data to PALS template file... \n')
write.csv(as.data.frame(PALSt,optional=TRUE),file=outfile,
	row.names=FALSE,quote=FALSE)
	
# Test by running command line call to PALS conversion script:
system(paste('r -f  ~/Documents/admin/PALS/scripts/working/pals-scripts/ConvertSpreadsheetToNcdf.R',
	outfile,' ~/Documents/admin/PALS/datasets/ObsUpload/tmpMet.nc',
	'~/Documents/admin/PALS/datasets/ObsUpload/tmpFlux.nc -35.78024',
	'148.00781 1200.0 70.0 tmpTest 0.11'))

cresult = readline(prompt = 'Conversion okay?')
if(cresult != ''){stop()}

# Generate and view QC plots:
system('r -f ~/Documents/admin/PALS/scripts/working/pals-scripts/QCplotsSpreadsheet.R ~/Documents/admin/PALS/datasets/ObsUpload/tmpMet.nc ~/Documents/admin/PALS/datasets/ObsUpload/tmpFlux.nc tmpQC png 1200 2200')
system('open -a Preview.app tmpQC.png')