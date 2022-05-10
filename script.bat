cd ./backend
docker build -t grupo6/backend .
docker run -d -p8080:8080 grupo6/backend

cd ../frontend
docker build -t grupo6/fronted .
docker run -d -p3000:3000 grupo6/fronted
