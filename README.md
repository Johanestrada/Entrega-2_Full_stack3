# Proyecto Colegio Bernardo O'Higgins

## Ejecutar tests (BFF)

Desde la carpeta `bff-service` puedes ejecutar los tests con Maven Wrapper:

```powershell
cd bff-service
.\mvnw test
```

Para ejecutar sólo los tests principales de integración/E2E:

```powershell
.\mvnw test "-Dtest=AcademicoIntegrationTest,AcademicoE2ETest,BffServiceApplicationTests"
```

## Documentación y entregables

La carpeta `documentation/` contiene los documentos de entrega requeridos:

- `Descripcion_Persistencia.pdf`
- `Informe_Pruebas_Unitarias.pdf`
- `API_REST_Especificacion.pdf`
- `video_checklist.md`
- `repositorios.txt`

Conviene incluir los `.pdf` en la entrega y verificar que estén actualizados.

# Proyecto Colegio Bernardo O'Higgins

Plataforma académica Angular y Spring Boot para gestionar estudiantes, asistencia y evaluaciones mediante microservicios y un BFF protegido con Microsoft Entra ID.

## Estructura del proyecto
- **estudiante-service**: Microservicio para la gestión de estudiantes.
- **asistencia-service**: Microservicio para el registro de asistencias.
- **bff-service**: Backend For Frontend que orquesta y compone datos de los microservicios.
- **eureka-server**: Servidor Eureka para el descubrimiento de servicios.
- **frontend-angular**: Aplicación Angular con MSAL para autenticación y consumo del BFF.

**Flujo de comunicación:**
- El usuario accede a través del frontend Angular.
- Las solicitudes van al **BFF Service** (puerto 8084) vía API Gateway.
- El BFF compone datos de los tres microservicios mediante WebClient
- Cada microservicio consulta su propia BD (MySQL o H2)
- Todos los servicios se registran con **Eureka Server** (puerto 8761) para descubrimiento dinámico

## Tecnologías principales
- Angular 18 y MSAL
- Java 17/21
- Spring Boot
- Spring Data JPA
- H2 Database
- Eureka Server y Eureka Client
- Swagger (springdoc-openapi)
- Maven

## Instalación y ejecución
1. Instala dependencias en cada servicio:
   ```
   ./mvnw clean install
   ```
2. Inicia primero el servidor Eureka:
   ```
   cd eureka-server
   ./mvnw spring-boot:run
   ```
3. Inicia cada microservicio y el BFF en terminales separadas:
   ```
   cd estudiante-service
   ./mvnw spring-boot:run
   
   cd asistencia-service
   ./mvnw spring-boot:run
   
   cd bff-service
   ./mvnw spring-boot:run
   ```
4. En otra terminal, instala y ejecuta el frontend Angular:
   ```
   cd frontend-angular
   npm install
   npm start
   ```

## Autenticación y seguridad

- Angular usa Microsoft Entra ID mediante `@azure/msal-angular`.
- El interceptor MSAL adjunta el access token al consumir el BFF.
- API Gateway valida issuer, audience y scope `api.access`.
- El BFF valida nuevamente el JWT con Spring Security Resource Server.
- Sin token se espera `401`; sin scope suficiente, `403`.

## Despliegue AWS

La infraestructura está en `infrastructure/aws-terraform/` y reutiliza la VPC, RDS, ALB y Security Groups existentes de AWS Academy.

```text
CloudFront HTTPS
   ↓
EC2 con Nginx y Docker Compose
   ↓
API Gateway + JWT Entra ID
   ↓
VPC Link → ALB interno → BFF
   ↓
Microservicios → RDS MySQL
```

Desde `infrastructure/aws-terraform/`:

```bash
terraform init
terraform validate
terraform plan
terraform apply
```

Después del despliegue, usar:

```bash
terraform output url_frontend
terraform output endpoint_api_gateway
```

El frontend debe abrirse con la URL `https://` de CloudFront. Esa URL debe registrarse en Microsoft Entra como Redirect URI de tipo SPA. La URL HTTP directa de EC2 queda solo para diagnóstico, porque MSAL requiere un contexto seguro.

## Arquitectura del Sistema

![Diagrama de Arquitectura](./documentation/Diagrama_Arquitectura.png)

*Diagrama que muestra la comunicación entre componentes: Frontend React, BFF Service, microservicios (estudiante, asistencia, evaluación), Eureka Server y MySQL.*

## Documentación

### Documentación del Proyecto
Consulta la carpeta [`documentation/`](./documentation/) para encontrar:
- **Diagrama de Arquitectura**: Diagrama visual de todos los componentes y sus interacciones
- **Descripción de Persistencia**: Detalles sobre la implementación de bases de datos con JPA
- **Informe de Pruebas Unitarias**: Métricas y cobertura de tests
- **Especificación de API REST**: Documentación de endpoints

### Documentación Swagger (en vivo)
Accede a la documentación de cada servicio en:
- Estudiantes: http://localhost:8081/swagger-ui.html
- Asistencias: http://localhost:8082/swagger-ui.html
- BFF: http://localhost:8084/swagger-ui.html
- Eureka: http://localhost:8761

### Con Docker Compose
```bash
docker-compose up
```
Todos los servicios estarán disponibles en los puertos indicados arriba.

## Instrucciones de Entrega
Consulta el archivo [`repositorios.txt`](./repositorios.txt) para encontrar:
- Enlaces a todos los repositorios
- Credenciales de prueba
- Instrucciones completas de ejecución

## Notas
- Todos los servicios fueron generados usando el arquetipo estándar de Spring Boot (Spring Initializr).
- Consulta los README.md de cada servicio para más detalles.
- Para ejecutar con Docker Compose, asegúrate de tener Docker instalado: `docker-compose up`
