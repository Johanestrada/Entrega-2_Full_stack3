package com.colegio.asistenciaservice.service;

import com.colegio.asistenciaservice.event.AsistenciaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaKafkaProducer.class);

    private final KafkaTemplate<String, AsistenciaEvent> kafkaTemplate;
    private final String topic;

    public AsistenciaKafkaProducer(
            KafkaTemplate<String, AsistenciaEvent> kafkaTemplate,
            @Value("${kafka.topic.asistencia.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publicarAsistencia(AsistenciaEvent evento) {
        if (evento == null || evento.getEstudianteId() == null) {
            log.warn("No se publica evento de asistencia porque el payload o el estudiante son nulos.");
            return;
        }

        String key = String.valueOf(evento.getEstudianteId());

        kafkaTemplate.send(topic, key, evento).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Fallo al publicar asistencia para estudianteId={} en topic={} eventId={}",
                        evento.getEstudianteId(), topic, evento.getEventId(), ex);
                return;
            }

            SendResult<String, AsistenciaEvent> sendResult = result;
            log.info("Evento de asistencia enviado correctamente a topic={} partition={} offset={} eventId={}",
                    sendResult.getRecordMetadata().topic(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().offset(),
                    evento.getEventId());
        });
    }
}
