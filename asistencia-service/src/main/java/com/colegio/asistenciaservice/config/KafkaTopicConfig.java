package com.colegio.asistenciaservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Value("${kafka.topic.asistencia.name}")
    private String asistenciaTopicName;

    @Value("${kafka.topic.asistencia.partitions}")
    private int asistenciaPartitions;

    @Value("${kafka.topic.asistencia.replicas}")
    private short asistenciaReplicas;

    @Value("${kafka.topic.asistencia.retention.ms}")
    private long asistenciaRetentionMs;

    @Value("${kafka.topic.asistencia.cleanup.policy}")
    private String asistenciaCleanupPolicy;

    @Value("${kafka.topic.asistencia.min.insync.replicas}")
    private short asistenciaMinInSyncReplicas;

    @Value("${kafka.topic.asistencia.dlt.name}")
    private String asistenciaDltTopicName;

    @Value("${kafka.topic.asistencia.dlt.partitions}")
    private int asistenciaDltPartitions;

    @Value("${kafka.topic.asistencia.dlt.replicas}")
    private short asistenciaDltReplicas;

    @Value("${kafka.topic.asistencia.dlt.retention.ms}")
    private long asistenciaDltRetentionMs;

    @Value("${kafka.topic.asistencia.dlt.cleanup.policy}")
    private String asistenciaDltCleanupPolicy;

    @Value("${kafka.topic.asistencia.dlt.min.insync.replicas}")
    private short asistenciaDltMinInSyncReplicas;

    // asistencia.eventos procesa eventos de alta frecuencia y de negocio operativo; por eso se usa menor retención
    // (24h) para evitar almacenamiento prolongado del volúmen operativo y particiones 3 para balancear carga sin sobrecoste.
    // Se reserva cleanup delete porque los eventos son de estado transitorio y el historial operativo no requiere compresión.
    @Bean
    public NewTopic asistenciaEventosTopic() {
        return TopicBuilder.name(asistenciaTopicName)
                .partitions(asistenciaPartitions)
                .replicas(asistenciaReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(asistenciaRetentionMs))
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, asistenciaCleanupPolicy)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(asistenciaMinInSyncReplicas))
                .build();
    }

    // El DLT de asistencia se mantiene corto por el volumen operativo; se busca diagnóstico rápido y limpieza automática
    // para no saturar disco, manteniendo 3 réplicas y min.insync=2 para resiliencia de la cola de error.
    @Bean
    public NewTopic asistenciaEventosDltTopic() {
        return TopicBuilder.name(asistenciaDltTopicName)
                .partitions(asistenciaDltPartitions)
                .replicas(asistenciaDltReplicas)
                .config(TopicConfig.RETENTION_MS_CONFIG, String.valueOf(asistenciaDltRetentionMs))
                .config(TopicConfig.CLEANUP_POLICY_CONFIG, asistenciaDltCleanupPolicy)
                .config(TopicConfig.MIN_IN_SYNC_REPLICAS_CONFIG, String.valueOf(asistenciaDltMinInSyncReplicas))
                .build();
    }
}
