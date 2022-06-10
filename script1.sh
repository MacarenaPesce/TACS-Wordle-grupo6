#!/bin/bash
#Cosas a mejorar: 
#   Usar compose
#   EN vez de hacer el run hacer un docker exec, en lugar de levantar un nuevo container para ejecutar comandos a mysql
#   Parametrizar url db y url del back en springboot, se pasa con -e al docker

docker stop test-mysql backend frontend
docker rm test-mysql backend frontend
docker network rm mired
# Creamos la red mired
docker network create mired
# Creamos el volumen
docker volume create mysql-db-data
docker run --network mired --name test-mysql -p3333:3306 -e MYSQL_ROOT_PASSWORD=root --mount src=mysql-db-data,dst=/var/lib/mysql -d mysql:8.0
# Este ps solo para que de unos segundos que el container levante ya que si hacemos el exec si aun no termino de levantar todo, nos da error.
docker ps
# ingresamos al container con su name para crear la database
docker exec test-mysql mysql -proot -e "CREATE DATABASE wordle;"

cd ./backend
docker build -t grupo6/backend .
docker run -d --network mired --name backend -it -p8080:8080 grupo6/backend

# Despues de levantar el container del back ejecutamos el data.sql que crea la vista ranking
#cd ./src/main/resources
#docker exec -i test-mysql sh -c "exec mysql -uroot -proot" < data.sql

cd ../../../../frontend
docker build -t grupo6/frontend .
docker run -d --network mired --name frontend -p3000:3000 grupo6/frontend
