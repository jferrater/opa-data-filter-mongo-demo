## user-service
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-mongo-demo.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-mongo-demo)
A microservice application which provides REST API for managing users for the PetClinic app
### Pre-requisites
- java 11
- docker
- docker-compose

### Quick Start
1. ``git clone https://github.com/jferrater/petclinic-app.git``
2. ``cd petclinic-app/user-service``
3. ``./gradlew bootJar``
4. ``docker-compose up --build``

#### REST API Documentation
- UI: http://localhost:8082/user-service/swagger-ui.html
- JSON: http://localhost:8082/user-service/v3/api-docs

### Supported Database
- MongoDB

#### Using remote database
```shell script
docker run -dit -p 8082:8082 \
    -e SPRING_DATA_MONGODB_URI: mongodb://user:password@remoteMongodDB:27017/user_service \
    -e SPRING_DATA_MONGODB_DATABASE: user_service \
    --name user-service jmferrater/user-service:0.0.2
```

### Development
#### Building
1. ``git clone https://github.com/jferrater/petclinic-app.git``
2. ``cd petclinic-app/user-service``
3. ``./gradlew clean build``

#### Running the service
1. Update `src/main/resources/application.yml` with MongoDB uri and database name
2. ``./gradlew bootRun``

``