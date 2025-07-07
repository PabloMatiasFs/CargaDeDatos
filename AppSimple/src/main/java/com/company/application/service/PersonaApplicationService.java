package com.company.application.service;

import com.company.application.usecase.ActualizarPersonaUseCase;
import com.company.application.usecase.CrearPersonaUseCase;
import com.company.application.usecase.EliminarPersonaUseCase;
import com.company.application.usecase.ObtenerPersonasUseCase;
import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Servicio de aplicación que orquesta los casos de uso
 * Punto de entrada para las operaciones de negocio relacionadas con Persona
 */
public class PersonaApplicationService {
    
    private final CrearPersonaUseCase crearPersonaUseCase;
    private final ObtenerPersonasUseCase obtenerPersonasUseCase;
    private final ActualizarPersonaUseCase actualizarPersonaUseCase;
    private final EliminarPersonaUseCase eliminarPersonaUseCase;

    public PersonaApplicationService(PersonaRepository personaRepository) {
        this.crearPersonaUseCase = new CrearPersonaUseCase(personaRepository);
        this.obtenerPersonasUseCase = new ObtenerPersonasUseCase(personaRepository);
        this.actualizarPersonaUseCase = new ActualizarPersonaUseCase(personaRepository);
        this.eliminarPersonaUseCase = new EliminarPersonaUseCase(personaRepository);
    }

    /**
     * Crea una nueva persona
     */
    public Persona crearPersona(String nombre, String apellido, String email, 
                               String telefono, String direccion) {
        CrearPersonaUseCase.CrearPersonaCommand command = 
            new CrearPersonaUseCase.CrearPersonaCommand(nombre, apellido, email, telefono, direccion);
        return crearPersonaUseCase.execute(command);
    }

    /**
     * Obtiene todas las personas
     */
    public List<Persona> obtenerTodasLasPersonas() {
        return obtenerPersonasUseCase.obtenerTodas();
    }

    /**
     * Obtiene una persona por ID
     */
    public Optional<Persona> obtenerPersonaPorId(Integer id) {
        return obtenerPersonasUseCase.obtenerPorId(id);
    }

    /**
     * Busca personas por nombre
     */
    public List<Persona> buscarPersonasPorNombre(String nombre) {
        return obtenerPersonasUseCase.buscarPorNombre(nombre);
    }

    /**
     * Busca personas por apellido
     */
    public List<Persona> buscarPersonasPorApellido(String apellido) {
        return obtenerPersonasUseCase.buscarPorApellido(apellido);
    }

    /**
     * Actualiza una persona existente
     */
    public Optional<Persona> actualizarPersona(Integer id, String nombre, String apellido, 
                                             String email, String telefono, String direccion) {
        ActualizarPersonaUseCase.ActualizarPersonaCommand command = 
            new ActualizarPersonaUseCase.ActualizarPersonaCommand(id, nombre, apellido, email, telefono, direccion);
        return actualizarPersonaUseCase.execute(command);
    }

    /**
     * Elimina una persona
     */
    public boolean eliminarPersona(Integer id) {
        return eliminarPersonaUseCase.execute(id);
    }
}