package com.colegio.bffservice.facade;

import com.colegio.bffservice.model.AcademicoDTO;
import com.colegio.bffservice.dto.EstudianteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import java.util.List;

@Component
public class AcademicoFacade {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("${estudiante.service.url:http://localhost:8081}")
    private String estudianteServiceUrl;

    @Value("${asistencia.service.url:http://localhost:8082}")
    private String asistenciaServiceUrl;

    @Value("${evaluacion.service.url:http://localhost:8083}")
    private String evaluacionServiceUrl;

    public AcademicoDTO obtenerDatosAcademicos(String estudianteId) {
        Long idBuscado = resolveEstudianteId(estudianteId);

        var estudiante = webClientBuilder.build()
                .get()
                .uri(estudianteServiceUrl + "/estudiantes/" + idBuscado)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        String nombreEstudiante = null;
        if (estudiante instanceof java.util.Map<?, ?> estudianteMap) {
            Object nombreValue = estudianteMap.get("nombre");
            if (nombreValue != null) {
                nombreEstudiante = nombreValue.toString();
            }
        }

        var asistencias = webClientBuilder.build()
            .get()
            .uri(asistenciaServiceUrl + "/asistencias/estudiante/" + idBuscado)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        var evaluaciones = webClientBuilder.build()
            .get()
            .uri(evaluacionServiceUrl + "/evaluaciones/estudiante/" + idBuscado)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        return new AcademicoDTO(estudiante, asistencias, evaluaciones);
    }

    public AcademicoDTO obtenerDatosAcademicosPorRun(String run) {
        try {
            var estudiantePorRun = webClientBuilder.build()
                .get()
                .uri(estudianteServiceUrl + "/estudiantes/run/" + run)
                .retrieve()
                .bodyToMono(java.util.Map.class)
                .block();

            if (estudiantePorRun == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado");
            }

            Long idBuscado = Long.valueOf(estudiantePorRun.get("id").toString());

            var asistencias = webClientBuilder.build()
                .get()
                .uri(asistenciaServiceUrl + "/asistencias/estudiante/" + idBuscado)
                .retrieve()
                .bodyToMono(List.class)
                .onErrorReturn(List.of())
                .block();

            var evaluaciones = webClientBuilder.build()
                .get()
                .uri(evaluacionServiceUrl + "/evaluaciones/estudiante/" + idBuscado)
                .retrieve()
                .bodyToMono(List.class)
                .onErrorReturn(List.of())
                .block();

            return new AcademicoDTO(estudiantePorRun, asistencias, evaluaciones);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al obtener datos académicos: " + e.getMessage(), e);
        }
    }

    public Object marcarAsistenciaEstudiante(Long estudianteId, Boolean presente) {
        return webClientBuilder.build()
            .post()
            .uri(asistenciaServiceUrl + "/asistencias")
            .bodyValue(java.util.Map.of("estudianteId", estudianteId, "presente", presente))
            .retrieve()
            .bodyToMono(Object.class)
            .block();
    }

    public List<Object> marcarAsistenciaCurso(String curso, Boolean presente) {
        var estudiantes = webClientBuilder.build()
            .get()
            .uri(estudianteServiceUrl + "/estudiantes/curso/" + curso)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        java.util.List<Object> results = new java.util.ArrayList<>();
        if (estudiantes != null) {
            for (Object e : estudiantes) {
                if (e instanceof java.util.Map<?, ?> m) {
                    Object idValue = m.get("id");
                    if (idValue != null) {
                        Long id = Long.valueOf(idValue.toString());
                        results.add(marcarAsistenciaEstudiante(id, presente));
                    }
                }
            }
        }

        return results;
    }

    public Object guardarEvaluacionEstudiante(Long estudianteId, String materia, Double nota) {
        return webClientBuilder.build()
            .post()
            .uri(evaluacionServiceUrl + "/evaluaciones")
            .bodyValue(java.util.Map.of("estudianteId", estudianteId, "materia", materia, "nota", nota))
            .retrieve()
            .bodyToMono(Object.class)
            .block();
    }

    public Object actualizarEvaluacionEstudiante(Long evaluacionId, Double nota) {
        return webClientBuilder.build()
            .put()
            .uri(evaluacionServiceUrl + "/evaluaciones/" + evaluacionId)
            .bodyValue(java.util.Map.of("nota", nota))
            .retrieve()
            .bodyToMono(Object.class)
            .block();
    }

    public List<Object> obtenerEstudiantesPorCurso(String curso) {
        return webClientBuilder.build()
            .get()
            .uri(estudianteServiceUrl + "/estudiantes/curso/" + curso)
            .retrieve()
            .bodyToMono(List.class)
            .block();
    }

    private Long resolveEstudianteId(String estudianteId) {
        if (estudianteId.matches("\\d+")) {
            return Long.valueOf(estudianteId);
        }

        var estudiante = webClientBuilder.build()
                .get()
                .uri(estudianteServiceUrl + "/estudiantes/run/" + estudianteId)
                .retrieve()
                .onStatus(HttpStatus.NOT_FOUND::equals, response ->
                    Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante con RUN " + estudianteId + " no encontrado."))
                )
                .bodyToMono(EstudianteDTO.class)
                .block();

        if (estudiante != null && estudiante.getId() != null) {
            return estudiante.getId();
        }

        throw new IllegalArgumentException("Estudiante no encontrado: " + estudianteId);
    }

    public Object crearEstudiante(Object estudiante) {
        // La URL para crear es http://estudiante-service:8081/estudiantes
        String url = estudianteServiceUrl + "/estudiantes";
        return webClientBuilder.build()
            .post()
            .uri(url)
            .bodyValue(estudiante)
            .retrieve()
            .bodyToMono(Object.class)
            .block();
    }

    public void eliminarEstudiante(Long id) {
        String url = estudianteServiceUrl + "/estudiantes/" + id;
        webClientBuilder.build()
            .delete()
            .uri(url)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}