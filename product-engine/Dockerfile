FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /product-engine

COPY /build/libs/*.jar product-engine.jar

CMD ["java", "-jar", "product-engine.jar"]