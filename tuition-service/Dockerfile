FROM eclipse-temurin:8-jre

# default values for dd.env, dd.version, etc
ENV  ENV=dev VERSION=1.0 PORT=8082 PROFILE=default

LABEL com.datadoghq.tags.env="$ENV"
LABEL com.datadoghq.tags.service="tuition-service"
LABEL com.datadoghq.tags.version="$VERSION"

VOLUME /tmp
RUN apt-get -y update && apt-get -y install curl
RUN wget -O dd-java-agent.jar https://dtdg.co/latest-java-tracer
COPY target/*.jar tuition-service.jar
COPY run.sh run.sh

ENTRYPOINT ["sh", "run.sh"]