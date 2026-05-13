package com.colegio.estudianteservice.controller;

import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.service.EstudianteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService service;

    @GetMapping
    public List<Estudiante> listar() {
        return service.listar();
    }

    @PostMapping
    public Estudiante guardar(
            @RequestBody Estudiante estudiante
    ) {

        return service.guardar(
                estudiante.getNombre(),
                estudiante.getCurso()
        );
    }
}