package com.company.infrastructure.adapter.mapper;

import com.company.domain.entity.Persona;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import com.company.infrastructure.adapter.persistence.PersonaJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre entidad de dominio y entidad JPA
 * Conversión manual para evitar conflictos con constructores
 */
@Component
public class PersonaMapper {

    /**
     * Convierte de entidad JPA a entidad de dominio
     */
    public Persona toDomainEntity(PersonaJpaEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }
        
        PersonaId id = integerToPersonaId(jpaEntity.getId());
        Email email = stringToEmail(jpaEntity.getEmail());
        Telefono telefono = stringToTelefono(jpaEntity.getTelefono());
        
        return new Persona(id, jpaEntity.getNombre(), jpaEntity.getApellido(), email, telefono, jpaEntity.getDireccion());
    }

    /**
     * Convierte de entidad de dominio a entidad JPA
     */
    public PersonaJpaEntity toJpaEntity(Persona domainEntity) {
        if (domainEntity == null) {
            return null;
        }
        
        PersonaJpaEntity jpaEntity = new PersonaJpaEntity();
        jpaEntity.setId(personaIdToInteger(domainEntity.getId()));
        jpaEntity.setNombre(domainEntity.getNombre());
        jpaEntity.setApellido(domainEntity.getApellido());
        jpaEntity.setEmail(emailToString(domainEntity.getEmail()));
        jpaEntity.setTelefono(telefonoToString(domainEntity.getTelefono()));
        jpaEntity.setDireccion(domainEntity.getDireccion());
        
        return jpaEntity;
    }

    // Métodos de conversión para Value Objects
    public PersonaId integerToPersonaId(Integer id) {
        return id != null ? new PersonaId(id) : null;
    }

    public Integer personaIdToInteger(PersonaId personaId) {
        return personaId != null ? personaId.getValue() : null;
    }

    public Email stringToEmail(String email) {
        return email != null ? new Email(email) : null;
    }

    public String emailToString(Email email) {
        return email != null ? email.getValue() : null;
    }

    public Telefono stringToTelefono(String telefono) {
        return telefono != null ? new Telefono(telefono) : null;
    }

    public String telefonoToString(Telefono telefono) {
        return telefono != null ? telefono.getValue() : null;
    }
}