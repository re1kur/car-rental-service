FROM maven:3.9.9-eclipse-temurin-24-alpine AS build

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:24-jre-alpine

COPY --from=build target/app-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]