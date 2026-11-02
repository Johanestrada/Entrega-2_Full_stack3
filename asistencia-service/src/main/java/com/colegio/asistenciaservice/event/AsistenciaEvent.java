package com.colegio.asistenciaservice.event;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.Instant;

public class AsistenciaEvent {

    @NotBlank(message = "El eventId es obligatorio")
    private String eventId;

    @NotBlank(message = "El tipo del evento es obligatorio")
    private String tipo;

    @NotNull(message = "El timestamp es obligatorio")
    private Instant timestamp;

    @NotNull(message = "El id de la asistencia es obligatorio")
    @Positive(message = "El id de la asistencia debe ser positivo")
    private Long asistenciaId;

    @NotNull(message = "El id del estudiante es obligatorio")
    @Positive(message = "El id del estudiante debe ser positivo")
    private Long estudianteId;

    @NotBlank(message = "La fecha es obligatoria")
    private String fecha;

    @NotBlank(message = "El estado es obligatorio")
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
