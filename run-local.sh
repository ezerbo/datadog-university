#!/bin/bash

while getopts "b:" option; do

  case $option in
    b)
      BUILD_IMAGE="$OPTARG";;
    *)
      BUILD_IMAGE=false;;
  esac
done

if [ "$BUILD_IMAGE" = true ]; then
   if ./mvnw clean install -DskipTests; then
     docker build . \
      -t ezerbo/enrollments-service \
      --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
      --label org.opencontainers.image.source=github.com/ezerbo/enrollments-service
   else
     echo 'Maven build failed, please fix it and try again'
   fi
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