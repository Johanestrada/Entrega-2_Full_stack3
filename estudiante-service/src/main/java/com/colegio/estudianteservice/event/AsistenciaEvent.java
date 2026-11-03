package com.colegio.estudianteservice.event;

import java.time.Instant;

public class AsistenciaEvent {

    private String eventId;
    private String tipo;
    private Instant timestamp;
    private Long asistenciaId;
    private Long estudianteId;
    private String fecha;
    private String estado;

    public AsistenciaEvent() {
    }

    public AsistenciaEvent(Long asistenciaId, Long estudianteId, String fecha, String estado) {
        this.eventId = java.util.UUID.randomUUID().toString();
        this.tipo = "asistencia.eventos";
        this.timestamp = Instant.now();
        this.asistenciaId = asistenciaId;
        this.estudianteId = estudianteId;
        this.fecha = fecha;
        this.estado = estado;
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

    public Long getAsistenciaId() {
        return asistenciaId;
    }

    public void setAsistenciaId(Long asistenciaId) {
        this.asistenciaId = asistenciaId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(Long estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
