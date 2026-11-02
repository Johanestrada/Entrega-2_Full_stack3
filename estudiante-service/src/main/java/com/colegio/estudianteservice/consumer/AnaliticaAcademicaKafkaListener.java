package com.colegio.estudianteservice.consumer;

import com.colegio.estudianteservice.event.AsistenciaEvent;
import com.colegio.estudianteservice.event.NotaEvent;
import com.colegio.estudianteservice.service.AnaliticaAcademicaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AnaliticaAcademicaKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(AnaliticaAcademicaKafkaListener.class);

    private final AnaliticaAcademicaService analiticaAcademicaService;

    public AnaliticaAcademicaKafkaListener(AnaliticaAcademicaService analiticaAcademicaService) {
        this.analiticaAcademicaService = analiticaAcademicaService;
    }

    /**
     * Política de errores:
     * - la analítica academica reintenta sólo fallos transitorios; payloads inválidos se descartan de inmediato al DLT.
     * - el objetivo es mantener la métrica consistente sin bloquear el consumo del tópico principal.
     */
    @KafkaListener(
            topics = "${kafka.topic.calificaciones.name}",
            groupId = "${kafka.consumer.analytics.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escucharCalificaciones(@Payload @Valid NotaEvent evento, Acknowledgment acknowledgment) {
        if (evento == null || evento.getEstudianteId() == null || evento.getNota() == null) {
            throw new IllegalArgumentException("Payload de analítica de calificaciones inválido");
        }

        try {
            log.info("📊 analitica-academica recibió nota para estudianteId={}", evento.getEstudianteId());
            analiticaAcademicaService.registrarNota(evento);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Error en analítica de calificaciones para eventId={}", evento.getEventId(), ex);
            throw ex;
        }
    }

    @KafkaListener(
            topics = "${kafka.topic.asistencia.name}",
            groupId = "${kafka.consumer.analytics.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escucharAsistencia(@Payload @Valid AsistenciaEvent evento, Acknowledgment acknowledgment) {
        if (evento == null || evento.getEstudianteId() == null || evento.getEstado() == null) {
            throw new IllegalArgumentException("Payload de analítica de asistencia inválido");
        }

        try {
            log.info("📊 analitica-academica recibió asistencia para estudianteId={}", evento.getEstudianteId());
            analiticaAcademicaService.registrarAsistencia(evento);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Error en analítica de asistencia para eventId={}", evento.getEventId(), ex);
            throw ex;
        }
    }
}
