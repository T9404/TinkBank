FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /product-engine

COPY /build/libs/*.jar product-engine.jar

EXPOSE ${PRODUCT_ENGINE_PORT}

EXPOSE ${PRODUCT_ENGINE_GRPC_PORT}

CMD ["java", "-jar", "product-engine.jar"]