package com.colegio.estudianteservice.event;

import java.time.Instant;

public class NotaEvent {

    private String eventId;
    private String tipo;
    private Instant timestamp;
    private Long evaluacionId;
    private Long estudianteId;
    private String materia;
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
