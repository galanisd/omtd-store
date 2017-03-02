#!/bin/bash

DockerImg="myomtdstore"
TARGERCONFIG="/opt/omtd-store/scripts/config.properties"

docker cp $1 $DockerImg:$TARGERCONFIG


