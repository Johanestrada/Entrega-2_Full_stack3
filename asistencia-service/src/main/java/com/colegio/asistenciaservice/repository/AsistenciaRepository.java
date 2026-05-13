package com.colegio.asistenciaservice.repository;

import com.colegio.asistenciaservice.model.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {
}