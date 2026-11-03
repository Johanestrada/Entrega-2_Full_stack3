package com.colegio.kafkaadmin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import java.util.Map;

@Schema(description = "Solicitud para crear un tópico Kafka")
public class TopicCreateRequest {

    @NotBlank(message = "El nombre del tópico es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "El nombre del tópico solo puede contener letras, números, puntos, guiones y guion bajo")
    @Schema(description = "Nombre del tópico", example = "calificaciones.eventos")
    private String name;

    @Min(value = 1, message = "Las particiones deben ser al menos 1")
    @Schema(description = "Cantidad de particiones", example = "3")
    private int partitions;

    @Min(value = 1, message = "Las réplicas deben ser al menos 1")
    @Schema(description = "Factor de replicas", example = "3")
    private short replicas;

    @Schema(description = "Configuración opcional del tópico", example = "{\"retention.ms\":\"86400000\"}")
    private Map<String, String> configs;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPartitions() {
        return partitions;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }

    public short getReplicas() {
        return replicas;
    }

    public void setReplicas(short replicas) {
        this.replicas = replicas;
    }

    public Map<String, String> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, String> configs) {
        this.configs = configs;
    }
}
