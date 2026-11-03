package com.colegio.kafkaadmin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

@Schema(description = "Solicitud para aumentar particiones de un tópico")
public class TopicPartitionRequest {

    @Min(value = 1, message = "La cantidad de particiones debe ser al menos 1")
    @Schema(description = "Número total de particiones final", example = "6")
    private int partitions;

    public int getPartitions() {
        return partitions;
    }

    public void setPartitions(int partitions) {
        this.partitions = partitions;
    }
}
