package com.company.infrastructure.adapter.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO para crear una nueva persona
 */
@Schema(description = "Datos para crear una nueva persona")
public class PersonaCreateRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 45, message = "El nombre no puede exceder 45 caracteres")
    @Schema(description = "Nombre de la persona", example = "Juan", required = true)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 45, message = "El apellido no puede exceder 45 caracteres")
    @Schema(description = "Apellido de la persona", example = "Pérez", required = true)
    private String apellido;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    @Size(max = 45, message = "El email no puede exceder 45 caracteres")
    @Schema(description = "Email de la persona", example = "juan.perez@email.com", required = true)
    private String email;

    @NotBlank(message = "El teléfono es obligatorio")
    @Schema(description = "Teléfono de la persona", example = "1234567890", required = true)
    private String telefono;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 100, message = "La dirección no puede exceder 100 caracteres")
    @Schema(description = "Dirección de la persona", example = "Calle Principal 123", required = true)
    private String direccion;

    // Constructor por defecto
    public PersonaCreateRequest() {}

    // Constructor completo
    public PersonaCreateRequest(String nombre, String apellido, String email, String telefono, String direccion) {
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
        return String.format("PersonaCreateRequest{nombre='%s', apellido='%s', email='%s', telefono='%s', direccion='%s'}", 
                nombre, apellido, email, telefono, direccion);
    }
}