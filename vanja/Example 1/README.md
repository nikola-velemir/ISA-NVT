# MQTT and AMQP example

This examples demonstrates communication via MQTT protocol (version 5) and AMQP protocol and storing data into PostgreSQL database.

## Applications

### backend-example

This is Spring Boot application that serves as MQTT and AMQP subscriber and saves data into database. It uses Maven to manage dependencies. It can be started as regular Spring Boot application by using run configuration in IDE or by running command in terminal.

It has API with two actions:

- `GET /topics?topic={value}` - gets last 10 messages that belong to topic with name *value*
- `POST /topics/mqtt` - Adds subscription to Mosquitto topic that is defined in request body
- `POST /topics/amqp` - Adds subscription to RabbitMQ topic that is defined in request body

Directory **postman** provides collection of Postman requests to test the API.

### mqtt-client
This is a simple Java console application that serves as MQTT publisher and periodically sends randomly generated data. It uses Maven to manage dependencies. It can be started by using run configuration in IDE or by running command in terminal. When running this application, it is mandatory to specify `topic name` as **CLI argument**. Topic name is name of some topic in Mosquitto MQTT broker where message will be published to.

### mqtt-go-example.zip 
This zip archive contains an example of a simple aplication written in Go that serves as MQTT client. This archive contains procedure for running 1000 instances of the application which can can be useful when performing the performance testing of the system.

### amqp-client
This is a simple Java console application that serves as AMQP publisher and periodically sends randomly generated data. It uses Maven to manage dependencies. It can be started by using run configuration in IDE or by running command in terminal. When running this application, it is mandatory to specify `topic name` as **CLI argument**. Topic name is name of some topic in RabbitMQ AMQP broker where message will be published to.

## Requirements

* Java 17
* Maven
* [PostgreSQL database](https://www.postgresql.org/) listening on port 5432
* [Mosquitto MQTT broker](https://mosquitto.org/) listening on port 1883
* [RabbitMQ AMQP broker](https://www.rabbitmq.com/) listening on port 5672

## Setup

1. Install Java and Maven
2. Install and start PostgreSQL
   - Set credentials for authentication in application.properties (`spring.datasource.username` and `spring.datasource.password`) and create database with name `mqttexample`
3. Install Mosquitto, add credentials and start it
   - Open cmd (it could require running it as Administrator) and position inside directory where Mosquitto is installed (on Windows machine default installation directory is `C:\Program Files\mosquitto`) 
   - Run command `mosquitto_passwd -c <password file> <username>` (example: `mosquitto_passwd -c pwfile client`) and insert password when asked (in this example password is `password`)
   - After that check if file with credentials is generated (in this example it is `pwfile`)
   - Run Mosquitto broker ((re)start service or start stand-alone application)
   - Set required information in **application.properties** (`mqtt.host`, `mqtt.port`, `mqtt.username` and `mqtt.password`)
4. Install RabbitMQ
   - Run RabbitMQ broker
   - Set required information in **application.properties** (`amqp.host` and `amqp.port`)
5. Load dependencies of all applications (`pom.xml`) with Maven and start applications
6. Run applications in following order:
   1) Mosquitto broker and RabbitMQ broker
   2) PostgreSQL database
   3) backend-example application
   4) mqtt-client and amqp-client (one or more of them)
   5) Send POST requests to `localhost:8081/topics/mqtt` and `localhost:8081/topics/amqp` to subscribe to the same topics that mqtt-client and amqp-client applications are publishing messages to
      - you can load collection from `postman` folder into **Postman** application 