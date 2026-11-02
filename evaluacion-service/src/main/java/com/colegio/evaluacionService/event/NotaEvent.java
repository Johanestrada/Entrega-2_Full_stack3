package com.colegio.evaluacionService.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;

public class NotaEvent {

    @NotBlank(message = "El eventId es obligatorio")
    private String eventId;

    @NotBlank(message = "El tipo del evento es obligatorio")
    private String tipo;

    @NotNull(message = "El timestamp es obligatorio")
    private Instant timestamp;

    @NotNull(message = "El id de la evaluación es obligatorio")
    @Positive(message = "El id de la evaluación debe ser positivo")
    private Long evaluacionId;

    @NotNull(message = "El id del estudiante es obligatorio")
    @Positive(message = "El id del estudiante debe ser positivo")
    private Long estudianteId;

    @NotBlank(message = "La materia es obligatoria")
    private String materia;

    @NotNull(message = "La nota es obligatoria")
    private Double nota;

    public NotaEvent() {
    }

    public NotaEvent(Long evaluacionId, Long estudianteId, String materia, Double nota) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.tipo = "calificaciones.eventos";
        this.timestamp = Instant.now();
        this.evaluacionId = evaluacionId;
        this.estudianteId = estudianteId;
        this.materia = materia;
        this.nota = nota;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public Long getEvaluacionId() {
        return evaluacionId;
    }

    public void setEvaluacionId(Long evaluacionId) {
        this.evaluacionId = evaluacionId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getMateria() {
        return materia;
    }

    public void setMateria(String materia) {
        this.materia = materia;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }
}
