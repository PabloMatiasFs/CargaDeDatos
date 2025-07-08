package com.company.infrastructure.adapter.web.mapper;

import com.company.domain.entity.Persona;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import com.company.infrastructure.adapter.web.dto.PersonaResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-08T20:38:57-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.42.50.v20250628-1110, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PersonaWebMapperImpl implements PersonaWebMapper {

    @Override
    public PersonaResponse toResponse(Persona persona) {
        if ( persona == null ) {
            return null;
        }

        PersonaResponse personaResponse = new PersonaResponse();

        personaResponse.setId( personaIdValue( persona ) );
        personaResponse.setEmail( personaEmailValue( persona ) );
        personaResponse.setTelefono( personaTelefonoValue( persona ) );
        personaResponse.setNombreCompleto( persona.getNombreCompleto() );
        personaResponse.setNombre( persona.getNombre() );
        personaResponse.setApellido( persona.getApellido() );
        personaResponse.setDireccion( persona.getDireccion() );

        return personaResponse;
    }

    @Override
    public List<PersonaResponse> toResponseList(List<Persona> personas) {
        if ( personas == null ) {
            return null;
        }

        List<PersonaResponse> list = new ArrayList<PersonaResponse>( personas.size() );
        for ( Persona persona : personas ) {
            list.add( toResponse( persona ) );
        }

        return list;
    }

    private Integer personaIdValue(Persona persona) {
        if ( persona == null ) {
            return null;
        }
        PersonaId id = persona.getId();
        if ( id == null ) {
            return null;
        }
        Integer value = id.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String personaEmailValue(Persona persona) {
        if ( persona == null ) {
            return null;
        }
        Email email = persona.getEmail();
        if ( email == null ) {
            return null;
        }
        String value = email.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }

    private String personaTelefonoValue(Persona persona) {
        if ( persona == null ) {
            return null;
        }
        Telefono telefono = persona.getTelefono();
        if ( telefono == null ) {
            return null;
        }
        String value = telefono.getValue();
        if ( value == null ) {
            return null;
        }
        return value;
    }
}
