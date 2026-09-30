package com.colegio.rabbit_admin.dto;

public class EventoEvaluacion extends EventoBase {
    private Long evaluacionId;
    private Long estudianteId;
    private Double calificacion;
    private String materia;

    public EventoEvaluacion() {
        super();
    }

    public EventoEvaluacion(Long evaluacionId, Long estudianteId, Double calificacion, String materia) {
        super("EVALUACION-" + System.currentTimeMillis(), "evaluacion.calificada", "evaluacion-service");
        this.evaluacionId = evaluacionId;
        this.estudianteId = estudianteId;
        this.calificacion = calificacion;
        this.materia = materia;
    }

    public Long getEvaluacionId() { return evaluacionId; }
    public void setEvaluacionId(Long evaluacionId) { this.evaluacionId = evaluacionId; }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public Double getCalificacion() { return calificacion; }
    public void setCalificacion(Double calificacion) { this.calificacion = calificacion; }

    public String getMateria() { return materia; }
    public void setMateria(String materia) { this.materia = materia; }
}
