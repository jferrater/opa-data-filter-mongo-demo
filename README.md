## user-service
[![Build Status](https://travis-ci.com/jferrater/opa-data-filter-mongo-demo.svg?branch=master)](https://travis-ci.com/jferrater/opa-data-filter-mongo-demo)<br>
This is a sample microservice application that uses [opa-datafilter-mongo-spring-boot-starter](https://github.com/jferrater/opa-data-filter-spring-boot-starter) with
Spring Data MongoDb to enforce authorization by filtering data using Open Policy Agent partial evaluation feature.

## Use case
- A manager can view the users he/she managed.
- A user can view his own info

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
# populate the mongodb with test users
curl -i http://localhost:8082/user-service/init

# alex, the manager of SOMA, should be able to view his employees he managed
curl -i --user alex:password -H "X-ORG-HEADER: SOMA" http://localhost:8082/user-service/users

# alex, the manager of SOMA, should NOT be able to view the employees from other organization
curl -i --user alex:password -H "X-ORG-HEADER: VETE" http://localhost:8082/user-service/users

# alice should be able to see her info
curl -i --user alice:password -H "X-ORG-HEADER: SOMA" http://localhost:8082/user-service/users

````