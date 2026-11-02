package com.colegio.estudianteservice.consumer;

import com.colegio.estudianteservice.event.NotaEvent;
import com.colegio.estudianteservice.service.CalificacionesConsumerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class CalificacionesKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(CalificacionesKafkaListener.class);

    private final CalificacionesConsumerService consumerService;

    public CalificacionesKafkaListener(CalificacionesConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    /**
     * Política de errores:
     * - payloads inválidos o con campos nulos se consideran no recuperables y se entregan al DLT.
     * - fallos temporales (por ejemplo, dependencia externa o transitorio) se reintentan con backoff exponencial.
     * - la validación de Jakarta se ejecuta antes de procesar el evento para evitar mensajes corruptos en la capa de negocio.
     */
    @KafkaListener(
            topics = "${kafka.topic.calificaciones.name}",
            groupId = "${kafka.consumer.notifications.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escuchar(@Payload @Valid NotaEvent evento, Acknowledgment acknowledgment) {
        if (evento == null || evento.getEstudianteId() == null || evento.getMateria() == null || evento.getNota() == null) {
            throw new IllegalArgumentException("Payload de calificación inválido: estudianteId, materia o nota son obligatorios");
        }

        try {
            log.info("🧾 Recibido evento de calificación desde Kafka: eventId={}, estudianteId={}",
                    evento.getEventId(), evento.getEstudianteId());
            consumerService.procesarEvento(evento);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Error al procesar nota Kafka eventId={}", evento.getEventId(), ex);
            throw ex;
        }
    }
}
