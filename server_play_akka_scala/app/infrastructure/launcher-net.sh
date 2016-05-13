#!/usr/bin/env bash

# Launches all components of the OpenOCR service
#
# How to run this script for generic docker
#
#   https://github.com/tleyden/open-ocr/blob/master/README.md
#
# How to run this script for Orchard docker PAAS
#
#   https://github.com/tleyden/open-ocr/wiki/Installation-on-Orchard
#

RABBITMQ_HOST=rabbitmq
RABBITMQ_PASS=aasderljcwei3jlbc93
HTTP_PORT=20080

DOCKER_PUBLIC_NETWORK=bridge
DOCKER=docker


export AMQP_URI=amqp://admin:${RABBITMQ_PASS}@${RABBITMQ_HOST}/

$DOCKER network create rabbitmq
$DOCKER network create openocr

$DOCKER run -d -p 5672:5672 -p 15672:15672 --name="rabbitmq" --net="rabbitmq" -e RABBITMQ_PASS=${RABBITMQ_PASS} tutum/rabbitmq

echo "Waiting 30s for rabbit MQ to startup .."
sleep 30 # workaround for startup race condition issue

$DOCKER run -d -p ${HTTP_PORT}:${HTTP_PORT} --name="openocr" --net="$DOCKER_PUBLIC_NETWORK" tleyden5iwx/open-ocr open-ocr-httpd -amqp_uri "${AMQP_URI}" -http_port ${HTTP_PORT}
$DOCKER network connect rabbitmq openocr

$DOCKER run -d --name="openocr-worker" --net="rabbitmq" tleyden5iwx/open-ocr open-ocr-worker -amqp_uri "${AMQP_URI}"
