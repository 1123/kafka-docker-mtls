#!/bin/bash

set -e 
set -u

kafka-topics \
  --bootstrap-server localhost:11091 \
  --command-config client.properties \
  --create \
  --topic test \
  --partitions 1 \
  --replication-factor 1 || echo "could not create topic; it may already exist"

kafka-console-producer \
  --bootstrap-server localhost:11091 \
  --producer.config client.properties \
  --topic test
