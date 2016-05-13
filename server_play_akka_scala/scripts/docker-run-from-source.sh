#!/bin/sh

#
# Creates a Docker image from sources, publishes the image locally and runs it.
# Use the script when developing and testing the server as a Docker image.
#

APPLICATION_SECRET="mysecret" # generate via "sbt playGenerateSecret"
FACEBOOK_APPID="0"
FACEBOOK_APPSECRET="0"


echo "Publishing Docker image locally"

SUCCESS_OUTPUT=`sbt docker:publishLocal | tee /dev/tty | grep "Successfully built "`
IMAGE_ID=`echo "$SUCCESS_OUTPUT" | sed "s/.*Successfully built \([0-9a-f]\{12\}\).*/\1/"`


if [ ! -z "$IMAGE_ID" ]; then
  echo "Starting Docker image $IMAGE_ID"
  docker run --rm \
            -p 8080:9000 \
            -e APPLICATION_SECRET="$APPLICATION_SECRET" \
            -e FACEBOOK_APPID="$FACEBOOK_APPID" \
            -e FACEBOOK_APPSECRET="$FACEBOOK_APPSECRET" \
            -it \
            "$IMAGE_ID"
fi