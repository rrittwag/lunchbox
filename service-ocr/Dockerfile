FROM --platform=$TARGETPLATFORM golang:1.23-alpine AS build

WORKDIR /build
COPY . .

ARG APP_VERSION=1.0.0
ENV CGO_ENABLED=0 GOOS=$TARGETOS GOARCH=$TARGETARCH

RUN go build  \
      -ldflags="-s -w -X rori.dev/lunchbox/ocr/app.Version="$APP_VERSION"" \
      -o runapp

# Add non root user
RUN addgroup -S nonroot && adduser -u 1001 -S nonroot -G nonroot -D

FROM alpine
LABEL maintainer="rori <mail@rori.info>"

WORKDIR /

COPY --from=build /etc/passwd /etc/passwd
COPY --from=build /build/runapp /

RUN apk update \
    && apk add --no-cache \
      tesseract-ocr \
      tesseract-ocr-data-deu

USER nonroot

EXPOSE 9292

ENTRYPOINT ["/runapp"]
