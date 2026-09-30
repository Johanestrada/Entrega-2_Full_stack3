package com.colegio.rabbit_admin.consumer;

import com.colegio.rabbit_admin.config.RabbitMQConfig;
import com.colegio.rabbit_admin.dto.EventoEstudiante;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;

@Slf4j
@Service
public class EstudianteEventConsumer {

    @RabbitListener(queues = RabbitMQConfig.ESTUDIANTE_QUEUE)
    public void consumirEventoEstudiante(
            EventoEstudiante evento,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {

        try {
            log.info("📨 Evento Estudiante recibido: {} - ID: {}", evento.getTipo(), evento.getId());
            log.debug("Detalles: Nombre={}, Email={}, Acción={}", evento.getNombre(), evento.getEmail(), evento.getAccion());

            // Aquí va la lógica de procesamiento
            procesarEventoEstudiante(evento);

            // ACK manual - confirmar que se procesó correctamente
            channel.basicAck(deliveryTag, false);
            log.info("✅ Mensaje confirmado: {}", evento.getId());

        } catch (Exception e) {
            log.error("❌ Error procesando evento de Estudiante: {}", evento.getId(), e);
            try {
                // NACK sin requeue - envía a DLQ
                channel.basicNack(deliveryTag, false, false);
                log.info("⚠️ Mensaje enviado a DLQ: {}", evento.getId());
            } catch (Exception nackError) {
                log.error("Error en NACK", nackError);
            }
        }
    }

    private void procesarEventoEstudiante(EventoEstudiante evento) {
        // Lógica de procesamiento
        log.info("Procesando evento de estudiante: {}", evento.getAccion());
        // Aquí irían operaciones como guardar en BD, enviar notificaciones, etc.
    }
}
