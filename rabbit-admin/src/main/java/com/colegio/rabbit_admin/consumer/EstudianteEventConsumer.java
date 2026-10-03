package com.colegio.rabbit_admin.consumer;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

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
            Map<String, Object> evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag,
            @Header(AmqpHeaders.REDELIVERED) boolean redelivered) {

        try {
            String eventoNombre = String.valueOf(evento.getOrDefault("evento", "desconocido"));
            Object id = evento.get("id");
            String nombre = String.valueOf(evento.getOrDefault("nombre", ""));
            String curso = String.valueOf(evento.getOrDefault("curso", ""));
            String timestamp = String.valueOf(evento.getOrDefault("timestamp", ""));

            log.info("EVENTO DE NEGOCIO RECIBIDO: Estudiante creado");
            log.info("ID: {}", id);
            log.info("Nombre: {}", nombre);
            log.info("Curso: {}", curso);
            log.info("Timestamp: {}", timestamp);
            log.info("Evento recibido: {}", eventoNombre);

            procesarEventoEstudiante(evento);

            channel.basicAck(tag, false);
            log.info("✅ Mensaje confirmado en cola eventos.estudiante para evento={}", eventoNombre);

        } catch (IllegalArgumentException | MessageConversionException e) {
            log.error("❌ Error no recuperable procesando evento de Estudiante. Se envía a DLQ.", e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ el evento de Estudiante.", nackError);
            }
        } catch (Exception e) {
            if (!redelivered) {
                log.warn("⚠️ Fallo temporal en evento de Estudiante. Se reintenta una sola vez.", e);
                try {
                    channel.basicNack(tag, false, true);
                } catch (Exception nackError) {
                    log.error("Error en reintento del evento de Estudiante.", nackError);
                }
                return;
            }

            log.error("❌ Evento de Estudiante ya reintentado y sigue fallando. Se envía a DLQ.", e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ tras reintento fallido.", nackError);
            }
        }
    }

    private void procesarEventoEstudiante(Map<String, Object> evento) {
        if (evento == null || evento.isEmpty()) {
            throw new IllegalArgumentException("El evento de estudiante no puede ser nulo o vacío");
        }

        Object id = evento.get("id");
        if (id == null) {
            throw new IllegalArgumentException("El id del evento de estudiante es obligatorio");
        }

        log.info("Procesando evento de estudiante con id={}", id);
    }
}
