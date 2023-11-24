# Product Gate

## Overview

The Payment Gateway (PG) facilitates the disbursement and receipt of funds 
for clients. It is responsible for both sending money to clients and accepting payments through a Payment Provider.

PG utilizes gRPC for seamless communication with the Payment Engine. You can find a contract in `src/main/proto`

## Dependencies

All dependencies used by the project can be found in the `build.gradle` files (both in the root and this module).

You can find the versions of the dependencies in the `gradle/libs.versions.toml` in the root.
