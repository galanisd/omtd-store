#!/bin/bash

Dockerfile="./omtd-store.dockerfile"
DockerImg="omtd-store-docker"

docker build -f $Dockerfile -t $DockerImg .

