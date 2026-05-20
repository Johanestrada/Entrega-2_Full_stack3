package com.colegio.estudianteservice.repository;

import com.colegio.estudianteservice.model.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {

    List<Estudiante> findByCurso(String curso);

    Estudiante findByRun(String run);
}