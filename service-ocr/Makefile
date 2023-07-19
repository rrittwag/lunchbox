.EXPORT_ALL_VARIABLES:
APP_NAME = ocr
APP_VERSION = DEV
BUILD_DIR = $(PWD)/build
LDFLAGS = -s -w -X rori.dev/lunchbox/$(APP_NAME)/app.Version=$(APP_VERSION)
CGO_ENABLED = 0

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

.PHONY: docker.build
docker.build:
	@docker build -t docker.rori.dev/$(APP_NAME)-backend:test .

.PHONY: docker.up
docker.up:
	@docker compose up -d

.PHONY: docker.down
docker.down:
	@docker compose down

.PHONY: upgrade
upgrade:
	@go get -u
	@go mod tidy
