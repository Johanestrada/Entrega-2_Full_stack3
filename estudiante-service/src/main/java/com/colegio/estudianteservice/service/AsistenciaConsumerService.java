package com.colegio.estudianteservice.service;

import com.colegio.estudianteservice.event.AsistenciaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaConsumerService.class);

    public void procesarEvento(AsistenciaEvent evento) {
        if (evento == null) {
            log.warn("Se recibió una asistencia Kafka nula y se ignora.");
            return;
        }

        if (evento.getEstudianteId() == null || evento.getEstado() == null || evento.getFecha() == null) {
            log.warn("Evento de asistencia inválido descartado: eventId={}, tipo={}",
                    evento.getEventId(), evento.getTipo());
            return;
        }

        log.info("📩 Kafka [asistencia] recibido para estudianteId={}, fecha={}, estado={}",
                evento.getEstudianteId(), evento.getFecha(), evento.getEstado());
    }
}
