#!/bin/sh

aws ecs register-task-definition --cli-input-json file://./enrollments-service-ecs.json