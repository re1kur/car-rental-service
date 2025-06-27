FROM maven:3.9.9-eclipse-temurin-21 AS build

COPY . .

RUN mvn clean package

FROM bellsoft/liberica-openjdk-debian:21

COPY --from=build /file-service/target/fs-0.0.1-SNAPSHOT.jar application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]