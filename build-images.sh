#!/bin/bash

set -u -e

rm -rf certs
mkdir -p certs

openssl req \
  -new -x509 \
  -keyout certs/snakeoil-ca-1.key \
  -out certs/snakeoil-ca-1.crt \
  -days 365 \
  -subj '/CN=ca1.test.confluentdemo.io/OU=TEST/O=CONFLUENT/L=PaloAlto/ST=Ca/C=US' \
  -passin pass:confluent \
  -passout pass:confluent

for user in kafka client zookeeper; do
  keytool -genkey -noprompt \
    -alias $user \
    -dname "CN=$user,OU=TEST,O=CONFLUENT,L=PaloAlto,S=Ca,C=US" \
    -ext "SAN=dns:$user,dns:localhost" \
    -keystore certs/kafka.$user.keystore.jks \
    -keyalg RSA \
    -storepass confluent \
    -keypass confluent \
    -storetype pkcs12

  keytool \
    -keystore certs/kafka.$user.keystore.jks \
    -alias $user \
    -certreq \
    -file certs/$user.csr \
    -storepass confluent \
    -keypass confluent \
    -ext "SAN=dns:$user,dns:localhost"

  openssl x509 \
    -req -CA certs/snakeoil-ca-1.crt \
    -CAkey certs/snakeoil-ca-1.key \
    -in certs/$user.csr \
    -out certs/$user-ca1-signed.crt \
    -sha256 \
    --CAcreateserial \
    -days 365 \
    -passin pass:confluent \
    -extensions v3_req \
    -extfile <(cat <<EOF
[req]
distinguished_name = req_distinguished_name
x509_extensions = v3_req
prompt = no
[req_distinguished_name]
CN = $user
[v3_req]
extendedKeyUsage = serverAuth, clientAuth
subjectAltName = @alt_names
[alt_names]
DNS.1 = $user 
DNS.2 = localhost
EOF
)

  keytool \
    -noprompt \
    -keystore certs/kafka.$user.keystore.jks \
    -alias snakeoil-caroot \
    -import \
    -file certs/snakeoil-ca-1.crt \
    -storepass confluent \
    -keypass confluent

  keytool \
    -noprompt \
    -keystore certs/kafka.$user.keystore.jks \
    -alias $user \
    -import \
    -file certs/$user-ca1-signed.crt \
    -storepass confluent \
    -keypass confluent \
    -ext "SAN=dns:$user,dns:localhost"

  keytool \
    -noprompt \
    -keystore certs/kafka.$user.truststore.jks \
    -alias snakeoil-caroot \
    -import \
    -file certs/snakeoil-ca-1.crt \
    -storepass confluent \
    -keypass confluent

done

rm -r kafka/certs || echo could not delete directory
cp -r certs kafka
cp zookeeper_jaas.conf kafka/certs/
cp kafka_sslkey_creds kafka/certs/
cp kafka_keystore_creds kafka/certs/
cp kafka_truststore_creds kafka/certs/
cp broker_jaas.conf kafka/certs/

cd kafka && docker build . -t cp-server-mtls:6.2.0
