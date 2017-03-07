#!/bin/bash

RESTSERVERJAR="$(pwd)/../omtd-store-rest/target/omtd-store-rest-0.0.1-SNAPSHOT.jar"

CONFIG=$1
APP="omtd-store-rest"
INSTALLEDJAR="$(pwd)/../bin/$APP.jar"
INSTALLEDJARCONFIG="$(pwd)/../bin/$APP.conf"

TARGET="/etc/init.d/omtdstore"

if [ -f $TARGET ]
then
echo "Cleaning up before installation."
rm -rf $TARGET
fi

cp $RESTSERVERJAR $INSTALLEDJAR
echo "Copied JAR to $INSTALLEDJAR."

ln -s $INSTALLEDJAR $TARGET
echo "Created symbolic link."

# Create the INSTALLEDJARCONFIG.
# Step 1: Copy the template
cat "$INSTALLEDJARCONFIG.orig" > $INSTALLEDJARCONFIG
# Step 2: Add this for specifying the storeApplicationCfg of the app. 
echo "RUN_ARGS=\"-DstoreApplicationCfg=file:$(pwd)/../scripts/$CONFIG\"" >> $INSTALLEDJARCONFIG
	
