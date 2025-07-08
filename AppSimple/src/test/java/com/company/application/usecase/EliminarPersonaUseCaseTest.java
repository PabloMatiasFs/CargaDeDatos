package com.company.application.usecase;

import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.PersonaId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EliminarPersonaUseCase Tests")
class EliminarPersonaUseCaseTest {

    @Mock
    private PersonaRepository personaRepository;

    private EliminarPersonaUseCase eliminarPersonaUseCase;

    @BeforeEach
    void setUp() {
        eliminarPersonaUseCase = new EliminarPersonaUseCase(personaRepository);
    }

    @Test
    @DisplayName("Should successfully delete existing persona")
    void shouldSuccessfullyDeleteExistingPersona() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.existsById(personaId)).thenReturn(true);

        // When
        boolean result = eliminarPersonaUseCase.execute(id);

        // Then
        assertThat(result).isTrue();
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, times(1)).deleteById(personaId);
    }

    @Test
    @DisplayName("Should return false when persona does not exist")
    void shouldReturnFalseWhenPersonaDoesNotExist() {
        // Given
        Integer id = 999;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.existsById(personaId)).thenReturn(false);

        // When
        boolean result = eliminarPersonaUseCase.execute(id);

        // Then
        assertThat(result).isFalse();
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, never()).deleteById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should throw exception when ID is invalid")
    void shouldThrowExceptionWhenIdIsInvalid() {
        // Given
        Integer invalidId = 0;

        // When & Then
        assertThatThrownBy(() -> eliminarPersonaUseCase.execute(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID de persona debe ser un entero positivo");

        // Verify repository methods were not called
        verify(personaRepository, never()).existsById(any(PersonaId.class));
        verify(personaRepository, never()).deleteById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should throw exception when ID is null")
    void shouldThrowExceptionWhenIdIsNull() {
        // Given
        Integer nullId = null;

        // When & Then
        assertThatThrownBy(() -> eliminarPersonaUseCase.execute(nullId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID de persona debe ser un entero positivo");

        // Verify repository methods were not called
        verify(personaRepository, never()).existsById(any(PersonaId.class));
        verify(personaRepository, never()).deleteById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should throw exception when ID is negative")
    void shouldThrowExceptionWhenIdIsNegative() {
        // Given
        Integer negativeId = -1;

        // When & Then
        assertThatThrownBy(() -> eliminarPersonaUseCase.execute(negativeId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID de persona debe ser un entero positivo");

        // Verify repository methods were not called
        verify(personaRepository, never()).existsById(any(PersonaId.class));
        verify(personaRepository, never()).deleteById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should pass correct PersonaId to repository methods")
    void shouldPassCorrectPersonaIdToRepositoryMethods() {
        // Given
        Integer id = 42;
        PersonaId expectedPersonaId = new PersonaId(id);
        when(personaRepository.existsById(expectedPersonaId)).thenReturn(true);

        // When
        eliminarPersonaUseCase.execute(id);

        // Then
        ArgumentCaptor<PersonaId> existsCaptor = ArgumentCaptor.forClass(PersonaId.class);
        ArgumentCaptor<PersonaId> deleteCaptor = ArgumentCaptor.forClass(PersonaId.class);

        verify(personaRepository).existsById(existsCaptor.capture());
        verify(personaRepository).deleteById(deleteCaptor.capture());

        PersonaId capturedExistsId = existsCaptor.getValue();
        PersonaId capturedDeleteId = deleteCaptor.getValue();

        assertThat(capturedExistsId.getValue()).isEqualTo(id);
        assertThat(capturedDeleteId.getValue()).isEqualTo(id);
        assertThat(capturedExistsId).isEqualTo(expectedPersonaId);
        assertThat(capturedDeleteId).isEqualTo(expectedPersonaId);
    }

    @Test
    @DisplayName("Should handle repository exception during exists check")
    void shouldHandleRepositoryExceptionDuringExistsCheck() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.existsById(personaId))
                .thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThatThrownBy(() -> eliminarPersonaUseCase.execute(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection error");

        // Verify deleteById was not called
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, never()).deleteById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should handle repository exception during delete")
    void shouldHandleRepositoryExceptionDuringDelete() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.existsById(personaId)).thenReturn(true);
        doThrow(new RuntimeException("Delete operation failed"))
                .when(personaRepository).deleteById(personaId);

        // When & Then
        assertThatThrownBy(() -> eliminarPersonaUseCase.execute(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Delete operation failed");

        // Verify both methods were called
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, times(1)).deleteById(personaId);
    }

    @Test
    @DisplayName("Should maintain correct order of operations")
    void shouldMaintainCorrectOrderOfOperations() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.existsById(personaId)).thenReturn(true);

        // When
        eliminarPersonaUseCase.execute(id);

        // Then - Verify order of calls
        InOrder inOrder = inOrder(personaRepository);
        inOrder.verify(personaRepository).existsById(personaId);
        inOrder.verify(personaRepository).deleteById(personaId);
        inOrder.verifyNoMoreInteractions();
    }

    @Test
    @DisplayName("Should work with large ID values")
    void shouldWorkWithLargeIdValues() {
        // Given
        Integer largeId = Integer.MAX_VALUE;
        PersonaId personaId = new PersonaId(largeId);
        when(personaRepository.existsById(personaId)).thenReturn(true);

        // When
        boolean result = eliminarPersonaUseCase.execute(largeId);

        // Then
        assertThat(result).isTrue();
        verify(personaRepository, times(1)).existsById(personaId);
        verify(personaRepository, times(1)).deleteById(personaId);
    }
}