package com.company.infrastructure.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO de respuesta para Persona
 */
@Schema(description = "Respuesta con datos de una persona")
public class PersonaResponse {

    @Schema(description = "ID único de la persona", example = "1")
    private Integer id;

    @Schema(description = "Nombre de la persona", example = "Juan")
    private String nombre;

    @Schema(description = "Apellido de la persona", example = "Pérez")
    private String apellido;

    @Schema(description = "Email de la persona", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Teléfono de la persona", example = "1234567890")
    private String telefono;

    @Schema(description = "Dirección de la persona", example = "Calle Principal 123")
    private String direccion;

    @Schema(description = "Nombre completo de la persona", example = "Juan Pérez")
    private String nombreCompleto;

    // Constructor por defecto
    public PersonaResponse() {}

    // Constructor completo
    public PersonaResponse(Integer id, String nombre, String apellido, String email, 
                          String telefono, String direccion, String nombreCompleto) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
        this.nombreCompleto = nombreCompleto;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    @Override
    public String toString() {
        return String.format("PersonaResponse{id=%d, nombre='%s', apellido='%s', email='%s', telefono='%s', direccion='%s', nombreCompleto='%s'}", 
                id, nombre, apellido, email, telefono, direccion, nombreCompleto);
    }
}