package com.colegio.rabbit_admin.service;

import com.colegio.rabbit_admin.config.RabbitMQConfig;
import com.colegio.rabbit_admin.dto.EventoAsistencia;
import com.colegio.rabbit_admin.dto.EventoEstudiante;
import com.colegio.rabbit_admin.dto.EventoEvaluacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventPublisherService {

    private final RabbitTemplate rabbitTemplate;

    public EventPublisherService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarEventoEstudiante(EventoEstudiante evento) {
        try {
            String routingKey = "evento.estudiante." + evento.getAccion();
            log.info("📤 Publicando evento Estudiante: {} con routing key: {}", evento.getId(), routingKey);
            
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EVENTS_EXCHANGE,
                    routingKey,
                    evento
            );
            
            log.info("✅ Evento Estudiante enviado exitosamente");
        } catch (Exception e) {
            log.error("❌ Error publicando evento Estudiante", e);
            throw new RuntimeException("Error al publicar evento de Estudiante", e);
        }
    }

    public void publicarEventoAsistencia(EventoAsistencia evento) {
        try {
            String routingKey = "evento.asistencia.registrada";
            log.info("📤 Publicando evento Asistencia: {} con routing key: {}", evento.getId(), routingKey);
            
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EVENTS_EXCHANGE,
                    routingKey,
                    evento
            );
            
            log.info("✅ Evento Asistencia enviado exitosamente");
        } catch (Exception e) {
            log.error("❌ Error publicando evento Asistencia", e);
            throw new RuntimeException("Error al publicar evento de Asistencia", e);
        }
    }

    public void publicarEventoEvaluacion(EventoEvaluacion evento) {
        try {
            String routingKey = "evento.evaluacion.calificada";
            log.info("📤 Publicando evento Evaluación: {} con routing key: {}", evento.getId(), routingKey);
            
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EVENTS_EXCHANGE,
                    routingKey,
                    evento
            );
            
            log.info("✅ Evento Evaluación enviado exitosamente");
        } catch (Exception e) {
            log.error("❌ Error publicando evento Evaluación", e);
            throw new RuntimeException("Error al publicar evento de Evaluación", e);
        }
    }
}
