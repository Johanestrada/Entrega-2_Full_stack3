package com.colegio.bffservice;

import com.colegio.bffservice.controller.AcademicoController;
import com.colegio.bffservice.dto.EstudianteDTO;
import com.colegio.bffservice.facade.AcademicoFacade;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.oauth2.resource.servlet.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AcademicoController.class, excludeAutoConfiguration = {
		SecurityAutoConfiguration.class,
		OAuth2ResourceServerAutoConfiguration.class
})
@AutoConfigureMockMvc(addFilters = false)
class BffServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AcademicoFacade academicoFacade;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void alBuscarPorCurso_debeRetornarListaDeEstudiantes() throws Exception {
		String curso = "1-A";

		EstudianteDTO estudiante1 = new EstudianteDTO();
		estudiante1.setId(1L);
		estudiante1.setRun("20.111.222-3");
		estudiante1.setNombre("Juan Perez");
		estudiante1.setCurso(curso);

		EstudianteDTO estudiante2 = new EstudianteDTO();
		estudiante2.setId(2L);
		estudiante2.setRun("21.222.333-4");
		estudiante2.setNombre("Ana Gomez");
		estudiante2.setCurso(curso);

		List<Object> estudiantes = Arrays.<Object>asList(estudiante1, estudiante2);

		when(academicoFacade.obtenerEstudiantesPorCurso(curso)).thenReturn(estudiantes);

		mockMvc.perform(get("/academico/curso/{curso}", curso)
				.contentType(MediaType.APPLICATION_JSON)
				.with(httpBasic("user", "password")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].nombre", Matchers.is("Juan Perez")))
				.andExpect(jsonPath("$[1].nombre", Matchers.is("Ana Gomez")));
	}

	@Test
	void alCrearAsistencia_debeRetornarObjetoCreado() throws Exception {
		Map<String, Object> response = Map.of("status", "creado");

		when(academicoFacade.crearAsistencia(any(Object.class))).thenReturn(response);

		mockMvc.perform(post("/academico/asistencias")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(Map.of(
								"estudianteId", 1L,
								"presente", true)))
						.with(httpBasic("user", "password"))
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.status", Matchers.is("creado")));

		verify(academicoFacade, times(1)).crearAsistencia(any(Object.class));
	}

	@Test
	void alCrearEvaluacion_debeRetornarObjetoCreado() throws Exception {
		Map<String, Object> response = Map.of("id", 100);

		when(academicoFacade.crearEvaluacion(any(Object.class))).thenReturn(response);

		mockMvc.perform(post("/academico/evaluaciones")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(Map.of(
								"estudianteId", 1L,
								"materia", "Matemáticas",
								"nota", 6.5)))
						.with(httpBasic("user", "password"))
						.with(csrf()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", Matchers.is(100)));

		verify(academicoFacade, times(1)).crearEvaluacion(any(Object.class));
	}
}
