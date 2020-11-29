#!/bin/sh
JAVA_OPT=${JAVA_OPT}
if [ -z "${JAVA_OPT}" ]; then
  exec java -jar /app.jar
else
  exec java ${JAVA_OPT} -jar /app.jar
fi
