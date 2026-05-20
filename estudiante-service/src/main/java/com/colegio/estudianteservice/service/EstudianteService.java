package com.colegio.estudianteservice.service;

import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    public List<Estudiante> getAll() {
        return estudianteRepository.findAll();
    }

    public Estudiante getEstudianteById(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public Estudiante save(Estudiante estudiante) {
        return estudianteRepository.save(estudiante);
    }

    public List<Estudiante> getEstudiantesPorCurso(String curso) {
        return estudianteRepository.findByCurso(curso);
    }

    public Estudiante getEstudianteByRun(String run) {
        return estudianteRepository.findByRun(run);
    }

    public void delete(Long id) {
        estudianteRepository.deleteById(id);
    }
}