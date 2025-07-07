package com.company.application.usecase;

import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.PersonaId;

/**
 * Caso de uso para eliminar una persona
 */
public class EliminarPersonaUseCase {
    
    private final PersonaRepository personaRepository;

    public EliminarPersonaUseCase(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public boolean execute(Integer id) {
        PersonaId personaId = new PersonaId(id);
        
        if (!personaRepository.existsById(personaId)) {
            return false; // La persona no existe
        }
        
        personaRepository.deleteById(personaId);
        return true;
    }
}