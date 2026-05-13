package com.colegio.estudianteservice.factory;

import com.colegio.estudianteservice.model.Estudiante;

public class EstudianteFactory {

    public static Estudiante crear(
            String nombre,
            String curso
    ) {

        Estudiante estudiante =
                new Estudiante();

        estudiante.setNombre(nombre);
        estudiante.setCurso(curso);

        return estudiante;
    }
}