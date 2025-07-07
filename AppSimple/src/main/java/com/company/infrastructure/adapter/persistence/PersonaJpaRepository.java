package com.company.infrastructure.adapter.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio JPA para PersonaJpaEntity
 * Interfaz de Spring Data JPA
 */
@Repository
public interface PersonaJpaRepository extends JpaRepository<PersonaJpaEntity, Integer> {
    
    /**
     * Busca personas por nombre (búsqueda parcial, case-insensitive)
     */
    @Query("SELECT p FROM PersonaJpaEntity p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))")
    List<PersonaJpaEntity> findByNombreContainingIgnoreCase(@Param("nombre") String nombre);
    
    /**
     * Busca personas por apellido (búsqueda parcial, case-insensitive)
     */
    @Query("SELECT p FROM PersonaJpaEntity p WHERE LOWER(p.apellido) LIKE LOWER(CONCAT('%', :apellido, '%'))")
    List<PersonaJpaEntity> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);
    
    /**
     * Busca por email exacto
     */
    PersonaJpaEntity findByEmail(String email);
    
    /**
     * Verifica si existe una persona con el email dado
     */
    boolean existsByEmail(String email);
}