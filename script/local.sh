#!/bin/bash

# environment passed as a parameter
ENV=dev

# paths based on the environment
ROOT=$(git rev-parse --show-toplevel)
ENV_FILE="$ROOT/env/.env.${ENV}"

# dev: make local containers
function setup_dev() {
  DOCKER_FILE="$ROOT/docker/${ENV}.docker-compose.yml"
  echo "Build docker compose for environment: $ENV"
  docker compose --env-file $ENV_FILE --file $DOCKER_FILE down
  docker compose build --no-cache
  docker compose --env-file $ENV_FILE --file $DOCKER_FILE up --detach --force-recreate --remove-orphans
}

setup_dev