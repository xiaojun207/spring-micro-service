#!/bin/bash
# eg.: bash ./deploy/build.sh test auth 1.0.0

set -xe

DOCKER_BASE_REPO=$1
APP_NAME=$2
DOCKER_BUILD_TAG=$3

declare -A modules=(
    ["auth"]="ms-auth"
    ["test"]="ms-test"
    ["gateway"]="ms-gateway"
)

module="${modules[${APP_NAME}]}"

#################
# get port #
#################
conf="./$module/src/main/resources/application.yml"
port=$(nl ${conf} | sed -n '/  port: /p' | awk 'NR=1{print $3}' | awk 'NR==1')
echo "port:$port"

Dockerfile="Dockerfile"

######################
# build java package #
######################
mvn clean install -pl ${module} -am
#mvn clean install -DskipTest

#####################
# build docker image #
#####################
docker build --build-arg jarPath=${module}/target/*.jar --build-arg module=${module} --build-arg port=$port -t ${DOCKER_BASE_REPO}/${APP_NAME}:${DOCKER_BUILD_TAG} -f deploy/${Dockerfile} .
docker tag ${DOCKER_BASE_REPO}/${APP_NAME}:${DOCKER_BUILD_TAG} ${DOCKER_BASE_REPO}/${APP_NAME}:latest
docker push ${DOCKER_BASE_REPO}/${APP_NAME}:${DOCKER_BUILD_TAG}
docker push ${DOCKER_BASE_REPO}/${APP_NAME}:latest
