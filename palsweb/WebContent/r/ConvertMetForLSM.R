# ConvertMetForLSM.R
#
# Adds variables to a PALS netcdf file for CABLE LSM simulations
#
# Gab Abramowitz UNSW, 2011
library(ncdf)

dataset = c('Amplero','Audubon','Bondv','Boreas','Cabauw','Harvard',
	'Howard','Hyytiala','Kruger','Loobos','Merbleue','Palang',
	'Tharandt','Tonzi','Tumba','Wallaby')
version = '1.3'
# CABLE-specific variables:
hc = c(1,0.2,3,12,0.1,23,
	16,14,10,14,0.5,26,
	26,9,45,80)
za = c(4,4,10,30,3,30,
	23,23,16,27,3,41,
	42,23,70,100)
iveg = c(10,10,12,1,10,4,
	8,1,9,1,11,2,
	1,8,2,2)
isoil = c(NA)

for(d in 2:length(dataset)){
	# Create netcdf path:
	filename = paste('~/Documents/admin/PALS/datasets/ObsNc/',
		dataset[d],'Fluxnet.',version,'_met.nc',sep='')
	cat('Modifying:',filename,'\n')
	# Open netcdf file:	
	ncid = open.ncdf(filename,write=TRUE,readunlim=FALSE)
	# Define new variables:
	if(exists('hc')){
		# Define variable:
		hcvar = var.def.ncdf('hc','m',list(ncid$dim[[1]],ncid$dim[[2]]),
			missval=-9999,longname='Vegetation height')
		# Add variable and then variable data:
		ncid = var.add.ncdf(ncid,hcvar) # CRITICAL - use new netcdf file handle
		put.var.ncdf(ncid,'hc',hc[d])
	}
	if(exists('za')){
		# Define variable:
		zavar = var.def.ncdf('za','m',list(ncid$dim[[1]],ncid$dim[[2]]),
			missval=-9999,longname='Reference height',prec='integer')
		# Add variable and then variable data:
		ncid = var.add.ncdf(ncid,zavar)
		put.var.ncdf(ncid,'za',za[d])
	}
	if(exists('iveg')){
		# Define variable:
		ivegvar = var.def.ncdf('iveg','-',list(ncid$dim[[1]],ncid$dim[[2]]),
			missval=-9999,longname='Vegetation type')
		# Add variable and then variable data:
		ncid = var.add.ncdf(ncid,ivegvar)
		put.var.ncdf(ncid,'iveg',iveg[d])
	}
	close(ncid)
}