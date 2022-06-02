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

3- Luego de levantar el proyecto backend, se debe ejecutar el script `backend\src\main\resources\data.sql` sobre la base de datos, en caso de no hacerlo automáticamente.

4- Cerramos la BD
>exit;

## Telegram

- Crear bot con BotFather y copiar el token
- Guardar el token en `backend\botToken.txt`
- Reiniciar el backend
- Correr ngrok con `ngrok http 8080` y copiar la URL
- Abrir en el navegador: `https://api.telegram.org/bot<botToken>/setWebhook?url=<URLngrok>/api/telegram/`
- Escribirle al bot

Hacer los comandos visibles en el menu de comandos bot:
- En BotFather, con `/setcommands` crear el `/help` (y los futuros que existan) (o usar el method `setMyCommands` en una request)

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


## Backend - Reporte de Cobertura
Para ejecutar los tests de la aplicación, y generar el reporte de cobertura, en la carpeta backend ejecutar
>mvn clean verify

Esto generará el reporte en la carpeta target, el mismo puede verse en el navegador accediendo a:
> http://localhost:63342/backend/target/site/jacoco-unit-test-coverage-report/index.html
