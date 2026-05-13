# Asistencia Service

Este microservicio gestiona los registros de asistencia de los estudiantes.

## Tecnologías
- Java 17
- Spring Boot
- Spring Data JPA
- H2 Database
- Eureka Client

## Instalación y ejecución
1. Instala dependencias:
   ```
   ./mvnw clean install
   ```
2. Ejecuta el servicio:
   ```
   ./mvnw spring-boot:run
   ```

## Swagger
La documentación de la API está disponible en:
http://localhost:8082/swagger-ui.html

## Notas
Este servicio fue generado usando el arquetipo estándar de Spring Boot (Spring Initializr).
