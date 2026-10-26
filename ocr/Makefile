.EXPORT_ALL_VARIABLES:
PACKAGE_NAME = lunchbox
APP_NAME = ocr
APP_VERSION = DEV
BUILD_DIR = $(PWD)/build
LDFLAGS = -s -w -X rori.dev/$(PACKAGE_NAME)/$(APP_NAME)/app.Version=$(APP_VERSION)
CGO_ENABLED = 0
DOCKER_CLI ?= docker

.PHONY: generate
generate:
	@go generate ./...

.PHONY: build
build:
	@go build -ldflags '$(LDFLAGS)' -o $(BUILD_DIR)/$(APP_NAME) .

.PHONY: build.arm64
build.arm64:
	@GOOS=linux GOARCH=arm64 go build -ldflags '$(LDFLAGS)' -o $(BUILD_DIR)/$(APP_NAME)-arm64 .

.PHONY: build.dryrun
build.dryrun:
	@go build -ldflags '$(LDFLAGS)' -o /dev/null .

.PHONY: run
run: build
	@$(BUILD_DIR)/$(APP_NAME)

.PHONY: clean
clean:
	@rm -rf $(BUILD_DIR)

.PHONY: format
format:
	@gofmt -l -s -w .

.PHONY: format.check
FORMAT_RESULT := $(shell gofmt -l -s .)
format.check:
	@if [ ! -z "$(FORMAT_RESULT)" ]; then echo "$(FORMAT_RESULT)"; exit 1; fi

.PHONY: lint
lint:
	@golangci-lint run -v --timeout 15m

.PHONY: test.unit
test.unit:
	@TZ="Europe/Berlin" \
	gotestsum --junitfile test-report.xml --format testname -- -short -v -timeout 10s ./...

.PHONY: test
test:
	@CGO_ENABLED=1 \
	TZ="Europe/Berlin" \
	gotestsum --junitfile test-report.xml --format testname -- -race -v -timeout 2m -cover -bench=. ./...

.PHONY: container.build
container.build:
	@$(DOCKER_CLI) build -t container.rori.dev/$(PACKAGE_NAME)/$(APP_NAME):latest .

.PHONY: container.up
container.up:
	@$(DOCKER_CLI) compose up -d

.PHONY: container.down
container.down:
	@$(DOCKER_CLI) compose down

.PHONY: upgrade
upgrade:
	@go get -u
	@go mod tidy
