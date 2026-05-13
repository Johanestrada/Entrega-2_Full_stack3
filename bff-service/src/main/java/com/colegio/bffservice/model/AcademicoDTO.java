package com.colegio.bffservice.model;

public class AcademicoDTO {
    private Object estudiante;
    private Object asistencias;
    private Object evaluaciones;

    public AcademicoDTO(Object estudiante, Object asistencias, Object evaluaciones) {
        this.estudiante = estudiante;
        this.asistencias = asistencias;
        this.evaluaciones = evaluaciones;
    }

    public Object getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Object estudiante) {
        this.estudiante = estudiante;
    }

    public Object getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(Object asistencias) {
        this.asistencias = asistencias;
    }

    public Object getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(Object evaluaciones) {
        this.evaluaciones = evaluaciones;
    }
}