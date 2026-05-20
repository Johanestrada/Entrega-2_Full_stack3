package com.colegio.bffservice.controller;

import com.colegio.bffservice.facade.AcademicoFacade;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.colegio.bffservice.model.AcademicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academico")
public class AcademicoController {

    @Autowired
    private AcademicoFacade facade;

    private static final Logger logger = LoggerFactory.getLogger(AcademicoController.class);

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

    @PostMapping("/evaluaciones")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object crearEvaluacion(@RequestBody java.util.Map<String, Object> body) {
        Long estudianteId = Long.valueOf(body.get("estudianteId").toString());
        String materia = body.get("materia").toString();
        Double nota = Double.valueOf(body.get("nota").toString());
        return facade.guardarEvaluacionEstudiante(estudianteId, materia, nota);
    }

    @PutMapping("/evaluaciones")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object actualizarEvaluacion(@RequestBody java.util.Map<String, Object> body) {
        Long evaluacionId = Long.valueOf(body.get("id").toString());
        Double nota = Double.valueOf(body.get("nota").toString());
        return facade.actualizarEvaluacionEstudiante(evaluacionId, nota);
    }

    @PostMapping("/asistencias")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object crearAsistencia(@RequestBody java.util.Map<String, Object> body) {
        Long estudianteId = Long.valueOf(body.get("estudianteId").toString());
        Boolean presente = Boolean.valueOf(body.get("presente").toString());
        return facade.marcarAsistenciaEstudiante(estudianteId, presente);
    }

    @PostMapping("/estudiantes")
    @CrossOrigin(origins = "http://localhost:4173")
    public Object crearEstudiante(@RequestBody Object estudiante) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] POST /academico/estudiantes - solicitud recibida desde frontend", requestId);
        return facade.crearEstudiante(estudiante);
    }

    @DeleteMapping("/estudiantes/{id}")
    @CrossOrigin(origins = "http://localhost:4173")
    public ResponseEntity<Void> eliminarEstudiante(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] DELETE /academico/estudiantes/{} - solicitud recibida", requestId, id);
        facade.eliminarEstudiante(id);
        return ResponseEntity.noContent().build();
    }
}