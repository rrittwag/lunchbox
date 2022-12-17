#!/usr/bin/env sh

# generate Docker image for latest nginx with brotli compression
# -> https://github.com/nginxinc/docker-nginx/tree/master/modules
wget -O Dockerfile.nginx https://raw.githubusercontent.com/nginxinc/docker-nginx/master/modules/Dockerfile.alpine
sed -i 's/mainline-alpine/mainline-alpine-slim/g' Dockerfile.nginx
docker build -f Dockerfile.nginx --build-arg ENABLED_MODULES="brotli" -t docker.rori.dev/nginx-brotli:latest --pull .
