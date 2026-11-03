package com.colegio.estudianteservice.service;

import com.colegio.estudianteservice.event.NotaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class CalificacionesConsumerService {

    private static final Logger log = LoggerFactory.getLogger(CalificacionesConsumerService.class);

    public void procesarEvento(NotaEvent evento) {
        if (evento == null) {
            log.warn("Se recibió una nota Kafka nula y se ignora.");
            return;
        }

        if (evento.getEstudianteId() == null || evento.getMateria() == null || evento.getNota() == null) {
            log.warn("Evento de calificación inválido descartado: eventId={}, tipo={}",
                    evento.getEventId(), evento.getTipo());
            return;
        }

        log.info("📩 Kafka [calificaciones] recibido para estudianteId={}, materia={}, nota={}",
                evento.getEstudianteId(), evento.getMateria(), evento.getNota());
    }
}
