#!/bin/bash

while getopts "b:" option; do
  # shellcheck disable=SC2220
  case $option in
    b)
      BUILD_IMAGE="$OPTARG";;
    :)
      BUILD_IMAGE=false;;
  esac
done

if [ "$BUILD_IMAGE" = true ]; then
  ./mvnw clean install -DskipTests
  docker build -t ezerbo/enrollments-service .
fi

CONTAINER_STATUS=$(docker inspect -f '{{.State.Status}}' enrollments-service)

if [ "$CONTAINER_STATUS" = "exited" ]; then
  docker rm enrollments-service
fi

if [ "$CONTAINER_STATUS" = "running" ]; then
  docker stop enrollments-service && docker rm enrollments-service
fi

docker run -d --name enrollments-service \
 --network dd_demo_net \
 -e ENV=dev \
 -e VERSION=1.0 \
 -e PORT=8080 \
 -e DD_AGENT_HOST=host.docker.internal \
 -p 8080:8080 \
 -it ezerbo/enrollments-service:latest

docker logs --follow enrollments-service