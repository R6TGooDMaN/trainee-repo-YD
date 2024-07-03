FROM openjdk:21-jdk-slim
LABEL authors="dimas"
WORKDIR /app
COPY target/product-service-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
