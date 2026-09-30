package com.colegio.rabbit_admin.controller;

import com.colegio.rabbit_admin.dto.BindingRequest;
import com.colegio.rabbit_admin.dto.ExchangeRequest;
import com.colegio.rabbit_admin.dto.QueueRequest;
import com.colegio.rabbit_admin.service.RabbitAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rabbit-admin")
@RequiredArgsConstructor
@Tag(name = "Rabbit Admin", description = "Gestión de colas, exchanges y bindings de RabbitMQ")
public class RabbitAdminController {

    private final RabbitAdminService rabbitAdminService;

    @PostMapping("/queues")
    @Operation(summary = "Crear una cola", description = "Crea una nueva cola en RabbitMQ con sus opciones de durabilidad, exclusividad y DLQ.", responses = {
            @ApiResponse(responseCode = "201", description = "Cola creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "La cola ya existe")
    })
    public ResponseEntity<?> createQueue(@Valid @RequestBody QueueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rabbitAdminService.createQueue(request));
    }

    @GetMapping("/queues")
    @Operation(summary = "Listar colas", description = "Devuelve el listado de colas existentes en RabbitMQ.", responses = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<String>> listQueues() {
        return ResponseEntity.ok(rabbitAdminService.listQueues());
    }

    @DeleteMapping("/queues/{name}")
    @Operation(summary = "Eliminar una cola", description = "Elimina una cola existente por nombre.", responses = {
            @ApiResponse(responseCode = "204", description = "Cola eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "La cola no existe")
    })
    public ResponseEntity<Void> deleteQueue(
            @Parameter(description = "Nombre de la cola a eliminar", required = true)
            @PathVariable String name) {
        rabbitAdminService.deleteQueue(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/exchanges")
    @Operation(summary = "Crear un exchange", description = "Crea un exchange con el tipo indicado (direct, topic, fanout, headers).", responses = {
            @ApiResponse(responseCode = "201", description = "Exchange creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "409", description = "El exchange ya existe")
    })
    public ResponseEntity<?> createExchange(@Valid @RequestBody ExchangeRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rabbitAdminService.createExchange(request));
    }

    @GetMapping("/exchanges")
    @Operation(summary = "Listar exchanges", description = "Devuelve el listado de exchanges existentes en RabbitMQ.", responses = {
            @ApiResponse(responseCode = "200", description = "Listado obtenido correctamente")
    })
    public ResponseEntity<List<String>> listExchanges() {
        return ResponseEntity.ok(rabbitAdminService.listExchanges());
    }

    @DeleteMapping("/exchanges/{name}")
    @Operation(summary = "Eliminar un exchange", description = "Elimina un exchange por nombre.", responses = {
            @ApiResponse(responseCode = "204", description = "Exchange eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "El exchange no existe")
    })
    public ResponseEntity<Void> deleteExchange(
            @Parameter(description = "Nombre del exchange a eliminar", required = true)
            @PathVariable String name) {
        rabbitAdminService.deleteExchange(name);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bindings")
    @Operation(summary = "Crear un binding", description = "Crea un binding entre una cola y un exchange con una routing key.", responses = {
            @ApiResponse(responseCode = "201", description = "Binding creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "La cola o el exchange no existe")
    })
    public ResponseEntity<?> createBinding(@Valid @RequestBody BindingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(rabbitAdminService.createBinding(request));
    }

    @DeleteMapping("/bindings")
    @Operation(summary = "Eliminar un binding", description = "Elimina un binding entre una cola y un exchange usando una routing key.", responses = {
            @ApiResponse(responseCode = "204", description = "Binding eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "La cola o el exchange no existe")
    })
    public ResponseEntity<Void> deleteBinding(@Valid @RequestBody BindingRequest request) {
        rabbitAdminService.deleteBinding(request);
        return ResponseEntity.noContent().build();
    }
}
