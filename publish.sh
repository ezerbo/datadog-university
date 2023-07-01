#!/bin/sh

if ./mvnw clean install -DskipTests; then
  docker buildx build ./enrollments-service \
   --platform linux/amd64,linux/arm64 \
   --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
   --label org.opencontainers.image.source=github.com/ezerbo/enrollments-service \
   --push -t ezerbo/enrollments-service:latest

   docker buildx build ./grades-service \
      --platform linux/amd64,linux/arm64 \
      --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
      --label org.opencontainers.image.source=github.com/ezerbo/grades-service \
      --push -t ezerbo/grades-service:latest

  docker buildx build ./tuition-service \
        --platform linux/amd64,linux/arm64 \
        --label org.opencontainers.image.revision="$(git rev-parse HEAD)" \
        --label org.opencontainers.image.source=github.com/ezerbo/tuition-service \
        --push -t ezerbo/tuition-service:latest
else
  echo 'Maven build failed, please fix it and try again'
fi