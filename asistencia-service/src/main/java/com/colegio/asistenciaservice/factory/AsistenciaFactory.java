package com.colegio.asistenciaservice.factory;

import com.colegio.asistenciaservice.model.Asistencia;

public class AsistenciaFactory {

    public static Asistencia crear(String estudiante, String fecha) {

        Asistencia asistencia = new Asistencia();

        asistencia.setEstudiante(estudiante);
        asistencia.setFecha(fecha);
        asistencia.setPresente(true);

        return asistencia;
    }
}