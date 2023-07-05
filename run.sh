#!/bin/bash
docker rm -f demo-db
docker-compose -f database.yml up -d
