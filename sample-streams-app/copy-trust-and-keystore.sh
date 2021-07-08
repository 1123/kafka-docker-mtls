#!/bin/bash

# This is just for development purposes. 
# On a production system, the key and truststores should be kept in a safe place where only the client application has access. 
cp ../kafka/certs/kafka.client.keystore.jks /tmp
cp ../kafka/certs/kafka.client.truststore.jks /tmp
