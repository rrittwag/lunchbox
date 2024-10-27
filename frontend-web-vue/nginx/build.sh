#!/usr/bin/env sh

docker image inspect docker.rori.dev/nginx-brotli:latest >/dev/null
if [ $? -eq 0 ]; then
  echo "Image existiert bereits!"
  exit 0
fi

# generate Docker image for latest nginx with brotli compression
# -> https://github.com/nginxinc/docker-nginx/tree/master/modules
wget -O Dockerfile.nginx https://raw.githubusercontent.com/nginxinc/docker-nginx/master/modules/Dockerfile.alpine

docker build \
  --file Dockerfile.nginx \
  --build-arg NGINX_FROM_IMAGE=nginx:mainline-alpine-slim \
  --build-arg ENABLED_MODULES="brotli" \
  --tag docker.rori.dev/nginx-brotli:latest \
  --pull .
