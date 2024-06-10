#!/bin/bash

# libs can be added to the classpath by adding them to the /app/BOOT-INF/lib/ directory
# /app/BOOT-INF/lib/

cd /app/
ls -al
sleep 2


java ${JAVA_OPTS} -cp /app -Dserver.port=${PORT:-8080} -javaagent:/app/spring-instrument.jar -Djava.security.disableSystemPropertiesFile=true --add-opens java.base/sun.net=ALL-UNNAMED org.springframework.boot.loader.launch.JarLauncher
