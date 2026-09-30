package com.colegio.rabbit_admin.consumer;

import com.colegio.rabbit_admin.dto.EventoEvaluacion;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EvaluacionEventConsumer {

    /**
     * Política de manejo de errores:
     * - Exito: ACK manual y se confirma el mensaje.
     * - Error no recuperable (IllegalArgumentException, JSON inválido/transformación fallida): NACK sin requeue hacia la DLQ.
     * - Error recuperable (fallo temporal): una sola reintento con requeue=true; si ya fue redelivered, se envía a DLQ.
     */
    @RabbitListener(queues = "${rabbitmq.queue.evaluacion}")
    public void consumirEventoEvaluacion(
            EventoEvaluacion evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag,
            @Header(AmqpHeaders.REDELIVERED) boolean redelivered) {

        try {
            log.info("📨 Evento Evaluación recibido: {} - ID: {}", evento.getTipo(), evento.getId());
            log.debug("Detalles: EstudianteID={}, Calificación={}, Materia={}", evento.getEstudianteId(), evento.getCalificacion(), evento.getMateria());

            procesarEventoEvaluacion(evento);

            channel.basicAck(tag, false);
            log.info("✅ Mensaje confirmado: {}", evento.getId());

        } catch (IllegalArgumentException | MessageConversionException e) {
            log.error("❌ Error no recuperable procesando evento de Evaluación: {}. Se envía a DLQ.", evento.getId(), e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ el evento de Evaluación: {}", evento.getId(), nackError);
            }
        } catch (Exception e) {
            if (!redelivered) {
                log.warn("⚠️ Fallo temporal en evento de Evaluación: {}. Se reintenta una sola vez.", evento.getId(), e);
                try {
                    channel.basicNack(tag, false, true);
                } catch (Exception nackError) {
                    log.error("Error en reintento del evento de Evaluación: {}", evento.getId(), nackError);
                }
                return;
            }

            log.error("❌ Evento de Evaluación ya reintentado y sigue fallando. Se envía a DLQ: {}", evento.getId(), e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ tras reintento fallido: {}", evento.getId(), nackError);
            }
        }
    }

    private void procesarEventoEvaluacion(EventoEvaluacion evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento de evaluación no puede ser nulo");
        }
        if (evento.getId() == null || evento.getId().isBlank()) {
            throw new IllegalArgumentException("El id del evento de evaluación es obligatorio");
        }

        log.info("Procesando evento de evaluación para estudianteID: {}", evento.getEstudianteId());
    }
}
