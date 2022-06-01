# Proyecto WORDLE

## TACS - UTN 2022


### Docker

Crear una imagen:

docker build -t grupo6/backend .

Correr la imagen:

-P ejecuta la imagen y mapeale puertos random a todo lo expuesto del docker
docker run -d -p8080:8080 grupo6/backend:latest
docker run -d -P grupo6/backend:latest


docker run -it grupo6/backend:latest

docker ps
CONTAINER ID   IMAGE                   COMMAND                  CREATED         STATUS         PORTS                                         NAMES
27db3aee37e8   grupo6/backend:latest   "java -jar target/wo…"   4 seconds ago   Up 4 seconds   0.0.0.0:49153->8080/tcp, :::49153->8080/tcp   elastic_swartz

Parar la imagen
docker stop <NAME>

### BD MySQL
Si aún no tenemos creada la BD localmente:

1- Accedemos a la consola de MySQL (pass: root)
>mysql -u root

2- Creamos la BD
>CREATE DATABASE wordle;

3- Luego de levantar el proyecto backend, se debe ejecutar el script `backend\src\main\resources\data.sql` sobre la base de datos, en caso de no hacerlo automáticamente.

4- Cerramos la BD
>exit;