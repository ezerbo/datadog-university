#!/bin/sh
aws ecs register-task-definition --cli-input-json file://./datadog-agent-ecs.json