FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app
COPY mvnw pom.xml .
COPY .mvn .mvn
COPY src src
RUN chmod +x ./mvnw
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
