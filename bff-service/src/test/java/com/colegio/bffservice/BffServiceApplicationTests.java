package com.colegio.bffservice;

import com.colegio.bffservice.dto.EstudianteDTO;
import com.colegio.bffservice.service.AcademicoService;
import com.colegio.bffservice.controller.AcademicoController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@SpringBootTest // Carga el contexto completo de la aplicación para las pruebas
@AutoConfigureMockMvc // Configura automáticamente MockMvc para pruebas de integración
class BffServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc; // Objeto para simular peticiones HTTP

	@MockBean
	private AcademicoService academicoService; // Mock del servicio para no depender de los microservicios reales

	@Test
	void contextLoads() {
		// Esta prueba básica sigue siendo útil para asegurar que el contexto del test se carga correctamente.
	}

	@Test
	void alBuscarPorCurso_debeRetornarListaDeEstudiantes() throws Exception {
		// 1. Preparación (Arrange)
		String curso = "1-A";
		EstudianteDTO estudiante1 = new EstudianteDTO(1L, "20.111.222-3", "Juan Perez", curso);
		EstudianteDTO estudiante2 = new EstudianteDTO(2L, "21.222.333-4", "Ana Gomez", curso);
		List<EstudianteDTO> estudiantes = Arrays.asList(estudiante1, estudiante2);

		// Simulamos la respuesta del servicio
		when(academicoService.getEstudiantesPorCurso(curso)).thenReturn(estudiantes);

		// 2. Actuación (Act) y 3. Aserción (Assert)
		mockMvc.perform(get("/academico/curso/{curso}", curso).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()) // Esperamos un código 200 OK
				.andExpect(jsonPath("$", hasSize(2))) // Esperamos que la lista JSON tenga 2 elementos
				.andExpect(jsonPath("$[0].nombre", is("Juan Perez"))) // Verificamos el nombre del primer estudiante
				.andExpect(jsonPath("$[1].nombre", is("Ana Gomez"))); // Verificamos el nombre del segundo
	}

}
