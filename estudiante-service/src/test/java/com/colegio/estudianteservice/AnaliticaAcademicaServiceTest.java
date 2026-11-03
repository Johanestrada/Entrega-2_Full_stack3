package com.colegio.estudianteservice;

import com.colegio.estudianteservice.event.EstudianteCreadoEvent;
import com.colegio.estudianteservice.service.AnaliticaAcademicaService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AnaliticaAcademicaServiceTest {

    @Test
    void registrarEstudianteCuentaIdsUnicos() {
        AnaliticaAcademicaService service = new AnaliticaAcademicaService();

        service.registrarEstudiante(new EstudianteCreadoEvent(1L, "Ana", "1-A"));
        service.registrarEstudiante(new EstudianteCreadoEvent(1L, "Ana", "1-A"));
        service.registrarEstudiante(new EstudianteCreadoEvent(2L, "Luis", "2-B"));

        assertEquals(2, service.obtenerResumen().get("totalEstudiantesRegistrados"));
    }
}
