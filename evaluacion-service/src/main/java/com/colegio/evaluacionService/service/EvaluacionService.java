package com.colegio.evaluacionService.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.colegio.evaluacionService.model.Evaluacion;
import com.colegio.evaluacionService.repository.EvaluacionRepository;

@Service
public class EvaluacionService {

    @Autowired
    private EvaluacionRepository repository;

    public Evaluacion guardar(String nombre, String materia, Double nota) {
        Evaluacion evaluacion = new Evaluacion();
        evaluacion.setNombre(nombre);
        evaluacion.setMateria(materia);
        evaluacion.setNota(nota);
        return repository.save(evaluacion);
    }

    public List<Evaluacion> listar() {
        return repository.findAll();
    }
}