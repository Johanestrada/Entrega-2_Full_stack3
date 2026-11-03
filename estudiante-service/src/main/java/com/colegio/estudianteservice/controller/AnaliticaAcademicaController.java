package com.colegio.estudianteservice.controller;

import com.colegio.estudianteservice.service.AnaliticaAcademicaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/analitica")
public class AnaliticaAcademicaController {

    private final AnaliticaAcademicaService analiticaAcademicaService;

    public AnaliticaAcademicaController(AnaliticaAcademicaService analiticaAcademicaService) {
        this.analiticaAcademicaService = analiticaAcademicaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> resumen() {
        return ResponseEntity.ok(analiticaAcademicaService.obtenerResumen());
    }
}
