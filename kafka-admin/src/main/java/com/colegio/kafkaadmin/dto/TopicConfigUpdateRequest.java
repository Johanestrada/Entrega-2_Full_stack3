package com.colegio.kafkaadmin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.Map;

@Schema(description = "Solicitud para modificar la configuración de un tópico")
public class TopicConfigUpdateRequest {

    @NotBlank(message = "La configuración es obligatoria")
    @Schema(description = "Mapa de propiedades a actualizar", example = "{\"retention.ms\":\"604800000\"}")
    private Map<String, String> configs;

    public Map<String, String> getConfigs() {
        return configs;
    }

    public void setConfigs(Map<String, String> configs) {
        this.configs = configs;
    }
}
