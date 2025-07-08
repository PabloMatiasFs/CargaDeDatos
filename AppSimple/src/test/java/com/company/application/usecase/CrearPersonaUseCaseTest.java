package com.company.application.usecase;

import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CrearPersonaUseCase Tests")
class CrearPersonaUseCaseTest {

    @Mock
    private PersonaRepository personaRepository;

    private CrearPersonaUseCase crearPersonaUseCase;

    private static final String VALID_NOMBRE = "Juan";
    private static final String VALID_APELLIDO = "Pérez";
    private static final String VALID_EMAIL = "juan.perez@email.com";
    private static final String VALID_TELEFONO = "1234567890";
    private static final String VALID_DIRECCION = "Calle Principal 123";

    @BeforeEach
    void setUp() {
        crearPersonaUseCase = new CrearPersonaUseCase(personaRepository);
    }

    @Test
    @DisplayName("Should create persona successfully with valid data")
    void shouldCreatePersonaSuccessfullyWithValidData() {
        // Given
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        );

        PersonaId expectedId = new PersonaId(1);
        Persona savedPersona = new Persona(
                expectedId,
                VALID_NOMBRE,
                VALID_APELLIDO,
                new Email(VALID_EMAIL),
                new Telefono(VALID_TELEFONO),
                VALID_DIRECCION
        );

        when(personaRepository.save(any(Persona.class))).thenReturn(savedPersona);

        // When
        Persona result = crearPersonaUseCase.execute(command);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(expectedId);
        assertThat(result.getNombre()).isEqualTo(VALID_NOMBRE);
        assertThat(result.getApellido()).isEqualTo(VALID_APELLIDO);
        assertThat(result.getEmail().getValue()).isEqualTo(VALID_EMAIL);
        assertThat(result.getTelefono().getValue()).isEqualTo(VALID_TELEFONO);
        assertThat(result.getDireccion()).isEqualTo(VALID_DIRECCION);

        // Verify repository interaction
        verify(personaRepository, times(1)).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should validate email format when creating persona")
    void shouldValidateEmailFormatWhenCreatingPersona() {
        // Given
        String invalidEmail = "invalid-email";
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, invalidEmail, VALID_TELEFONO, VALID_DIRECCION
        );

        // When & Then
        assertThatThrownBy(() -> crearPersonaUseCase.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Formato de email inválido: " + invalidEmail);

        // Verify repository was not called
        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should validate telefono format when creating persona")
    void shouldValidateTelefonoFormatWhenCreatingPersona() {
        // Given
        String invalidTelefono = "abc123";
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, invalidTelefono, VALID_DIRECCION
        );

        // When & Then
        assertThatThrownBy(() -> crearPersonaUseCase.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Formato de teléfono inválido: " + invalidTelefono);

        // Verify repository was not called
        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should validate nombre when creating persona")
    void shouldValidateNombreWhenCreatingPersona() {
        // Given
        String invalidNombre = "";
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                invalidNombre, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        );

        // When & Then
        assertThatThrownBy(() -> crearPersonaUseCase.execute(command))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Nombre no puede estar vacío");

        // Verify repository was not called
        verify(personaRepository, never()).save(any(Persona.class));
    }

    @Test
    @DisplayName("Should pass correct persona to repository save method")
    void shouldPassCorrectPersonaToRepositorySaveMethod() {
        // Given
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        );

        Persona savedPersona = new Persona(
                new PersonaId(1),
                VALID_NOMBRE,
                VALID_APELLIDO,
                new Email(VALID_EMAIL),
                new Telefono(VALID_TELEFONO),
                VALID_DIRECCION
        );

        when(personaRepository.save(any(Persona.class))).thenReturn(savedPersona);

        // When
        crearPersonaUseCase.execute(command);

        // Then
        ArgumentCaptor<Persona> personaCaptor = ArgumentCaptor.forClass(Persona.class);
        verify(personaRepository).save(personaCaptor.capture());

        Persona capturedPersona = personaCaptor.getValue();
        assertThat(capturedPersona.getId()).isNull(); // New persona should not have ID
        assertThat(capturedPersona.getNombre()).isEqualTo(VALID_NOMBRE);
        assertThat(capturedPersona.getApellido()).isEqualTo(VALID_APELLIDO);
        assertThat(capturedPersona.getEmail().getValue()).isEqualTo(VALID_EMAIL);
        assertThat(capturedPersona.getTelefono().getValue()).isEqualTo(VALID_TELEFONO);
        assertThat(capturedPersona.getDireccion()).isEqualTo(VALID_DIRECCION);
    }

    @Test
    @DisplayName("Should normalize email to lowercase when creating persona")
    void shouldNormalizeEmailToLowercaseWhenCreatingPersona() {
        // Given
        String mixedCaseEmail = "Juan.PEREZ@Example.COM";
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, mixedCaseEmail, VALID_TELEFONO, VALID_DIRECCION
        );

        Persona savedPersona = new Persona(
                new PersonaId(1),
                VALID_NOMBRE,
                VALID_APELLIDO,
                new Email(mixedCaseEmail.toLowerCase()),
                new Telefono(VALID_TELEFONO),
                VALID_DIRECCION
        );

        when(personaRepository.save(any(Persona.class))).thenReturn(savedPersona);

        // When
        Persona result = crearPersonaUseCase.execute(command);

        // Then
        assertThat(result.getEmail().getValue()).isEqualTo(mixedCaseEmail.toLowerCase());
    }

    @Test
    @DisplayName("Should clean telefono format when creating persona")
    void shouldCleanTelefonoFormatWhenCreatingPersona() {
        // Given
        String formattedTelefono = "(123) 456-7890";
        String expectedCleanTelefono = "1234567890";
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, formattedTelefono, VALID_DIRECCION
        );

        Persona savedPersona = new Persona(
                new PersonaId(1),
                VALID_NOMBRE,
                VALID_APELLIDO,
                new Email(VALID_EMAIL),
                new Telefono(expectedCleanTelefono),
                VALID_DIRECCION
        );

        when(personaRepository.save(any(Persona.class))).thenReturn(savedPersona);

        // When
        Persona result = crearPersonaUseCase.execute(command);

        // Then
        assertThat(result.getTelefono().getValue()).isEqualTo(expectedCleanTelefono);
    }

    @Test
    @DisplayName("Command should have correct getters")
    void commandShouldHaveCorrectGetters() {
        // Given
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        );

        // When & Then
        assertThat(command.getNombre()).isEqualTo(VALID_NOMBRE);
        assertThat(command.getApellido()).isEqualTo(VALID_APELLIDO);
        assertThat(command.getEmail()).isEqualTo(VALID_EMAIL);
        assertThat(command.getTelefono()).isEqualTo(VALID_TELEFONO);
        assertThat(command.getDireccion()).isEqualTo(VALID_DIRECCION);
    }

    @Test
    @DisplayName("Should throw exception when repository throws exception")
    void shouldThrowExceptionWhenRepositoryThrowsException() {
        // Given
        CrearPersonaUseCase.CrearPersonaCommand command = new CrearPersonaUseCase.CrearPersonaCommand(
                VALID_NOMBRE, VALID_APELLIDO, VALID_EMAIL, VALID_TELEFONO, VALID_DIRECCION
        );

        when(personaRepository.save(any(Persona.class)))
                .thenThrow(new RuntimeException("Database error"));

        // When & Then
        assertThatThrownBy(() -> crearPersonaUseCase.execute(command))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database error");
    }
}