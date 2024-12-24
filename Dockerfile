# Giai đoạn build
FROM maven:3-eclipse-temurin-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Giai đoạn runtime
FROM eclipse-temurin:17-alpine
COPY --from=build /target/*.jar DATN.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "DATN.jar"]