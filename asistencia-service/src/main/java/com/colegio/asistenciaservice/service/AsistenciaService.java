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

    public Asistencia obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Asistencia actualizar(Long id, Asistencia asistencia) {
        return repository.findById(id)
                .map(actual -> {
                    actual.setEstudianteId(asistencia.getEstudianteId());
                    actual.setFecha(asistencia.getFecha());
                    actual.setPresente(asistencia.isPresente());
                    return repository.save(actual);
                })
                .orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Asistencia> listarPorEstudiante(String estudiante) {
        return repository.findByEstudianteId(Long.valueOf(estudiante));
    }
}