package com.company.infrastructure.adapter.persistence;

import jakarta.persistence.*;

/**
 * Entidad JPA para persistir Persona
 * Adaptador entre el modelo de dominio y la base de datos
 */
@Entity
@Table(name = "datospersonas")
public class PersonaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpersona")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 45)
    private String nombre;

    @Column(name = "apellido", nullable = false, length = 45)
    private String apellido;

    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Column(name = "tel", nullable = false)
    private String telefono;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    // Constructor por defecto requerido por JPA
    public PersonaJpaEntity() {}

    // Constructor para crear nueva entidad
    public PersonaJpaEntity(String nombre, String apellido, String email, String telefono, String direccion) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    // Constructor completo
    public PersonaJpaEntity(Integer id, String nombre, String apellido, String email, String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
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
}