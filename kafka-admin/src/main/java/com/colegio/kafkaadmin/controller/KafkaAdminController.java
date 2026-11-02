package com.colegio.kafkaadmin.controller;

import com.colegio.kafkaadmin.dto.TopicConfigUpdateRequest;
import com.colegio.kafkaadmin.dto.TopicCreateRequest;
import com.colegio.kafkaadmin.dto.TopicPartitionRequest;
import com.colegio.kafkaadmin.service.KafkaAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/kafka-admin")
@Tag(name = "Kafka Admin", description = "Administración de tópicos y grupos de consumo de Kafka")
public class KafkaAdminController {

    private final KafkaAdminService kafkaAdminService;

    public KafkaAdminController(KafkaAdminService kafkaAdminService) {
        this.kafkaAdminService = kafkaAdminService;
    }

    @PostMapping("/topics")
    @Operation(summary = "Crear un tópico Kafka")
    public ResponseEntity<Map<String, Object>> createTopic(@Valid @RequestBody TopicCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(kafkaAdminService.createTopic(request));
    }

    @GetMapping("/topics")
    @Operation(summary = "Listar tópicos Kafka")
    public ResponseEntity<List<String>> listTopics() {
        return ResponseEntity.ok(kafkaAdminService.listTopics());
    }

    @GetMapping("/topics/{name}")
    @Operation(summary = "Describir un tópico Kafka")
    public ResponseEntity<Map<String, Object>> describeTopic(@PathVariable String name) {
        return ResponseEntity.ok(kafkaAdminService.describeTopic(name));
    }

    @PatchMapping("/topics/{name}/config")
    @Operation(summary = "Modificar la configuración de un tópico")
    public ResponseEntity<Map<String, String>> updateTopicConfig(@PathVariable String name,
                                                                @Valid @RequestBody TopicConfigUpdateRequest request) {
        return ResponseEntity.ok(kafkaAdminService.updateTopicConfig(name, request));
    }

    @PutMapping("/topics/{name}/partitions")
    @Operation(summary = "Aumentar particiones de un tópico")
    public ResponseEntity<Map<String, Object>> increasePartitions(@PathVariable String name,
                                                                 @Valid @RequestBody TopicPartitionRequest request) {
        return ResponseEntity.ok(kafkaAdminService.increasePartitions(name, request));
    }

    @DeleteMapping("/topics/{name}")
    @Operation(summary = "Eliminar un tópico Kafka")
    public ResponseEntity<Void> deleteTopic(@PathVariable String name) {
        kafkaAdminService.deleteTopic(name);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consumer-groups")
    @Operation(summary = "Listar grupos de consumo Kafka")
    public ResponseEntity<List<Map<String, Object>>> listConsumerGroups() {
        return ResponseEntity.ok(kafkaAdminService.listConsumerGroups());
    }

    @GetMapping("/consumer-groups/{id}")
    @Operation(summary = "Describir un grupo de consumo Kafka")
    public ResponseEntity<Map<String, Object>> describeConsumerGroup(@PathVariable("id") String groupId) {
        return ResponseEntity.ok(kafkaAdminService.describeConsumerGroup(groupId));
    }

    @GetMapping("/consumer-groups/{id}/lag")
    @Operation(summary = "Consultar lag por partición de un grupo de consumo")
    public ResponseEntity<List<Map<String, Object>>> getConsumerGroupLag(@PathVariable("id") String groupId) {
        return ResponseEntity.ok(kafkaAdminService.getConsumerGroupLag(groupId));
    }
}
