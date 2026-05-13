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
        // Llamadas a microservicios (mock, puedes mejorar luego)
        var estudiante = webClientBuilder.build()
                .get()
                .uri("http://localhost:8081/estudiantes/" + estudianteId)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        var asistencias = webClientBuilder.build()
                .get()
                .uri("http://localhost:8082/asistencias/estudiante/" + estudianteId)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        var evaluaciones = webClientBuilder.build()
                .get()
                .uri("http://localhost:8083/evaluaciones?estudianteId=" + estudianteId)
                .retrieve()
                .bodyToMono(List.class)
                .block();

        return new AcademicoDTO(estudiante, asistencias, evaluaciones);
    }
}