package com.colegio.rabbit_admin.controller;

import com.colegio.rabbit_admin.dto.EventoAsistencia;
import com.colegio.rabbit_admin.dto.EventoEstudiante;
import com.colegio.rabbit_admin.dto.EventoEvaluacion;
import com.colegio.rabbit_admin.service.EventPublisherService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rabbit-admin")
@CrossOrigin(origins = "*")
public class RabbitAdminController {

    private final EventPublisherService eventPublisherService;

    public RabbitAdminController(EventPublisherService eventPublisherService) {
        this.eventPublisherService = eventPublisherService;
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "rabbit-admin");
        response.put("message", "RabbitMQ Admin Service está operativo");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/eventos/estudiante")
    public ResponseEntity<?> publicarEventoEstudiante(@RequestBody EventoEstudiante evento) {
        try {
            log.info("Recibiendo evento de estudiante: {}", evento.getNombre());
            eventPublisherService.publicarEventoEstudiante(evento);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Evento de estudiante publicado exitosamente");
            response.put("eventId", evento.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error publicando evento estudiante", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/eventos/asistencia")
    public ResponseEntity<?> publicarEventoAsistencia(@RequestBody EventoAsistencia evento) {
        try {
            log.info("Recibiendo evento de asistencia para estudiante: {}", evento.getEstudianteId());
            eventPublisherService.publicarEventoAsistencia(evento);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Evento de asistencia publicado exitosamente");
            response.put("eventId", evento.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error publicando evento asistencia", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @PostMapping("/eventos/evaluacion")
    public ResponseEntity<?> publicarEventoEvaluacion(@RequestBody EventoEvaluacion evento) {
        try {
            log.info("Recibiendo evento de evaluación para estudiante: {}", evento.getEstudianteId());
            eventPublisherService.publicarEventoEvaluacion(evento);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Evento de evaluación publicado exitosamente");
            response.put("eventId", evento.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            log.error("Error publicando evento evaluacion", e);
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getInfo() {
        Map<String, Object> info = new HashMap<>();
        info.put("service", "rabbit-admin");
        info.put("version", "1.0.0");
        info.put("colas", new String[]{
            "eventos.estudiante",
            "eventos.asistencia",
            "eventos.evaluacion"
        });
        info.put("dlqs", new String[]{
            "eventos.estudiante.dlq",
            "eventos.asistencia.dlq",
            "eventos.evaluacion.dlq"
        });
        info.put("endpoints", new String[]{
            "POST /api/rabbit-admin/eventos/estudiante",
            "POST /api/rabbit-admin/eventos/asistencia",
            "POST /api/rabbit-admin/eventos/evaluacion",
            "GET /api/rabbit-admin/health",
            "GET /api/rabbit-admin/info"
        });
        return ResponseEntity.ok(info);
    }
}
