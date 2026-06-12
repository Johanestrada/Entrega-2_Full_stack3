package com.colegio.bffservice;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

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
class AcademicoIntegrationTest extends AbstractMockWebServerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Prueba de integración: Verificar que BFF obtiene datos académicos de un estudiante
     * Flujo: BFF → Estudiante Service → BD
     */
    @Test
    void testObtenerDatosAcademicosPorId_DebeComponerDatosDeMultiplesServicios() throws Exception {
        Long estudianteId = 1L;

        mockMvc.perform(get("/academico/{estudianteId}", estudianteId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estudiante", notNullValue()));
    }

    @Test
    void testCrearAsistencia_DebeGuardarEnAsistenciaService() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("estudianteId", 1L);
        body.put("presente", true);

        mockMvc.perform(post("/academico/asistencias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testCrearEvaluacion_DebeGuardarEnEvaluacionService() throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("estudianteId", 1L);
        body.put("materia", "Matemáticas");
        body.put("nota", 7.5);

        mockMvc.perform(post("/academico/evaluaciones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerEstudiantesPorCurso_DebeRetornarLista() throws Exception {
        String curso = "1-A";

        mockMvc.perform(get("/academico/curso/{curso}", curso)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void testObtenerDatosAcademicosPorRun_DebeRetornarDatos() throws Exception {
        String run = "20.111.222-3";

        MvcResult result = mockMvc.perform(get("/academico/run/{run}", run)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        int status = result.getResponse().getStatus();
        assertTrue(status == 200 || status == 404, "Expected 200 OK or 404 Not Found but got " + status);
    }

    @Test
    void testMarcarAsistenciaCurso_DebeActualizarTodosLosEstudiantes() throws Exception {
        String curso = "1-A";
        Map<String, Object> body = new HashMap<>();
        body.put("presente", true);

        mockMvc.perform(post("/academico/curso/{curso}/asistencia", curso)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }
}
