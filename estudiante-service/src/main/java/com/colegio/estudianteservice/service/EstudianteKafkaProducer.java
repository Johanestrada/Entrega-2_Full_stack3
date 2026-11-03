package com.colegio.estudianteservice.service;

import com.colegio.estudianteservice.event.EstudianteCreadoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class EstudianteKafkaProducer {

    private static final Logger log = LoggerFactory.getLogger(EstudianteKafkaProducer.class);

    private final KafkaTemplate<String, EstudianteCreadoEvent> kafkaTemplate;
    private final String topic;

    public EstudianteKafkaProducer(
            KafkaTemplate<String, EstudianteCreadoEvent> kafkaTemplate,
            @Value("${kafka.topic.estudiante.name}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publicarEstudianteCreado(EstudianteCreadoEvent evento) {
        if (evento == null || evento.getId() == null) {
            log.warn("No se publica evento de estudiante porque el payload o el id son nulos.");
            return;
        }

        String key = String.valueOf(evento.getId());

        kafkaTemplate.send(topic, key, evento).whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("Fallo al publicar estudiante id={} en topic={}", evento.getId(), topic, ex);
                return;
            }

            SendResult<String, EstudianteCreadoEvent> sendResult = result;
            log.info("Evento de estudiante enviado a topic={} partition={} offset={} id={}",
                    sendResult.getRecordMetadata().topic(),
                    sendResult.getRecordMetadata().partition(),
                    sendResult.getRecordMetadata().offset(),
                    evento.getId());
        });
    }
}
