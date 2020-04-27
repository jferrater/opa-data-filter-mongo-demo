## user-service
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-mongo-demo.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-mongo-demo)
This is a sample microservice application that uses [opa-datafilter-mongo-spring-boot-starter](https://github.com/jferrater/opa-data-filter-spring-boot-starter)
Spring Data MongoDb is used together with opa-datafilter-mongo-spring-boot-starter to enforce authorization by filtering data using Open Policy Agent partial evaluation feature.

## Use case
A user can view other users info in their own organization

## The OPA Policy
````text
package userservice.authz

default allow = false

allow {
  input.method = "GET"
  input.path = ["users"]
  allowed[user]
}

allowed[user] {
  user = data.users[_]
  user.username = input.subject.username
}

allowed[user] {
  user = data.users[_]
  user.organization = input.subject.attributes.organization
}
````

### Pre-requisites
- java 11
- docker
- docker-compose

### Quick Start
1. ``git clone https://github.com/jferrater/opa-data-filter-mongo-demo.git``
2. ``cd opa-data-filter-mongo-demo``
3. ``./gradlew bootJar``
4. ``docker-compose up --build``

#### REST API Documentation
- UI: http://localhost:8082/user-service/swagger-ui.html
- JSON: http://localhost:8082/user-service/v3/api-docs

### Testing
````shell script
curl -i --user alice:password -H "X-ORG-HEADER: SOMA" http://localhost:8082/user-service/users
````
- alice should be able to see other users in their own organization