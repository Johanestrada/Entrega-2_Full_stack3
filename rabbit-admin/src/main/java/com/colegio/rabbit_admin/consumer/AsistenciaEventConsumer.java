package com.colegio.rabbit_admin.consumer;

import com.colegio.rabbit_admin.dto.EventoAsistencia;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.converter.MessageConversionException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AsistenciaEventConsumer {

    // Escucha los eventos de asistencia en la cola configurada.
    @RabbitListener(queues = "${rabbitmq.queue.asistencia}")
    public void consumirEventoAsistencia(
            EventoAsistencia evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag,
            @Header(AmqpHeaders.REDELIVERED) boolean redelivered) {

        try {
            log.info("📨 Evento Asistencia recibido: {} - ID: {}", evento.getTipo(), evento.getId());
            log.debug("Detalles: EstudianteID={}, Fecha={}, Estado={}", evento.getEstudianteId(), evento.getFecha(), evento.getEstado());

            procesarEventoAsistencia(evento);

            // Confirma el mensaje después de completar el procesamiento.
            channel.basicAck(tag, false);
            log.info("✅ Mensaje confirmado: {}", evento.getId());

        } catch (IllegalArgumentException | MessageConversionException e) {
            // Un evento inválido se rechaza sin requeue para enviarlo a la DLQ.
            log.error("❌ Error no recuperable procesando evento de Asistencia: {}. Se envía a DLQ.", evento.getId(), e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ el evento de Asistencia: {}", evento.getId(), nackError);
            }
        } catch (Exception e) {
            if (!redelivered) {
                // En el primer fallo temporal, devuelve el mensaje a la cola para reintentar.
                log.warn("⚠️ Fallo temporal en evento de Asistencia: {}. Se reintenta una sola vez.", evento.getId(), e);
                try {
                    channel.basicNack(tag, false, true);
                } catch (Exception nackError) {
                    log.error("Error en reintento del evento de Asistencia: {}", evento.getId(), nackError);
                }
                return;
            }

            // Si ya fue reentregado, lo rechaza sin requeue para enviarlo a la DLQ.
            log.error("❌ Evento de Asistencia ya reintentado y sigue fallando. Se envía a DLQ: {}", evento.getId(), e);
            try {
                channel.basicNack(tag, false, false);
            } catch (Exception nackError) {
                log.error("Error enviando a DLQ tras reintento fallido: {}", evento.getId(), nackError);
            }
        }
    }

    // Valida los campos básicos y registra el evento; no ejecuta otra operación de negocio.
    private void procesarEventoAsistencia(EventoAsistencia evento) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento de asistencia no puede ser nulo");
        }
        if (evento.getId() == null || evento.getId().isBlank()) {
            throw new IllegalArgumentException("El id del evento de asistencia es obligatorio");
        }

        log.info("Procesando evento de asistencia para estudianteID: {}", evento.getEstudianteId());
    }
}
