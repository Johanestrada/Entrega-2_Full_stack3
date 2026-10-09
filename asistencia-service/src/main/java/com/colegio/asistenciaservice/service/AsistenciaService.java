package com.colegio.asistenciaservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Service;

import com.colegio.asistenciaservice.model.Asistencia;
import com.colegio.asistenciaservice.repository.AsistenciaRepository;

@Service
public class AsistenciaService {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaService.class);

    private final AsistenciaRepository repository;
    private final AsistenciaEventPublisher eventPublisher;

    public AsistenciaService(AsistenciaRepository repository, AsistenciaEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    public List<Asistencia> listar() {
        return repository.findAll();
    }

    public Asistencia guardar(Asistencia asistencia) {
        Asistencia guardada = repository.save(asistencia);
        try {
            eventPublisher.publicarAsistenciaRegistrada(guardada);
        } catch (AmqpException e) {
            log.error("La asistencia id={} quedó guardada, pero no se pudo publicar asistencia.registrada.",
                    guardada.getId(), e);
        }
        return guardada;
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