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
      -t ezerbo/grades-service \
      --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
      --label org.opencontainers.image.source=github.com/ezerbo/grades-service
   else
     echo 'Maven build failed, please fix it and try again'
   fi
fi

CONTAINER_STATUS=$(docker inspect -f '{{.State.Status}}' grades-service)

if [ "$CONTAINER_STATUS" = "exited" ]; then
  docker rm grades-service
fi

if [ "$CONTAINER_STATUS" = "running" ]; then
  docker stop grades-service && docker rm grades-service
fi

docker run -d --name grades-service \
 --network dd_demo_net \
 -e ENV=dev \
 -e VERSION=1.0 \
 -e PORT=8081 \
 -e DD_AGENT_HOST=host.docker.internal \
 -e ENROLLMENTS_SERVICE_URL=http://host.docker.internal:8080 \
 -e TUITION_SERVICE_URL=http://host.docker.internal:8082/tuition \
 -p 8081:8081 \
 -it ezerbo/grades-service:latest

docker logs --follow grades-service