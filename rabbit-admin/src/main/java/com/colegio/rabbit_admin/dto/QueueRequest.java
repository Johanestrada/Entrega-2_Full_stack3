package com.colegio.rabbit_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueueRequest {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre de la cola debe tener entre 1 y 255 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9_.:-]+$", message = "El nombre de la cola solo puede contener letras, números y los caracteres . _ : -")
    private String name;

    @Builder.Default
    private boolean durable = true;

    @Builder.Default
    private boolean exclusive = false;

    @Builder.Default
    private boolean autoDelete = false;

    private String deadLetterExchange;
    private String deadLetterRoutingKey;
    private Integer messageTtl;
    private Integer maxPriority;
}
