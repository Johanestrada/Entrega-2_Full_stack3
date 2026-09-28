package com.colegio.bffservice.model;

import com.colegio.bffservice.dto.EstudianteDTO;
import java.util.List;

public class AcademicoDTO {
    private Object estudiante;
    private List<?> asistencias;
    private List<?> evaluaciones;

    // Constructor para la búsqueda por ID (que devuelve un Object)
    public AcademicoDTO(Object estudiante, List<?> asistencias, List<?> evaluaciones) {
        this.estudiante = estudiante;
        this.asistencias = asistencias;
        this.evaluaciones = evaluaciones;
    }

    // Constructor para la búsqueda por RUN (que devuelve un EstudianteDTO)
    public AcademicoDTO(EstudianteDTO estudiante, List<?> asistencias, List<?> evaluaciones) {
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

    public List<?> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<?> asistencias) {
        this.asistencias = asistencias;
    }

    public List<?> getEvaluaciones() {
        return evaluaciones;
    }

    public void setEvaluaciones(List<?> evaluaciones) {
        this.evaluaciones = evaluaciones;
    }
}