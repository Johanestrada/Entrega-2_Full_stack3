package com.colegio.rabbit_admin.consumer;

import com.colegio.rabbit_admin.config.RabbitMQConfig;
import com.colegio.rabbit_admin.dto.EventoEvaluacion;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

@Slf4j
@Service
public class EvaluacionEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.EVALUACION_QUEUE)
    public void consumirEventoEvaluacion(
            EventoEvaluacion evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        try {
            log.info("📨 Evento Evaluación recibido: {} - ID: {}", evento.getTipo(), evento.getId());
            log.debug("Detalles: EstudianteID={}, Calificación={}, Materia={}", evento.getEstudianteId(), evento.getCalificacion(), evento.getMateria());

            procesarEventoEvaluacion(evento);

            channel.basicAck(deliveryTag, false);
            log.info("✅ Mensaje confirmado: {}", evento.getId());

        } catch (Exception e) {
            log.error("❌ Error procesando evento de Evaluación: {}", evento.getId(), e);
            try {
                channel.basicNack(deliveryTag, false, false);
                log.info("⚠️ Mensaje enviado a DLQ: {}", evento.getId());
            } catch (Exception nackError) {
                log.error("Error en NACK", nackError);
            }
        }
    }

    private void procesarEventoEvaluacion(EventoEvaluacion evento) {
        log.info("Procesando evento de evaluación para estudianteID: {}", evento.getEstudianteId());
    }
}
