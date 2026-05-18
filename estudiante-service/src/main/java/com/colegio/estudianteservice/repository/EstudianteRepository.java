package com.colegio.estudianteservice.repository;

import com.colegio.estudianteservice.model.Estudiante;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {
    Estudiante findByRun(String run);
    List<Estudiante> findByCurso(String curso);
}
