export DOCKER_TAG_NAME := savantly/nexus-command
export PROJECT_ROOT := $(shell pwd)
VERSION := $(shell mvn -q \
    -Dexec.executable=echo \
    -Dexec.args='$${project.version}' \
    --non-recursive \
    exec:exec)
# Strip the -SNAPSHOT suffix
VERSION := $(shell echo $(VERSION) | sed 's/-SNAPSHOT//')
TAGGED_VERSION := $(VERSION)
NEXT_VERSION := $(shell echo $(VERSION) | awk -F. '{$$NF = $$NF + 1;} 1' | sed 's/ /./g')

GIT_COMMIT := $(shell git rev-parse --short HEAD)

.PHONY: get-version
get-version:
	@echo $(VERSION)

.PHONY: dev
dev:
	$(call setup_env, .env)
	@echo "Running the program"
	./mvnw install -DskipTests -U
	./mvnw -pl webapp spring-boot:run

.PHONY: test-docker
test-docker: build-image
	$(call setup_env, .env)
	@echo "Running the program"
	docker compose -f docker-compose.yml up


.PHONY: dev-docker
dev-docker:
	$(call setup_env, .env)
	@echo "Running the program"
	docker compose -f docker-compose.dev.yml up


.PHONY: start-kafka
start-kafka:
	@echo "Running the program"
	docker compose -f docker-compose.kafka.yml up


.PHONY: test
test:
	@echo "Running the tests"
	./mvnw test integration-test

.PHONY: clean
clean:
	@echo "Cleaning the project"
	./mvnw clean

.PHONY: build-image
build-image:
	@echo "Building the project"
	./mvnw install -DskipTests
	docker build \
	-f docker/Dockerfile \
	-t ${DOCKER_TAG_NAME}:${VERSION} \
	-t ${DOCKER_TAG_NAME}:latest \
	.

.PHONY: push-image
	docker push ${DOCKER_TAG_NAME}:${VERSION}


.PHONY: run-image
run-image:
	docker compose up

.PHONY: run-image-postgres
run-image-postgres:
	docker compose -f docker-compose.postgres.yml up


.PHONY: ensure-git-repo-pristine
ensure-git-repo-pristine:
	@echo "Ensuring git repo is pristine"
	@[[ $(shell git status --porcelain=v1 2>/dev/null | wc -l) -gt 0 ]] && echo "Git repo is not pristine" && exit 1 || echo "Git repo is pristine"


.PHONY: bump-version
bump-version:
	@echo "Bumping version to $(NEXT_VERSION)"
	./mvnw versions:set -DnewVersion=$(NEXT_VERSION)-SNAPSHOT
	./mvnw versions:commit
	git add * || true
	git commit -m "Prepared for $(NEXT_VERSION)"

.PHONY: tag-version
tag-version:
	@echo "Preparing release..."
	@echo "Version: $(VERSION)"
	@echo "Commit: $(GIT_COMMIT)"
	@echo "Image Tag: $(IMAGE_TAG)"
	./mvnw versions:set -DnewVersion=$(VERSION)
	./mvnw versions:commit
	git add * || true
	git commit -m "Published $(VERSION)"
	git tag -a $(TAGGED_VERSION) -m "Release $(VERSION)"
	git push origin $(TAGGED_VERSION)
	@echo "Tag $(TAGGED_VERSION) created and pushed to origin"

.PHONY: release
release: ensure-git-repo-pristine tag-version bump-version 
	git push
	@echo "Release $(VERSION) completed and pushed to origin"
	


define setup_env
	$(eval ENV_FILE := $(1))
	@echo " - setup env $(ENV_FILE)"
	$(eval include $(1))
	$(eval export)
endef