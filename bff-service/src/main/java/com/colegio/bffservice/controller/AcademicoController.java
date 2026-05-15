package com.colegio.bffservice.controller;

import com.colegio.bffservice.facade.AcademicoFacade;
import com.colegio.bffservice.model.AcademicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/academico")
public class AcademicoController {

    @Autowired
    private AcademicoFacade facade;

    @GetMapping("/{estudianteId}")
    @CrossOrigin(origins = "http://localhost:4173")
    public AcademicoDTO obtenerDatosAcademicos(@PathVariable Long estudianteId) {
        return facade.obtenerDatosAcademicos(estudianteId);
    }
}