package com.colegio.estudianteservice.event;

import java.time.LocalDateTime;

public class EstudianteCreadoEvent {

    private String evento;
    private Long id;
    private String nombre;
    private String curso;
    private LocalDateTime timestamp;

    public EstudianteCreadoEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public EstudianteCreadoEvent(Long id, String nombre, String curso) {
        this.evento = "estudiante.creado";
        this.id = id;
        this.nombre = nombre;
        this.curso = curso;
        this.timestamp = LocalDateTime.now();
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCurso() {
        return curso;
    }

    public void setCurso(String curso) {
        this.curso = curso;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
