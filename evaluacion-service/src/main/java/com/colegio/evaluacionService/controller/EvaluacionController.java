package com.colegio.evaluacionService.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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

    @GetMapping("/{id}")
    public Evaluacion obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping(params = "nombre")
    public List<Evaluacion> listarPorNombre(@RequestParam String nombre) {
        return service.listarPorNombre(nombre);
    }

    @PostMapping
    public Evaluacion guardar(@RequestBody Evaluacion evaluacion) {
        return service.guardar(
                evaluacion.getNombre(),
                evaluacion.getMateria(),
                evaluacion.getNota()
        );
    }

    @PutMapping("/{id}")
    public Evaluacion actualizar(
            @PathVariable Long id,
            @RequestBody Evaluacion evaluacion
    ) {
        return service.actualizar(id, evaluacion);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}