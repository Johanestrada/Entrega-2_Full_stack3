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

## Documentación disponible

La carpeta `documentation/` contiene los siguientes documentos y recursos:

- `Descripcion_Persistencia.pdf`
- `Informe_Pruebas_Unitarias.pdf`
- `API_REST_Especificacion.pdf`
- `Diagrama_Arquitectura.png`
- `repositorios.txt`

Incluye en la entrega los archivos que solicite la pauta y verifica que correspondan a la versión final del proyecto.

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

### Ejecutar el stack integrado con Docker Compose

El Compose principal incluye RabbitMQ, Kafka, Kafka UI y los microservicios. Los valores locales tienen defaults para levantar el stack sin configurar imágenes publicadas. Copia `.env.example` a `.env` y reemplaza los marcadores `YOUR_TENANT_ID` y `YOUR_API_CLIENT_ID` con los datos de tu registro Entra ID; sin esos valores el BFF no puede iniciar, porque valida el emisor JWT al arrancar. Compose crea automáticamente la red Kafka:

```bash
cp .env.example .env
# Edita .env con los valores locales y de Entra ID.
docker compose up --build -d
docker compose ps
```

Los brokers y nodos ZooKeeper publican sus puertos de diagnóstico sólo en `127.0.0.1`. Kafka UI también queda local en `http://localhost:8090`; el puerto del servicio Kafka Admin (8086) está protegido de igual manera. Los contenedores se comunican por `colegio-kafka-network`, usando `kafka-1:19092`, `kafka-2:19093` y `kafka-3:19094`.

Al iniciar, un contenedor de una sola ejecución espera a que los brokers estén saludables y crea los seis tópicos; que ese contenedor termine con código 0 es normal. Los tópicos principales son `estudiantes.eventos`, `asistencia.eventos` y `calificaciones.eventos`, todos con 3 particiones y factor de réplica 3. Cada uno tiene un tópico `.DLT` con 3 particiones para conservar la partición de origen. El servicio de estudiantes publica los eventos de creación en `estudiantes.eventos`; el grupo `analitica-academica` consume los tres tópicos y actualiza las métricas de alumnos, asistencia y calificaciones. El grupo `estudiante-notificaciones` sigue consumiendo asistencia y calificaciones. El endpoint de analítica se publica desde `estudiante-service` en el puerto 8081. El estado agregado de analítica se guarda en memoria y se reinicia junto con ese servicio.

## Autenticación y seguridad

- Angular usa Microsoft Entra ID mediante `@azure/msal-angular`.
- El interceptor MSAL adjunta el access token al consumir el BFF.
- API Gateway valida issuer, audience y scope `api.access`.
- El BFF valida nuevamente el JWT con Spring Security Resource Server.
- Sin token se espera `401`; sin scope suficiente, `403`.

## Despliegue AWS

La infraestructura está en `infrastructure/aws-terraform/` y reutiliza la VPC, RDS, ALB y Security Groups existentes de AWS Academy.

### Flujo de eventos RabbitMQ

Los POST de estudiantes, asistencias y evaluaciones persisten primero el registro y luego publican un evento JSON al exchange topic `eventos.exchange`. Las routing keys son `evento.estudiante.creado`, `evento.asistencia.registrada` y `evento.evaluacion.calificada`, enrutadas respectivamente a `eventos.estudiante`, `eventos.asistencia` y `eventos.evaluacion`. `rabbit-admin` consume las tres colas y confirma los mensajes válidos con ACK. Los mensajes rechazados van al exchange direct `eventos.dlx` y a su DLQ de dominio; las DLQ durables no tienen consumidores automáticos y retienen los mensajes hasta su revisión/eliminación manual. La actualización de estudiante reutiliza actualmente el flujo de guardado y también publica `estudiante.creado`.

RabbitMQ se ejecuta como contenedor en la EC2, persiste sus datos en el volumen Docker `rabbitmq_data` al recrear el contenedor en la misma instancia y no publica AMQP hacia la red del host. Ese volumen local no sobrevive a la terminación o reemplazo de la EC2; para ese escenario se requiere almacenamiento EBS persistente y respaldos. En AWS, el panel de administración escucha en el host por el puerto `15672`; Terraform permite el acceso desde la IP detectada automáticamente o el CIDR explícito de `rabbitmq_admin_cidr`. Usa una IP pública autorizada con máscara `/32` y accede directamente desde el navegador:

`http://<dns-publico-ec2>:15672`

Terraform detecta automáticamente la IP pública del equipo que ejecuta `apply` y la autoriza con máscara `/32` para SSH y los paneles cuando `admin_cidr` y `rabbitmq_admin_cidr` están vacíos. La consulta requiere acceso a Internet; puedes proporcionar un CIDR explícito para reemplazar la detección o poner `auto_detect_public_ip = false` para desactivarla. No abras los paneles a `0.0.0.0/0`. Define una contraseña de RabbitMQ de entre 6 y 128 caracteres usando solo letras, números, `-` o `_`. Para la práctica puedes usar `123456`, aunque es una contraseña débil y no se recomienda fuera de un entorno académico aislado. Terraform la recibe mediante `TF_VAR_rabbitmq_password`; también puedes definir `rabbitmq_username` en `terraform.tfvars` si cambias el usuario por defecto `colegio_app`. El valor queda en el estado de Terraform y en la configuración de arranque de EC2, por lo que protege también ese estado y restringe el acceso administrativo a la instancia.

Las colas `eventos.*.dlq` son durables y no tienen consumidores automáticos: los mensajes rechazados permanecen en ellas hasta que se revisen y eliminen manualmente. Los consumidores de las colas principales registran el error antes de enviar el mensaje a la DLQ. Para inspeccionar un mensaje desde RabbitMQ Management, abre la cola DLQ y usa **Get messages** con requeue habilitado para no retirarlo de forma permanente. Después de revisarlo, confirma/elimina el mensaje desde el panel. Esta política no aplica expiración automática; monitorea el crecimiento de las DLQ y límpialas periódicamente.

### Kafka en AWS

El bootstrap de EC2 inicia también los tres ZooKeeper, los tres brokers Kafka, Kafka UI y Kafka Admin desde el mismo Compose integrado. La IP detectada automáticamente o el valor de `rabbitmq_admin_cidr` autoriza RabbitMQ, Kafka UI y Kafka Admin. Kafka UI se publica en el puerto `8090`. Kafka Admin es una API REST con Swagger interactivo en el puerto `8086`. Consulta las URLs con `terraform output -raw url_panel_kafka` y `terraform output -raw url_kafka_admin`; abre la segunda para administrar tópicos y grupos desde Swagger. Estos servicios no tienen autenticación y solo se restringen por IP, así que no uses `0.0.0.0/0`; deja `rabbitmq_admin_cidr` vacío para que la IP se detecte automáticamente en cada ejecución.

La topología de tres brokers y tres ZooKeeper dentro de una única EC2 no constituye alta disponibilidad ante la pérdida de esa instancia. El tipo predeterminado es `t3.large` (8 GiB), para dar más memoria al stack completo que `t3.medium` (4 GiB); puede aumentar el costo y no garantiza disponibilidad si otro servicio falla. Los POST persisten el registro aunque un publicador RabbitMQ o Kafka falle, y el fallo de uno de esos brokers ya no impide intentar la publicación al otro; no hay outbox transaccional, por lo que un fallo de publicación puede dejar el evento sin entregar.

Para crear EC2 con acceso SSH, define `ec2_key_name` en `terraform.tfvars` con el nombre de un key pair existente en la región configurada. Si no lo necesitas, déjalo vacío; la instancia se creará sin key pair.

El workflow de GitHub Actions publica imágenes Docker, incluido `rabbit-admin`, en GHCR. Para usarlas en EC2, configura `image_prefix` con el prefijo `ghcr.io/<owner>/<repo-en-minusculas>-` y `image_tag` con el tag publicado, y asegúrate de que la instancia pueda descargar esos paquetes. Si no se configura el prefijo o falla la descarga, el script intenta construir las imágenes localmente.

Cuando la descarga de imágenes no está configurada o falla, el bootstrap construye y arranca primero el frontend y después compila los servicios backend. La página puede mostrarse antes de que la API esté lista; las operaciones que requieren el backend funcionarán cuando esos servicios terminen de arrancar.

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
Antes, copia `.env.example` a `.env` y reemplaza `RABBITMQ_PASSWORD` por un secreto local aleatorio. En desarrollo local, el panel RabbitMQ se publica solo en `localhost:15672`; AMQP permanece disponible únicamente dentro de la red Docker.

## Instrucciones de Entrega
Consulta el archivo [`repositorios.txt`](./repositorios.txt) para encontrar:
- Enlaces a todos los repositorios
- Credenciales de prueba
- Instrucciones completas de ejecución

## Notas
- Todos los servicios fueron generados usando el arquetipo estándar de Spring Boot (Spring Initializr).
- Consulta los README.md de cada servicio para más detalles.
- Para ejecutar con Docker Compose, asegúrate de tener Docker instalado: `docker-compose up`
