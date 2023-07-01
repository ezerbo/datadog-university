#!/bin/sh

: "${DD_AGENT_HOST:=$(curl http://169.254.169.254/latest/meta-data/local-ipv4)}"

export DD_AGENT_HOST

java -javaagent:./dd-java-agent.jar \
 -XX:FlightRecorderOptions=stackdepth=256 \
 -Ddd.env="$ENV" \
 -Ddd.service=tuition-service \
 -Ddd.version="$VERSION" \
 -Ddd.service.mapping=h2:tuition_db \
 -Dserver.port="$PORT" \
 -Dspring.profiles.active="$PROFILE" \
 -jar tuition-service.jar