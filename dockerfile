# build stage
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn clean package -DskipTests

# production stage
FROM openjdk:17-jdk-slim
WORKDIR /app

# Set environment variables
ENV APP_DB_URL=jdbc:mysql://host.docker.internal:3306/tlcn
ENV APP_DB_USERNAME=root
ENV APP_DB_PASSWORD=12345

EXPOSE 8001
COPY --from=build /app/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]
