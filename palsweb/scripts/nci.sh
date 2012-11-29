#!/bin/bash
mkdir /scratch
mkdir /scratch/pals
mkdir /scratch/pals/webappdata

mkdir /root/code
cd /root/code
git clone https://github.com/edenduthie/pals.git
cd pals
git checkout eduthie
cd palsweb
ant dist-nci

service tomcat6 stop
rm -rf /usr/share/tomcat6/webapps/pals
rm /usr/share/tomcat6/webapps/pals.war
cp /root/code/pals/palsweb/dist/pals.war /usr/share/tomcat6/webapps
service tomcat6 start
sleep 1m

cd /usr/share/tomcat6/webapps/pals
chmod -R 777 r
cd r
mkdir workspace
chmod -R 777 workspace
dos2unix rWrapper
./rWrapper CMD INSTALL pals
./rWrapper CMD INSTALL plotrix
./rWrapper CMD INSTALL ncdf
./rWrapper CMD INSTALL cairoDevice