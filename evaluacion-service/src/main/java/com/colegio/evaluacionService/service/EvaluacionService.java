package com.colegio.evaluacionService.service;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.colegio.evaluacionService.event.NotaEvent;
import com.colegio.evaluacionService.model.Evaluacion;
import com.colegio.evaluacionService.repository.EvaluacionRepository;
import org.springframework.kafka.KafkaException;

@Service
public class EvaluacionService {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionService.class);

    @Autowired
    private EvaluacionRepository repository;

    private final EvaluacionEventPublisher eventPublisher;
    private final NotaKafkaProducer notaKafkaProducer;

    public EvaluacionService(EvaluacionEventPublisher eventPublisher, NotaKafkaProducer notaKafkaProducer) {
        this.eventPublisher = eventPublisher;
        this.notaKafkaProducer = notaKafkaProducer;
    }

    public Evaluacion guardar(String nombre, String materia, Double nota) {
        Evaluacion evaluacion = new Evaluacion();
        // expect 'nombre' to be estudianteId as string or pass estudianteId directly from client
        try {
            evaluacion.setEstudianteId(nombre != null ? Long.valueOf(nombre) : null);
        } catch (NumberFormatException ex) {
            evaluacion.setEstudianteId(null);
        }
        evaluacion.setMateria(materia);
        evaluacion.setNota(nota);
        Evaluacion guardada = repository.save(evaluacion);
        try {
            eventPublisher.publicarEvaluacionCalificada(guardada);
            if (guardada.getEstudianteId() != null) {
                NotaEvent evento = new NotaEvent(
                        guardada.getId(),
                        guardada.getEstudianteId(),
                        guardada.getMateria(),
                        guardada.getNota());
                notaKafkaProducer.publicarNota(evento);
            }
        } catch (AmqpException | KafkaException e) {
            log.error("La evaluación id={} quedó guardada, pero no se pudo publicar el evento Kafka/Rabbit.",
                    guardada.getId(), e);
        }
        return guardada;
    }

    public List<Evaluacion> listar() {
        return repository.findAll();
    }

    public Evaluacion obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Evaluacion actualizar(Long id, Double nota) {
        return repository.findById(id)
                .map(actual -> {
                    actual.setNota(nota);
                    return repository.save(actual);
                }).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public List<Evaluacion> listarPorNombre(String nombre) {
        try {
            Long id = Long.valueOf(nombre);
            return repository.findByEstudianteId(id);
        } catch (NumberFormatException ex) {
            return List.of();
        }
    }

    public List<Evaluacion> obtenerPorEstudiante(Long estudianteId) {
        return repository.findByEstudianteId(estudianteId);
    }
}