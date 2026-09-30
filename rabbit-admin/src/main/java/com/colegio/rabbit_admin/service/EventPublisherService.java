package com.colegio.rabbit_admin.service;

import com.colegio.rabbit_admin.dto.EventoAsistencia;
import com.colegio.rabbit_admin.dto.EventoEstudiante;
import com.colegio.rabbit_admin.dto.EventoEvaluacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.eventos}")
    private String eventosExchange;

    @Value("${rabbitmq.routing.estudiante.creado}")
    private String estudianteCreadoRoutingKey;

    @Value("${rabbitmq.routing.asistencia.registrada}")
    private String asistenciaRegistradaRoutingKey;

    @Value("${rabbitmq.routing.evaluacion.calificada}")
    private String evaluacionCalificadaRoutingKey;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarEventoEstudiante(EventoEstudiante evento) {
        try {
            String routingKey = estudianteCreadoRoutingKey;
            log.info("📤 Publicando evento Estudiante: {} con routing key: {}", evento.getId(), routingKey);
            
            rabbitTemplate.convertAndSend(eventosExchange, routingKey, evento);
            
            log.info("✅ Evento Estudiante enviado exitosamente");
        } catch (Exception e) {
            log.error("❌ Error publicando evento Estudiante", e);
            throw new RuntimeException("Error al publicar evento de Estudiante", e);
        }
    }

    public void publicarEventoAsistencia(EventoAsistencia evento) {
        try {
            String routingKey = asistenciaRegistradaRoutingKey;
            log.info("📤 Publicando evento Asistencia: {} con routing key: {}", evento.getId(), routingKey);
            
            rabbitTemplate.convertAndSend(eventosExchange, routingKey, evento);
            
            log.info("✅ Evento Asistencia enviado exitosamente");
        } catch (Exception e) {
            log.error("❌ Error publicando evento Asistencia", e);
            throw new RuntimeException("Error al publicar evento de Asistencia", e);
        }
    }

    public void publicarEventoEvaluacion(EventoEvaluacion evento) {
        try {
            String routingKey = evaluacionCalificadaRoutingKey;
            log.info("📤 Publicando evento Evaluación: {} con routing key: {}", evento.getId(), routingKey);
            
            rabbitTemplate.convertAndSend(eventosExchange, routingKey, evento);
            
            log.info("✅ Evento Evaluación enviado exitosamente");
        } catch (Exception e) {
            log.error("❌ Error publicando evento Evaluación", e);
            throw new RuntimeException("Error al publicar evento de Evaluación", e);
        }
    }
}
