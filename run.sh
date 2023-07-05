#!/bin/bash
docker-compose down
./gradlew clean build -x test
docker-compose up -d
