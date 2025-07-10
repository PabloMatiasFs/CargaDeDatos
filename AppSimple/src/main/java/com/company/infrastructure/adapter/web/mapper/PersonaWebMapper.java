package com.company.infrastructure.adapter.web.mapper;

import com.company.domain.entity.Persona;
import com.company.infrastructure.adapter.web.dto.PersonaResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper para convertir entre entidades de dominio y DTOs web
 */
@Component
public class PersonaWebMapper {

    /**
     * Convierte de entidad de dominio a DTO de respuesta
     */
    public PersonaResponse toResponse(Persona persona) {
        if (persona == null) {
            return null;
        }
        
        return new PersonaResponse(
            persona.getId() != null ? persona.getId().getValue() : null,
            persona.getNombre(),
            persona.getApellido(),
            persona.getEmail() != null ? persona.getEmail().getValue() : null,
            persona.getTelefono() != null ? persona.getTelefono().getValue() : null,
            persona.getDireccion(),
            persona.getNombreCompleto()
        );
    }

    /**
     * Convierte una lista de entidades de dominio a lista de DTOs de respuesta
     */
    public List<PersonaResponse> toResponseList(List<Persona> personas) {
        if (personas == null) {
            return null;
        }
        
        return personas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}