#!/bin/sh

if ./mvnw clean install -DskipTests; then
  docker buildx build . \
   --platform linux/amd64,linux/arm64 \
   --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
   --label org.opencontainers.image.source=github.com/ezerbo/tuition-service \
   --push -t ezerbo/tuition-service:latest
else
  echo 'Maven build failed, please fix it and try again'
fi