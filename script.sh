#!/bin/bash

cd ./backend
docker build -t grupo6/backend .
docker run --name backend -d -it -p8080:8080 grupo6/backend

cd ../frontend
docker build -t grupo6/fronted .
docker run -d -p3000:3000 grupo6/fronted

docker run --name test-mysql -p3333:3306 -it -e MYSQL_ROOT_PASSWORD=root -d mysql:latest
docker run -it --link test-mysql:mysql --rm mysql sh -c 'exec mysql -h"$MYSQL_PORT_3306_TCP_ADDR" -P"$MYSQL_PORT_3306_TCP_PORT" -uroot -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD" -e "CREATE DATABASE wordle; exit;"'


