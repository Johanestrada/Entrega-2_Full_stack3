package com.colegio.evaluacionService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.evaluacionService.model.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
}