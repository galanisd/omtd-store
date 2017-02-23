#!/bin/bash

java -DstoreApplicationCfg="file:"$(pwd)"/"$1 -jar $(pwd)"/../omtd-store-rest/target/omtd-store-rest-0.0.1-SNAPSHOT.jar" 
