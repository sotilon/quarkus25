curl -i -X POST http://localhost:8001/services \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "get-users",
      	"protocol": "http",
      	"host": "ms-quarkus-security-api",
      	"port": 8084,
      	"path": "/api/v1/users",
      	"tags": ["user-service", "security-api"]
      }'

curl -i -X POST http://localhost:8001/services/get-users/routes \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "get-users-route",
      	"paths": [
      		"/api/v1/users"
      	],
      	"methods": [
      		"GET",
      		"POST"
      	]
      }'

curl -i -X POST http://localhost:8001/services \
    -H "Content-Type: application/json" \
    -d '{
        	"name": "get-username",
        	"protocol": "http",
        	"host": "ms-quarkus-security-api",
        	"port": 8084,
        	"path": "/api/v1/user",
        	"tags": ["user-service", "security-api"]
        }'

curl -i -X POST http://localhost:8001/services/get-username/routes \
    -H "Content-Type: application/json" \
    -d '{
        	"name": "get-username-route",
        	"paths": [
        		"/api/v1/user"
        	],
        	"methods": [
        		"GET"
        	]
        }'

curl -i -X POST http://localhost:8001/services \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "get-token",
      	"protocol": "http",
      	"host": "ms-quarkus-security-api",
      	"port": 8084,
      	"path": "/api/v1/admin/login",
      	"tags": ["user-service", "security-api"]
      }'

curl -i -X POST http://localhost:8001/services/get-token/routes \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "get-token-route",
      	"paths": [
      		"/api/v1/admin/login"
      	],
      	"methods": [
      		"POST"
      	]
      }'

curl -i -X POST http://localhost:8001/services \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "update-product",
      	"protocol": "http",
      	"host": "ms-quarkus-product-api",
      	"port": 8081,
      	"path": "/admin/api/v1/update",
      	"tags": ["product-service", "product-api"]
      }'

curl -i -X POST http://localhost:8001/services/update-product/routes \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "update-product-route",
      	"paths": [
      		"/admin/api/v1/update"
      	],
      	"methods": [
      		"PUT"
      	]
      }'

curl -i -X POST http://localhost:8001/services \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "get-products",
      	"protocol": "http",
      	"host": "ms-quarkus-product-api",
      	"port": 8081,
      	"path": "/api/v1/products",
      	"tags": ["product-service", "product-api"]
      }'

curl -i -X POST http://localhost:8001/services/get-products/routes \
  -H "Content-Type: application/json" \
  -d '{
      	"name": "get-products-route",
      	"paths": [
      		"/api/v1/products"
      	],
      	"methods": [
      		"GET"
      	]
      }'
