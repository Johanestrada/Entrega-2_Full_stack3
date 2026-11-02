package com.colegio.estudianteservice.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
public class DltKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(DltKafkaListener.class);
    private static final List<String> HEADER_NAMES = Arrays.asList(
            "kafka_dlt-exception-fqcn",
            "kafka_dlt-exception-message",
            "kafka_dlt-exception-stacktrace",
            "kafka_dlt-exception-class",
            "kafka_dlt-original-topic",
            "kafka_dlt-original-partition",
            "kafka_dlt-original-offset",
            "kafka_dlt-reason"
    );

    @KafkaListener(
            topics = "${kafka.topic.calificaciones.dlt.name:calificaciones.eventos.DLT},${kafka.topic.asistencia.dlt.name:asistencia.eventos.DLT}",
            groupId = "${kafka.consumer.notifications.group-id}-dlt",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void escucharDlt(ConsumerRecord<String, byte[]> record) {
        log.error("🚨 Kafka DLT recibido: topic={}, partition={}, offset={}, key={}, value={}",
                record.topic(), record.partition(), record.offset(), record.key(), new String(record.value(), StandardCharsets.UTF_8));

        for (String headerName : HEADER_NAMES) {
            Header header = record.headers().lastHeader(headerName);
            if (header != null) {
                String value = new String(header.value(), StandardCharsets.UTF_8);
                log.error("DLT header {} = {}", headerName, value);
            }
        }

        log.error("Kafka DLT original-topic={}, original-partition={}, original-offset={}, reason={}",
                record.headers().lastHeader("kafka_dlt-original-topic") != null
                        ? new String(record.headers().lastHeader("kafka_dlt-original-topic").value(), StandardCharsets.UTF_8)
                        : "n/a",
                record.headers().lastHeader("kafka_dlt-original-partition") != null
                        ? new String(record.headers().lastHeader("kafka_dlt-original-partition").value(), StandardCharsets.UTF_8)
                        : "n/a",
                record.headers().lastHeader("kafka_dlt-original-offset") != null
                        ? new String(record.headers().lastHeader("kafka_dlt-original-offset").value(), StandardCharsets.UTF_8)
                        : "n/a",
                record.headers().lastHeader("kafka_dlt-exception-message") != null
                        ? new String(record.headers().lastHeader("kafka_dlt-exception-message").value(), StandardCharsets.UTF_8)
                        : "n/a");
    }
}
