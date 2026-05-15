package com.colegio.bffservice.controller;

import com.colegio.bffservice.facade.AcademicoFacade;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.colegio.bffservice.model.AcademicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academico")
public class AcademicoController {

    @Autowired
    private AcademicoFacade facade;

    private static final Logger logger = LoggerFactory.getLogger(AcademicoController.class);

    @GetMapping("/{estudianteId}")
    @CrossOrigin(origins = "http://localhost:4173")
    public AcademicoDTO obtenerDatosAcademicos(@PathVariable Long estudianteId) {
        String requestId = UUID.randomUUID().toString();
        logger.info("[requestId={}] GET /academico/{} - solicitud recibida desde frontend", requestId, estudianteId);
        return facade.obtenerDatosAcademicos(estudianteId);
    }
}