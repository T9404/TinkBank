FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /payment-gate

COPY /build/libs/*.jar payment-gate.jar

CMD ["java", "-jar", "payment-gate.jar"]