#!/bin/bash

#
# Creates a Docker image from sources, publishes the image locally and runs it.
# Use the script when running production mode. Change variables first!
#

APPLICATION_SECRET="mysecret" # generate via "sbt playGenerateSecret"
FACEBOOK_APPID="0"
FACEBOOK_APPSECRET="0"

docker create --name="lunchbox-server" \
              -p 8080:9000 \
              -e APPLICATION_SECRET="$APPLICATION_SECRET" \
              -e FACEBOOK_APPID="$FACEBOOK_APPID" \
              -e FACEBOOK_APPSECRET="$FACEBOOK_APPSECRET" \
              "rori/lunchbox-server"

docker network connect openocr lunchbox-server

docker start lunchbox-server