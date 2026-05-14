package com.colegio.bffservice.facade;

import com.colegio.bffservice.model.AcademicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.List;

@Component
public class AcademicoFacade {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AcademicoDTO obtenerDatosAcademicos(Long estudianteId) {
        var estudiante = webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/estudiantes/" + estudianteId)
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

        if (nombreEstudiante == null || nombreEstudiante.isBlank()) {
            return new AcademicoDTO(estudiante, List.of(), List.of());
        }

        // Usar el nombre extraído en las llamadas
        final String nombreFinal = nombreEstudiante;

        var asistencias = webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8082)
                        .path("/asistencias/estudiante/{nombre}")
                        .build(nombreFinal)
                )
                .retrieve()
                .bodyToMono(List.class)
                .block();

        var evaluaciones = webClientBuilder.build()
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(8083)
                        .path("/evaluaciones")
                        .queryParam("nombre", nombreFinal)
                        .build()
                )
                .retrieve()
                .bodyToMono(List.class)
                .block();

        return new AcademicoDTO(estudiante, asistencias, evaluaciones);
    }
}