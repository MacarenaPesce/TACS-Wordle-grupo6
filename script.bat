rem Cosas a mejorar: 
rem   Usar compose
rem   EN vez de hacer el run hacer un docker exec, en lugar de levantar un nuevo container para ejecutar comandos a mysql
rem   Parametrizar url db y url del back en springboot, se pasa con -e al docker

rem   hacemos un Clean up del ambiente
docker stop test-mysql backend frontend
docker rm test-mysql backend frontend
docker network rm mired

rem Creamos la red mired
docker network create mired
rem Creamos el volumen
docker volume create mysql-db-data
docker run --network mired --name test-mysql -p3333:3306 -e MYSQL_ROOT_PASSWORD=root --mount src=mysql-db-data,dst=/var/lib/mysql -d mysql:8.0

cd ./backend
docker build -t grupo6/backend .
docker run -d --network mired --name backend -it -p8080:8080 grupo6/backend

cd ../frontend
docker build -t grupo6/frontend .
docker run -d --network mired --name frontend -p3000:3000 grupo6/frontend

rem Este ps solo para que de unos segundos que el container levante ya que si hacemos el exec si aun no termino de levantar todo, nos da error.
docker ps
rem ingresamos al container con su name para crear la database
docker exec test-mysql mysql -proot -e "CREATE DATABASE wordle;"

rem Despues de levantar el container del back ejecutamos el data.sql que crea la vista ranking
cd ../backend/src/main/resources
docker exec -i test-mysql sh -c "exec mysql -uroot -proot" < data.sql
