#!/bin/bash

CURRENTVERSION=$( cat ../VERSION)
echo "Version: $CURRENTVERSION"

JARPATH=$(pwd)"/../omtd-store-rest/target/omtd-store-rest-$CURRENTVERSION.jar"

if [ "$#" -eq  "0" ]
then
     echo "Store Service will start with default configuration"
     java -jar $JARPATH
else
     java -DstoreApplicationCfg="file:"$(pwd)"/"$1 -jar $JARPATH
fi
