package com.colegio.estudianteservice;

import com.colegio.estudianteservice.controller.EstudianteController;
import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.repository.EstudianteRepository;
import com.colegio.estudianteservice.service.EstudianteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EstudianteController.class)
@Import(EstudianteService.class)
class EstudianteServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EstudianteRepository estudianteRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void listar_debeRetornarListaDeEstudiantes() throws Exception {
		Estudiante estudiante1 = new Estudiante(1L, "20.111.222-3", "Juan Perez", "1-A");
		Estudiante estudiante2 = new Estudiante(2L, "21.222.333-4", "Ana Gomez", "1-A");
		List<Estudiante> estudiantes = Arrays.asList(estudiante1, estudiante2);

		when(estudianteRepository.findAll()).thenReturn(estudiantes);

		mockMvc.perform(get("/estudiantes").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].nombre", is("Juan Perez")))
				.andExpect(jsonPath("$[1].nombre", is("Ana Gomez")));
	}

	@Test
	void listar_sinDatos_debeRetornarNoContent() throws Exception {
		when(estudianteRepository.findAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/estudiantes").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void obtenerPorId_debeRetornarEstudiante() throws Exception {
		Estudiante estudiante = new Estudiante(1L, "20.111.222-3", "Juan Perez", "1-A");
		when(estudianteRepository.findById(1L)).thenReturn(java.util.Optional.of(estudiante));

		mockMvc.perform(get("/estudiantes/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.nombre", is("Juan Perez")));
	}

	@Test
	void obtenerPorId_inexistente_debeRetornarNotFound() throws Exception {
		when(estudianteRepository.findById(99L)).thenReturn(java.util.Optional.empty());

		mockMvc.perform(get("/estudiantes/{id}", 99L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void obtenerPorCurso_debeRetornarListaFiltrada() throws Exception {
		String curso = "1-A";
		Estudiante estudiante1 = new Estudiante(1L, "20.111.222-3", "Juan Perez", curso);
		Estudiante estudiante2 = new Estudiante(2L, "21.222.333-4", "Ana Gomez", curso);

		when(estudianteRepository.findByCurso(curso)).thenReturn(Arrays.asList(estudiante1, estudiante2));

		mockMvc.perform(get("/estudiantes/curso/{curso}", curso).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].curso", is(curso)));
	}

	@Test
	void obtenerPorRun_debeRetornarEstudiante() throws Exception {
		Estudiante estudiante = new Estudiante(1L, "20.111.222-3", "Juan Perez", "1-A");
		when(estudianteRepository.findByRun("20.111.222-3")).thenReturn(estudiante);

		mockMvc.perform(get("/estudiantes/run/{run}", "20.111.222-3").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.run", is("20.111.222-3")));
	}

	@Test
	void guardar_debeRetornarEstudianteCreado() throws Exception {
		Estudiante request = new Estudiante(null, "20.111.222-3", "Juan Perez", "1-A");
		Estudiante response = new Estudiante(1L, "20.111.222-3", "Juan Perez", "1-A");

		when(estudianteRepository.save(any(Estudiante.class))).thenReturn(response);

		mockMvc.perform(post("/estudiantes")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.nombre", is("Juan Perez")));

		verify(estudianteRepository, times(1)).save(any(Estudiante.class));
	}

	@Test
	void eliminar_debeRetornarNoContent() throws Exception {
		mockMvc.perform(delete("/estudiantes/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(estudianteRepository, times(1)).deleteById(1L);
	}
}
