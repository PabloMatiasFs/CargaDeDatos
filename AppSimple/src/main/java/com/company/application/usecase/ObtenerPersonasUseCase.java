package com.company.application.usecase;

import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.PersonaId;

import java.util.List;
import java.util.Optional;

/**
 * Casos de uso para obtener información de personas
 */
public class ObtenerPersonasUseCase {
    
    private final PersonaRepository personaRepository;

    public ObtenerPersonasUseCase(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    /**
     * Obtiene todas las personas
     */
    public List<Persona> obtenerTodas() {
        return personaRepository.findAll();
    }

    /**
     * Obtiene una persona por ID
     */
    public Optional<Persona> obtenerPorId(Integer id) {
        PersonaId personaId = new PersonaId(id);
        return personaRepository.findById(personaId);
    }

    /**
     * Busca personas por nombre
     */
    public List<Persona> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            return List.of();
        }
        return personaRepository.findByNombreContaining(nombre.strip());
    }

    /**
     * Busca personas por apellido
     */
    public List<Persona> buscarPorApellido(String apellido) {
        if (apellido == null || apellido.isBlank()) {
            return List.of();
        }
        return personaRepository.findByApellidoContaining(apellido.strip());
    }
}