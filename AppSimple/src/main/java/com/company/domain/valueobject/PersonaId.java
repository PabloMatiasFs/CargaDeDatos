package com.company.domain.valueobject;

import java.util.Objects;

/**
 * Value Object para el ID de Persona
 * Encapsula la lógica de validación del identificador
 */
public final class PersonaId {
    
    private final Integer value;

    public PersonaId(Integer value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ID de persona debe ser un entero positivo");
        }
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonaId personaId = (PersonaId) o;
        return Objects.equals(value, personaId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return String.format("PersonaId{%d}", value);
    }
}