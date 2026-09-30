package com.colegio.rabbit_admin.dto;

public class EventoAsistencia extends EventoBase {
    private Long asistenciaId;
    private Long estudianteId;
    private String fecha;
    private String estado;

    public EventoAsistencia() {
        super();
    }

    public EventoAsistencia(Long asistenciaId, Long estudianteId, String fecha, String estado) {
        super("ASISTENCIA-" + System.currentTimeMillis(), "asistencia.registrada", "asistencia-service");
        this.asistenciaId = asistenciaId;
        this.estudianteId = estudianteId;
        this.fecha = fecha;
        this.estado = estado;
    }

    public Long getAsistenciaId() { return asistenciaId; }
    public void setAsistenciaId(Long asistenciaId) { this.asistenciaId = asistenciaId; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
