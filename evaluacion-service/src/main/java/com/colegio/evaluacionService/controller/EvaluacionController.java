package com.colegio.evaluacionService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.evaluacionService.model.Evaluacion;
import com.colegio.evaluacionService.service.EvaluacionService;

@RestController
@RequestMapping("/evaluaciones")
public class EvaluacionController {

    @Autowired
    private EvaluacionService service;

    @GetMapping
    public List<Evaluacion> listar() {
        return service.listar();
    }

    @PostMapping
    public Evaluacion guardar(@RequestBody Evaluacion evaluacion) {
        return service.guardar(
                evaluacion.getNombre(),
                evaluacion.getMateria(),
                evaluacion.getNota()
        );
    }
}