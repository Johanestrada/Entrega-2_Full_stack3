package com.colegio.rabbit_admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BindingRequest {

    @NotBlank(message = "El nombre de la cola es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre de la cola debe tener entre 1 y 255 caracteres")
    private String queue;

    @NotBlank(message = "El nombre del exchange es obligatorio")
    @Size(min = 1, max = 255, message = "El nombre del exchange debe tener entre 1 y 255 caracteres")
    private String exchange;

    @NotBlank(message = "La routing key es obligatoria")
    @Size(min = 1, max = 255, message = "La routing key debe tener entre 1 y 255 caracteres")
    private String routingKey;
}
