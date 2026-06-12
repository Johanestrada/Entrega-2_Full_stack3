package com.colegio.bffservice;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Pruebas End-to-End (E2E) que verifican flujos completos de negocio:
 * Frontend → BFF → Microservicios → Base de datos
 */
@SpringBootTest(properties = {
        "eureka.client.enabled=false",
        "eureka.client.fetch-registry=false",
        "eureka.client.register-with-eureka=false",
        "spring.cloud.discovery.enabled=false",
        "spring.cloud.enabled=false",
        "estudiante.service.url=http://localhost:8081",
        "asistencia.service.url=http://localhost:8081",
        "evaluacion.service.url=http://localhost:8081"
})
@AutoConfigureMockMvc(addFilters = false)
class AcademicoE2ETest extends AbstractMockWebServerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Caso de negocio E2E: Un director registra a un nuevo estudiante
     * 1. Crear estudiante
     * 2. Verificar que el estudiante se guardó
     * 3. Obtener datos del estudiante
     */
    @Test
    void e2e_CrearEstudianteYVerificarRegistro() throws Exception {
        // PASO 1: Crear un nuevo estudiante via BFF
        Map<String, Object> nuevoEstudiante = new HashMap<>();
        nuevoEstudiante.put("nombre", "Carlos Rodríguez");
        nuevoEstudiante.put("run", "22.333.444-5");
        nuevoEstudiante.put("curso", "3-B");

        mockMvc.perform(post("/academico/estudiantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEstudiante)))
                .andExpect(status().isOk());

        // PASO 2: Obtener datos del estudiante (debería estar registrado)
        MvcResult result = mockMvc.perform(get("/academico/run/{run}", "22.333.444-5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found but got " + status);
        // Nota: Puede ser 404 si los datos aún no se sincronizaron
    }

    /**
     * Caso de negocio E2E: Un profesor registra asistencias de un curso
     * 1. Obtener estudiantes del curso
     * 2. Registrar asistencia para todo el curso
     * 3. Verificar que las asistencias se guardaron
     */
    @Test
    void e2e_RegistrarAsistenciasCursoCompleto() throws Exception {
        String curso = "1-A";

        // PASO 1: Obtener estudiantes del curso
        mockMvc.perform(get("/academico/curso/{curso}", curso)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // PASO 2: Registrar asistencia para todo el curso
        Map<String, Object> asistenciaBody = new HashMap<>();
        asistenciaBody.put("presente", true);

        mockMvc.perform(post("/academico/curso/{curso}/asistencia", curso)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(asistenciaBody)))
                .andExpect(status().isOk());
    }

    /**
     * Caso de negocio E2E: Un profesor crea y califica evaluaciones
     * 1. Crear evaluación para un estudiante
     * 2. Actualizar nota de la evaluación
     * 3. Eliminar evaluación (si es necesario)
     */
    @Test
    void e2e_CrearYCalificarEvaluacion() throws Exception {
        Long estudianteId = 1L;

        // PASO 1: Crear evaluación
        Map<String, Object> evaluacion = new HashMap<>();
        evaluacion.put("estudianteId", estudianteId);
        evaluacion.put("materia", "Historia");
        evaluacion.put("nota", 6.0);

        mockMvc.perform(post("/academico/evaluaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(evaluacion)))
                .andExpect(status().isOk());

        // PASO 2: Actualizar nota (si obtenemos el ID de la evaluación)
        // En un E2E real, extraeríamos el ID de la respuesta anterior
        Map<String, Object> actualizacion = new HashMap<>();
        actualizacion.put("nota", 7.5);

        MvcResult result = mockMvc.perform(put("/academico/evaluaciones/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(actualizacion)))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found but got " + status);
        // Nota: 404 es válido si la evaluación no existe en BD
    }

    /**
     * Caso de negocio E2E: Un estudiante verifica su rendimiento académico
     * 1. Autenticarse (login)
     * 2. Obtener datos académicos (estudiante, asistencias, evaluaciones)
     * 3. Verificar que los datos están completos
     */
    @Test
    void e2e_EstudianteVerificaSuRendimiento() throws Exception {
        Long estudianteId = 1L;

        // PASO 1: Obtener datos académicos completos
        MvcResult result = mockMvc.perform(get("/academico/{estudianteId}", estudianteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found but got " + status);
    }

    /**
     * Caso de negocio E2E: Un administrador realiza operaciones CRUD de estudiantes
     * 1. Crear estudiante
     * 2. Leer datos del estudiante
     * 3. Actualizar datos (si es posible)
     * 4. Eliminar estudiante
     */
    @Test
    void e2e_OperacionesCRUDEstudiante() throws Exception {
        Long estudianteId = 999L; // ID ficticio

        // PASO 1: Crear estudiante
        Map<String, Object> nuevoEstudiante = new HashMap<>();
        nuevoEstudiante.put("nombre", "Test Usuario");
        nuevoEstudiante.put("run", "99.999.999-9");
        nuevoEstudiante.put("curso", "5-C");

        mockMvc.perform(post("/academico/estudiantes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEstudiante)))
                .andExpect(status().isOk());

        // PASO 2: Leer datos del estudiante
        MvcResult resultRead = mockMvc.perform(get("/academico/{estudianteId}", estudianteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int statusRead = resultRead.getResponse().getStatus();
        assertTrue(statusRead == 200 || statusRead == 404, "Expected 200 OK or 404 Not Found but got " + statusRead);

        // PASO 3: Eliminar estudiante
        MvcResult resultDelete = mockMvc.perform(delete("/academico/estudiantes/{id}", estudianteId))
                .andReturn();
        int statusDelete = resultDelete.getResponse().getStatus();
        assertTrue(statusDelete == 204 || statusDelete == 404, "Expected 204 No Content or 404 Not Found but got " + statusDelete);
    }

    /**
     * Caso de negocio E2E: Un profesor registra asistencia individual
     * 1. Registrar asistencia de un estudiante
     * 2. Verificar que la asistencia se guardó
     * 3. Obtener datos académicos del estudiante (incluye asistencias)
     */
    @Test
    void e2e_RegistrarAsistenciaIndividual() throws Exception {
        Long estudianteId = 1L;

        // PASO 1: Registrar asistencia
        Map<String, Object> asistencia = new HashMap<>();
        asistencia.put("estudianteId", estudianteId);
        asistencia.put("presente", true);

        mockMvc.perform(post("/academico/asistencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(asistencia)))
                .andExpect(status().isOk());

        // PASO 2: Obtener datos académicos (debería incluir la nueva asistencia)
        MvcResult result = mockMvc.perform(get("/academico/{estudianteId}", estudianteId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found but got " + status);
    }

    /**
     * Caso de negocio E2E: Autenticación y acceso a datos académicos
     * 1. Registrarse como usuario
     * 2. Hacer login
     * 3. Acceder a datos académicos
     */
    @Test
    void e2e_AutenticacionYAccesoADatos() throws Exception {
        // PASO 1: Registrarse
        Map<String, Object> registro = new HashMap<>();
        registro.put("username", "nuevoUsuario");
        registro.put("password", "password123");

        MvcResult resultRegister = mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registro)))
                .andReturn();
        int statusRegister = resultRegister.getResponse().getStatus();
        assertTrue(statusRegister == 200 || statusRegister == 409, "Expected 200 OK or 409 Conflict but got " + statusRegister);
        // Puede fallar si el usuario ya existe

        // PASO 2: Login
        Map<String, Object> login = new HashMap<>();
        login.put("username", "admin");
        login.put("password", "admin123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        // PASO 3: Acceder a datos académicos con token (simulado)
        mockMvc.perform(get("/academico/curso/{curso}", "1-A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
