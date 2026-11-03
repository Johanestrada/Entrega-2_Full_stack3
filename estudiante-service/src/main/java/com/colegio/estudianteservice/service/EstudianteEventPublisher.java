package com.colegio.estudianteservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.colegio.estudianteservice.event.EstudianteCreadoEvent;
import com.colegio.estudianteservice.model.Estudiante;

@Service
public class EstudianteEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(EstudianteEventPublisher.class);

    private final RabbitTemplate rabbitTemplate; // Inyección de RabbitTemplate para enviar mensajes a RabbitMQ

    @Value("${rabbitmq.exchange.eventos:eventos.exchange}") // Nombre del exchange de eventos, con valor por defecto.
    private String exchange;

    @Value("${rabbitmq.routing.estudiante.creado:evento.estudiante.creado}") // Routing key para el evento de estudiante creado, con valor por defecto.
    private String routingKey;

    public EstudianteEventPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarEstudianteCreado(Estudiante estudiante) {
        if (estudiante == null) {
            log.warn("No se puede publicar estudiante.creado porque el estudiante recibido es nulo.");
            return;
        }

        EstudianteCreadoEvent evento = new EstudianteCreadoEvent(
                estudiante.getId(),
                estudiante.getNombre(),
                estudiante.getCurso()
        );

        log.info("Publicando evento estudiante.creado para id={}, nombre={}, curso={}",
                evento.getId(), evento.getNombre(), evento.getCurso());

        rabbitTemplate.convertAndSend(exchange, routingKey, evento, message -> {
            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
            return message; // routingKey es "evento.estudiante.creado" y exchange es "eventos.exchange"
        });

        log.info("Evento estudiante.creado publicado correctamente en exchange={} con routingKey={}",
                exchange, routingKey);
    }
}
