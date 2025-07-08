package com.company.application.service;

import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PersonaApplicationService Tests")
class PersonaApplicationServiceTest {

    @Mock
    private PersonaRepository personaRepository;

    private PersonaApplicationService personaApplicationService;

    private static final String VALID_NOMBRE = "Juan";
    private static final String VALID_APELLIDO = "Pérez";
    private static final String VALID_EMAIL = "juan.perez@email.com";
    private static final String VALID_TELEFONO = "1234567890";
    private static final String VALID_DIRECCION = "Calle Principal 123";

    @BeforeEach
    void setUp() {
        personaApplicationService = new PersonaApplicationService(personaRepository);
    }

    @Test
    @DisplayName("Should create persona successfully")
    void shouldCreatePersonaSuccessfully() {
        // Given
        Persona savedPersona = createTestPersona(1);
        when(personaRepository.save(any(Persona.class))).thenReturn(savedPersona);

        // When
        Persona result = personaApplicationService.crearPersona(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(new PersonaId(1));
        assertThat(result.getNombre()).isEqualTo(VALID_NOMBRE);
        assertThat(result.getApellido()).isEqualTo(VALID_APELLIDO);
        assertThat(result.getEmail().getValue()).isEqualTo(VALID_EMAIL);
        assertThat(result.getTelefono().getValue()).isEqualTo(VALID_TELEFONO);
        assertThat(result.getDireccion()).isEqualTo(VALID_DIRECCION);

        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should get all personas")
    void shouldGetAllPersonas() {
        // Given
        Persona persona1 = createTestPersona(1);
        Persona persona2 = createTestPersona(2);
        List<Persona> expectedPersonas = List.of(persona1, persona2);

        when(personaRepository.findAll()).thenReturn(expectedPersonas);

        // When
        List<Persona> result = personaApplicationService.obtenerTodasLasPersonas();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(persona1, persona2);
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should get persona by ID when exists")
    void shouldGetPersonaByIdWhenExists() {
        // Given
        Integer id = 1;
        Persona expectedPersona = createTestPersona(id);
        PersonaId personaId = new PersonaId(id);

        when(personaRepository.findById(personaId)).thenReturn(Optional.of(expectedPersona));

        // When
        Optional<Persona> result = personaApplicationService.obtenerPersonaPorId(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedPersona);
        verify(personaRepository, times(1)).findById(personaId);
    }

    @Test
    @DisplayName("Should return empty when persona not found by ID")
    void shouldReturnEmptyWhenPersonaNotFoundById() {
        // Given
        Integer id = 999;
        PersonaId personaId = new PersonaId(id);

        when(personaRepository.findById(personaId)).thenReturn(Optional.empty());

        // When
        Optional<Persona> result = personaApplicationService.obtenerPersonaPorId(id);

        // Then
        assertThat(result).isEmpty();
        verify(personaRepository, times(1)).findById(personaId);
    }

    @Test
    @DisplayName("Should search personas by nombre")
    void shouldSearchPersonasByNombre() {
        // Given
        String nombre = "Juan";
        Persona persona = createTestPersona(1);
        List<Persona> expectedPersonas = List.of(persona);

        when(personaRepository.findByNombreContaining(nombre)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = personaApplicationService.buscarPersonasPorNombre(nombre);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(persona);
        verify(personaRepository, times(1)).findByNombreContaining(nombre);
    }

    @Test
    @DisplayName("Should search personas by apellido")
    void shouldSearchPersonasByApellido() {
        // Given
        String apellido = "Pérez";
        Persona persona = createTestPersona(1);
        List<Persona> expectedPersonas = List.of(persona);

        when(personaRepository.findByApellidoContaining(apellido)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = personaApplicationService.buscarPersonasPorApellido(apellido);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(persona);
        verify(personaRepository, times(1)).findByApellidoContaining(apellido);
    }

    @Test
    @DisplayName("Should update persona when exists")
    void shouldUpdatePersonaWhenExists() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        Persona existingPersona = createTestPersona(id);
        Persona updatedPersona = new Persona(
                personaId,
                "Carlos",
                "García",
                new Email("carlos.garcia@email.com"),
                new Telefono("0987654321"),
                "Avenida Central 456"
        );

        when(personaRepository.findById(personaId)).thenReturn(Optional.of(existingPersona));
        when(personaRepository.update(any(Persona.class))).thenReturn(updatedPersona);

        // When
        Optional<Persona> result = personaApplicationService.actualizarPersona(
                id, "Carlos", "García", "carlos.garcia@email.com", "0987654321", "Avenida Central 456"
        );

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getNombre()).isEqualTo("Carlos");
        assertThat(result.get().getApellido()).isEqualTo("García");
        assertThat(result.get().getEmail().getValue()).isEqualTo("carlos.garcia@email.com");
        assertThat(result.get().getTelefono().getValue()).isEqualTo("0987654321");
        assertThat(result.get().getDireccion()).isEqualTo("Avenida Central 456");

        verify(personaRepository, times(1)).findById(personaId);
        verify(personaRepository, times(1)).update(any(Persona.class));
    }

    @Test
    @DisplayName("Should return empty when updating non-existent persona")
    void shouldReturnEmptyWhenUpdatingNonExistentPersona() {
        // Given
        Integer id = 999;
        PersonaId personaId = new PersonaId(id);

        when(personaRepository.findById(personaId)).thenReturn(Optional.empty());

        // When
        Optional<Persona> result = personaApplicationService.actualizarPersona(
                id, "Carlos", "García", "carlos.garcia@email.com", "0987654321", "Avenida Central 456"
        );

        // Then
        assertThat(result).isEmpty();
        verify(personaRepository, times(1)).findById(personaId);
        verify(personaRepository, never()).update(any(Persona.class));
    }

    @Test
    @DisplayName("Should delete persona when exists")
    void shouldDeletePersonaWhenExists() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);

        when(personaRepository.existsById(personaId)).thenReturn(true);

        // When
        boolean result = personaApplicationService.eliminarPersona(id);

        // Then
        assertThat(result).isTrue();
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, times(1)).deleteById(personaId);
    }

    @Test
    @DisplayName("Should return false when deleting non-existent persona")
    void shouldReturnFalseWhenDeletingNonExistentPersona() {
        // Given
        Integer id = 999;
        PersonaId personaId = new PersonaId(id);

        when(personaRepository.existsById(personaId)).thenReturn(false);

        // When
        boolean result = personaApplicationService.eliminarPersona(id);

        // Then
        assertThat(result).isFalse();
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, never()).deleteById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should handle partial updates correctly")
    void shouldHandlePartialUpdatesCorrectly() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        Persona existingPersona = createTestPersona(id);

        when(personaRepository.findById(personaId)).thenReturn(Optional.of(existingPersona));
        when(personaRepository.update(any(Persona.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When - Solo actualizar nombre y apellido
        Optional<Persona> result = personaApplicationService.actualizarPersona(
                id, "Carlos", "García", null, null, null
        );

        // Then
        assertThat(result).isPresent();
        Persona updatedPersona = result.get();
        assertThat(updatedPersona.getNombre()).isEqualTo("Carlos");
        assertThat(updatedPersona.getApellido()).isEqualTo("García");
        // Email, teléfono y dirección deben mantenerse igual que el original
        assertThat(updatedPersona.getEmail()).isEqualTo(existingPersona.getEmail());
        assertThat(updatedPersona.getTelefono()).isEqualTo(existingPersona.getTelefono());
        assertThat(updatedPersona.getDireccion()).isEqualTo(existingPersona.getDireccion());
    }

    @Test
    @DisplayName("Should validate email format when creating persona")
    void shouldValidateEmailFormatWhenCreatingPersona() {
        // Given
        String invalidEmail = "invalid-email";

        // When & Then
        assertThatThrownBy(() -> personaApplicationService.crearPersona(
                VALID_NOMBRE, VALID_APELLIDO, invalidEmail, VALID_TELEFONO, VALID_DIRECCION
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Formato de email inválido: " + invalidEmail);

        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should validate telefono format when creating persona")
    void shouldValidateTelefonoFormatWhenCreatingPersona() {
        // Given
        String invalidTelefono = "abc123";

        // When & Then
        assertThatThrownBy(() -> personaApplicationService.crearPersona(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, invalidTelefono, VALID_DIRECCION
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Formato de teléfono inválido: " + invalidTelefono);

        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should propagate repository exceptions")
    void shouldPropagateRepositoryExceptions() {
        // Given
        when(personaRepository.save(any(Persona.class)))
                .thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThatThrownBy(() -> personaApplicationService.crearPersona(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        )).isInstanceOf(RuntimeException.class)
          .hasMessage("Database connection error");
    }

    // Helper method
    private Persona createTestPersona(Integer id) {
        return new Persona(
                new PersonaId(id),
                VALID_NOMBRE,
                VALID_APELLIDO,
                new Email(VALID_EMAIL),
                new Telefono(VALID_TELEFONO),
                VALID_DIRECCION
        );
    }
}