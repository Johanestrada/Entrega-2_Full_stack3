package com.colegio.evaluacionService.event;

import java.time.LocalDateTime;
import java.util.UUID;

public class EvaluacionCalificadaEvent {

    private String id;
    private String tipo;
    private LocalDateTime timestamp;
    private String fuente;
    private Long evaluacionId;
    private Long estudianteId;
    private Double calificacion;
    private String materia;

    public EvaluacionCalificadaEvent() {
    }

    public EvaluacionCalificadaEvent(Long evaluacionId, Long estudianteId, Double calificacion, String materia) {
        this.id = UUID.randomUUID().toString();
        this.tipo = "evaluacion.calificada";
        this.timestamp = LocalDateTime.now();
        this.fuente = "evaluacion-service";
        this.evaluacionId = evaluacionId;
        this.estudianteId = estudianteId;
        this.calificacion = calificacion;
        this.materia = materia;
    }

    public String getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFuente() {
        return fuente;
    }

    public Long getEvaluacionId() {
        return evaluacionId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public Double getCalificacion() {
        return calificacion;
    }

    public String getMateria() {
        return materia;
    }
}
