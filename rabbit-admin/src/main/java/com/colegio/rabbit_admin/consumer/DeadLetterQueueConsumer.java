package com.colegio.rabbit_admin.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class DeadLetterQueueConsumer {

    @RabbitListener(queues = "eventos.estudiante.dlq")
    public void handleEstudianteDLQ(String message) {
        log.error("🔴 [DLQ] Evento de Estudiante rechazado: {}", message);
        // Aquí se puede guardar en BD para posterior análisis o reintentos
    }

    @RabbitListener(queues = "eventos.asistencia.dlq")
    public void handleAsistenciaDLQ(String message) {
        log.error("🔴 [DLQ] Evento de Asistencia rechazado: {}", message);
    }

    @RabbitListener(queues = "eventos.evaluacion.dlq")
    public void handleEvaluacionDLQ(String message) {
        log.error("🔴 [DLQ] Evento de Evaluación rechazado: {}", message);
    }
}
