package com.colegio.estudianteservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.colegio.estudianteservice.model.Estudiante;
import com.colegio.estudianteservice.repository.EstudianteRepository;

@Service
public class EstudianteService {

    private static final Logger log = LoggerFactory.getLogger(EstudianteService.class);

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired(required = false)
    private EstudianteEventPublisher estudianteEventPublisher;

    public List<Estudiante> getAll() {
        return estudianteRepository.findAll();
    }

    public Estudiante getEstudianteById(Long id) {
        return estudianteRepository.findById(id).orElse(null);
    }

    public Estudiante save(Estudiante estudiante) {
        Estudiante estudianteGuardado = estudianteRepository.save(estudiante);
        Estudiante estudiantePersistido = estudianteRepository.findById(estudianteGuardado.getId())
                .orElse(estudianteGuardado);

        try {
            // La operación principal del negocio es guardar el estudiante en MySQL.
            // La publicación a RabbitMQ es un efecto secundario del dominio y no debe
            // bloquear la respuesta HTTP ni invalidar la creación correcta del registro.
            if (estudianteEventPublisher != null) {
                estudianteEventPublisher.publicarEstudianteCreado(estudiantePersistido);
            }
        } catch (Exception ex) {
            log.error(
                    "RabbitMQ falló al publicar estudiante.creado para id={} y nombre={}. " +
                            "El estudiante ya quedó persistido en MySQL y se devuelve la respuesta normal.",
                    estudiantePersistido.getId(),
                    estudiantePersistido.getNombre(),
                    ex
            );
        }

        return estudiantePersistido;
    }

    public List<Estudiante> getEstudiantesPorCurso(String curso) {
        return estudianteRepository.findByCurso(curso);
    }

    public Estudiante getEstudianteByRun(String run) {
        return estudianteRepository.findByRun(run);
    }

    public void delete(Long id) {
        estudianteRepository.deleteById(id);
    }
}