package com.colegio.asistenciaservice.factory;

import com.colegio.asistenciaservice.model.Asistencia;

public class AsistenciaFactory {

    public static Asistencia crear(Long estudianteId, String fecha) {

        Asistencia asistencia = new Asistencia();

        asistencia.setEstudianteId(estudianteId);
        asistencia.setFecha(fecha);
        asistencia.setPresente(true);

        return asistencia;
    }
}