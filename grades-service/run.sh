#!/bin/sh

: "${DD_AGENT_HOST:=$(curl http://169.254.169.254/latest/meta-data/local-ipv4)}"

export DD_AGENT_HOST

java -javaagent:./dd-java-agent.jar \
 -XX:FlightRecorderOptions=stackdepth=256 \
 -Ddd.env="$ENV" \
 -Ddd.service=grades-service \
 -Ddd.version="$VERSION" \
 -Ddd.service.mapping=h2:grades_db \
 -Dserver.port="$PORT" \
 -Dspring.profiles.active="$PROFILE" \
 -Dapp.enrollments-service-config.url="$ENROLLMENTS_SERVICE_URL" \
 -Dapp.tuition-service-config.url="$TUITION_SERVICE_URL" \
 -jar grades-service.jar