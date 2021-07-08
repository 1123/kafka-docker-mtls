#!/bin/bash

set -u -e

echo "Creating topic teams"
kafka-topics \
  --bootstrap-server kafka:12091 \
  --create \
  --topic teams \
  --partitions 1 \
  --replication-factor 1 || echo "could not create topic; it may already exist"

echo "Importing teams.json"
cat /tmp/sample_data/teams.json | kafka-console-producer \
  --bootstrap-server kafka:12091 \
  --topic teams
