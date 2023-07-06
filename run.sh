#!/bin/bash
docker-compose down
docker-compose rm -f
./gradlew clean build -x test
docker-compose up
