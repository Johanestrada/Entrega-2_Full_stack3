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

    public Evaluacion obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Evaluacion actualizar(Long id, Evaluacion evaluacion) {
        return repository.findById(id)
                .map(actual -> {
                    actual.setNombre(evaluacion.getNombre());
                    actual.setMateria(evaluacion.getMateria());
                    actual.setNota(evaluacion.getNota());
                    return repository.save(actual);
                })
                .orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Evaluacion> listarPorNombre(String nombre) {
        return repository.findByNombre(nombre);
    }
}