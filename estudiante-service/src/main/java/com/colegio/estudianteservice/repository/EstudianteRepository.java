package com.colegio.estudianteservice.repository;

import com.colegio.estudianteservice.model.Estudiante;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository
        extends JpaRepository<Estudiante, Long> {
}