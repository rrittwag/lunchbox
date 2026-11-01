#!/usr/bin/env bash

DOCKER_CLI="${DOCKER_CLI:-docker}"

$DOCKER_CLI inspect container.rori.dev/runtime/nginx-brotli:latest >/dev/null
if [ $? -eq 0 ]; then
  echo "Image exists locally"
  exit 0
fi

$DOCKER_CLI pull container.rori.dev/runtime/nginx-brotli:latest >/dev/null
if [ $? -eq 0 ]; then
  echo "Image downloaded"
  exit 0
fi

# generate Docker image for latest nginx with brotli compression
# -> https://github.com/nginxinc/docker-nginx/tree/master/modules
curl -o Dockerfile.nginx https://raw.githubusercontent.com/nginxinc/docker-nginx/master/modules/Dockerfile.alpine

$DOCKER_CLI build \
  --file Dockerfile.nginx \
  --build-arg NGINX_FROM_IMAGE=docker.io/library/nginx:mainline-alpine-slim \
  --build-arg ENABLED_MODULES="brotli" \
  --tag container.rori.dev/runtime/nginx-brotli:latest \
  --pull .
