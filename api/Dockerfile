FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /api

COPY /build/libs/*.jar api.jar

EXPOSE ${API_PORT}

CMD ["java", "-jar", "api.jar"]