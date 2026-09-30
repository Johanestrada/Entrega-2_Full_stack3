package com.colegio.rabbit_admin.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "${rabbitmq.queue.dlq.estudiante}")
    public void handleEstudianteDLQ(
            String message,
            @Header(value = "x-death", required = false) Object xDeath,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.error("🔴 [DLQ] Evento de Estudiante rechazado. Mensaje: {}. Motivo x-death: {}", message, extractReason(xDeath));
        acknowledge(channel, tag, "Estudiante");
    }

    @RabbitListener(queues = "${rabbitmq.queue.dlq.asistencia}")
    public void handleAsistenciaDLQ(
            String message,
            @Header(value = "x-death", required = false) Object xDeath,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.error("🔴 [DLQ] Evento de Asistencia rechazado. Mensaje: {}. Motivo x-death: {}", message, extractReason(xDeath));
        acknowledge(channel, tag, "Asistencia");
    }

    @RabbitListener(queues = "${rabbitmq.queue.dlq.evaluacion}")
    public void handleEvaluacionDLQ(
            String message,
            @Header(value = "x-death", required = false) Object xDeath,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        log.error("🔴 [DLQ] Evento de Evaluación rechazado. Mensaje: {}. Motivo x-death: {}", message, extractReason(xDeath));
        acknowledge(channel, tag, "Evaluación");
    }

    private void acknowledge(Channel channel, long tag, String tipo) {
        try {
            channel.basicAck(tag, false);
            log.info("✅ Mensaje DLQ de {} confirmado y archivado para revisión.", tipo);
        } catch (Exception e) {
            log.error("❌ No se pudo confirmar el mensaje DLQ de {}.", tipo, e);
        }
    }

    private String extractReason(Object xDeath) {
        if (xDeath == null) {
            return "Sin información x-death";
        }

        try {
            if (xDeath instanceof List<?> list && !list.isEmpty()) {
                Object first = list.get(0);
                if (first instanceof Map<?, ?> map) {
                    Object reason = map.get("reason");
                    Object count = map.get("count");
                    Object routingKeys = map.get("routing-keys");
                    return "reason=" + reason + ", count=" + count + ", routingKeys=" + routingKeys;
                }
            }
        } catch (Exception e) {
            log.debug("No se pudo parsear x-death para DLQ", e);
        }

        return String.valueOf(xDeath);
    }
}
