# 1. Build aşaması
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 2. Çalıştırma aşaması
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/apartment-app-0.0.1-SNAPSHOT.jar app.jar

# AWS Credentials için çevresel değişkenleri container'a geçmeniz gerekecek
ENV AWS_ACCESS_KEY_ID=your_access_key
ENV AWS_SECRET_ACCESS_KEY=your_secret_key
ENV AWS_REGION=us-east-1

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]