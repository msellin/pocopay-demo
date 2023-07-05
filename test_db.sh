#!/bin/bash
docker rm -f test-db
docker-compose -f db.yml up -d
