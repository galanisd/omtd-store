#!/bin/bash

RESTSERVERJAR="$(pwd)/../omtd-store-rest/target/omtd-store-rest-0.0.1-SNAPSHOT.jar"

APP="omtd-store-rest"
INSTALLEDJAR="$(pwd)/../bin/$APP.jar"
INSTALLEDJARCONFIG="$(pwd)/../bin/$APP.conf"

TARGET="/etc/init.d/omtdstore"

if [ -f $TARGET ]
then
rm -rf $TARGET
echo "Cleanup"
fi
cp $RESTSERVERJAR $INSTALLEDJAR
echo "Copied JAR to $INSTALLEDJAR"
ln -s  $INSTALLEDJAR $TARGET
echo "Created symbolic link"

echo "RUN_ARGS=\"-DstoreApplicationCfg=file:$(pwd)/../scripts/configLocal.properties""" >> $INSTALLEDJARCONFIG
	
