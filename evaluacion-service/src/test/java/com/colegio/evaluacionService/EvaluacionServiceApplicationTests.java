package com.colegio.evaluacionService;

import com.colegio.evaluacionService.controller.EvaluacionController;
import com.colegio.evaluacionService.model.Evaluacion;
import com.colegio.evaluacionService.repository.EvaluacionRepository;
import com.colegio.evaluacionService.service.EvaluacionService;
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

@WebMvcTest(EvaluacionController.class)
@Import(EvaluacionService.class)
class EvaluacionServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EvaluacionRepository evaluacionRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	void contextLoads() {
	}

	@Test
	void listar_debeRetornarListaDeEvaluaciones() throws Exception {
		Evaluacion ev1 = new Evaluacion(1L, "Matemáticas", 6.5);
		ev1.setId(10L);
		Evaluacion ev2 = new Evaluacion(1L, "Lenguaje", 5.8);
		ev2.setId(11L);

		when(evaluacionRepository.findAll()).thenReturn(Arrays.asList(ev1, ev2));

		mockMvc.perform(get("/evaluaciones").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].materia", is("Matemáticas")))
				.andExpect(jsonPath("$[1].materia", is("Lenguaje")));
	}

	@Test
	void obtenerPorId_debeRetornarEvaluacion() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.findById(10L)).thenReturn(Optional.of(ev));

		mockMvc.perform(get("/evaluaciones/{id}", 10L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(10)))
				.andExpect(jsonPath("$.materia", is("Matemáticas")));
	}

	@Test
	void obtenerPorId_inexistente_debeRetornarNullComoRespuestaVacua() throws Exception {
		when(evaluacionRepository.findById(99L)).thenReturn(Optional.empty());

		mockMvc.perform(get("/evaluaciones/{id}", 99L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	void listarPorNombre_debeFiltrarPorEstudianteId() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.findByEstudianteId(1L)).thenReturn(Collections.singletonList(ev));

		mockMvc.perform(get("/evaluaciones").param("nombre", "1").contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].estudianteId", is(1)));
	}

	@Test
	void guardar_conDatosValidos_debeRetornarCreado() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.save(any())).thenReturn(ev);

		mockMvc.perform(post("/evaluaciones")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"estudianteId\": 1, \"materia\": \"Matemáticas\", \"nota\": 6.5}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(10)))
				.andExpect(jsonPath("$.materia", is("Matemáticas")));
	}

	@Test
	void guardar_conDatosValidos_y_guardarEnBD() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(ev);

		mockMvc.perform(post("/evaluaciones")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"estudianteId\": 1, \"materia\": \"Matemáticas\", \"nota\": 6.5}"))
				.andExpect(status().isOk());
	}

	@Test
	void obtenerPorEstudiante_debeRetornarListaFiltrada() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.findByEstudianteId(1L)).thenReturn(Collections.singletonList(ev));

		mockMvc.perform(get("/evaluaciones/estudiante/{estudianteId}", 1L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void obtenerPorEstudiante_sinEvaluaciones_debeRetornarListaVacia() throws Exception {
		when(evaluacionRepository.findByEstudianteId(99L)).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/evaluaciones/estudiante/{estudianteId}", 99L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void actualizar_conDatosValidos_debeActualizarNota() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.findById(10L)).thenReturn(Optional.of(ev));
		when(evaluacionRepository.save(any())).thenReturn(ev);

		mockMvc.perform(put("/evaluaciones/{id}", 10L)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"nota\": 7.5}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(10)));
	}

	@Test
	void eliminar_conIdValido_debeRetornarNoContent() throws Exception {
		mockMvc.perform(delete("/evaluaciones/{id}", 10L)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(evaluacionRepository, times(1)).deleteById(10L);
	}

	@Test
	void listar_conResultados_debeRetornarOk() throws Exception {
		Evaluacion ev1 = new Evaluacion(1L, "Matemáticas", 6.5);
		ev1.setId(10L);

		when(evaluacionRepository.findAll()).thenReturn(Collections.singletonList(ev1));

		mockMvc.perform(get("/evaluaciones")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void listar_sinResultados_debeRetornarOkConListaVacia() throws Exception {
		when(evaluacionRepository.findAll()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/evaluaciones")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void guardar_debeRetornarEvaluacionCreada() throws Exception {
		Evaluacion request = new Evaluacion(1L, "Matemáticas", 6.5);
		Evaluacion response = new Evaluacion(1L, "Matemáticas", 6.5);
		response.setId(100L);

		when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(response);

		mockMvc.perform(post("/evaluaciones")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(100)))
				.andExpect(jsonPath("$.materia", is("Matemáticas")));

		verify(evaluacionRepository, times(1)).save(any(Evaluacion.class));
	}

	@Test
	void obtenerPorEstudiante_debeRetornarLista() throws Exception {
		Evaluacion ev = new Evaluacion(1L, "Matemáticas", 6.5);
		ev.setId(10L);

		when(evaluacionRepository.findByEstudianteId(1L)).thenReturn(Collections.singletonList(ev));

		mockMvc.perform(get("/evaluaciones/estudiante/{estudianteId}", 1L).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)));
	}

	@Test
	void actualizar_debeRetornarEvaluacionActualizada() throws Exception {
		Evaluacion actual = new Evaluacion(1L, "Matemáticas", 6.5);
		actual.setId(10L);
		Evaluacion updated = new Evaluacion(1L, "Matemáticas", 7.0);
		updated.setId(10L);

		when(evaluacionRepository.findById(10L)).thenReturn(Optional.of(actual));
		when(evaluacionRepository.save(any(Evaluacion.class))).thenReturn(updated);

		mockMvc.perform(put("/evaluaciones/{id}", 10L)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(Collections.singletonMap("nota", 7.0))))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nota", is(7.0)));
	}

	@Test
	void eliminar_debeRetornarNoContent() throws Exception {
		mockMvc.perform(delete("/evaluaciones/{id}", 10L))
				.andExpect(status().isNoContent());

		verify(evaluacionRepository, times(1)).deleteById(10L);
	}
}
