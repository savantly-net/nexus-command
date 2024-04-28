export DOCKER_TAG_NAME := savantly/nexus-command
export DOCKER_TAG_VERSION := 2.0.0
export PROJECT_ROOT := $(shell pwd)

.PHONY: run
run:
	@echo "Running the program"
	mvn install -DskipTests
	mvn -pl webapp spring-boot:run

.PHONY: test
test:
	@echo "Running the tests"
	mvn test

.PHONY: clean
clean:
	@echo "Cleaning the project"
	mvn clean

.PHONY: build-image
build:
	@echo "Building the project"
	mvn install -DskipTests
	docker build \
	-f docker/Dockerfile \
	-t ${DOCKER_TAG_NAME}:${DOCKER_TAG_VERSION} \
	-t ${DOCKER_TAG_NAME}:latest \
	.

.PHONY: push-image
	docker push ${DOCKER_TAG_NAME}:${DOCKER_TAG_VERSION}


.PHONY: run-image
run-image:
	docker compose up