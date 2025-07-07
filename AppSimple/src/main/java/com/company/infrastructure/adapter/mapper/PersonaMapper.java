package com.company.infrastructure.adapter.mapper;

import com.company.domain.entity.Persona;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import com.company.infrastructure.adapter.persistence.PersonaJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper entre entidad de dominio y entidad JPA
 * Utiliza MapStruct para la conversión automática
 */
@Mapper(componentModel = "spring")
public interface PersonaMapper {

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "integerToPersonaId")
    @Mapping(target = "email", source = "email", qualifiedByName = "stringToEmail")
    @Mapping(target = "telefono", source = "telefono", qualifiedByName = "stringToTelefono")
    Persona toDomainEntity(PersonaJpaEntity jpaEntity);

    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    @Mapping(target = "id", source = "id", qualifiedByName = "personaIdToInteger")
    @Mapping(target = "email", source = "email", qualifiedByName = "emailToString")
    @Mapping(target = "telefono", source = "telefono", qualifiedByName = "telefonoToString")
    PersonaJpaEntity toJpaEntity(Persona domainEntity);

    // Métodos de conversión para Value Objects
    @org.mapstruct.Named("integerToPersonaId")
    default PersonaId integerToPersonaId(Integer id) {
        return id != null ? new PersonaId(id) : null;
    }

    @org.mapstruct.Named("personaIdToInteger")
    default Integer personaIdToInteger(PersonaId personaId) {
        return personaId != null ? personaId.getValue() : null;
    }

    @org.mapstruct.Named("stringToEmail")
    default Email stringToEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    @org.mapstruct.Named("emailToString")
    default String emailToString(Email email) {
        return email != null ? email.getValue() : null;
    }

    @org.mapstruct.Named("stringToTelefono")
    default Telefono stringToTelefono(String telefono) {
        return telefono != null ? new Telefono(telefono) : null;
    }

    @org.mapstruct.Named("telefonoToString")
    default String telefonoToString(Telefono telefono) {
        return telefono != null ? telefono.getValue() : null;
    }
}