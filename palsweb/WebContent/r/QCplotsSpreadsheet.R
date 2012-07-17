# QCplotsSpreadsheet.R
#
# Plots a simple timeseries of variables recently 
# converted to netcdf from a PALS formatted spreadhseet.
#
# Gab Abramowitz UNSW 2010 (palshelp at gmail dot com)

library(pals)
library(ncdf) # load netcdf library

analysisType='QCplotsSpreadsheet'

checkUsage(analysisType)

metfilename = commandArgs()[4]
fluxfilename = commandArgs()[5]

plotcex = 2.2

# List of possible variables in netcdf files:
allvarnames=c('SWdown','LWdown','Tair','Qair','Wind',
	'Rainf','Snowf','PSurf','CO2air','Rnet','SWup',
	'Qle','Qh','NEE','Qg')
# Open netcdf files previously created from spreadsheet:
mID=open.ncdf(metfilename,readunlim=FALSE) # open met data file
fID=open.ncdf(fluxfilename,readunlim=FALSE) # open flux data file
# Get sitename:
dname=att.get.ncdf(mID,0,'PALS_dataset_name')
dvers=att.get.ncdf(mID,0,'PALS_dataset_version')
sitename = paste(dname$value,dvers$value)
# Get timing details:
timing = GetTimingNcfile(mID)
# Initialisations:
varname = c()
varlongname = c()
varunits = c()
varctr = 0
varqc = c()
# Determine how many variables we'll plot:
for(mv in 1:mID$nvars){
	foundqc=FALSE # init
	if(any(allvarnames==(mID$var[[mv]]$name))){
		varctr = varctr + 1
		varname[varctr] = mID$var[[mv]]$name
		varlongname[varctr] = mID$var[[mv]]$longname
		varunits[varctr] = mID$var[[mv]]$units
		# Now check if variable's qc time series exists:
		if(mv<mID$nvars){
			for(mvqc in (mv+1):mID$nvars){
				if(mID$var[[mvqc]]$name == paste(varname[varctr],'_qc',sep='')){
					foundqc = TRUE
					break
				}
			}
		}
		if(foundqc){
			varqc[varctr] = TRUE
		}else{
			varqc[varctr] = FALSE
		}
	}		
}
# Save number of variables from met file:
nfrommet = varctr
for(fv in 1:fID$nvars){
	foundqc=FALSE # init
	if(any(allvarnames==(fID$var[[fv]]$name))){
		varctr = varctr + 1
		varname[varctr] = fID$var[[fv]]$name
		varlongname[varctr] = fID$var[[fv]]$longname
		varunits[varctr] = fID$var[[fv]]$units
		# Now check if variable's qc time series exists:
		if(fv<fID$nvars){
			for(fvqc in (fv+1):fID$nvars){
				if(fID$var[[fvqc]]$name == paste(varname[varctr],'_qc',sep='')){
					foundqc = TRUE
					break
				}
			}
		}
		if(foundqc){
			varqc[varctr] = TRUE
		}else{
			varqc[varctr] = FALSE
		}

	}		
}
# Note total number of variables found:
nvars = varctr
# Now plot time series of each variable:
# Set output:
setOutput(analysisType)
# Set plot layout:
#vert = floor(sqrt(nvars)) # widescreen layout
#horiz = ceiling(nvars / vert) # widescreen layout
horiz = 3
vert = ceiling(nvars / horiz)
par(mfcol=c(vert,horiz),mar=c(4,4,3,0.5),oma=c(0,0,0,1),
		mgp=c(2.5,0.7,0),ps=12,tcl=-0.4)
# Create matrix to store one variable's data:
vdata = matrix(NA,mID$dim[[mID$unlimdim]]$len,1)
# Create matrix to store one variable's qc time series data:
qcdata = matrix(NA,mID$dim[[mID$unlimdim]]$len,1)
# Get all variable data
for(v in 1:nfrommet){
	# Read variable:
	vdata[,1] = get.var.ncdf(mID,varname[v])
	if(varqc[v]){ # if qc time series exists for this variable:
		# Read qc time series and convert anything not marked as 
		# gapfilled to be accepted as measured:
		qcdata[,1] = as.integer(as.logical(
			get.var.ncdf(mID,paste(varname[v],'_qc',sep=''))))
		# Plot variable with qc info
		errtext = Timeseries(sitename,vdata,varname[v],
			ytext=varunits[v],c(varlongname[v]),plotcex,timing,
			smoothed=FALSE,winsize=1,modlabel='no',vqcdata=qcdata)
	}else{
		# Plot variable
		errtext = Timeseries(sitename,vdata,varname[v],
			ytext=varunits[v],c(varlongname[v]),plotcex,timing)
	}
}
for(v in (nfrommet+1):nvars){
	# Read variable:
	vdata[,1] = get.var.ncdf(fID,varname[v])
	if(varqc[v]){ # if qc time series exists for this variable:
		# Read qc time series:
		qcdata[,1] = get.var.ncdf(fID,paste(varname[v],'_qc',sep=''))
		# Plot variable with qc info
		errtext = Timeseries(sitename,vdata,varname[v],
			ytext=varunits[v],c(varlongname[v]),plotcex,timing,
			vqcdata=qcdata)
	}else{
		# Plot variable
		errtext = Timeseries(sitename,vdata,varname[v],
			ytext=varunits[v],c(varlongname[v]),plotcex,timing)
	}
}
# Close met and flux files:
close(mID)
close(fID)