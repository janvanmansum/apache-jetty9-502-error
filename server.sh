#!/usr/bin/env bash

JETTY=${1:-jetty9}

mvn -P$JETTY clean install dependency:copy-dependencies
java -cp "target/dependency/*:target/test.jar" nl.knaw.dans.test.BackendServer
