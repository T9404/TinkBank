FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /scoring

COPY /build/libs/*.jar scoring.jar

EXPOSE ${SERVER_PORT}

EXPOSE ${SERVER_PORT_GRPC}

CMD ["java", "-jar", "scoring.jar"]