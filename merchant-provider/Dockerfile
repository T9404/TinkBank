FROM openjdk:17-ea-22-jdk-oracle

WORKDIR /merchant-provider

COPY /build/libs/*.jar merchant-provider.jar

EXPOSE ${MERCHANT_PROVIDER_PORT}

CMD ["java", "-jar", "merchant-provider.jar"]
