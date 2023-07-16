#!/usr/bin/env sh

# generate Docker image for latest nginx with brotli compression
# -> https://github.com/nginxinc/docker-nginx/tree/master/modules
wget -O Dockerfile.nginx https://raw.githubusercontent.com/nginxinc/docker-nginx/master/modules/Dockerfile.alpine

docker build \
  --file Dockerfile.nginx \
  --build-arg NGINX_FROM_IMAGE=nginx:mainline-alpine-slim \
  --build-arg ENABLED_MODULES="brotli" \
  --tag docker.rori.dev/nginx-brotli:latest \
  --pull .
