FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /origination

COPY /build/libs/*.jar origination.jar

CMD ["java", "-jar", "origination.jar"]

EXPOSE 9094