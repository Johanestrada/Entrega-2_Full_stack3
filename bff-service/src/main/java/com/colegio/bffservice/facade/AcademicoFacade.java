package com.colegio.bffservice.facade;

import com.colegio.bffservice.model.AcademicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
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

    public AcademicoDTO obtenerDatosAcademicos(Long estudianteId) {
        var estudiante = webClientBuilder.build()
                .get()
                .uri(estudianteServiceUrl + "/estudiantes/" + estudianteId)
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

        // Usar el estudianteId en las llamadas a asistencia/evaluacion
        var asistencias = webClientBuilder.build()
            .get()
            .uri(asistenciaServiceUrl + "/asistencias/estudiante/" + estudianteId)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        var evaluaciones = webClientBuilder.build()
            .get()
            .uri(evaluacionServiceUrl + "/evaluaciones?nombre=" + estudianteId)
            .retrieve()
            .bodyToMono(List.class)
            .block();

        return new AcademicoDTO(estudiante, asistencias, evaluaciones);
    }
}