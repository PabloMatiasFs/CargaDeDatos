package com.company.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("PersonaId Value Object Tests")
class PersonaIdTest {

    @Test
    @DisplayName("Should create PersonaId with valid positive integer")
    void shouldCreatePersonaIdWithValidPositiveInteger() {
        // Given
        Integer validId = 1;

        // When
        PersonaId personaId = new PersonaId(validId);

        // Then
        assertThat(personaId.getValue()).isEqualTo(validId);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 10, 100, 999999})
    @DisplayName("Should create PersonaId with various valid positive integers")
    void shouldCreatePersonaIdWithVariousValidPositiveIntegers(Integer validId) {
        // When
        PersonaId personaId = new PersonaId(validId);

        // Then
        assertThat(personaId.getValue()).isEqualTo(validId);
    }

    @Test
    @DisplayName("Should throw exception when PersonaId is null")
    void shouldThrowExceptionWhenPersonaIdIsNull() {
        // Given
        Integer nullId = null;

        // When & Then
        assertThatThrownBy(() -> new PersonaId(nullId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID de persona debe ser un entero positivo");
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("Should throw exception when PersonaId is zero or negative")
    void shouldThrowExceptionWhenPersonaIdIsZeroOrNegative(Integer invalidId) {
        // When & Then
        assertThatThrownBy(() -> new PersonaId(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID de persona debe ser un entero positivo");
    }

    @Test
    @DisplayName("Should be equal when PersonaIds have same value")
    void shouldBeEqualWhenPersonaIdsHaveSameValue() {
        // Given
        Integer id = 1;
        PersonaId personaId1 = new PersonaId(id);
        PersonaId personaId2 = new PersonaId(id);

        // When & Then
        assertThat(personaId1).isEqualTo(personaId2);
        assertThat(personaId1.hashCode()).isEqualTo(personaId2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when PersonaIds have different values")
    void shouldNotBeEqualWhenPersonaIdsHaveDifferentValues() {
        // Given
        PersonaId personaId1 = new PersonaId(1);
        PersonaId personaId2 = new PersonaId(2);

        // When & Then
        assertThat(personaId1).isNotEqualTo(personaId2);
        assertThat(personaId1.hashCode()).isNotEqualTo(personaId2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal to null or different class")
    void shouldNotBeEqualToNullOrDifferentClass() {
        // Given
        PersonaId personaId = new PersonaId(1);

        // When & Then
        assertThat(personaId).isNotEqualTo(null);
        assertThat(personaId).isNotEqualTo("not a PersonaId");
        assertThat(personaId).isNotEqualTo(Integer.valueOf(1));
    }

    @Test
    @DisplayName("Should have meaningful toString representation")
    void shouldHaveMeaningfulToStringRepresentation() {
        // Given
        Integer id = 42;
        PersonaId personaId = new PersonaId(id);

        // When
        String toString = personaId.toString();

        // Then
        assertThat(toString).isEqualTo("PersonaId{42}");
    }
}