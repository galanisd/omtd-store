#!/bin/bash

CURRENTVERSION=$( cat ../VERSION)
echo "Version: $CURRENTVERSION"
java -jar $(pwd)"/../omtd-store-rest-client/target/omtd-store-rest-client-"$CURRENTVERSION"-exec.jar" 
