# syntax = docker/dockerfile:1.2
FROM clojure:latest

WORKDIR /
COPY . /

RUN clojure -T:build uber

EXPOSE $PORT

ENTRYPOINT exec java $JAVA_OPTS -jar target/cheffy-backend.jar
