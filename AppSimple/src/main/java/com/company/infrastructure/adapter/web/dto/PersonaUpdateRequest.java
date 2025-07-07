package com.company.infrastructure.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * DTO para actualizar una persona existente
 * Todos los campos son opcionales para permitir actualizaciones parciales
 */
@Schema(description = "Datos para actualizar una persona existente")
public class PersonaUpdateRequest {

    @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
    @Schema(description = "Nombre de la persona", example = "Juan")
    private String nombre;

    @Size(max = 45, message = "El apellido no puede exceder 45 caracteres")
    @Schema(description = "Apellido de la persona", example = "Pérez")
    private String apellido;

    @Email(message = "El email debe tener un formato válido")
    @Size(max = 45, message = "El email no puede exceder 45 caracteres")
    @Schema(description = "Email de la persona", example = "juan.perez@email.com")
    private String email;

    @Schema(description = "Teléfono de la persona", example = "1234567890")
    private String telefono;

    @Size(max = 100, message = "La dirección no puede exceder 100 caracteres")
    @Schema(description = "Dirección de la persona", example = "Calle Principal 123")
    private String direccion;

    // Constructor por defecto
    public PersonaUpdateRequest() {}

    // Constructor completo
    public PersonaUpdateRequest(String nombre, String apellido, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Getters y Setters
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

    @Override
    public String toString() {
        return String.format("PersonaUpdateRequest{nombre='%s', apellido='%s', email='%s', telefono='%s', direccion='%s'}", 
                nombre, apellido, email, telefono, direccion);
    }
}