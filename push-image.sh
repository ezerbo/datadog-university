#!/bin/sh

# TODO: Only build image when Maven build succeeds
./mvnw clean install -DskipTests
docker buildx build --platform linux/amd64,linux/arm64 \
 --push -t ezerbo/enrollments-service:latest .