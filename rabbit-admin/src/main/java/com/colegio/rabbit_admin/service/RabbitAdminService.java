package com.colegio.rabbit_admin.service;

import com.colegio.rabbit_admin.dto.BindingRequest;
import com.colegio.rabbit_admin.dto.ExchangeRequest;
import com.colegio.rabbit_admin.dto.QueueRequest;
import com.colegio.rabbit_admin.exception.ApiException;
import com.colegio.rabbit_admin.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AbstractExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class RabbitAdminService {

    private final RabbitAdmin rabbitAdmin;
    private final RestClient managementApi;

    public RabbitAdminService(
            RabbitAdmin rabbitAdmin,
            @Value("${rabbitmq.management.url:http://localhost:15672/api}") String managementUrl,
            @Value("${spring.rabbitmq.username:guest}") String username,
            @Value("${spring.rabbitmq.password:guest}") String password) {
        this.rabbitAdmin = rabbitAdmin;
        this.managementApi = RestClient.builder()
                .baseUrl(managementUrl)
                .defaultHeaders(headers -> headers.setBasicAuth(username, password))
                .build();
    }

    public Queue createQueue(QueueRequest request) {
        validateQueueName(request.getName());
        if (rabbitAdmin.getQueueProperties(request.getName()) != null) {
            throw new ApiException("La cola '" + request.getName() + "' ya existe");
        }

        Queue queue = new Queue(request.getName(), request.isDurable(), request.isExclusive(), request.isAutoDelete(), buildQueueArguments(request));
        rabbitAdmin.declareQueue(queue);
        log.info("Cola creada: {}", request.getName());
        return queue;
    }

    public List<String> listQueues() {
        return listResourceNames("/queues");
    }

    public void deleteQueue(String name) {
        if (!queueExists(name)) {
            throw new ResourceNotFoundException("La cola '" + name + "' no existe");
        }
        rabbitAdmin.deleteQueue(name);
        log.info("Cola eliminada: {}", name);
    }

    public Exchange createExchange(ExchangeRequest request) {
        validateExchangeName(request.getName());
        if (exchangeExists(request.getName())) {
            throw new ApiException("El exchange '" + request.getName() + "' ya existe");
        }

        Exchange exchange = buildExchange(request);
        rabbitAdmin.declareExchange(exchange);
        log.info("Exchange creado: {} ({})", request.getName(), request.getType());
        return exchange;
    }

    public List<String> listExchanges() {
        return listResourceNames("/exchanges");
    }

    public void deleteExchange(String name) {
        if (!exchangeExists(name)) {
            throw new ResourceNotFoundException("El exchange '" + name + "' no existe");
        }
        rabbitAdmin.deleteExchange(name);
        log.info("Exchange eliminado: {}", name);
    }

    public Binding createBinding(BindingRequest request) {
        if (!queueExists(request.getQueue())) {
            throw new ResourceNotFoundException("La cola '" + request.getQueue() + "' no existe");
        }
        if (!exchangeExists(request.getExchange())) {
            throw new ResourceNotFoundException("El exchange '" + request.getExchange() + "' no existe");
        }

        Binding binding = new Binding(
            request.getQueue(),
            Binding.DestinationType.QUEUE,
            request.getExchange(),
            request.getRoutingKey(),
            Map.of());

        rabbitAdmin.declareBinding(binding);
        log.info("Binding creado: {} -> {} [{}]", request.getQueue(), request.getExchange(), request.getRoutingKey());
        return binding;
    }

    public void deleteBinding(BindingRequest request) {
        if (!queueExists(request.getQueue())) {
            throw new ResourceNotFoundException("La cola '" + request.getQueue() + "' no existe");
        }
        if (!exchangeExists(request.getExchange())) {
            throw new ResourceNotFoundException("El exchange '" + request.getExchange() + "' no existe");
        }

        rabbitAdmin.removeBinding(BindingBuilder.bind(new Queue(request.getQueue()))
                .to(new DirectExchange(request.getExchange()))
                .with(request.getRoutingKey()));

        log.info("Binding eliminado: {} -> {} [{}]", request.getQueue(), request.getExchange(), request.getRoutingKey());
    }

    private boolean queueExists(String name) {
        return rabbitAdmin.getQueueProperties(name) != null;
    }

    private boolean exchangeExists(String name) {
        return listResourceNames("/exchanges").contains(name);
    }

    private List<String> listResourceNames(String endpoint) {
        List<Map<String, Object>> resources = managementApi.get()
                .uri(endpoint)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});

        if (resources == null) {
            return List.of();
        }

        return resources.stream()
                .map(resource -> resource.get("name"))
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .sorted()
                .toList();
    }

    private Map<String, Object> buildQueueArguments(QueueRequest request) {
        Map<String, Object> args = new java.util.HashMap<>();
        if (request.getDeadLetterExchange() != null && !request.getDeadLetterExchange().isBlank()) {
            args.put("x-dead-letter-exchange", request.getDeadLetterExchange());
        }
        if (request.getDeadLetterRoutingKey() != null && !request.getDeadLetterRoutingKey().isBlank()) {
            args.put("x-dead-letter-routing-key", request.getDeadLetterRoutingKey());
        }
        if (request.getMessageTtl() != null) {
            args.put("x-message-ttl", request.getMessageTtl());
        }
        if (request.getMaxPriority() != null) {
            args.put("x-max-priority", request.getMaxPriority());
        }
        return args;
    }

    private Exchange buildExchange(ExchangeRequest request) {
        AbstractExchange exchange = switch (request.getType().toLowerCase()) {
            case "direct" -> new DirectExchange(request.getName(), request.isDurable(), request.isAutoDelete());
            case "topic" -> new TopicExchange(request.getName(), request.isDurable(), request.isAutoDelete());
            case "fanout" -> new FanoutExchange(request.getName(), request.isDurable(), request.isAutoDelete());
            case "headers" -> new HeadersExchange(request.getName(), request.isDurable(), request.isAutoDelete());
            default -> throw new ApiException("Tipo de exchange no soportado: " + request.getType());
        };
        exchange.setInternal(request.isInternal());
        return exchange;
    }

    private void validateQueueName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre de la cola es obligatorio");
        }
        if (!name.matches("^[A-Za-z0-9_.:-]+$")) {
            throw new IllegalArgumentException("Nombre de cola no válido");
        }
    }

    private void validateExchangeName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("El nombre del exchange es obligatorio");
        }
        if (!name.matches("^[A-Za-z0-9_.:-]+$")) {
            throw new IllegalArgumentException("Nombre de exchange no válido");
        }
    }
}
