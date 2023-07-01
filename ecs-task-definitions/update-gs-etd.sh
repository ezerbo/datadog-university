#!/bin/sh

aws ecs register-task-definition --cli-input-json file://./grades-service-ecs.json