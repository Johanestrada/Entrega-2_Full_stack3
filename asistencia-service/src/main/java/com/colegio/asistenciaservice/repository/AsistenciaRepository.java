package com.colegio.asistenciaservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.asistenciaservice.model.Asistencia;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
	List<Asistencia> findByEstudianteId(Long estudianteId);
}