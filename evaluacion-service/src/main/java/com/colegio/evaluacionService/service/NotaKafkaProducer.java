package com.colegio.evaluacionService.service;

import com.colegio.evaluacionService.event.NotaEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class NotaKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(NotaKafkaProducer.class);

    private final KafkaTemplate<String, NotaEvent> kafkaTemplate;
    private final String topic;

    public NotaKafkaProducer(
            KafkaTemplate<String, NotaEvent> kafkaTemplate,
            @Value("${kafka.topic.calificaciones.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publicarNota(NotaEvent evento) {
        if (evento == null || evento.getEstudianteId() == null) {
            log.warn("No se publica evento de calificación porque el payload o el estudiante son nulos.");
            return;
        }

        String key = String.valueOf(evento.getEstudianteId());

        kafkaTemplate.send(topic, key, evento).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Fallo al publicar nota para estudianteId={} en topic={} eventId={}",
                        evento.getEstudianteId(), topic, evento.getEventId(), ex);
                return;
            }

            SendResult<String, NotaEvent> sendResult = result;
            log.info("Evento de calificación enviado correctamente a topic={} partition={} offset={} eventId={}",
                    sendResult.getRecordMetadata().topic(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().offset(),
                    evento.getEventId());
        });
    }
}
