ARG BASE_IMAGE=eclipse-temurin
ARG JRE_VERSION=17-jdk

FROM ${BASE_IMAGE}:${JRE_VERSION} as builder
WORKDIR /build
COPY webapp/target/*-exec.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM ${BASE_IMAGE}:${JRE_VERSION}

USER root
WORKDIR /app

ENV APP_USER=1000
RUN chown -R ${APP_USER}:0 /app
USER ${APP_USER}

COPY --from=builder build/dependencies/ ./
COPY --from=builder build/snapshot-dependencies/ ./
COPY --from=builder build/spring-boot-loader/ ./
COPY --from=builder build/application/ ./
COPY lib/spring-instrument.jar spring-instrument.jar
COPY docker/entrypoint.sh entrypoint.sh

ENV PORT=8080

ENTRYPOINT ["/app/entrypoint.sh"]
