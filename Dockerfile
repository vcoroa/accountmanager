FROM maven:3.9-eclipse-temurin-21-alpine as maven

COPY ./pom.xml ./pom.xml

RUN mvn dependency:go-offline -B

COPY ./src ./src

RUN mvn package -DskipTests

FROM openjdk:21-slim

WORKDIR /app

COPY --from=maven target/accountmanager.jar ./

EXPOSE 8080

CMD ["java", "-jar", "accountmanager.jar"]