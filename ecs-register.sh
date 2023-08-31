#!/bin/sh

aws ecs register-task-definition --cli-input-json file://./datadog-agent-ecs.json \
    && aws ecs register-task-definition --cli-input-json file://./enrollments-service-ecs.json \
    && aws ecs register-task-definition --cli-input-json file://./grades-service-ecs.json \
    && aws ecs register-task-definition --cli-input-json file://./tuition-service-ecs.json