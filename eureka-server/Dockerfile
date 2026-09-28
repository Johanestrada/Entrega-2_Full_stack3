FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY mvnw pom.xml .
COPY .mvn .mvn
COPY src src
RUN chmod +x ./mvnw
RUN ./mvnw -B clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
