package com.colegio.estudianteservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.colegio.estudianteservice.factory.EstudianteFactory;
import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.repository.EstudianteRepository;

@Service
public class EstudianteService {

    @Autowired
    private EstudianteRepository repository;

    public Estudiante guardar(
            String nombre,
            String curso
    ) {

        Estudiante estudiante =
                EstudianteFactory.crear(
                        nombre,
                        curso
                );

        return repository.save(estudiante);
    }

    public List<Estudiante> listar() {
        return repository.findAll();
    }

    public Estudiante obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }
}