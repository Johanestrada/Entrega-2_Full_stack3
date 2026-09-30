FROM eclipse-temurin:21-jre

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
RUN ./mvnw -q dependency:go-offline

COPY src ./src
RUN ./mvnw -q -DskipTests package

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "/app/target/rabbit-admin-0.0.1-SNAPSHOT.jar"]
