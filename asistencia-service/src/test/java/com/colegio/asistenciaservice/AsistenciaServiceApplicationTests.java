package com.colegio.asistenciaservice;

import com.colegio.asistenciaservice.controller.AsistenciaController;
import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.repository.AsistenciaRepository;
import com.colegio.asistenciaservice.service.AsistenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AsistenciaController.class)
@Import(AsistenciaService.class)
class AsistenciaServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private AsistenciaRepository asistenciaRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void listar_debeRetornarListaDeAsistencias() throws Exception {
		Asistencia a1 = new Asistencia();
		a1.setId(1L);
		a1.setEstudianteId(10L);
		a1.setFecha("2026-05-30");
		a1.setPresente(true);

		Asistencia a2 = new Asistencia();
		a2.setId(2L);
		a2.setEstudianteId(11L);
		a2.setFecha("2026-05-29");
		a2.setPresente(false);

		when(asistenciaRepository.findAll()).thenReturn(Arrays.asList(a1, a2));

		mockMvc.perform(get("/asistencias").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].estudianteId", is(10)));
	}

	@Test
	void obtenerPorId_debeRetornarAsistencia() throws Exception {
		Asistencia asistencia = new Asistencia();
		asistencia.setId(1L);
		asistencia.setEstudianteId(10L);
		asistencia.setFecha("2026-05-30");
		asistencia.setPresente(true);

		when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistencia));

		mockMvc.perform(get("/asistencias/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.presente", is(true)));
	}

	@Test
	void obtenerPorId_inexistente_debeRetornarCuerpoVacio() throws Exception {
		when(asistenciaRepository.findById(99L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/asistencias/{id}", 99L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	void listarPorEstudiante_debeRetornarFiltrado() throws Exception {
		Asistencia asistencia = new Asistencia();
		asistencia.setId(1L);
		asistencia.setEstudianteId(10L);
		asistencia.setFecha("2026-05-30");
		asistencia.setPresente(true);

		when(asistenciaRepository.findByEstudianteId(10L)).thenReturn(Collections.singletonList(asistencia));

		mockMvc.perform(get("/asistencias/estudiante/{estudianteId}", 10L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].estudianteId", is(10)));
	}

	@Test
	void guardar_debeRetornarAsistenciaCreada() throws Exception {
		Asistencia request = new Asistencia();
		request.setEstudianteId(10L);
		request.setFecha("2026-05-30");
		request.setPresente(true);

		Asistencia response = new Asistencia();
		response.setId(100L);
		response.setEstudianteId(10L);
		response.setFecha("2026-05-30");
		response.setPresente(true);

		when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(response);

		mockMvc.perform(post("/asistencias")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(100)))
				.andExpect(jsonPath("$.presente", is(true)));

		verify(asistenciaRepository, times(1)).save(any(Asistencia.class));
	}

	@Test
	void actualizar_debeRetornarAsistenciaActualizada() throws Exception {
		Asistencia actual = new Asistencia();
		actual.setId(1L);
		actual.setEstudianteId(10L);
		actual.setFecha("2026-05-30");
		actual.setPresente(true);

		Asistencia updated = new Asistencia();
		updated.setId(1L);
		updated.setEstudianteId(10L);
		updated.setFecha("2026-05-31");
		updated.setPresente(false);

		when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(actual));
		when(asistenciaRepository.save(any(Asistencia.class))).thenReturn(updated);

		mockMvc.perform(put("/asistencias/{id}", 1L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updated)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.fecha", is("2026-05-31")))
				.andExpect(jsonPath("$.presente", is(false)));
	}

	@Test
	void listarPorEstudiante_conMultiplesAsistencias_debeRetornarTodas() throws Exception {
		Asistencia a1 = new Asistencia();
		a1.setId(1L);
		a1.setEstudianteId(10L);
		a1.setFecha("2026-05-30");
		a1.setPresente(true);

		Asistencia a2 = new Asistencia();
		a2.setId(2L);
		a2.setEstudianteId(10L);
		a2.setFecha("2026-05-29");
		a2.setPresente(true);

		Asistencia a3 = new Asistencia();
		a3.setId(3L);
		a3.setEstudianteId(10L);
		a3.setFecha("2026-05-28");
		a3.setPresente(false);

		when(asistenciaRepository.findByEstudianteId(10L)).thenReturn(Arrays.asList(a1, a2, a3));

		mockMvc.perform(get("/asistencias/estudiante/{estudianteId}", 10L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(3)));
	}

	@Test
	void obtenerPorId_conIdValido_debeRetornarAsistencia() throws Exception {
		Asistencia asistencia = new Asistencia();
		asistencia.setId(1L);
		asistencia.setEstudianteId(10L);
		asistencia.setFecha("2026-05-30");
		asistencia.setPresente(true);

		when(asistenciaRepository.findById(1L)).thenReturn(Optional.of(asistencia));

		mockMvc.perform(get("/asistencias/{id}", 1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.estudianteId", is(10)))
				.andExpect(jsonPath("$.presente", is(true)));
	}

	@Test
	void eliminar_conIdValido_debeRetornarNoContent() throws Exception {
		mockMvc.perform(delete("/asistencias/{id}", 1L))
				.andExpect(status().isNoContent());

		verify(asistenciaRepository, times(1)).deleteById(1L);
	}

	@Test
	void eliminar_conIdInexistente_debeRetornarNoContent() throws Exception {
		mockMvc.perform(delete("/asistencias/{id}", 999L))
				.andExpect(status().isNoContent());

		verify(asistenciaRepository, times(1)).deleteById(999L);
	}
}
