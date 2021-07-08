#!/bin/bash

set -e 
set -u

kafka-console-consumer \
  --bootstrap-server localhost:11091 \
  --consumer.config client.properties \
  --topic teams \
  --from-beginning
