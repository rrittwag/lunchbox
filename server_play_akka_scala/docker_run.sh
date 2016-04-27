#!/bin/sh

echo "Publishing Docker image locally"

SUCCESS_OUTPUT=`sbt docker:publishLocal | tee /dev/tty | grep "Successfully built "`
IMAGE_ID=`echo "$SUCCESS_OUTPUT" | sed "s/.*Successfully built \([0-9a-f]\{12\}\).*/\1/"`


if [ ! -z "$IMAGE_ID" ]; then
  echo "Starting Docker image $IMAGE_ID"
  docker run --rm \
            -p 8080:9000 \
            -e APPLICATION_SECRET="my_secret" \
            -e FACEBOOK_APPID="$FACEBOOK_APPID" \
            -e FACEBOOK_APPSECRET="$FACEBOOK_APPSECRET" \
            -it \
            "$IMAGE_ID"
fi