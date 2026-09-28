package com.colegio.bffservice.controller;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.bffservice.facade.AcademicoFacade;
import com.colegio.bffservice.model.AcademicoDTO;

@RestController
@RequestMapping("/academico")
public class AcademicoController {

    @Autowired
    private AcademicoFacade facade;

    private static final Logger logger = LoggerFactory.getLogger(AcademicoController.class);

    @GetMapping("/estudiantes")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> listarEstudiantes() {
        return facade.listarEstudiantes();
    }

    @GetMapping("/estudiantes/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object obtenerEstudiante(@PathVariable Long id) {
        return facade.obtenerEstudiante(id);
    }

    @PutMapping("/estudiantes/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object actualizarEstudiante(@PathVariable Long id, @RequestBody Object estudiante) {
        return facade.actualizarEstudiante(id, estudiante);
    }

    @PostMapping("/estudiantes")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object crearEstudianteProxy(@RequestBody Object estudiante) {
        return facade.crearEstudiante(estudiante);
    }

    @DeleteMapping("/estudiantes/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<Void> eliminarEstudianteProxy(@PathVariable Long id) {
        facade.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/asistencias")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> listarAsistencias() {
        return facade.listarAsistencias();
    }

    @GetMapping("/asistencias/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object obtenerAsistencia(@PathVariable Long id) {
        return facade.obtenerAsistencia(id);
    }

    @GetMapping("/asistencias/estudiante/{estudianteId}")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> listarAsistenciasPorEstudiante(@PathVariable Long estudianteId) {
        return facade.listarAsistenciasPorEstudiante(estudianteId);
    }

    @PostMapping("/asistencias")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object crearAsistenciaProxy(@RequestBody Object asistencia) {
        return facade.crearAsistencia(asistencia);
    }

    @PutMapping("/asistencias/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object actualizarAsistencia(@PathVariable Long id, @RequestBody Object asistencia) {
        return facade.actualizarAsistencia(id, asistencia);
    }

    @DeleteMapping("/asistencias/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<Void> eliminarAsistencia(@PathVariable Long id) {
        facade.eliminarAsistencia(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/evaluaciones")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> listarEvaluaciones() {
        return facade.listarEvaluaciones();
    }

    @GetMapping("/evaluaciones/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object obtenerEvaluacion(@PathVariable Long id) {
        return facade.obtenerEvaluacion(id);
    }

    @GetMapping("/evaluaciones/estudiante/{estudianteId}")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> listarEvaluacionesPorEstudiante(@PathVariable Long estudianteId) {
        return facade.listarEvaluacionesPorEstudiante(estudianteId);
    }

    @PostMapping("/evaluaciones")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object crearEvaluacionProxy(@RequestBody Object evaluacion) {
        return facade.crearEvaluacion(evaluacion);
    }

    @GetMapping("/{estudianteId}")
    @CrossOrigin(origins = "http://localhost:4173")
    public AcademicoDTO obtenerDatosAcademicos(@PathVariable String estudianteId) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] GET /academico/{} - solicitud recibida desde frontend", requestId, estudianteId);
        return facade.obtenerDatosAcademicos(estudianteId);
    }

    @GetMapping("/run/{run}")
    @CrossOrigin(origins = "http://localhost:4173")
    public AcademicoDTO obtenerDatosAcademicosPorRun(@PathVariable String run) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] GET /academico/run/{} - solicitud recibida desde frontend", requestId, run);
        return facade.obtenerDatosAcademicosPorRun(run);
    }

    @GetMapping("/curso/{curso}")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> listarPorCurso(@PathVariable String curso) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] GET /academico/curso/{} - solicitud recibida desde frontend", requestId, curso);
        return facade.obtenerEstudiantesPorCurso(curso);
    }

    @PostMapping("/estudiante/{id}/asistencia")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object marcarAsistenciaEstudiante(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] POST /academico/estudiante/{}/asistencia - solicitud recibida", requestId, id);
        Boolean presente = Boolean.valueOf(String.valueOf(body.getOrDefault("presente", false)));
        return facade.marcarAsistenciaEstudiante(id, presente);
    }

    @PostMapping("/curso/{curso}/asistencia")
    @CrossOrigin(origins = "http://localhost:4173")
    public List<Object> marcarAsistenciaCurso(@PathVariable String curso, @RequestBody java.util.Map<String, Object> body) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] POST /academico/curso/{}/asistencia - solicitud recibida", requestId, curso);
        Boolean presente = Boolean.valueOf(String.valueOf(body.getOrDefault("presente", false)));
        return facade.marcarAsistenciaCurso(curso, presente);
    }

    @PostMapping("/estudiante/{id}/evaluacion")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object guardarEvaluacionEstudiante(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] POST /academico/estudiante/{}/evaluacion - solicitud recibida", requestId, id);
        String materia = String.valueOf(body.getOrDefault("materia", ""));
        Double nota = Double.valueOf(String.valueOf(body.getOrDefault("nota", "0")));
        return facade.guardarEvaluacionEstudiante(id, materia, nota);
    }

    @PutMapping("/evaluaciones/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object actualizarEvaluacion(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        Double nota = Double.valueOf(body.get("nota").toString());
        return facade.actualizarEvaluacionEstudiante(id, nota);
    }

    @DeleteMapping("/evaluaciones/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<Void> eliminarEvaluacion(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] DELETE /academico/evaluaciones/{} - solicitud recibida", requestId, id);
        facade.eliminarEvaluacion(id);
        return ResponseEntity.noContent().build();
    }
}