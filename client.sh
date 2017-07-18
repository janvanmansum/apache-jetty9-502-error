#!/usr/bin/env bash

java -cp "target/dependency/*:target/test.jar" nl.knaw.dans.test.Client $1 $2
