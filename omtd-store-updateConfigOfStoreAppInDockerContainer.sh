#!/bin/bash

DockerContainer="myomtdstore"
TARGETCONFIG="/opt/omtd-store/scripts/config.properties"

if [ "$#" -eq  "0" ]
then
	echo "Please provide a config file."
else
	SOURCECONFIG=$1
	echo "Copying $SOURCECONFIG to $TARGETCONFIG of $DockerContainer container."
	docker cp $SOURCECONFIG $DockerContainer:$TARGETCONFIG && echo "DONE!	"
fi




