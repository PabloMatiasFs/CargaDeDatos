package com.company.infrastructure.adapter.persistence;

import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.PersonaId;
import com.company.infrastructure.adapter.mapper.PersonaMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador del repositorio de Persona
 * Implementa el puerto del dominio usando JPA
 */
@Repository
public class PersonaRepositoryAdapter implements PersonaRepository {

    private final PersonaJpaRepository jpaRepository;
    private final PersonaMapper mapper;

    public PersonaRepositoryAdapter(PersonaJpaRepository jpaRepository, PersonaMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Persona save(Persona persona) {
        PersonaJpaEntity jpaEntity = mapper.toJpaEntity(persona);
        PersonaJpaEntity savedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(savedEntity);
    }

    @Override
    public Persona update(Persona persona) {
        if (persona.getId() == null) {
            throw new IllegalArgumentException("No se puede actualizar una persona sin ID");
        }
        
        PersonaJpaEntity jpaEntity = mapper.toJpaEntity(persona);
        PersonaJpaEntity updatedEntity = jpaRepository.save(jpaEntity);
        return mapper.toDomainEntity(updatedEntity);
    }

    @Override
    public Optional<Persona> findById(PersonaId id) {
        return jpaRepository.findById(id.getValue())
                .map(mapper::toDomainEntity);
    }

    @Override
    public List<Persona> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(PersonaId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsById(PersonaId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public List<Persona> findByNombreContaining(String nombre) {
        return jpaRepository.findByNombreContainingIgnoreCase(nombre)
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Persona> findByApellidoContaining(String apellido) {
        return jpaRepository.findByApellidoContainingIgnoreCase(apellido)
                .stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toList());
    }
}