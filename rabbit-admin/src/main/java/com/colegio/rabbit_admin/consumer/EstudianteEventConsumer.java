package com.colegio.rabbit_admin.consumer;

import com.colegio.rabbit_admin.dto.EventoEstudiante;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EstudianteEventConsumer {

    /**
     * Política de manejo de errores:
     * - Exito: ACK manual y se confirma el mensaje.
     * - Error no recuperable (IllegalArgumentException, JSON inválido/transformación fallida): NACK sin requeue hacia la DLQ.
     * - Error recuperable (fallo temporal): una sola reintento con requeue=true; si ya fue redelivered, se envía a DLQ.
     */
    @RabbitListener(queues = "${rabbitmq.queue.estudiante}")
    public void consumirEventoEstudiante(
            EventoEstudiante evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag,
            @Header(AmqpHeaders.REDELIVERED) boolean redelivered) {

        try {
            log.info("📨 Evento Estudiante recibido: {} - ID: {}", evento.getTipo(), evento.getId());
            log.debug("Detalles: Nombre={}, Email={}, Acción={}", evento.getNombre(), evento.getEmail(), evento.getAccion());

            procesarEventoEstudiante(evento);

            channel.basicAck(tag, false);
            log.info("✅ Mensaje confirmado: {}", evento.getId());

        } catch (IllegalArgumentException | MessageConversionException e) {
            log.error("❌ Error no recuperable procesando evento de Estudiante: {}. Se envía a DLQ.", evento.getId(), e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ el evento de Estudiante: {}", evento.getId(), nackError);
            }
        } catch (Exception e) {
            if (!redelivered) {
                log.warn("⚠️ Fallo temporal en evento de Estudiante: {}. Se reintenta una sola vez.", evento.getId(), e);
                try {
                    channel.basicNack(tag, false, true);
                } catch (Exception nackError) {
                    log.error("Error en reintento del evento de Estudiante: {}", evento.getId(), nackError);
                }
                return;
            }

            log.error("❌ Evento de Estudiante ya reintentado y sigue fallando. Se envía a DLQ: {}", evento.getId(), e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ tras reintento fallido: {}", evento.getId(), nackError);
            }
        }
    }

    private void procesarEventoEstudiante(EventoEstudiante evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento de estudiante no puede ser nulo");
        }
        if (evento.getId() == null || evento.getId().isBlank()) {
            throw new IllegalArgumentException("El id del evento de estudiante es obligatorio");
        }

        log.info("Procesando evento de estudiante: {}", evento.getAccion());
    }
}
