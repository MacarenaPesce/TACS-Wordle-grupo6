# TACS - Wordle Helper App - grupo6

## TACS - UTN 2022

### Api Documentacion

#### Enunciado
https://docs.google.com/document/d/e/2PACX-1vS850Wxcrs3LThOAQamtTEhG6IEMmebXJxV3xXo-iPqCWHDI9LlncGyUONLx-hbIOBblutYCisS5aXh/pub

#### Asociada al proyecto
https://docs.google.com/document/d/11ClJBshXqy4Gd_h9j42n2G0AOld9yTBFamIOkuuGPr0/edit?usp=sharing

### Build

#### Requerimientos para buildear el proyecto
- Tener Docker instalado en su ambiente.

## Docker
Levantar el proyecto localmente:
- `docker compose up`
- Esperar a que el backend reintente un par de veces, hasta que levante la base de datos.

Config para deployar:
- Dentro de `docker-compose.yml`, cambiar el dockerfile de frontend de `Dockerfile-local` a `Dockerfile`
- Configure su IP en `frontend/.env.production`


## FRONTEND
Esperar unos segundos y luego ir a un navegador e ingresar a la dirección: http://localhost:3000
y la aplicaciòn **Wordle-Helper** se abrirá

Frontend This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).


## Telegram
Aplicación alternativa al frontend, en forma de servidor de Telegram. Se comparte backend entre ambas.
1. Crear bot con BotFather y copiar el token
2. Guardar el token en `backend\botToken.txt`
3. Reiniciar el backend
4. Correr ngrok con `ngrok http 8080` y copiar la URL
5. Abrir en el navegador: `https://api.telegram.org/bot<botToken>/setWebhook?url=<URLngrok>/api/telegram/`
6. Escribirle al bot

Cerrar el webhook al cerrar ngrok:
- `https://api.telegram.org/bot<botToken>/deleteWebhook`

Hacer visibles los comandos en el menu de comandos del bot:
- Entrar al siguiente link: `https://api.telegram.org/bot<botToken>/setMyCommands?commands=[{"command":"start","description":"Reiniciar"},{"command":"users","description":"Administrar usuarios"},{"command":"tournaments","description":"Administrar torneos"}]`


## Generar datos extra
- Se generan usuarios, puntajes diarios de usuarios, torneos, y registraciones de usuarios a torneos.
- Configurar datos a generar en la temp table opciones dentro de `data_generator.sql`

Desde MySQL Workbench:
- Ejecutar `data__names.sql` y luego `data_generator.sql`

Desde Linux:
- `cat *.sql | docker exec -i test-mysql sh -c "exec mysql -uroot -proot -t"`

Desde Windows:
- `type data__names.sql data_generator.sql | docker exec -i test-mysql sh -c "exec mysql -uroot -proot -t"`


## BACKEND
### Reporte de Cobertura
Para ejecutar los tests de la aplicación, y generar el reporte de cobertura, en la carpeta backend ejecutar
>mvn clean verify

Esto generará el reporte en la carpeta target, el mismo puede verse en el navegador accediendo a:
> http://localhost:63342/backend/target/site/jacoco-unit-test-coverage-report/index.html

### BD MySQL
Instancia solo para desarrollo local

Si aún no tenemos creada la BD localmente:

1. Accedemos a la consola de MySQL (pass: root)
>mysql -u root

2. Creamos la BD
>CREATE DATABASE wordle;

3. Configurar string de conexión en `backend\src\main\resources\application.properties`.

