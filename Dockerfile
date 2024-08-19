FROM maven:3.8.3-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17-jdk-alpine
WORKDIR /app
CMD mvn clean install
EXPOSE 8080
COPY --from=build /app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]