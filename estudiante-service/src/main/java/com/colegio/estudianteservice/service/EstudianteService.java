package com.colegio.estudianteservice.service;

import com.colegio.estudianteservice.factory.EstudianteFactory;
import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.repository.EstudianteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}