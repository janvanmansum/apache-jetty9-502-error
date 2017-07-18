#!/usr/bin/env bash

mvn dependency:copy-dependencies
java -cp "target/dependency/*:target/apache-jetty9-502-error.jar" nl.knaw.dans.test.BackendServer
