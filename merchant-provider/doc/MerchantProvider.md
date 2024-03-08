# Merchant Provider

## Overview

The Merchant Provider (MP) is responsible for managing payment methods. 
It is also responsible for the disbursement and receipt of funds for merchant. 
The MP is responsible for both sending money to merchants and accepting payments through a Payment Provider.

MP utilizes gRPC for seamless communication with the Payment Engine. You can find a contract in `src/main/proto`

## Architecture Overview

Link to the architecture diagram: [Architecture Diagram](https://miro.com/app/board/uXjVNWFTMec=/?share_link_id=279324309467)


## Folder Structure

Following a [hybrid]((https://priyalwalpita.medium.com/software-architecture-patterns-layered-architecture-a3b89b71a057)) (layered) architecture.

## Dependencies

All dependencies used by the project can be found in the `build.gradle` files (both in the root and this module).

You can find the versions of the dependencies in the `gradle/libs.versions.toml` in the root.
