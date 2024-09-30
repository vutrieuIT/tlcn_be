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
ENV TZ=Asia/Ho_Chi_Minh
ENV APP_KEYSTORE=/app/keystore.p12
ENV APP_MAIL_USER=user@gmail.com
ENV APP_MAIL_PASS=pass
ENV APP_KEYSTORE_PASS=123456

EXPOSE 8001
EXPOSE 8443
COPY --from=build /app/target/*.jar app.jar
COPY .env .env
COPY ./src/main/resources/keystore.p12 /app/keystore.p12
CMD ["java", "-jar", "app.jar"]
