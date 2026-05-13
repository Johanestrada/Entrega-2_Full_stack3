package com.colegio.asistenciaservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.repository.AsistenciaRepository;

@Service
public class AsistenciaService {

    private final AsistenciaRepository repository;

    public AsistenciaService(AsistenciaRepository repository) {
        this.repository = repository;
    }

    public List<Asistencia> listar() {
        return repository.findAll();
    }

    public Asistencia guardar(Asistencia asistencia) {
        return repository.save(asistencia);
    }

    public List<Asistencia> listarPorEstudiante(String estudiante) {
        return repository.findByEstudiante(estudiante);
    }
}