FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /scoring

COPY /build/libs/*.jar scoring.jar

CMD ["java", "-jar", "scoring.jar"]