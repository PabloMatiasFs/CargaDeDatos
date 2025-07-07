package com.company.domain.entity;

import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;

import java.util.Objects;

/**
 * Entidad de dominio Persona
 * Representa el concepto central del negocio
 */
public class Persona {
    
    private final PersonaId id;
    private String nombre;
    private String apellido;
    private Email email;
    private Telefono telefono;
    private String direccion;

    // Constructor para crear nueva persona (sin ID)
    public Persona(String nombre, String apellido, Email email, Telefono telefono, String direccion) {
        this.id = null; // Se asignará al persistir
        this.nombre = validarNombre(nombre);
        this.apellido = validarApellido(apellido);
        this.email = email;
        this.telefono = telefono;
        this.direccion = validarDireccion(direccion);
    }

    // Constructor para persona existente (con ID)
    public Persona(PersonaId id, String nombre, String apellido, Email email, Telefono telefono, String direccion) {
        this.id = Objects.requireNonNull(id, "ID no puede ser null");
        this.nombre = validarNombre(nombre);
        this.apellido = validarApellido(apellido);
        this.email = email;
        this.telefono = telefono;
        this.direccion = validarDireccion(direccion);
    }

    // Métodos de negocio
    public void actualizarInformacionPersonal(String nombre, String apellido, String direccion) {
        this.nombre = validarNombre(nombre);
        this.apellido = validarApellido(apellido);
        this.direccion = validarDireccion(direccion);
    }

    public void cambiarEmail(Email nuevoEmail) {
        this.email = Objects.requireNonNull(nuevoEmail, "Email no puede ser null");
    }

    public void cambiarTelefono(Telefono nuevoTelefono) {
        this.telefono = Objects.requireNonNull(nuevoTelefono, "Teléfono no puede ser null");
    }

    public String getNombreCompleto() {
        return String.format("%s %s", nombre, apellido);
    }

    // Validaciones de negocio
    private String validarNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("Nombre no puede estar vacío");
        }
        if (nombre.length() > 45) {
            throw new IllegalArgumentException("Nombre no puede exceder 45 caracteres");
        }
        return nombre.strip();
    }

    private String validarApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            throw new IllegalArgumentException("Apellido no puede estar vacío");
        }
        if (apellido.length() > 45) {
            throw new IllegalArgumentException("Apellido no puede exceder 45 caracteres");
        }
        return apellido.strip();
    }

    private String validarDireccion(String direccion) {
        if (direccion == null || direccion.isBlank()) {
            throw new IllegalArgumentException("Dirección no puede estar vacía");
        }
        if (direccion.length() > 100) {
            throw new IllegalArgumentException("Dirección no puede exceder 100 caracteres");
        }
        return direccion.strip();
    }

    // Getters
    public PersonaId getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public Email getEmail() {
        return email;
    }

    public Telefono getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var persona = (Persona) o;
        return Objects.equals(id, persona.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Persona{id=%s, nombre='%s', apellido='%s', email=%s, telefono=%s, direccion='%s'}", 
                id, nombre, apellido, email, telefono, direccion);
    }
}