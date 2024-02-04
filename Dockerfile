# syntax = docker/dockerfile:1.2
FROM clojure:latest

WORKDIR /
COPY . /

RUN clojure -T:build uber

EXPOSE $PORT

ENTRYPOINT exec java $JAVA_OPTS -cp target/cheffy-backend.jar clojure.main -m cheffy-backend.server "resources/config.edn"
