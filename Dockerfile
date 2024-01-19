# Stage 1: Build Stage
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /appS

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Production Stage
FROM azul/zulu-openjdk:17

WORKDIR /app

COPY --from=build appS/target/booking-management-system-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]