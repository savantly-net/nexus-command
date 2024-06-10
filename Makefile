export DOCKER_TAG_NAME := savantly/nexus-command
export DOCKER_TAG_VERSION := 2.0.0
export PROJECT_ROOT := $(shell pwd)

.PHONY: dev
dev:
	$(call setup_env, .env)
	@echo "Running the program"
	mvn install -DskipTests
	mvn -pl webapp spring-boot:run

.PHONY: test
test:
	@echo "Running the tests"
	mvn test integration-test

.PHONY: clean
clean:
	@echo "Cleaning the project"
	mvn clean

.PHONY: build-image
build-image:
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

.PHONY: run-image-postgres
run-image-postgres:
	docker compose -f docker-compose.postgres.yml up


define setup_env
	$(eval ENV_FILE := $(1))
	@echo " - setup env $(ENV_FILE)"
	$(eval include $(1))
	$(eval export)
endef