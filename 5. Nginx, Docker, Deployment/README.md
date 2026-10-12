# Nginx example

This examples demonstrates Nginx functionalities of serving static content and being reverse proxy.

## Example content

### productsapp

This is a Spring Boot application with simple REST API for managing products. It can be started as a regular Spring Boot application by using run configuration in IDE or by running command in terminal. It uses PostgreSQL for storing data. It listens on the port 8085.

### carsapp

This is a Spring Boot application with simple REST API for managing cars. It can be started as a regular Spring Boot application by using run configuration in IDE or by running command in terminal. It uses PostgreSQL for storing data. It listens on the port 8086.

### view

This directory contains simple html page that uses fetch API to consume data from two previously mentioned Spring Boot applications. 

### nginx.conf

This file presents a simple Nginx configuration.

## Requirements

* Java 17
* Maven
* [PostgreSQL database](https://www.postgresql.org/) listening on port 5432
* [Nginx](https://nginx.org/en/)

## Setup

1. Install Java and Maven
2. Install Nginx
3. Install and start PostgreSQL
   - Set credentials for authentication in application.properties (`spring.datasource.username` and `spring.datasource.password`) in both Spring Boot applications and create databases with names `productsdb` and `carsdb`
4. Load dependencies of both Spring Boot applications (`pom.xml`) with Maven and start applications
5. Copy `nginx.conf` file into `conf` directory where Nginx is installed
6. Inside `nginx.conf` in **line 45**, replace `"path_to_example_5\\view"` with path to *view* directory from Example 5 on your disk.
7. Run applications in the following order:
   1) Both Spring Boot applications
   2) Nginx
8. Open browser and navigate to `localhost:8787`; index.html page should be seen