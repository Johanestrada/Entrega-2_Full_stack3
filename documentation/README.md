# Documentación - Entrega 2 Full Stack III

Esta carpeta contiene toda la documentación del proyecto.

## Contenido

### 1. Diagrama de Arquitectura
**Archivo**: `Diagrama_Arquitectura.png` (o .pdf)

Diagrama visual que muestra:
- Frontend React
- BFF Service
- Microservicios (estudiante, asistencia, evaluación)
- Eureka Server
- Base de datos MySQL
- Flujo de comunicación entre componentes

**Generado desde**: PlantUML

---

### 2. Descripción de Persistencia
**Archivo**: `Descripcion_Persistencia.pdf`

Documento que explica:
- Implementación de Spring Data JPA y Hibernate
- Configuración de MySQL
- Variables de entorno (`SPRING_DATASOURCE_URL`, etc.)
- Entidades y repositorios
- Ejemplos de mapeo JPA
- Estrategia de persistencia en Docker Compose

---

### 3. Informe de Pruebas Unitarias
**Archivo**: `Informe_Pruebas_Unitarias.pdf`

Documento que incluye:
- Frameworks utilizados (JUnit 5, Mockito, Spring Boot Test)
- Cobertura de pruebas por servicio
- Ejemplos de tests implementados
- Resultados de ejecución
- Métricas de calidad

**Tests disponibles**:
- `estudiante-service`: GET, POST, DELETE, búsqueda por RUN y curso
- `asistencia-service`: GET, POST, PUT, DELETE
- `evaluacion-service`: GET, POST, PUT, DELETE
- `bff-service`: endpoints académicos y autenticación

---

### 4. Especificación de API REST
**Archivo**: `API_REST_Especificacion.pdf` o Postman Collection

Documentación de todos los endpoints:
- `/estudiantes/*`
- `/asistencias/*`
- `/evaluaciones/*`
- `/academico/*`
- `/auth/*`

**Acceso en vivo**:
- BFF: http://localhost:8084/swagger-ui.html
- Estudiantes: http://localhost:8081/swagger-ui.html
- Asistencias: http://localhost:8082/swagger-ui.html
- Evaluaciones: http://localhost:8083/swagger-ui.html

---

## Cómo usar esta documentación

1. **Para entender la arquitectura**: Ver `Diagrama_Arquitectura.png`
2. **Para entender cómo se persisten los datos**: Leer `Descripcion_Persistencia.pdf`
3. **Para verificar la calidad**: Revisar `Informe_Pruebas_Unitarias.pdf`
4. **Para probar los endpoints**: Usar `API_REST_Especificacion.pdf` o Swagger en vivo

---

## Ejecución con Docker Compose

```bash
docker-compose up
```

Esto levanta:
- MySQL (puerto 3306)
- Eureka Server (puerto 8761)
- Estudiante Service (puerto 8081)
- Asistencia Service (puerto 8082)
- Evaluacion Service (puerto 8083)
- BFF Service (puerto 8084)
- Frontend (puerto 4173)

---

## Contacto

**Equipo de Desarrollo**:
- Johan Estrada
- Juan Opazo

**Docente**: Israel Alejandro
**Sección**: 302D
**Asignatura**: DSY1106 - DESARROLLO FULLSTACK III
