package com.colegio.rabbit_admin.consumer;

import com.colegio.rabbit_admin.config.RabbitMQConfig;
import com.colegio.rabbit_admin.dto.EventoAsistencia;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

@Slf4j
@Service
public class AsistenciaEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.ASISTENCIA_QUEUE)
    public void consumirEventoAsistencia(
            EventoAsistencia evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        try {
            log.info("📨 Evento Asistencia recibido: {} - ID: {}", evento.getTipo(), evento.getId());
            log.debug("Detalles: EstudianteID={}, Fecha={}, Estado={}", evento.getEstudianteId(), evento.getFecha(), evento.getEstado());

            procesarEventoAsistencia(evento);

            channel.basicAck(deliveryTag, false);
            log.info("✅ Mensaje confirmado: {}", evento.getId());

        } catch (Exception e) {
            log.error("❌ Error procesando evento de Asistencia: {}", evento.getId(), e);
            try {
                channel.basicNack(deliveryTag, false, false);
                log.info("⚠️ Mensaje enviado a DLQ: {}", evento.getId());
            } catch (Exception nackError) {
                log.error("Error en NACK", nackError);
            }
        }
    }

    private void procesarEventoAsistencia(EventoAsistencia evento) {
        log.info("Procesando evento de asistencia para estudianteID: {}", evento.getEstudianteId());
    }
}
