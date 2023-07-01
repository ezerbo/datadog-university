#!/bin/sh

: "${DD_AGENT_HOST:=$(cat "$ECS_CONTAINER_METADATA_FILE" | grep "HostPrivateIPv4Address" | awk '{print $2}' | tr -d '"')}"

export DD_AGENT_HOST

exec java -javaagent:./dd-java-agent.jar \
 -XX:FlightRecorderOptions=stackdepth=256 \
 -Ddd.env="$ENV" \
 -Ddd.service=enrollments-service \
 -Ddd.version="$VERSION" \
 -Ddd.service.mapping=h2:enrollments_db \
 -Dserver.port="$PORT" \
 -Dspring.profiles.active="$PROFILE" \
 -Dapp.grades-service-config.url="$GRADES_SERVICE_URL" \
 -Dapp.tuition-service-config.url="$TUITION_SERVICE_URL" \
 -jar enrollments-service.jar