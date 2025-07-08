package com.company.domain.entity;

import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Persona Domain Entity Tests")
class PersonaTest {

    // Test data constants
    private static final String VALID_NOMBRE = "Juan";
    private static final String VALID_APELLIDO = "Pérez";
    private static final String VALID_EMAIL = "juan.perez@email.com";
    private static final String VALID_TELEFONO = "1234567890";
    private static final String VALID_DIRECCION = "Calle Principal 123";

    @Test
    @DisplayName("Should create new Persona without ID")
    void shouldCreateNewPersonaWithoutId() {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // Then
        assertThat(persona.getId()).isNull();
        assertThat(persona.getNombre()).isEqualTo(VALID_NOMBRE);
        assertThat(persona.getApellido()).isEqualTo(VALID_APELLIDO);
        assertThat(persona.getEmail()).isEqualTo(email);
        assertThat(persona.getTelefono()).isEqualTo(telefono);
        assertThat(persona.getDireccion()).isEqualTo(VALID_DIRECCION);
    }

    @Test
    @DisplayName("Should create existing Persona with ID")
    void shouldCreateExistingPersonaWithId() {
        // Given
        PersonaId id = new PersonaId(1);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When
        Persona persona = new Persona(id, VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // Then
        assertThat(persona.getId()).isEqualTo(id);
        assertThat(persona.getNombre()).isEqualTo(VALID_NOMBRE);
        assertThat(persona.getApellido()).isEqualTo(VALID_APELLIDO);
        assertThat(persona.getEmail()).isEqualTo(email);
        assertThat(persona.getTelefono()).isEqualTo(telefono);
        assertThat(persona.getDireccion()).isEqualTo(VALID_DIRECCION);
    }

    @Test
    @DisplayName("Should throw exception when creating Persona with null ID")
    void shouldThrowExceptionWhenCreatingPersonaWithNullId() {
        // Given
        PersonaId nullId = null;
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(nullId, VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("ID no puede ser null");
    }

    @Test
    @DisplayName("Should strip whitespace from nombre, apellido and direccion")
    void shouldStripWhitespaceFromNombreApellidoAndDireccion() {
        // Given
        String nombreWithSpaces = "  Juan  ";
        String apellidoWithSpaces = "  Pérez  ";
        String direccionWithSpaces = "  Calle Principal 123  ";
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When
        Persona persona = new Persona(nombreWithSpaces, apellidoWithSpaces, email, telefono, direccionWithSpaces);

        // Then
        assertThat(persona.getNombre()).isEqualTo("Juan");
        assertThat(persona.getApellido()).isEqualTo("Pérez");
        assertThat(persona.getDireccion()).isEqualTo("Calle Principal 123");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    @DisplayName("Should throw exception for invalid nombre")
    void shouldThrowExceptionForInvalidNombre(String invalidNombre) {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(invalidNombre, VALID_APELLIDO, email, telefono, VALID_DIRECCION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nombre no puede estar vacío");
    }

    @Test
    @DisplayName("Should throw exception when nombre exceeds maximum length")
    void shouldThrowExceptionWhenNombreExceedsMaximumLength() {
        // Given
        String longNombre = "a".repeat(46);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(longNombre, VALID_APELLIDO, email, telefono, VALID_DIRECCION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nombre no puede exceder 45 caracteres");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    @DisplayName("Should throw exception for invalid apellido")
    void shouldThrowExceptionForInvalidApellido(String invalidApellido) {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(VALID_NOMBRE, invalidApellido, email, telefono, VALID_DIRECCION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Apellido no puede estar vacío");
    }

    @Test
    @DisplayName("Should throw exception when apellido exceeds maximum length")
    void shouldThrowExceptionWhenApellidoExceedsMaximumLength() {
        // Given
        String longApellido = "a".repeat(46);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(VALID_NOMBRE, longApellido, email, telefono, VALID_DIRECCION))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Apellido no puede exceder 45 caracteres");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t\n"})
    @DisplayName("Should throw exception for invalid direccion")
    void shouldThrowExceptionForInvalidDireccion(String invalidDireccion) {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, invalidDireccion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dirección no puede estar vacía");
    }

    @Test
    @DisplayName("Should throw exception when direccion exceeds maximum length")
    void shouldThrowExceptionWhenDireccionExceedsMaximumLength() {
        // Given
        String longDireccion = "a".repeat(101);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        // When & Then
        assertThatThrownBy(() -> new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, longDireccion))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dirección no puede exceder 100 caracteres");
    }

    @Test
    @DisplayName("Should update personal information")
    void shouldUpdatePersonalInformation() {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        String nuevoNombre = "Carlos";
        String nuevoApellido = "García";
        String nuevaDireccion = "Avenida Central 456";

        // When
        persona.actualizarInformacionPersonal(nuevoNombre, nuevoApellido, nuevaDireccion);

        // Then
        assertThat(persona.getNombre()).isEqualTo(nuevoNombre);
        assertThat(persona.getApellido()).isEqualTo(nuevoApellido);
        assertThat(persona.getDireccion()).isEqualTo(nuevaDireccion);
        // Email y telefono no deben cambiar
        assertThat(persona.getEmail()).isEqualTo(email);
        assertThat(persona.getTelefono()).isEqualTo(telefono);
    }

    @Test
    @DisplayName("Should change email")
    void shouldChangeEmail() {
        // Given
        Email originalEmail = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, originalEmail, telefono, VALID_DIRECCION);

        Email nuevoEmail = new Email("carlos.garcia@email.com");

        // When
        persona.cambiarEmail(nuevoEmail);

        // Then
        assertThat(persona.getEmail()).isEqualTo(nuevoEmail);
        assertThat(persona.getEmail()).isNotEqualTo(originalEmail);
    }

    @Test
    @DisplayName("Should throw exception when changing to null email")
    void shouldThrowExceptionWhenChangingToNullEmail() {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // When & Then
        assertThatThrownBy(() -> persona.cambiarEmail(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Email no puede ser null");
    }

    @Test
    @DisplayName("Should change telefono")
    void shouldChangeTelefono() {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono originalTelefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, originalTelefono, VALID_DIRECCION);

        Telefono nuevoTelefono = new Telefono("0987654321");

        // When
        persona.cambiarTelefono(nuevoTelefono);

        // Then
        assertThat(persona.getTelefono()).isEqualTo(nuevoTelefono);
        assertThat(persona.getTelefono()).isNotEqualTo(originalTelefono);
    }

    @Test
    @DisplayName("Should throw exception when changing to null telefono")
    void shouldThrowExceptionWhenChangingToNullTelefono() {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // When & Then
        assertThatThrownBy(() -> persona.cambiarTelefono(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Teléfono no puede ser null");
    }

    @Test
    @DisplayName("Should return complete name")
    void shouldReturnCompleteName() {
        // Given
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // When
        String nombreCompleto = persona.getNombreCompleto();

        // Then
        assertThat(nombreCompleto).isEqualTo("Juan Pérez");
    }

    @Test
    @DisplayName("Should be equal when personas have same ID")
    void shouldBeEqualWhenPersonasHaveSameId() {
        // Given
        PersonaId id = new PersonaId(1);
        Email email1 = new Email("email1@example.com");
        Email email2 = new Email("email2@example.com");
        Telefono telefono1 = new Telefono("1111111111");
        Telefono telefono2 = new Telefono("2222222222");

        Persona persona1 = new Persona(id, "Nombre1", "Apellido1", email1, telefono1, "Direccion1");
        Persona persona2 = new Persona(id, "Nombre2", "Apellido2", email2, telefono2, "Direccion2");

        // When & Then
        assertThat(persona1).isEqualTo(persona2);
        assertThat(persona1.hashCode()).isEqualTo(persona2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when personas have different IDs")
    void shouldNotBeEqualWhenPersonasHaveDifferentIds() {
        // Given
        PersonaId id1 = new PersonaId(1);
        PersonaId id2 = new PersonaId(2);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        Persona persona1 = new Persona(id1, VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);
        Persona persona2 = new Persona(id2, VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // When & Then
        assertThat(persona1).isNotEqualTo(persona2);
        assertThat(persona1.hashCode()).isNotEqualTo(persona2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when one persona has no ID")
    void shouldNotBeEqualWhenOnePersonaHasNoId() {
        // Given
        PersonaId id = new PersonaId(1);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);

        Persona personaWithId = new Persona(id, VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);
        Persona personaWithoutId = new Persona(VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // When & Then
        assertThat(personaWithId).isNotEqualTo(personaWithoutId);
    }

    @Test
    @DisplayName("Should have meaningful toString representation")
    void shouldHaveMeaningfulToStringRepresentation() {
        // Given
        PersonaId id = new PersonaId(1);
        Email email = new Email(VALID_EMAIL);
        Telefono telefono = new Telefono(VALID_TELEFONO);
        Persona persona = new Persona(id, VALID_NOMBRE, VALID_APELLIDO, email, telefono, VALID_DIRECCION);

        // When
        String toString = persona.toString();

        // Then
        assertThat(toString).contains("Persona{");
        assertThat(toString).contains("id=" + id.toString());
        assertThat(toString).contains("nombre='" + VALID_NOMBRE + "'");
        assertThat(toString).contains("apellido='" + VALID_APELLIDO + "'");
        assertThat(toString).contains("email=" + email.toString());
        assertThat(toString).contains("telefono=" + telefono.toString());
        assertThat(toString).contains("direccion='" + VALID_DIRECCION + "'");
    }
}