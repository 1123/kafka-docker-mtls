# Kafka Single Node Sample Environment with mTLS Authentication and Sample Data and Sample Spring Apps

This project provides sample docker containers for locally running a single node Kafka cluster with mTLS enabled, and the option to easily add sample data. 
The certificate configuration scripts and the docker configuration are based upon the https://github.com/confluentinc/cp-demo repository, but were heavily adopted to the needs of this repository. 
In addition Spring Boot Producer, Streams and Consumer Apps are provided and preconfigured for this development environment. 

![Overview](Docker-MTLS-Setup.png?raw=true "Overview")

## Building the Images

Prerequisites

- network access to docker hub
- openssl installed
- keytool installed
- bash 
- docker installed

The Kafka and Zookeeper images are based upon confluentinc/cp-server and confluentinc/cp-zookeeper, respectively.
They are built with a single command: 

- `./build_images.sh`

This will 
- generate a root certificate authority 
- create certificates for a client, Zookeeper Server, Kafka Broker
- sign the certificates with the certificate authority
- import the signed certs as well as the CA into keystores and truststores for each component
- copy the truststores to the docker build directories for Zookeeper and Kafka
- build the images and tag them 

Once built, you should upload the images to the image repository of your choice. 

## Using the Images

Prerequisites

- access to an image registry where the built images have been stored. Alternatively you can build the images locally. 
- `docker` and `docker-compose` for running the images. 

Running the images:

- `docker-compose up -d`

Testing if everything works: 

- inspect the logs of Zookeeper and Kafka: `docker-compose logs -f zookeeper` and `docker-compose logs -f kafka`.
- import the sample data contained in `kafka/sample_data`: `./import-sample-data-via-docker-compose.sh`
- consume the imported sample data: `./consume-sample-data.sh`. This command will require the kafka-console-consumer installed locally. 

Stopping the containers: 

- `docker-compose down`

## Running the Spring Sample Apps

![Overview](Spring-Kafka-Docker-Sample-Apps-mTLS.png?raw=true "Overview")

Prerequisites: 

- JDK 11 or higher
- A recent version of Maven
- Access to Maven Central 
- The docker demo environment up and running

Steps to run: 

- start the producer application: `cd sample-producer-app; ./copy-trust-and-keystore.sh; mvn spring-boot:run`
- start the streams app: `cd sample-streams-app; mvn spring-boot:run`
- start the consumer app: `cd sample-consumer-app; mvn spring-boot:run`
