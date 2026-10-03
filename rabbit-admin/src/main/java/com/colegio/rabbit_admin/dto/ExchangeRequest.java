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
public class ExchangeRequest {

    @NotBlank(message = "El nombre del exchange es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre del exchange debe tener entre 1 y 255 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9_.:-]+$", message = "El nombre del exchange solo puede contener letras, números y los caracteres . _ : -")
    private String name;

    @NotBlank(message = "El tipo del exchange es obligatorio")
    @Pattern(regexp = "^(direct|topic|fanout|headers)$", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "El tipo del exchange debe ser: direct, topic, fanout o headers")
    private String type;

    @Builder.Default
    private boolean durable = true;

    @Builder.Default
    private boolean autoDelete = false;

    @Builder.Default
    private boolean internal = false;
}
