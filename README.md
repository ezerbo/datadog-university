# Enrollments Service (Datadog APM Demo)
A Java service to demonstrate a few features of Datadog:
- [APM (Application Performance Monitoring)](https://docs.datadoghq.com/tracing/)
- [Logs](https://docs.datadoghq.com/logs/)
- [Synthetic Monitoring](https://docs.datadoghq.com/synthetics/)
- [Monitors & SLOs](https://docs.datadoghq.com/dashboards/widgets/slo)
- [Dashboards](https://docs.datadoghq.com/dashboards)
## Tech Stack
    - Java 8
    - Spring Boot 2.7.9
    - dd-trace-api 1.9.0
    - H2 (In-Memory)

## Endpoints
Two resources are exposed, /students & /courses. CRUD operations can be performed on them.
In addition to those operations, the enrollment records for a student are available at ***/students/{id}/enrollments***
and the list of students enrolled into a course can be retrieved from ***/courses/{id}/enrollments***

## Docker

A [Dockerfile](Dockerfile) (find details below) is provided to run the app both locally and on ECS.
***ENV*** & ***VERSION*** are used in tandem with the service's name to enable Datadog's [Unified Service Tagging](https://docs.datadoghq.com/getting_started/tagging/unified_service_tagging/).

To allow the collection of infrastructure metrics and link telemetry data to the source code, a few docker labels are required:
    
1. Infrastructure
   - com.datadoghq.tags.env
   - com.datadoghq.tags.service
   - com.datadoghq.tags.version
2. Source code linking (Used at build time, find more details in the [build script](push-image.sh))
   - org.opencontainers.image.revision (commit id)
   - org.opencontainers.image.source (url of the code repository, without the scheme)
    
- provides environment variables for unified service monitoring
- labels are required to collect infrastructure metrics
- downloads the latest Datadog Java tracer
- Start the app

Dockerfile
```
FROM openjdk:8-jdk-alpine

# default values for dd.env, dd.version, etc
ENV  ENV=dev VERSION=1.0 PORT=8080 PROFILE=default

LABEL com.datadoghq.tags.env="$ENV"
LABEL com.datadoghq.tags.service="enrollments-service"
LABEL com.datadoghq.tags.version="$VERSION"

VOLUME /tmp
RUN apk --no-cache add curl
RUN wget -O dd-java-agent.jar https://dtdg.co/latest-java-tracer
COPY target/*.jar enrollments-service.jar
COPY run.sh run.sh

ENTRYPOINT ["sh", "run.sh"]
```
The script used to launch the application references the Datadog Java tracer for instrumentation.
The tracer submits traces to the agent on port 8126 and metrics and logs on port 8125 (see the [ECS task definition](ecs-task-definitions/datadog-agent-ecs.json) for more details).

When running locally, a docker network is created to allow the app container to communicate with the agent's.
On ECS, however, the environment variable 'DD_AGENT_HOST' is used in the application container to allow the tracer to know where to submit telemetry data.

run.sh

```
#!/bin/sh

: "${DD_AGENT_HOST:=$(curl http://169.254.169.254/latest/meta-data/local-ipv4)}"

export DD_AGENT_HOST

java -javaagent:./dd-java-agent.jar \
 -Ddd.env="$ENV" \
 -Ddd.service=enrollments-service \
 -Ddd.version="$VERSION" \
 -Ddd.service.mapping=h2:enrollments_db \
 -Dserver.port="$PORT" \
 -Dspring.profiles.active="$PROFILE" \
 -jar enrollments-service.jar
```

## Building, Running Locally & Pushing to Docker Hub

The application can be build using the maven wrapper as follows

```
./mvnw clean install (Include '-DskipTests' to skip the tests)
```

A convenience script, [run-local.sh](run-local.sh) as been provided to build the .jar file, the docker image and run the application container.
By default, it only runs the latest container available locally

```
./run-local.sh
```

To build the jar file and a new image, use the '-b' flag and set it to 'true'

```
./run-local.sh -b true
```

When the application successfully starts, the swagger documentation can be accessed from [here](http://localhost:8080/swagger-ui/index.html)

## ECS Task Definitions
Two task definitions are available here: [the agent's](ecs-task-definitions/datadog-agent-ecs.json) and [the application's](ecs-task-definitions/enrollments-service-ecs.json). 
A few features of the agent are turned on including APM, Log Collection, Networking, etc.

The DD_API_KEY environment variable is set through AWS Secrets Manager. Secrets are retrieved by specifying the execution role's and the secrets' ARNs.

```
"executionRoleArn": "arn:aws:iam::<account_id>:role/ecsTaskExecutionRole"

"secrets": [
  {
     "valueFrom": "arn:aws:secretsmanager:<aws_region>:<account_id>:secret:<key_name>:<secret_key>::", (Secret's ARN)
     "name": "DD_API_KEY"
  }
]
```
The following scripts, although incomplete, can be used to update task definitions in ECS. (Make sure you are already logged in) 
[App's task definition](ecs-task-definitions/update-es-etd.sh), [Agent's](ecs-task-definitions/update-dd-agent-etd.sh)

## Running on ECS

The agent's container has to run as a daemon so that a single instance is created on each EC2 instances in the cluster.
The app can be run as a replica.

Do not forget to create CloudWatch log groups for both the app and the agent. The names can be found in the respective task definitions.

## Datadog Service Catalog Entries

The Datadog integration for GitHub scans the root folder of every repository with read permissions for a 'service.datadog.yaml' file, which is used to create entries in the Service Catalog.
This app uses one to create two entries, one for the service itself and the second for the H2 database.


