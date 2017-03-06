#!/bin/bash

DockerImg="myomtdstore"
SOURCECONFIG=$1
TARGETCONFIG="/opt/omtd-store/scripts/config.properties"

docker cp $SOURCECONFIG $DockerImg:$TARGETCONFIG


