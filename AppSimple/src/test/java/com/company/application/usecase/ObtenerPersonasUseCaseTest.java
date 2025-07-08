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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ObtenerPersonasUseCase Tests")
class ObtenerPersonasUseCaseTest {

    @Mock
    private PersonaRepository personaRepository;

    private ObtenerPersonasUseCase obtenerPersonasUseCase;

    private Persona persona1;
    private Persona persona2;

    @BeforeEach
    void setUp() {
        obtenerPersonasUseCase = new ObtenerPersonasUseCase(personaRepository);

        // Setup test data
        persona1 = new Persona(
                new PersonaId(1),
                "Juan",
                "Pérez",
                new Email("juan.perez@email.com"),
                new Telefono("1234567890"),
                "Calle Principal 123"
        );

        persona2 = new Persona(
                new PersonaId(2),
                "María",
                "García",
                new Email("maria.garcia@email.com"),
                new Telefono("0987654321"),
                "Avenida Central 456"
        );
    }

    @Test
    @DisplayName("Should return all personas when obtaining all")
    void shouldReturnAllPersonasWhenObtainingAll() {
        // Given
        List<Persona> expectedPersonas = List.of(persona1, persona2);
        when(personaRepository.findAll()).thenReturn(expectedPersonas);

        // When
        List<Persona> result = obtenerPersonasUseCase.obtenerTodas();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(persona1, persona2);
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no personas exist")
    void shouldReturnEmptyListWhenNoPersonasExist() {
        // Given
        when(personaRepository.findAll()).thenReturn(List.of());

        // When
        List<Persona> result = obtenerPersonasUseCase.obtenerTodas();

        // Then
        assertThat(result).isEmpty();
        verify(personaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return persona when found by valid ID")
    void shouldReturnPersonaWhenFoundByValidId() {
        // Given
        Integer id = 1;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.findById(personaId)).thenReturn(Optional.of(persona1));

        // When
        Optional<Persona> result = obtenerPersonasUseCase.obtenerPorId(id);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(persona1);
        verify(personaRepository, times(1)).findById(personaId);
    }

    @Test
    @DisplayName("Should return empty optional when persona not found by ID")
    void shouldReturnEmptyOptionalWhenPersonaNotFoundById() {
        // Given
        Integer id = 999;
        PersonaId personaId = new PersonaId(id);
        when(personaRepository.findById(personaId)).thenReturn(Optional.empty());

        // When
        Optional<Persona> result = obtenerPersonasUseCase.obtenerPorId(id);

        // Then
        assertThat(result).isEmpty();
        verify(personaRepository, times(1)).findById(personaId);
    }

    @Test
    @DisplayName("Should throw exception when searching by invalid ID")
    void shouldThrowExceptionWhenSearchingByInvalidId() {
        // Given
        Integer invalidId = 0;

        // When & Then
        assertThatThrownBy(() -> obtenerPersonasUseCase.obtenerPorId(invalidId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID de persona debe ser un entero positivo");

        // Verify repository was not called
        verify(personaRepository, never()).findById(any(PersonaId.class));
    }

    @Test
    @DisplayName("Should return personas when searching by valid nombre")
    void shouldReturnPersonasWhenSearchingByValidNombre() {
        // Given
        String nombre = "Juan";
        List<Persona> expectedPersonas = List.of(persona1);
        when(personaRepository.findByNombreContaining(nombre)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorNombre(nombre);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(persona1);
        verify(personaRepository, times(1)).findByNombreContaining(nombre);
    }

    @Test
    @DisplayName("Should strip whitespace from nombre before searching")
    void shouldStripWhitespaceFromNombreBeforeSearching() {
        // Given
        String nombreWithSpaces = "  Juan  ";
        String expectedCleanNombre = "Juan";
        List<Persona> expectedPersonas = List.of(persona1);
        when(personaRepository.findByNombreContaining(expectedCleanNombre)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorNombre(nombreWithSpaces);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(persona1);
        verify(personaRepository, times(1)).findByNombreContaining(expectedCleanNombre);
    }

    @Test
    @DisplayName("Should return empty list when searching by null or blank nombre")
    void shouldReturnEmptyListWhenSearchingByNullOrBlankNombre() {
        // Test null nombre
        List<Persona> result1 = obtenerPersonasUseCase.buscarPorNombre(null);
        assertThat(result1).isEmpty();

        // Test empty nombre
        List<Persona> result2 = obtenerPersonasUseCase.buscarPorNombre("");
        assertThat(result2).isEmpty();

        // Test blank nombre
        List<Persona> result3 = obtenerPersonasUseCase.buscarPorNombre("   ");
        assertThat(result3).isEmpty();

        // Verify repository was never called
        verify(personaRepository, never()).findByNombreContaining(anyString());
    }

    @Test
    @DisplayName("Should return empty list when no personas match nombre")
    void shouldReturnEmptyListWhenNoPersonasMatchNombre() {
        // Given
        String nombre = "NoExiste";
        when(personaRepository.findByNombreContaining(nombre)).thenReturn(List.of());

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorNombre(nombre);

        // Then
        assertThat(result).isEmpty();
        verify(personaRepository, times(1)).findByNombreContaining(nombre);
    }

    @Test
    @DisplayName("Should return personas when searching by valid apellido")
    void shouldReturnPersonasWhenSearchingByValidApellido() {
        // Given
        String apellido = "García";
        List<Persona> expectedPersonas = List.of(persona2);
        when(personaRepository.findByApellidoContaining(apellido)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorApellido(apellido);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(persona2);
        verify(personaRepository, times(1)).findByApellidoContaining(apellido);
    }

    @Test
    @DisplayName("Should strip whitespace from apellido before searching")
    void shouldStripWhitespaceFromApellidoBeforeSearching() {
        // Given
        String apellidoWithSpaces = "  García  ";
        String expectedCleanApellido = "García";
        List<Persona> expectedPersonas = List.of(persona2);
        when(personaRepository.findByApellidoContaining(expectedCleanApellido)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorApellido(apellidoWithSpaces);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).containsExactly(persona2);
        verify(personaRepository, times(1)).findByApellidoContaining(expectedCleanApellido);
    }

    @Test
    @DisplayName("Should return empty list when searching by null or blank apellido")
    void shouldReturnEmptyListWhenSearchingByNullOrBlankApellido() {
        // Test null apellido
        List<Persona> result1 = obtenerPersonasUseCase.buscarPorApellido(null);
        assertThat(result1).isEmpty();

        // Test empty apellido
        List<Persona> result2 = obtenerPersonasUseCase.buscarPorApellido("");
        assertThat(result2).isEmpty();

        // Test blank apellido
        List<Persona> result3 = obtenerPersonasUseCase.buscarPorApellido("   ");
        assertThat(result3).isEmpty();

        // Verify repository was never called
        verify(personaRepository, never()).findByApellidoContaining(anyString());
    }

    @Test
    @DisplayName("Should return empty list when no personas match apellido")
    void shouldReturnEmptyListWhenNoPersonasMatchApellido() {
        // Given
        String apellido = "NoExiste";
        when(personaRepository.findByApellidoContaining(apellido)).thenReturn(List.of());

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorApellido(apellido);

        // Then
        assertThat(result).isEmpty();
        verify(personaRepository, times(1)).findByApellidoContaining(apellido);
    }

    @Test
    @DisplayName("Should return multiple personas when multiple matches found")
    void shouldReturnMultiplePersonasWhenMultipleMatchesFound() {
        // Given - Both personas have names starting with similar letters
        String searchTerm = "a"; // Could match "Juan" and "María"
        List<Persona> expectedPersonas = List.of(persona1, persona2);
        when(personaRepository.findByNombreContaining(searchTerm)).thenReturn(expectedPersonas);

        // When
        List<Persona> result = obtenerPersonasUseCase.buscarPorNombre(searchTerm);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(persona1, persona2);
        verify(personaRepository, times(1)).findByNombreContaining(searchTerm);
    }

    @Test
    @DisplayName("Should handle repository exceptions gracefully")
    void shouldHandleRepositoryExceptionsGracefully() {
        // Given
        when(personaRepository.findAll()).thenThrow(new RuntimeException("Database connection error"));

        // When & Then
        assertThatThrownBy(() -> obtenerPersonasUseCase.obtenerTodas())
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Database connection error");
    }
}