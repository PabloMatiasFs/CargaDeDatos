package com.company.domain.port;

import com.company.domain.entity.Persona;
import com.company.domain.valueobject.PersonaId;

import java.util.List;
import java.util.Optional;

/**
 * Puerto del repositorio de Persona
 * Define el contrato para persistencia sin exponer detalles de implementación
 */
public interface PersonaRepository {
    
    /**
     * Guarda una nueva persona
     */
    Persona save(Persona persona);
    
    /**
     * Actualiza una persona existente
     */
    Persona update(Persona persona);
    
    /**
     * Busca una persona por su ID
     */
    Optional<Persona> findById(PersonaId id);
    
    /**
     * Obtiene todas las personas
     */
    List<Persona> findAll();
    
    /**
     * Elimina una persona por su ID
     */
    void deleteById(PersonaId id);
    
    /**
     * Verifica si existe una persona con el ID dado
     */
    boolean existsById(PersonaId id);
    
    /**
     * Busca personas por nombre (búsqueda parcial)
     */
    List<Persona> findByNombreContaining(String nombre);
    
    /**
     * Busca personas por apellido (búsqueda parcial)
     */
    List<Persona> findByApellidoContaining(String apellido);
}