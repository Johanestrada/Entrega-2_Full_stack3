# Video Checklist - Rúbrica de Evaluación

Guía para grabar 2 videos de demostración. Total: **61 items** (52 arquitectura + 9 uso).

---

## VIDEO 1: ARQUITECTURA Y DISEÑO (Objetivo: 5-10 minutos)

### Presentación Inicial (1 min)
- [ ] Presentar proyecto: "Plataforma académica con microservicios"
- [ ] Mostrar diagrama de arquitectura en pantalla (documentation/diagramma.png)
- [ ] Explicar visión general: "Sistema para gestionar estudiantes, asistencias y evaluaciones"

---

## RÚBRICA ARQUITECTURA (52 ITEMS)

### A. PATRONES DE DISEÑO (5 items)

**A1. Microservicios (1 min - 0:00-1:00)**
- [ ] Mostrar estructura de carpetas: 3 microservicios independientes + BFF
- [ ] Explicar: "Cada servicio tiene responsabilidad única (SRP)"
- [ ] Puertos: Estudiante (8081), Asistencia (8082), Evaluación (8083), BFF (8084)
- [ ] Mencionar ventajas: escalabilidad, deploys independientes, tolerancia a fallos

**A2. Backend For Frontend - BFF (1 min - 1:00-2:00)**
- [ ] Mostrar bff-service como capa de orquestación
- [ ] Explicar: "Compone datos de 3 servicios en una única llamada"
- [ ] Mostrar controller: `/academico/{id}` retorna AcademicoDTO con estudiante + asistencias + evaluaciones
- [ ] Ventaja: Frontend no conoce complejidad interna

**A3. API Gateway Pattern (30 seg - 2:00-2:30)**
- [ ] Mencionar: BFF actúa como puerta única de entrada
- [ ] Muestra ruta: Frontend (4173) → BFF (8084) → Microservicios (8081-8083)
- [ ] Beneficio: CORS centralizado, autenticación centralizada

**A4. Service Discovery (1 min - 2:30-3:30)**
- [ ] Mostrar Eureka Server en diagrama (puerto 8761)
- [ ] Explicar: "Registro dinámico de servicios"
- [ ] Mostrar: eureka-server/pom.xml con `@EnableEurekaServer`
- [ ] Cómo servicios se registran automáticamente (Eureka Client)

**A5. Database per Service (1 min - 3:30-4:30)**
- [ ] Mostrar 3 bases de datos independientes en diagrama (MySQL)
- [ ] Explicar: "Cada servicio gestiona sus datos, sin acceso directo a otras BDs"
- [ ] Beneficio: desacoplamiento, escalado independiente
- [ ] Mostrar application.properties con ENV vars para BD

---

### B. TECNOLOGÍAS Y FRAMEWORKS (8 items)

**B1. Spring Boot (1 min - 4:30-5:30)**
- [ ] Versión utilizada: 3.5.14, Java 17
- [ ] Mostrar pom.xml de un servicio
- [ ] Dependencias principales: Web, Data JPA, Eureka Client
- [ ] `@SpringBootApplication`, server.port en application.properties

**B2. Spring Cloud (1 min - 5:30-6:30)**
- [ ] Versión: 2025.0.2
- [ ] Componentes: Eureka (Service Discovery)
- [ ] `@EnableEurekaClient` en servicios
- [ ] `@EnableEurekaServer` en eureka-server

**B3. Spring Data JPA / Hibernate (1 min - 6:30-7:30)**
- [ ] Mostrar entidades: Estudiante, Asistencia, Evaluacion
- [ ] `@Entity @Table @Column @Id @GeneratedValue`
- [ ] Estrategia: IDENTITY (auto-increment)
- [ ] Repositories: `extends JpaRepository<Entity, Long>`

**B4. Spring Security + JWT (1 min - 7:30-8:30)**
- [ ] Mostrar SecurityConfig.java
- [ ] JWT: Token generado en `/auth/login`, incluido en Authorization header
- [ ] Usuarios de prueba: admin/admin123, alumno/password
- [ ] Filter: `@Component` que valida token en cada request

**B5. Spring WebFlux / WebClient (1 min - 8:30-9:30)**
- [ ] Mostrar AcademicoFacade.java
- [ ] WebClient: llamadas HTTP asíncronas a microservicios
- [ ] URLs inyectadas: `@Value("${estudiante.service.url}")`
- [ ] Composición de respuestas en AcademicoDTO

**B6. REST API (1 min - 9:30-10:30)**
- [ ] Endpoints del BFF: GET, POST, PUT, DELETE
- [ ] Rutas principales: /auth/login, /academico/*, /asistencias, /evaluaciones
- [ ] HTTP status codes: 200, 201, 204, 400, 401, 404
- [ ] Request/Response JSON examples

**B7. Swagger / OpenAPI (1 min - 10:30-11:30)**
- [ ] Mostrar URL Swagger: http://localhost:8084/swagger-ui.html
- [ ] Demostrar documentación automática de endpoints
- [ ] `@OpenAPIDefinition @ApiOperation @ApiResponse` (opcional)
- [ ] Facilita testing y documentación

**B8. Docker & Docker Compose (1 min - 11:30-12:30)**
- [ ] Mostrar docker-compose.yml
- [ ] Servicios: mysql, eureka-server, 4 aplicaciones Spring, frontend
- [ ] `docker-compose up` inicia todo el stack
- [ ] Ventaja: reproducibilidad, ambiente uniforme

---

### C. PERSISTENCIA Y DATOS (6 items)

**C1. Modelado de Datos (1 min - 12:30-13:30)**
- [ ] Mostrar diagramas ER en Descripcion_Persistencia.pdf
- [ ] Tablas: estudiantes, asistencias, evaluaciones
- [ ] Relaciones: FK estudiante_id (sin constraint de BD)
- [ ] Campos principales en cada tabla

**C2. JPA Entities (1 min - 13:30-14:30)**
- [ ] Código de Estudiante.java: @Entity, @Table, @Column
- [ ] Getters/Setters (Lombok @Getter @Setter)
- [ ] Constructores: @NoArgsConstructor, @AllArgsConstructor
- [ ] Ejemplo concreto en pantalla

**C3. Índices y Optimización (1 min - 14:30-15:30)**
- [ ] Mostrar índices en DDL: `CREATE INDEX idx_estudiante_id`
- [ ] Índices en asistencias y evaluaciones (searches frecuentes)
- [ ] HikariCP: pool size, timeout
- [ ] Estrategia: select * optimizado

**C4. Migraciones de BD (1 min - 15:30-16:30)**
- [ ] spring.jpa.hibernate.ddl-auto=update (desarrollo)
- [ ] Alternativa: Flyway/Liquibase (producción recomendado)
- [ ] Scripts de versioning
- [ ] Backward compatibility

**C5. MySQL Configuration (1 min - 16:30-17:30)**
- [ ] Mostrar application.properties: datasource URL, user, password
- [ ] ENV vars: SPRING_DATASOURCE_URL, etc.
- [ ] Dialect: org.hibernate.dialect.MySQL8Dialect
- [ ] Connection timeout, max pool size

**C6. Data Integrity (1 min - 17:30-18:30)**
- [ ] Validación en capa de aplicación (no FK en BD)
- [ ] Ventaja: flexibilidad en escala
- [ ] Detalles en Descripcion_Persistencia.pdf
- [ ] Ejemplo: comprobar estudiante existe antes de crear asistencia

---

### D. TESTING (6 items)

**D1. Unit Tests (1 min - 18:30-19:30)**
- [ ] Mostrar BffServiceApplicationTests.java
- [ ] 4 tests: contextLoads(), buscarPorCurso, crearAsistencia, crearEvaluacion
- [ ] Mocks: @MockBean(AcademicoFacade)
- [ ] Ejecutar: `./mvnw test -Dtest=BffServiceApplicationTests` (mostrar en terminal)

**D2. Integration Tests (1 min - 19:30-20:30)**
- [ ] Mostrar AcademicoIntegrationTest.java (6 tests)
- [ ] MockWebServer para stubear microservicios
- [ ] Propiedades test: Eureka disabled, URLs locales
- [ ] Validar: BFF → MockServer → respuesta
- [ ] Ejecutar en terminal

**D3. E2E Tests (1 min - 20:30-21:30)**
- [ ] Mostrar AcademicoE2ETest.java (7 tests)
- [ ] Flujos de negocio: crear estudiante, registrar asistencia, calificar
- [ ] Comprobaciones: datos sincronizados, respuestas correctas
- [ ] Ejecutar: `./mvnw test` (mostrar 17/17 PASSING)

**D4. Test Coverage (1 min - 21:30-22:30)**
- [ ] JaCoCo: `./mvnw jacoco:report`
- [ ] Cobertura estimada: 70%+ BFF, 60%+ Facade
- [ ] Reportes en: target/site/jacoco/index.html
- [ ] Mostrar coverage summary

**D5. MockWebServer / Stubs (1 min - 22:30-23:30)**
- [ ] Mostrar AbstractMockWebServerTest.java
- [ ] Dispatcher: intercept /estudiantes, /asistencias, /evaluaciones
- [ ] Respuestas mockeadas para cada endpoint
- [ ] Ventaja: tests rápidos, sin BD real

**D6. Testing Best Practices (1 min - 23:30-24:30)**
- [ ] Arrange-Act-Assert pattern
- [ ] Aislamiento: mocks para dependencias externas
- [ ] Velocidad: in-memory tests
- [ ] Repetibilidad: mismos resultados siempre
- [ ] Documentación en Informe_Pruebas_Unitarias.pdf

---

### E. SEGURIDAD (4 items)

**E1. Autenticación JWT (1 min - 24:30-25:30)**
- [ ] POST /auth/login con credenciales
- [ ] Respuesta: token JWT (larga cadena base64)
- [ ] Mostrar header `Authorization: Bearer <token>`
- [ ] Token válido en cada request

**E2. Authorization / CORS (1 min - 25:30-26:30)**
- [ ] CORS: habilitado para http://localhost:4173 (Frontend)
- [ ] Otros orígenes: 403 Forbidden
- [ ] SecurityConfig: `@EnableWebSecurity`
- [ ] CrossOrigin annotation en controllers

**E3. Password Hashing (1 min - 26:30-27:30)**
- [ ] Contraseñas: almacenadas con hash (no plaintext)
- [ ] Framework: Spring Security BCrypt
- [ ] Usuarios en BD: admin y alumno (pre-cargados)
- [ ] Comparación en login: hash coincide

**E4. Token Validation (1 min - 27:30-28:30)**
- [ ] Filtro JWT: intercept requests
- [ ] Valida: firma, expiración, claims
- [ ] Si inválido: 401 Unauthorized
- [ ] Si válido: permite acceso a endpoint

---

### F. COMUNICACIÓN INTER-SERVICIOS (4 items)

**F1. Service-to-Service HTTP (1 min - 28:30-29:30)**
- [ ] BFF llama a microservicios vía HTTP
- [ ] Respuestas: JSON con datos del servicio
- [ ] Timeout configurado (resilencia)
- [ ] Retry policy (opcional)

**F2. Eureka Service Discovery (1 min - 29:30-30:30)**
- [ ] Servicios registrados en Eureka automáticamente
- [ ] BFF: busca URLs dinámicamente
- [ ] Si servicio cae: detección automática
- [ ] Ventaja: sin hardcoding de IPs

**F3. Circuit Breaker / Resilience (1 min - 30:30-31:30)**
- [ ] Si microservicio no responde: fallback (opcional)
- [ ] Timeout: no esperar infinito
- [ ] Retry: reintentar con backoff
- [ ] Mencionado en docs (implementación futura)

**F4. Message Format & Contracts (1 min - 31:30-32:30)**
- [ ] DTOs: EstudianteDTO, AsistenciaDTO, EvaluacionDTO
- [ ] JSON schema consistente
- [ ] Versioning: compatibilidad hacia atrás
- [ ] Documentación en API_REST_Especificacion.pdf

---

### G. ESCALABILIDAD Y PERFORMANCE (6 items)

**G1. Horizontal Scaling (1 min - 32:30-33:30)**
- [ ] Múltiples instancias de cada servicio (load balancer)
- [ ] Eureka distribuye requests
- [ ] BD: shared pool, índices optimizados
- [ ] Stateless services: sin sesión local

**G2. Caching Strategy (1 min - 33:30-34:30)**
- [ ] Redis (opcional, no implementado)
- [ ] Caché local con TTL
- [ ] Invalidación de caché en updates
- [ ] Mejora latencia en reads frecuentes

**G3. Database Optimization (1 min - 34:30-35:30)**
- [ ] Índices: idx_estudiante en asistencias/evaluaciones
- [ ] Query optimization: select only needed columns
- [ ] Connection pooling: HikariCP 10 connexiones
- [ ] Índices compuestos: (estudiante_id, fecha)

**G4. Load Balancing (1 min - 35:30-36:30)**
- [ ] Nginx/LoadBalancer: distribuye requests entre instancias
- [ ] Round-robin, sticky sessions
- [ ] Health checks: verifica servicios vivos
- [ ] Failover automático

**G5. Asynchronous Processing (1 min - 36:30-37:30)**
- [ ] WebClient: non-blocking I/O
- [ ] Async composition de datos
- [ ] Reactor/Project Reactor (Spring WebFlux)
- [ ] Mejora throughput

**G6. Monitoring & Metrics (1 min - 37:30-38:30)**
- [ ] Spring Boot Actuator: /actuator/health, /actuator/metrics
- [ ] Prometheus: exportar métricas
- [ ] Grafana: visualizar dashboards
- [ ] Alertas: CPU, memory, response time

---

### H. RESILIENCIA Y TOLERANCIA A FALLOS (4 items)

**H1. Service Isolation (1 min - 38:30-39:30)**
- [ ] Fallo en evaluacion-service no afecta estudiantes
- [ ] Cada servicio: BD independiente
- [ ] Fallos aislados
- [ ] Degradación elegante: mostrar error al usuario

**H2. Retry & Timeout (1 min - 39:30-40:30)**
- [ ] Timeout en WebClient: no espera eternamente
- [ ] Retry: reintentar con backoff exponencial
- [ ] CircuitBreaker: detener retries si patrón falla
- [ ] Configuración en application.properties

**H3. Graceful Degradation (1 min - 40:30-41:30)**
- [ ] Si asistencia-service down: retorna asistencias vacías
- [ ] Usuario ve: estudiante sin asistencias (no error)
- [ ] Sistema sigue funcionando parcialmente
- [ ] Mejora UX en fallos

**H4. Health Checks & Liveness (1 min - 41:30-42:30)**
- [ ] Actuator: /actuator/health
- [ ] Verifica BD disponible
- [ ] Verifica Eureka reachable
- [ ] Liveness: ¿servicio vivo? Readiness: ¿listo para traffic?

---

### I. DEPLOYMENT & INFRASTRUCTURE (5 items)

**I1. Docker Containerization (1 min - 42:30-43:30)**
- [ ] Mostrar Dockerfile de cada servicio
- [ ] Base image: eclipse-temurin:17-jre (Java 17)
- [ ] COPY jar, CMD java -jar
- [ ] Ventaja: ambiente reproducible

**I2. Docker Compose Orchestration (1 min - 43:30-44:30)**
- [ ] docker-compose.yml: define todos los servicios
- [ ] Redes: servicios se hablan por nombre (DNS)
- [ ] Volúmenes: persistencia de datos MySQL
- [ ] Orden de inicio: depends_on

**I3. Environment Variables (1 min - 44:30-45:30)**
- [ ] Configuración via ENV vars (12-factor app)
- [ ] application.properties: `${VAR_NAME}`
- [ ] Facilita CI/CD, diferentes ambientes
- [ ] Seguridad: credenciales no en código

**I4. CI/CD Pipeline (1 min - 45:30-46:30)**
- [ ] GitHub Actions (opcional, no mostrado)
- [ ] Pasos: Build → Test → Package → Deploy
- [ ] Automatiza: compile, run tests, push Docker image
- [ ] Mencionado en workflow (implementación futura)

**I5. Versioning & Releases (1 min - 46:30-47:30)**
- [ ] Git branches: main (estable), develop (activo)
- [ ] Release branches: preparar versiones
- [ ] Tags: versionado semántico (1.0.0, 1.0.1)
- [ ] Historiales en GitHub

---

### J. FRONTEND INTEGRATION (4 items)

**J1. React Frontend (1 min - 47:30-48:30)**
- [ ] Mostrar frontend folder: React + Vite
- [ ] npm run dev: inicia server en :4173
- [ ] Comunicación: fetch/axios a BFF (8084)
- [ ] Componentes: login, list, create, delete

**J2. API Consumption (1 min - 48:30-49:30)**
- [ ] Frontend: POST /auth/login, recibe token
- [ ] Almacena token en localStorage
- [ ] Cada request: incluye `Authorization: Bearer <token>`
- [ ] Axios interceptor: manejo de errores

**J3. CORS Configuration (1 min - 49:30-50:30)**
- [ ] BFF: @CrossOrigin(origins="http://localhost:4173")
- [ ] Frontend puede hacer requests cross-origin
- [ ] Otros orígenes: 403 error
- [ ] CORS headers: Allow-Origin, Allow-Methods, Allow-Headers

**J4. State Management & Caching (1 min - 50:30-51:30)**
- [ ] React: useState, useEffect, Context API (o Redux)
- [ ] Cache en frontend: localStorage, sessionStorage
- [ ] Sincronización: cuando datos cambian en backend
- [ ] Optimista UI: updates inmediatos, revert en fallo

---

**SUBTOTAL VIDEO 1: ~51 minutos (solo arquitectura)**

---

## VIDEO 2: DEMOSTRACIÓN DE USO (Objetivo: 5-10 minutos)

### Introducción (30 seg - 0:00-0:30)
- [ ] "Demostración de funcionalidades de la plataforma académica"
- [ ] Mostrar pantalla completa (puede ser UI o API calls)

---

## RÚBRICA USO (9 ITEMS)

### U1. Autenticación y Login (1 min - 0:30-1:30)
- [ ] Frontend: Ir a login page
- [ ] Ingresa credenciales: admin / admin123
- [ ] Click "Iniciar sesión"
- [ ] Resultado: Token JWT generado, redirige a dashboard
- [ ] Mostrar token en DevTools si es útil

**Validación rubrica**: Autenticación funcional, JWT presente

### U2. Listar Estudiantes (1 min - 1:30-2:30)
- [ ] Dashboard muestra lista de estudiantes
- [ ] Columnas: ID, Nombre, RUN, Curso
- [ ] Ejemplo: "Juan Perez" (20.111.222-3), "1-A"
- [ ] Filtro por curso (si está implementado)

**Validación**: GET /academico/curso/{curso} funciona, datos reales

### U3. Crear Estudiante (1 min 30 seg - 2:30-4:00)
- [ ] Click "Agregar Estudiante"
- [ ] Formulario: Nombre, RUN, Curso
- [ ] Ejemplo: "Carlos Rodríguez", "22.333.444-5", "3-B"
- [ ] Click "Guardar"
- [ ] Resultado: Estudiante agregado a lista
- [ ] Mostrar request/response en Network tab (si está visible)

**Validación**: POST /academico/estudiantes funciona, estudiante persiste

### U4. Ver Detalle Académico (1 min 30 seg - 4:00-5:30)
- [ ] Click en un estudiante → Ver detalles
- [ ] Página muestra: Datos estudiante + Asistencias + Evaluaciones
- [ ] Asistencias: lista con fechas y presente/ausente
- [ ] Evaluaciones: lista con materias y notas
- [ ] Datos compuestos de múltiples servicios

**Validación**: BFF compone datos, GET /academico/{id} retorna AcademicoDTO

### U5. Registrar Asistencia (1 min 30 seg - 5:30-7:00)
- [ ] Ir a sección "Registrar Asistencia"
- [ ] Seleccionar curso: "1-A"
- [ ] Checkbox: "Todos presentes" (marca todos)
- [ ] Click "Guardar"
- [ ] Resultado: Se crea registro de asistencia para cada estudiante
- [ ] Verificar: datos aparecen en detalle de estudiantes

**Validación**: POST /academico/curso/{curso}/asistencia funciona, asistencias creadas

### U6. Crear Evaluación (1 min 30 seg - 7:00-8:30)**
- [ ] Click "Agregar Evaluación"
- [ ] Formulario: Estudiante, Materia, Nota
- [ ] Ejemplo: "Juan Perez", "Matemáticas", "7.5"
- [ ] Click "Guardar"
- [ ] Resultado: Evaluación agregada
- [ ] Verificar en detalle del estudiante

**Validación**: POST /academico/evaluaciones funciona, nota almacenada

### U7. Editar Evaluación (1 min - 8:30-9:30)**
- [ ] Ir a evaluación existente
- [ ] Click "Editar nota"
- [ ] Cambiar nota: 7.5 → 8.0
- [ ] Click "Guardar"
- [ ] Resultado: Nota actualizada en lista

**Validación**: PUT /academico/evaluaciones/{id} funciona

### U8. Eliminar Estudiante (1 min - 9:30-10:30)**
- [ ] Seleccionar un estudiante de la lista
- [ ] Click "Eliminar"
- [ ] Confirmación: "¿Estás seguro?"
- [ ] Click "Aceptar"
- [ ] Resultado: Estudiante removido de lista

**Validación**: DELETE /academico/estudiantes/{id} funciona

### U9. Ver Reportes / Rendimiento (1 min - 10:30-11:30)**
- [ ] Sección "Reportes" o "Análisis"
- [ ] Mostrar: Asistencia por curso, promedio de notas
- [ ] Gráficos (opcional): bar chart, pie chart
- [ ] Exportar PDF (si está implementado)

**Validación**: Datos agregados visualizados, comprensibles

---

### Cierre (30 seg - 11:30-12:00)
- [ ] "Resumen de funcionalidades demostradas"
- [ ] "Sistema completo y funcional"
- [ ] Mencionar: Microservicios, API, Tests, Documentación
- [ ] "Gracias por ver"

---

## 📊 RESUMEN DE COBERTURA

| Sección | Items | Video | Duración |
|---------|-------|-------|----------|
| Arquitectura | 52 | Video 1 | 45-51 min |
| Uso | 9 | Video 2 | 10-12 min |
| **TOTAL** | **61** | **Ambos** | **55-63 min** |

---

## 💡 CONSEJOS PARA GRABAR

### Antes de grabar
1. ✅ Ejecutar sistema: `docker-compose up` o servicios locales
2. ✅ Verificar acceso: http://localhost:4173 (Frontend)
3. ✅ Verificar tests: `./mvnw test` (17/17 PASSING)
4. ✅ Tener documentación a mano (PDFs en documentation/)
5. ✅ Preparar guión: leer checklist antes de cada sección

### Durante la grabación
- Mostrar pantalla/código mientras explicas
- Habla claro y a velocidad moderada
- Señala elementos importantes en pantalla
- Haz demostraciones en vivo (no videos pregrabados)
- Si cometes error: pausa, reinicia toma, edita luego

### Después de grabar
- Editar: cortar silencios, ajustar volumen
- Agregar títulos/captions (accesibilidad)
- Exportar: MP4 o similar (máximo 1-2GB por video)
- Subir a plataforma del profesor

---

## 📚 DOCUMENTACIÓN DE REFERENCIA

- Persistencia: `documentation/Descripcion_Persistencia.pdf`
- Tests: `documentation/Informe_Pruebas_Unitarias.pdf`
- API: `documentation/API_REST_Especificacion.pdf`
- Arquitectura: `documentation/diagramma.png`
- Repos: `documentation/repositorios.txt`

---

## 🎯 CHECKLIST FINAL ANTES DE ENVIAR

### Video 1 (Arquitectura)
- [ ] 52 items cubiertos (revisar checklist arriba)
- [ ] Duración: 45-51 minutos (aceptable 40-60 min)
- [ ] Audio clara, sin ruido de fondo
- [ ] Pantalla visible: código, diagrama, terminal
- [ ] Demostraciones de tests ejecutándose

### Video 2 (Uso)
- [ ] 9 items cubiertos (login, CRUD, etc.)
- [ ] Duración: 10-12 minutos (aceptable 8-15 min)
- [ ] Flujo natural: login → listar → crear → editar → eliminar
- [ ] Datos reales mostrados
- [ ] Errors manejados gracefully

### Archivos adjuntos
- [ ] video_checklist.md (este archivo)
- [ ] 3 PDFs: Persistencia, Pruebas, API
- [ ] diagramma.png (arquitectura)
- [ ] repositorios.txt (enlaces)
- [ ] 2 videos: .mp4 o .avi
- [ ] ZIP final con todo

---

**Última actualización**: 2026-06-12  
**Versión**: 1.0 (Entrega 2)  
**Estado**: Listo para grabar
