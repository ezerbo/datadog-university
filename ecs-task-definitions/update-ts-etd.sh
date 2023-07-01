#!/bin/sh

aws ecs register-task-definition --cli-input-json file://./tuition-service-ecs.json