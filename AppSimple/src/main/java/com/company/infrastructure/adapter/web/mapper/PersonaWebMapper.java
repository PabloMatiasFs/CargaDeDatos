package com.company.infrastructure.adapter.web.mapper;

import com.company.domain.entity.Persona;
import com.company.infrastructure.adapter.web.dto.PersonaResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper para convertir entre entidades de dominio y DTOs web
 */
@Mapper(componentModel = "spring")
public interface PersonaWebMapper {

    /**
     * Convierte de entidad de dominio a DTO de respuesta
     */
    @Mapping(target = "id", source = "id.value")
    @Mapping(target = "email", source = "email.value")
    @Mapping(target = "telefono", source = "telefono.value")
    @Mapping(target = "nombreCompleto", source = "nombreCompleto")
    PersonaResponse toResponse(Persona persona);

    /**
     * Convierte una lista de entidades de dominio a lista de DTOs de respuesta
     */
    List<PersonaResponse> toResponseList(List<Persona> personas);
}