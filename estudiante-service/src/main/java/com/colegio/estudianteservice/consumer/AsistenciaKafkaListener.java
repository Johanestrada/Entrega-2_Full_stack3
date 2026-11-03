package com.colegio.estudianteservice.consumer;

import com.colegio.estudianteservice.event.AsistenciaEvent;
import com.colegio.estudianteservice.service.AsistenciaConsumerService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class AsistenciaKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaKafkaListener.class);

    private final AsistenciaConsumerService consumerService;

    public AsistenciaKafkaListener(AsistenciaConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    /**
     * Política de errores:
     * - mensajes con datos faltantes o de estructura inválida no se vuelven a intentar y pasan al DLT.
     * - errores recuperables se gestionan con reintentos exponenciales antes de enviar al DLT.
     * - la validación previene que una carga corrupta llegue al procesamiento de negocio.
     */
    @KafkaListener(
            topics = "${kafka.topic.asistencia.name}",
            groupId = "${kafka.consumer.notifications.group-id}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escuchar(@Payload @Valid AsistenciaEvent evento, Acknowledgment acknowledgment) {
        if (evento == null || evento.getEstudianteId() == null || evento.getEstado() == null || evento.getFecha() == null) {
            throw new IllegalArgumentException("Payload de asistencia inválido: estudianteId, fecha o estado son obligatorios");
        }

        try {
            log.info("🧾 Recibido evento de asistencia desde Kafka: eventId={}, estudianteId={}",
                    evento.getEventId(), evento.getEstudianteId());
            consumerService.procesarEvento(evento);
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("Error al procesar asistencia Kafka eventId={}", evento.getEventId(), ex);
            throw ex;
        }
    }
}
