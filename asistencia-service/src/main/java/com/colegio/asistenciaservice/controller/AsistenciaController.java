package com.colegio.asistenciaservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.service.AsistenciaService;

@RestController
@RequestMapping("/asistencias")
public class AsistenciaController {

    private final AsistenciaService service;

    public AsistenciaController(AsistenciaService service) {
        this.service = service;
    }


    @GetMapping
    public List<Asistencia> listar() {
        return service.listar();
    }

    @GetMapping("/estudiante/{nombre}")
    public List<Asistencia> listarPorEstudiante(@PathVariable String nombre) {
        return service.listarPorEstudiante(nombre);
    }

    @PostMapping
    public Asistencia guardar(@RequestBody Asistencia asistencia) {
        return service.guardar(asistencia);
    }
}