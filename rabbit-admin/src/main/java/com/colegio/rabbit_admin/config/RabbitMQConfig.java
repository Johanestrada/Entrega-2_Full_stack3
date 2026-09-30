package com.colegio.rabbit_admin.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.eventos}")
    private String eventosExchange;

    @Value("${rabbitmq.exchange.dlx}")
    private String dlxExchange;

    @Value("${rabbitmq.queue.estudiante}")
    private String estudianteQueue;

    @Value("${rabbitmq.queue.asistencia}")
    private String asistenciaQueue;

    @Value("${rabbitmq.queue.evaluacion}")
    private String evaluacionQueue;

    @Value("${rabbitmq.queue.dlq.estudiante}")
    private String estudianteDlq;

    @Value("${rabbitmq.queue.dlq.asistencia}")
    private String asistenciaDlq;

    @Value("${rabbitmq.queue.dlq.evaluacion}")
    private String evaluacionDlq;

    @Value("${rabbitmq.routing.estudiante}")
    private String estudianteRoutingKey;

    @Value("${rabbitmq.routing.asistencia}")
    private String asistenciaRoutingKey;

    @Value("${rabbitmq.routing.evaluacion}")
    private String evaluacionRoutingKey;

    // === EXCHANGES ===
    @Bean
    public TopicExchange eventosExchange() {
        return new TopicExchange(eventosExchange, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchange, true, false);
    }

    // === MAIN QUEUES ===
    @Bean
    public Queue estudianteQueue() {
        return QueueBuilder.durable(estudianteQueue)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", estudianteRoutingKey)
                .build();
    }

    @Bean
    public Queue asistenciaQueue() {
        return QueueBuilder.durable(asistenciaQueue)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", asistenciaRoutingKey)
                .build();
    }

    @Bean
    public Queue evaluacionQueue() {
        return QueueBuilder.durable(evaluacionQueue)
                .withArgument("x-dead-letter-exchange", dlxExchange)
                .withArgument("x-dead-letter-routing-key", evaluacionRoutingKey)
                .build();
    }

    // === DEAD LETTER QUEUES ===
    @Bean
    public Queue estudianteDLQ() {
        return QueueBuilder.durable(estudianteDlq).build();
    }

    @Bean
    public Queue asistenciaDLQ() {
        return QueueBuilder.durable(asistenciaDlq).build();
    }

    @Bean
    public Queue evaluacionDLQ() {
        return QueueBuilder.durable(evaluacionDlq).build();
    }

    // === BINDINGS ===
    @Bean
    public Binding estudianteBinding(Queue estudianteQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(estudianteQueue)
                .to(eventosExchange)
                .with(estudianteRoutingKey);
    }

    @Bean
    public Binding asistenciaBinding(Queue asistenciaQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(asistenciaQueue)
                .to(eventosExchange)
                .with(asistenciaRoutingKey);
    }

    @Bean
    public Binding evaluacionBinding(Queue evaluacionQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(evaluacionQueue)
                .to(eventosExchange)
                .with(evaluacionRoutingKey);
    }

    // === DLQ BINDINGS ===
    @Bean
    public Binding estudianteDLQBinding(Queue estudianteDLQ, DirectExchange dlxExchange) {
        return BindingBuilder.bind(estudianteDLQ)
                .to(dlxExchange)
                .with(estudianteRoutingKey);
    }

    @Bean
    public Binding asistenciaDLQBinding(Queue asistenciaDLQ, DirectExchange dlxExchange) {
        return BindingBuilder.bind(asistenciaDLQ)
                .to(dlxExchange)
                .with(asistenciaRoutingKey);
    }

    @Bean
    public Binding evaluacionDLQBinding(Queue evaluacionDLQ, DirectExchange dlxExchange) {
        return BindingBuilder.bind(evaluacionDLQ)
                .to(dlxExchange)
                .with(evaluacionRoutingKey);
    }
}
