echo "down datases ........."

cd databases/docker/customer_postgres
docker compose down
cd ..
cd ..
cd ..

cd databases/docker/product_postgresql
docker compose down
cd ..
cd ..
cd ..

cd  databases/security_mysql
docker compose down
cd ..
cd ..

cd databases/postgresql
docker compose down
cd ..
cd ..

echo "down microservices ........."
cd consul/docker
docker compose down
cd ..
cd ..

cd kong/compose
docker compose down
cd ..
cd ..

cd microservices/docker-compose/customer
docker compose down
cd ..

cd product
docker compose down
cd ..

cd product-backup
docker compose down
cd ..

cd security
docker compose down

#docker image ls
docker ps -a
