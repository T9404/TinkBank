FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /payment-gate

COPY /build/libs/*.jar payment-gate.jar

EXPOSE ${PAYMENT_GATE_PORT}

EXPOSE ${PAYMENT_GATE_GRPC_PORT}

CMD ["java", "-jar", "payment-gate.jar"]
