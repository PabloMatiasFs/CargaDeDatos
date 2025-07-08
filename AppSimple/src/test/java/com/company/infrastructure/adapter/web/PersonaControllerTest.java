package com.company.infrastructure.adapter.web;

import com.company.application.service.PersonaApplicationService;
import com.company.domain.entity.Persona;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;
import com.company.infrastructure.adapter.web.dto.PersonaCreateRequest;
import com.company.infrastructure.adapter.web.dto.PersonaUpdateRequest;
import com.company.infrastructure.adapter.web.mapper.PersonaWebMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PersonaController.class)
@DisplayName("PersonaController REST API Tests")
class PersonaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PersonaApplicationService personaApplicationService;

    @MockBean
    private PersonaWebMapper personaWebMapper;

    private static final String BASE_URL = "/api/v1/personas";

    @Test
    @DisplayName("GET /api/v1/personas should return all personas")
    void shouldReturnAllPersonas() throws Exception {
        // Given
        Persona persona1 = createTestPersona(1, "Juan", "Pérez");
        Persona persona2 = createTestPersona(2, "María", "García");
        List<Persona> personas = List.of(persona1, persona2);

        when(personaApplicationService.obtenerTodasLasPersonas()).thenReturn(personas);
        when(personaWebMapper.toResponseList(personas)).thenReturn(List.of(
            createPersonaResponse(1, "Juan", "Pérez"),
            createPersonaResponse(2, "María", "García")
        ));

        // When & Then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"))
                .andExpect(jsonPath("$[0].apellido").value("Pérez"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].nombre").value("María"))
                .andExpect(jsonPath("$[1].apellido").value("García"));

        verify(personaApplicationService, times(1)).obtenerTodasLasPersonas();
        verify(personaWebMapper, times(1)).toResponseList(personas);
    }

    @Test
    @DisplayName("GET /api/v1/personas should return empty array when no personas exist")
    void shouldReturnEmptyArrayWhenNoPersonasExist() throws Exception {
        // Given
        when(personaApplicationService.obtenerTodasLasPersonas()).thenReturn(List.of());
        when(personaWebMapper.toResponseList(List.of())).thenReturn(List.of());

        // When & Then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("GET /api/v1/personas/{id} should return persona when found")
    void shouldReturnPersonaWhenFoundById() throws Exception {
        // Given
        Integer id = 1;
        Persona persona = createTestPersona(id, "Juan", "Pérez");
        com.company.infrastructure.adapter.web.dto.PersonaResponse response = createPersonaResponse(id, "Juan", "Pérez");

        when(personaApplicationService.obtenerPersonaPorId(id)).thenReturn(Optional.of(persona));
        when(personaWebMapper.toResponse(persona)).thenReturn(response);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"));

        verify(personaApplicationService, times(1)).obtenerPersonaPorId(id);
        verify(personaWebMapper, times(1)).toResponse(persona);
    }

    @Test
    @DisplayName("GET /api/v1/personas/{id} should return 404 when persona not found")
    void shouldReturn404WhenPersonaNotFound() throws Exception {
        // Given
        Integer id = 999;
        when(personaApplicationService.obtenerPersonaPorId(id)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(personaApplicationService, times(1)).obtenerPersonaPorId(id);
        verify(personaWebMapper, never()).toResponse(any(Persona.class));
    }

    @Test
    @DisplayName("POST /api/v1/personas should create persona with valid data")
    void shouldCreatePersonaWithValidData() throws Exception {
        // Given
        PersonaCreateRequest request = new PersonaCreateRequest(
                "Juan", "Pérez", "juan.perez@email.com", "1234567890", "Calle Principal 123"
        );
        
        Persona createdPersona = createTestPersona(1, "Juan", "Pérez");
        com.company.infrastructure.adapter.web.dto.PersonaResponse response = createPersonaResponse(1, "Juan", "Pérez");

        when(personaApplicationService.crearPersona(
                request.getNombre(), request.getApellido(), request.getEmail(), 
                request.getTelefono(), request.getDireccion()
        )).thenReturn(createdPersona);
        when(personaWebMapper.toResponse(createdPersona)).thenReturn(response);

        // When & Then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Juan"))
                .andExpect(jsonPath("$.apellido").value("Pérez"));

        verify(personaApplicationService, times(1)).crearPersona(
                request.getNombre(), request.getApellido(), request.getEmail(),
                request.getTelefono(), request.getDireccion()
        );
        verify(personaWebMapper, times(1)).toResponse(createdPersona);
    }

    @Test
    @DisplayName("POST /api/v1/personas should return 400 with invalid email")
    void shouldReturn400WithInvalidEmail() throws Exception {
        // Given
        PersonaCreateRequest request = new PersonaCreateRequest(
                "Juan", "Pérez", "invalid-email", "1234567890", "Calle Principal 123"
        );

        when(personaApplicationService.crearPersona(
                anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenThrow(new IllegalArgumentException("Formato de email inválido"));

        // When & Then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/personas should return 400 with validation errors")
    void shouldReturn400WithValidationErrors() throws Exception {
        // Given - Invalid request with empty fields
        PersonaCreateRequest request = new PersonaCreateRequest(
                "", "", "", "", ""
        );

        // When & Then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // Verify service was not called due to validation failure
        verify(personaApplicationService, never()).crearPersona(
                anyString(), anyString(), anyString(), anyString(), anyString()
        );
    }

    @Test
    @DisplayName("PUT /api/v1/personas/{id} should update persona with valid data")
    void shouldUpdatePersonaWithValidData() throws Exception {
        // Given
        Integer id = 1;
        PersonaUpdateRequest request = new PersonaUpdateRequest(
                "Carlos", "García", "carlos.garcia@email.com", "0987654321", "Avenida Central 456"
        );
        
        Persona updatedPersona = createTestPersona(id, "Carlos", "García");
        com.company.infrastructure.adapter.web.dto.PersonaResponse response = createPersonaResponse(id, "Carlos", "García");

        when(personaApplicationService.actualizarPersona(
                eq(id), eq(request.getNombre()), eq(request.getApellido()), 
                eq(request.getEmail()), eq(request.getTelefono()), eq(request.getDireccion())
        )).thenReturn(Optional.of(updatedPersona));
        when(personaWebMapper.toResponse(updatedPersona)).thenReturn(response);

        // When & Then
        mockMvc.perform(put(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.nombre").value("Carlos"))
                .andExpect(jsonPath("$.apellido").value("García"));
    }

    @Test
    @DisplayName("PUT /api/v1/personas/{id} should return 404 when persona not found")
    void shouldReturn404WhenUpdatingNonExistentPersona() throws Exception {
        // Given
        Integer id = 999;
        PersonaUpdateRequest request = new PersonaUpdateRequest(
                "Carlos", "García", "carlos.garcia@email.com", "0987654321", "Avenida Central 456"
        );

        when(personaApplicationService.actualizarPersona(
                eq(id), anyString(), anyString(), anyString(), anyString(), anyString()
        )).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put(BASE_URL + "/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/v1/personas/{id} should delete existing persona")
    void shouldDeleteExistingPersona() throws Exception {
        // Given
        Integer id = 1;
        when(personaApplicationService.eliminarPersona(id)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNoContent());

        verify(personaApplicationService, times(1)).eliminarPersona(id);
    }

    @Test
    @DisplayName("DELETE /api/v1/personas/{id} should return 404 when persona not found")
    void shouldReturn404WhenDeletingNonExistentPersona() throws Exception {
        // Given
        Integer id = 999;
        when(personaApplicationService.eliminarPersona(id)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/{id}", id))
                .andExpect(status().isNotFound());

        verify(personaApplicationService, times(1)).eliminarPersona(id);
    }

    @Test
    @DisplayName("GET /api/v1/personas/buscar/nombre should search by nombre")
    void shouldSearchByNombre() throws Exception {
        // Given
        String nombre = "Juan";
        Persona persona = createTestPersona(1, "Juan", "Pérez");
        List<Persona> personas = List.of(persona);
        List<com.company.infrastructure.adapter.web.dto.PersonaResponse> responses = List.of(createPersonaResponse(1, "Juan", "Pérez"));

        when(personaApplicationService.buscarPersonasPorNombre(nombre)).thenReturn(personas);
        when(personaWebMapper.toResponseList(personas)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/buscar/nombre")
                .param("nombre", nombre))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Juan"));

        verify(personaApplicationService, times(1)).buscarPersonasPorNombre(nombre);
    }

    @Test
    @DisplayName("GET /api/v1/personas/buscar/apellido should search by apellido")
    void shouldSearchByApellido() throws Exception {
        // Given
        String apellido = "García";
        Persona persona = createTestPersona(2, "María", "García");
        List<Persona> personas = List.of(persona);
        List<com.company.infrastructure.adapter.web.dto.PersonaResponse> responses = List.of(createPersonaResponse(2, "María", "García"));

        when(personaApplicationService.buscarPersonasPorApellido(apellido)).thenReturn(personas);
        when(personaWebMapper.toResponseList(personas)).thenReturn(responses);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/buscar/apellido")
                .param("apellido", apellido))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].apellido").value("García"));

        verify(personaApplicationService, times(1)).buscarPersonasPorApellido(apellido);
    }

    // Helper methods
    private Persona createTestPersona(Integer id, String nombre, String apellido) {
        return new Persona(
                new PersonaId(id),
                nombre,
                apellido,
                new Email(nombre.toLowerCase() + "." + apellido.toLowerCase() + "@email.com"),
                new Telefono("1234567890"),
                "Calle Principal 123"
        );
    }

    private com.company.infrastructure.adapter.web.dto.PersonaResponse createPersonaResponse(
            Integer id, String nombre, String apellido) {
        com.company.infrastructure.adapter.web.dto.PersonaResponse response = new com.company.infrastructure.adapter.web.dto.PersonaResponse();
        response.setId(id);
        response.setNombre(nombre);
        response.setApellido(apellido);
        response.setEmail(nombre.toLowerCase() + "." + apellido.toLowerCase() + "@email.com");
        response.setTelefono("1234567890");
        response.setDireccion("Calle Principal 123");
        response.setNombreCompleto(nombre + " " + apellido);
        return response;
    }
}