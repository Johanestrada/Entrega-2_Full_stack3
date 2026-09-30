package com.colegio.rabbit_admin.dto;

public class EventoEstudiante extends EventoBase {
    private Long estudianteId;
    private String nombre;
    private String email;
    private String accion;

    public EventoEstudiante() {
        super();
    }

    public EventoEstudiante(Long estudianteId, String nombre, String email, String accion) {
        super("ESTUDIANTE-" + System.currentTimeMillis(), "estudiante.creado", "estudiante-service");
        this.estudianteId = estudianteId;
        this.nombre = nombre;
        this.email = email;
        this.accion = accion;
    }

    public Long getEstudianteId() { return estudianteId; }
    public void setEstudianteId(Long estudianteId) { this.estudianteId = estudianteId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
}
