package com.colegio.bffservice.facade;

import com.colegio.bffservice.model.AcademicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;
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
            .uri(evaluacionServiceUrl + "/evaluaciones?nombre=" + idBuscado)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        return new AcademicoDTO(estudiante, asistencias, evaluaciones);
    }

    public AcademicoDTO obtenerDatosAcademicosPorRun(String run) {
        var estudiantePorRun = webClientBuilder.build()
            .get()
            .uri(estudianteServiceUrl + "/estudiantes/run/" + run)
            .retrieve()
            .bodyToMono(java.util.Map.class)
            .block();

        if (!(estudiantePorRun instanceof java.util.Map<?, ?> estudianteMap)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado: " + run);
        }

        Object idValue = estudianteMap.get("id");
        if (idValue == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Estudiante no encontrado: " + run);
        }

        Long idBuscado = Long.valueOf(idValue.toString());

        var estudiante = webClientBuilder.build()
                .get()
                .uri(estudianteServiceUrl + "/estudiantes/" + idBuscado)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        var asistencias = webClientBuilder.build()
            .get()
            .uri(asistenciaServiceUrl + "/asistencias/estudiante/" + idBuscado)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        var evaluaciones = webClientBuilder.build()
            .get()
            .uri(evaluacionServiceUrl + "/evaluaciones?nombre=" + idBuscado)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        return new AcademicoDTO(estudiante, asistencias, evaluaciones);
    }

    public Object marcarAsistenciaEstudiante(Long estudianteId, Boolean presente) {
        String fecha = java.time.LocalDate.now().toString();
        var asistenciaBody = java.util.Map.of(
            "estudianteId", estudianteId,
            "fecha", fecha,
            "presente", presente
        );

        var created = webClientBuilder.build()
            .post()
            .uri(asistenciaServiceUrl + "/asistencias")
            .bodyValue(asistenciaBody)
            .retrieve()
            .bodyToMono(Object.class)
            .block();

        return created;
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
        var evaluacionBody = java.util.Map.of(
            "estudianteId", estudianteId,
            "materia", materia,
            "nota", nota
        );

        return webClientBuilder.build()
            .post()
            .uri(evaluacionServiceUrl + "/evaluaciones")
            .bodyValue(evaluacionBody)
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
                .bodyToMono(java.util.Map.class)
                .block();

        if (estudiante instanceof java.util.Map<?, ?> estudianteMap) {
            Object idValue = estudianteMap.get("id");
            if (idValue != null) {
                return Long.valueOf(idValue.toString());
            }
        }

        throw new IllegalArgumentException("Estudiante no encontrado: " + estudianteId);
    }
}