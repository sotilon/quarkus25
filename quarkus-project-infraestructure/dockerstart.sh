echo "starting databases ........."
cd databases/docker/customer_postgres
docker compose up -d --build
cd ..
cd ..
cd ..

cd databases/docker/product_postgresql
docker compose up -d --build
cd ..
cd ..
cd ..

cd  databases/security_mysql
docker compose up -d --build
cd ..
cd ..

#cd databases/postgresql
#docker compose up -d --build
#cd ..
#cd ..

echo "starting microservices ........."
cd consul/docker
docker compose up -d --build
cd ..
cd ..

echo "sleeping started consul ........."
sleep 10s
cd kong/compose
docker compose up -d --build
KONG_DATABASE=postgres docker compose --profile database up -d
cd ..
cd ..

cd microservices/docker-compose/customer
docker compose up -d --build
cd ..

cd product
docker compose up -d --build
cd ..

cd product-backup
docker compose up -d --build
cd ..

cd security
docker compose up -d --build

docker ps -a
