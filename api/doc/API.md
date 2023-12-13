# API

## Overview

The API manages interactions with client-facing web forms as well as mobile applications. It serves as a unified service 
for handling the client-side of the system. It receives completed web forms from clients and forwards them to Origination. 
Additionally, if a client requests information about their credit status, the service communicates with PE and returns 
the payment schedule.

API utilizes gRPC for seamless communication with the Origination and Product Engine. 
You can find a contract in `src/main/proto`

## Architecture Overview

Link to the architecture diagram: [Architecture Diagram](https://miro.com/app/board/uXjVNWFTMec=/?share_link_id=279324309467)

## Testing

JUnit and Mockito were used for unit testing, and Testcontainers was employed for integration testing.


## Dependencies

All dependencies used by the project can be found in the `build.gradle` files (both in the root and this module).

You can find the versions of the dependencies in the `gradle/libs.versions.toml` in the root.
