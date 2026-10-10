package com.colegio.asistenciaservice.service;

import com.colegio.asistenciaservice.event.AsistenciaRegistradaEvent;
import com.colegio.asistenciaservice.model.Asistencia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AsistenciaEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(AsistenciaEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public AsistenciaEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.eventos:eventos.exchange}") String exchange,
            @Value("${rabbitmq.routing.asistencia.registrada:evento.asistencia.registrada}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publicarAsistenciaRegistrada(Asistencia asistencia) {
        AsistenciaRegistradaEvent evento = new AsistenciaRegistradaEvent(
                asistencia.getId(),
                asistencia.getEstudianteId(),
                asistencia.getFecha(),
                asistencia.isPresente() ? "PRESENTE" : "AUSENTE");

        // Publica el evento en el exchange con la routing key configurada.
        rabbitTemplate.convertAndSend(exchange, routingKey, evento, message -> {
            // Marca el mensaje como persistente en RabbitMQ.
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
        log.info("Evento asistencia.registrada publicado en exchange={} routingKey={} asistenciaId={}",
                exchange, routingKey, asistencia.getId());
    }
}
