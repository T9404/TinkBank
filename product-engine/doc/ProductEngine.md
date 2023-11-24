# Product Engine

## Overview

The PE (Payment Engine) stores information about client loans, their payment schedules, and the status of each payment. Additionally, this service maintains information about products supported in the system. The interest rates on loans are calculated based on these products.

## Table of Contents

1. [Architecture Overview](#architecture-overview)
2. [Technologies Used](#technologies-used)
3. [ORM Diagram](#orm-diagram)
4. [Implemented Formulas](#implemented-formulas)
5. [Folder Structure](#folder-structure)
6. [Testing](#testing)
7. [Dependencies](#dependencies)

## Architecture Overview

Link to the architecture diagram: [Architecture Diagram](https://miro.com/app/board/uXjVNWFTMec=/?share_link_id=279324309467)

## Technologies Used

List the technologies/frameworks/libraries used in the project.

- Liquibase for database migration: [Liquibase](https://www.liquibase.org/)
- gRPC for communication between services: [gRPC](https://grpc.io/)
- JPA for ORM: [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- Testing:
    - JUnit: [JUnit](https://junit.org/junit5/)
    - Mockito: [Mockito](https://site.mockito.org/)
    - Testcontainers for integration testing: [Testcontainers](https://www.testcontainers.org/)
- Logging: [SLF4J](http://www.slf4j.org/)

## ORM Diagram

Link to the ORM Diagram: [ORM Diagram](https://drive.google.com/file/d/1OoC0OK53pCZ432edcC1FjMAUZnwTq_IR/view?usp=sharing)

## Implemented Formulas

- PMT Formula: [Superuser - PMT Formula](https://superuser.com/questions/871404/what-would-be-the-the-mathematical-equivalent-of-this-excel-formula-pmt)
- IPMT, PPMT Formulas: [Microsoft Answers](https://answers.microsoft.com/en-us/msoffice/forum/all/what-is-the-equation-that-excel-uses-for-the-ipmt/2b2a7c0d-f39b-4fdc-a713-ba2810b3d166)


## Folder Structure

Following a [hybrid]((https://priyalwalpita.medium.com/software-architecture-patterns-layered-architecture-a3b89b71a057)) (layered) architecture.

## Testing

JUnit and Mockito were used for unit testing, and Testcontainers was employed for integration testing.

## Dependencies

All dependencies used by the project can be found in the `build.gradle` files (both in the root and this module).

You can find the versions of the dependencies in the `gradle/libs.versions.toml` in the root.
