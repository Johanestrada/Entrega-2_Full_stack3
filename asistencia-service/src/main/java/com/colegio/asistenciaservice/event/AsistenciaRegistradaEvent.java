package com.colegio.asistenciaservice.event;

import java.time.LocalDateTime;

public class AsistenciaRegistradaEvent {

    private String id;
    private String tipo;
    private LocalDateTime timestamp;
    private String fuente;
    private Long asistenciaId;
    private Long estudianteId;
    private String fecha;
    private String estado;

    public AsistenciaRegistradaEvent() {
    }

    public AsistenciaRegistradaEvent(Long asistenciaId, Long estudianteId, String fecha, String estado) {
        this.id = java.util.UUID.randomUUID().toString();
        this.tipo = "asistencia.registrada";
        this.timestamp = LocalDateTime.now();
        this.fuente = "asistencia-service";
        this.asistenciaId = asistenciaId;
        this.estudianteId = estudianteId;
        this.fecha = fecha;
        this.estado = estado;
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

    public Long getAsistenciaId() {
        return asistenciaId;
    }

    public Long getEstudianteId() {
        return estudianteId;
    }

    public String getFecha() {
        return fecha;
    }

    public String getEstado() {
        return estado;
    }
}
