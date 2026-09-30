package com.colegio.rabbit_admin.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange Names
    public static final String EVENTS_EXCHANGE = "eventos.exchange";
    public static final String DLX_EXCHANGE = "eventos.dlx";

    // Queue Names
    public static final String ESTUDIANTE_QUEUE = "eventos.estudiante";
    public static final String ASISTENCIA_QUEUE = "eventos.asistencia";
    public static final String EVALUACION_QUEUE = "eventos.evaluacion";

    // DLQ Names
    public static final String ESTUDIANTE_DLQ = "eventos.estudiante.dlq";
    public static final String ASISTENCIA_DLQ = "eventos.asistencia.dlq";
    public static final String EVALUACION_DLQ = "eventos.evaluacion.dlq";

    // Routing Keys
    public static final String ESTUDIANTE_ROUTING_KEY = "evento.estudiante.*";
    public static final String ASISTENCIA_ROUTING_KEY = "evento.asistencia.*";
    public static final String EVALUACION_ROUTING_KEY = "evento.evaluacion.*";

    // === EXCHANGES ===
    @Bean
    public TopicExchange eventosExchange() {
        return new TopicExchange(EVENTS_EXCHANGE, true, false);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(DLX_EXCHANGE, true, false);
    }

    // === MAIN QUEUES ===
    @Bean
    public Queue estudianteQueue() {
        return QueueBuilder.durable(ESTUDIANTE_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ESTUDIANTE_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue asistenciaQueue() {
        return QueueBuilder.durable(ASISTENCIA_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", ASISTENCIA_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue evaluacionQueue() {
        return QueueBuilder.durable(EVALUACION_QUEUE)
                .withArgument("x-dead-letter-exchange", DLX_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", EVALUACION_ROUTING_KEY)
                .build();
    }

    // === DEAD LETTER QUEUES ===
    @Bean
    public Queue estudianteDLQ() {
        return QueueBuilder.durable(ESTUDIANTE_DLQ).build();
    }

    @Bean
    public Queue asistenciaDLQ() {
        return QueueBuilder.durable(ASISTENCIA_DLQ).build();
    }

    @Bean
    public Queue evaluacionDLQ() {
        return QueueBuilder.durable(EVALUACION_DLQ).build();
    }

    // === BINDINGS ===
    @Bean
    public Binding estudianteBinding(Queue estudianteQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(estudianteQueue)
                .to(eventosExchange)
                .with(ESTUDIANTE_ROUTING_KEY);
    }

    @Bean
    public Binding asistenciaBinding(Queue asistenciaQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(asistenciaQueue)
                .to(eventosExchange)
                .with(ASISTENCIA_ROUTING_KEY);
    }

    @Bean
    public Binding evaluacionBinding(Queue evaluacionQueue, TopicExchange eventosExchange) {
        return BindingBuilder.bind(evaluacionQueue)
                .to(eventosExchange)
                .with(EVALUACION_ROUTING_KEY);
    }

    // === DLQ BINDINGS ===
    @Bean
    public Binding estudianteDLQBinding(Queue estudianteDLQ, DirectExchange dlxExchange) {
        return BindingBuilder.bind(estudianteDLQ)
                .to(dlxExchange)
                .with(ESTUDIANTE_ROUTING_KEY);
    }

    @Bean
    public Binding asistenciaDLQBinding(Queue asistenciaDLQ, DirectExchange dlxExchange) {
        return BindingBuilder.bind(asistenciaDLQ)
                .to(dlxExchange)
                .with(ASISTENCIA_ROUTING_KEY);
    }

    @Bean
    public Binding evaluacionDLQBinding(Queue evaluacionDLQ, DirectExchange dlxExchange) {
        return BindingBuilder.bind(evaluacionDLQ)
                .to(dlxExchange)
                .with(EVALUACION_ROUTING_KEY);
    }
}
