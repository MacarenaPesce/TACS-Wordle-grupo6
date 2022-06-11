# TACS-Wordle-grupo6

## TACS - UTN 2022

### Enunciado
https://docs.google.com/document/d/e/2PACX-1vS850Wxcrs3LThOAQamtTEhG6IEMmebXJxV3xXo-iPqCWHDI9LlncGyUONLx-hbIOBblutYCisS5aXh/pub


### Dcoumentacion asociada al proyecto
https://docs.google.com/document/d/11ClJBshXqy4Gd_h9j42n2G0AOld9yTBFamIOkuuGPr0/edit?usp=sharing

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

http://localhost:3000/

## BACKEND
### BD MySQL
Instancia solo para desarrollo local
Si aún no tenemos creada la BD localmente:

1- Accedemos a la consola de MySQL (pass: root)
>mysql -u root

2- Creamos la BD
>CREATE DATABASE wordle;

3- Luego de levantar el proyecto backend, se debe ejecutar el script `backend\src\main\resources\data.sql` sobre la base de datos, en caso de no hacerlo automáticamente.

4- Cerramos la BD
>exit;

### Reporte de Cobertura
Para ejecutar los tests de la aplicación, y generar el reporte de cobertura, en la carpeta backend ejecutar
>mvn clean verify

Esto generará el reporte en la carpeta target, el mismo puede verse en el navegador accediendo a:
> http://localhost:63342/backend/target/site/jacoco-unit-test-coverage-report/index.html

## Telegram

- Crear bot con BotFather y copiar el token
- Guardar el token en `backend\botToken.txt`
- Reiniciar el backend
- Correr ngrok con `ngrok http 8080` y copiar la URL
- Abrir en el navegador: `https://api.telegram.org/bot<botToken>/setWebhook?url=<URLngrok>/api/telegram/`
- Escribirle al bot

Hacer visibles los comandos en el menu de comandos del bot:
- En BotFather, con `/setcommands` crear el `/help` (y los futuros que existan) (o usar el method `setMyCommands` en una request)

## Docker

Las imagenes, redes, volumenes, contenedores necesarios para el despliegue de la aplicación, se generarán automaticamente con la ejecución del script.sh

A continuación algunos comandos básicos: 

Crear una imagen:

docker build -t grupo6/backend .
docker build -t grupo6/frontend .

Correr la imagen:

docker run -d --name backend  -p8080:8080 grupo6/backend
docker run -d --name frontend -p3000:3000 grupo6/frontend


Parar la imagen:

docker stop <NAME>

