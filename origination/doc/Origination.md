# Origination

## Overview

Origination handles the online submission of credit applications through an API, processes the application via scoring, 
and, upon approval, initiates a fund transfer operation to the client's account through a payment gateway.

Origination utilizes gRPC for seamless communication with the Scoring Engine, Product Engine and Payment Gateway. 
You can find a contract in `src/main/proto`

## Architecture Overview

Link to the architecture diagram: [Architecture Diagram](https://miro.com/app/board/uXjVNWFTMec=/?share_link_id=279324309467)

List the technologies/frameworks/libraries used in the project.

- Liquibase for database migration: [Liquibase](https://www.liquibase.org/)
- gRPC for communication between services: [gRPC](https://grpc.io/)
- JPA for ORM: [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- Testing:
    - JUnit: [JUnit](https://junit.org/junit5/)
    - Mockito: [Mockito](https://site.mockito.org/)
    - Testcontainers for integration testing: [Testcontainers](https://www.testcontainers.org/)
- Logging: [SLF4J](http://www.slf4j.org/)

## Testing

JUnit and Mockito were used for unit testing, and Testcontainers was employed for integration testing.


## Dependencies

All dependencies used by the project can be found in the `build.gradle` files (both in the root and this module).

You can find the versions of the dependencies in the `gradle/libs.versions.toml` in the root.
