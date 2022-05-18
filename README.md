# TACS-Wordle-grupo6

## TACS - UTN 2022

### Enunciado
https://docs.google.com/document/d/e/2PACX-1vS850Wxcrs3LThOAQamtTEhG6IEMmebXJxV3xXo-iPqCWHDI9LlncGyUONLx-hbIOBblutYCisS5aXh/pub


### Para Deployar la aplicación:
Se asume que tiene instalado Docker en su ambiente.
Luego de Clonar el proyecto, puede ejecutar el siguiente script de acuerdo a si su ambiente es Windows o Linux

### Entorno Windows:
Ejecutar script.bat

### Entorno Linux:
Ejecutar script.sh

Esperar unos segundos y luego ir a un navegador e ingresar a la dirección: http://localhost:3000
y la aplicaciòn **Wordle-Helper** se abrirá


## FRONTEND
Frontend This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## BACKEND
### BD MySQL
Si aún no tenemos creada la BD localmente:

1- Accedemos a la consola de MySQL (pass: root)
>mysql -u root

2- Creamos la BD
>CREATE DATABASE wordle;

3- Cerramos la BD
>exit;

## Docker

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
