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
