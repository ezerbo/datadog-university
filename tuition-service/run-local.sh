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
      -t ezerbo/tuition-service \
      --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
      --label org.opencontainers.image.source=github.com/ezerbo/tuition-service
   else
     echo 'Maven build failed, please fix it and try again'
   fi
fi

CONTAINER_STATUS=$(docker inspect -f '{{.State.Status}}' tuition-service)

if [ "$CONTAINER_STATUS" = "exited" ]; then
  docker rm tuition-service
fi

if [ "$CONTAINER_STATUS" = "running" ]; then
  docker stop tuition-service && docker rm tuition-service
fi

docker run -d --name tuition-service \
 --network dd_demo_net \
 -e ENV=dev \
 -e VERSION=1.0 \
 -e PORT=8082 \
 -e DD_AGENT_HOST=host.docker.internal \
 -p 8082:8082 \
 -it ezerbo/tuition-service:latest

docker logs --follow tuition-service