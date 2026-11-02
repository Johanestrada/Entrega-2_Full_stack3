package com.colegio.evaluacionService.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.calificaciones.name}")
    private String calificacionesTopicName;

    @Value("${kafka.topic.calificaciones.partitions}")
    private int calificacionesPartitions;

    @Value("${kafka.topic.calificaciones.replicas}")
    private short calificacionesReplicas;

    @Value("${kafka.topic.calificaciones.retention.ms}")
    private long calificacionesRetentionMs;

    @Value("${kafka.topic.calificaciones.cleanup.policy}")
    private String calificacionesCleanupPolicy;

    @Value("${kafka.topic.calificaciones.min.insync.replicas}")
    private short calificacionesMinInSyncReplicas;

    @Value("${kafka.topic.calificaciones.dlt.name}")
    private String calificacionesDltTopicName;

    @Value("${kafka.topic.calificaciones.dlt.partitions}")
    private int calificacionesDltPartitions;

    @Value("${kafka.topic.calificaciones.dlt.replicas}")
    private short calificacionesDltReplicas;

    @Value("${kafka.topic.calificaciones.dlt.retention.ms}")
    private long calificacionesDltRetentionMs;

    @Value("${kafka.topic.calificaciones.dlt.cleanup.policy}")
    private String calificacionesDltCleanupPolicy;

    @Value("${kafka.topic.calificaciones.dlt.min.insync.replicas}")
    private short calificacionesDltMinInSyncReplicas;

    // calificaciones.eventos tiene alta importancia analítica, por lo que se mantiene mayor retención
    // (7 días) y más particiones (6) para distribuir la carga de eventos de nota/materia por estudiante.
    // El cleanup policy delete evita compresión innecesaria porque estos eventos son históricos y no se consultan
    // como estado mutable; se usa replica factor 3 y min.insync.replicas=2 para tolerar fallos sin perder durabilidad.
    @Bean
    public NewTopic calificacionesEventosTopic() {
        return TopicBuilder.name(calificacionesTopicName)
                .partitions(calificacionesPartitions)
                .replicas(calificacionesReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(calificacionesRetentionMs))
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, calificacionesCleanupPolicy)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(calificacionesMinInSyncReplicas))
                .build();
    }

    // El DLT de calificaciones se conserva con una retención más corta que el tópico principal para evitar acumulación
    // de mensajes fallidos, pero mantiene 3 réplicas y min.insync.replicas=2 para no perder trazabilidad analítica.
    @Bean
    public NewTopic calificacionesEventosDltTopic() {
        return TopicBuilder.name(calificacionesDltTopicName)
                .partitions(calificacionesDltPartitions)
                .replicas(calificacionesDltReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(calificacionesDltRetentionMs))
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, calificacionesDltCleanupPolicy)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(calificacionesDltMinInSyncReplicas))
                .build();
    }
}
