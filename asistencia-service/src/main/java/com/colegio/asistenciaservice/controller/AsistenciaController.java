package com.colegio.asistenciaservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.service.AsistenciaService;

@RestController
@CrossOrigin(origins = "http://localhost:4173")
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

    @GetMapping("/{id}")
    public Asistencia obtenerPorId(@PathVariable Long id) {
        return service.obtenerPorId(id);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<Asistencia> listarPorEstudiante(@PathVariable Long estudianteId) {
        return service.listarPorEstudiante(String.valueOf(estudianteId));
    }

    @PostMapping
    public Asistencia guardar(@RequestBody Asistencia asistencia) {
        if (asistencia.getEstudianteId() == null) {
            throw new IllegalArgumentException("estudianteId must be provided");
        }
        asistencia.setId(null);
        return service.guardar(asistencia);
    }

    @PutMapping("/{id}")
    public Asistencia actualizar(
            @PathVariable Long id,
            @RequestBody Asistencia asistencia
    ) {
        return service.actualizar(id, asistencia);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}

