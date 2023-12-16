# Scoring

## Overview

Scoring checks the client and decides whether it is possible to issue a loan to the client. He can go to PE to find out the status of the client. For example, if a customer has already taken out a loan and has been overdue, Scoring may reject the application.

## Architecture Overview

Link to the architecture diagram: [Architecture Diagram](https://miro.com/app/board/uXjVNWFTMec=/?share_link_id=279324309467)

## ORM Diagram

Link to the ORM Diagram: [ORM Diagram](https://drive.google.com/file/d/1OoC0OK53pCZ432edcC1FjMAUZnwTq_IR/view?usp=sharing)

## Folder Structure

Following a [hybrid]((https://priyalwalpita.medium.com/software-architecture-patterns-layered-architecture-a3b89b71a057)) (layered) architecture.

## Testing

JUnit and Mockito were used for unit testing, and Testcontainers was employed for integration testing.

## Dependencies

All dependencies used by the project can be found in the `build.gradle` files (both in the root and this module).

You can find the versions of the dependencies in the `gradle/libs.versions.toml` in the root.

