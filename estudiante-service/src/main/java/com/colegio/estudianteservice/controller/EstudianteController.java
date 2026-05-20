package com.colegio.estudianteservice.controller;

import java.util.List;

import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<Estudiante>> listar() {
        List<Estudiante> estudiantes = estudianteService.getAll();
        if(estudiantes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerPorId(@PathVariable("id") Long id) {
        Estudiante estudiante = estudianteService.getEstudianteById(id);
        if(estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudiante);
    }

    @GetMapping("/curso/{curso}")
    public ResponseEntity<List<Estudiante>> obtenerPorCurso(@PathVariable("curso") String curso) {
        List<Estudiante> estudiantes = estudianteService.getEstudiantesPorCurso(curso);
        return ResponseEntity.ok(estudiantes);
    }

    @GetMapping("/run/{run}")
    public ResponseEntity<Estudiante> obtenerPorRun(@PathVariable("run") String run) {
        Estudiante estudiante = estudianteService.getEstudianteByRun(run);
        if(estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(estudiante);
    }

    @PostMapping
    public ResponseEntity<Estudiante> guardar(@RequestBody Estudiante estudiante) {
        Estudiante nuevoEstudiante = estudianteService.save(estudiante);
        return ResponseEntity.ok(nuevoEstudiante);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        estudianteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}