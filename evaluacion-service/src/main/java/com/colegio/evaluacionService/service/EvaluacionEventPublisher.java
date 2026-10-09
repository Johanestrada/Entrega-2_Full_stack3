package com.colegio.evaluacionService.service;

import com.colegio.evaluacionService.event.EvaluacionCalificadaEvent;
import com.colegio.evaluacionService.model.Evaluacion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EvaluacionEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EvaluacionEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public EvaluacionEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.eventos:eventos.exchange}") String exchange,
            @Value("${rabbitmq.routing.evaluacion.calificada:evento.evaluacion.calificada}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void publicarEvaluacionCalificada(Evaluacion evaluacion) {
        EvaluacionCalificadaEvent evento = new EvaluacionCalificadaEvent(
                evaluacion.getId(),
                evaluacion.getEstudianteId(),
                evaluacion.getNota(),
                evaluacion.getMateria());

        rabbitTemplate.convertAndSend(exchange, routingKey, evento, message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message;
        });
        log.info("Evento evaluacion.calificada publicado en exchange={} routingKey={} evaluacionId={}",
                exchange, routingKey, evaluacion.getId());
    }
}
