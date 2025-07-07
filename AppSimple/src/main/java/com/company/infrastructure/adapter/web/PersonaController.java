package com.company.infrastructure.adapter.web;

import com.company.application.service.PersonaApplicationService;
import com.company.domain.entity.Persona;
import com.company.infrastructure.adapter.web.dto.PersonaCreateRequest;
import com.company.infrastructure.adapter.web.dto.PersonaResponse;
import com.company.infrastructure.adapter.web.dto.PersonaUpdateRequest;
import com.company.infrastructure.adapter.web.mapper.PersonaWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestión de personas
 * Adaptador web que expone las operaciones de negocio
 */
@RestController
@RequestMapping("/api/v1/personas")
@Tag(name = "Personas", description = "API para gestión de personas")
@CrossOrigin(origins = "*")
public class PersonaController {

    private static final Logger log = LoggerFactory.getLogger(PersonaController.class);

    private final PersonaApplicationService personaApplicationService;
    private final PersonaWebMapper webMapper;

    public PersonaController(PersonaApplicationService personaApplicationService, PersonaWebMapper webMapper) {
        this.personaApplicationService = personaApplicationService;
        this.webMapper = webMapper;
    }

    @GetMapping
    @Operation(summary = "Obtener todas las personas", description = "Devuelve una lista de todas las personas registradas")
    @ApiResponse(responseCode = "200", description = "Lista de personas obtenida exitosamente")
    public ResponseEntity<List<PersonaResponse>> obtenerTodasLasPersonas() {
        log.info("Obteniendo todas las personas");
        List<Persona> personas = personaApplicationService.obtenerTodasLasPersonas();
        List<PersonaResponse> response = webMapper.toResponseList(personas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener persona por ID", description = "Devuelve una persona específica por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persona encontrada"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<PersonaResponse> obtenerPersonaPorId(
            @Parameter(description = "ID de la persona") @PathVariable Integer id) {
        log.info("Obteniendo persona con ID: {}", id);
        
        Optional<Persona> persona = personaApplicationService.obtenerPersonaPorId(id);
        return persona
                .map(p -> ResponseEntity.ok(webMapper.toResponse(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva persona", description = "Crea una nueva persona con la información proporcionada")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Persona creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<PersonaResponse> crearPersona(@Valid @RequestBody PersonaCreateRequest request) {
        log.info("Creando nueva persona: {}", request);
        
        try {
            Persona persona = personaApplicationService.crearPersona(
                    request.getNombre(),
                    request.getApellido(),
                    request.getEmail(),
                    request.getTelefono(),
                    request.getDireccion()
            );
            PersonaResponse response = webMapper.toResponse(persona);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            log.error("Error validando datos de persona: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar persona", description = "Actualiza la información de una persona existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Persona actualizada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    public ResponseEntity<PersonaResponse> actualizarPersona(
            @Parameter(description = "ID de la persona") @PathVariable Integer id,
            @Valid @RequestBody PersonaUpdateRequest request) {
        log.info("Actualizando persona con ID: {}, datos: {}", id, request);
        
        try {
            Optional<Persona> persona = personaApplicationService.actualizarPersona(
                    id,
                    request.getNombre(),
                    request.getApellido(),
                    request.getEmail(),
                    request.getTelefono(),
                    request.getDireccion()
            );
            return persona
                    .map(p -> ResponseEntity.ok(webMapper.toResponse(p)))
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            log.error("Error validando datos de persona: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar persona", description = "Elimina una persona por su ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Persona eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Persona no encontrada")
    })
    public ResponseEntity<Void> eliminarPersona(
            @Parameter(description = "ID de la persona") @PathVariable Integer id) {
        log.info("Eliminando persona con ID: {}", id);
        
        boolean eliminada = personaApplicationService.eliminarPersona(id);
        return eliminada ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/buscar/nombre")
    @Operation(summary = "Buscar por nombre", description = "Busca personas por nombre (búsqueda parcial)")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    public ResponseEntity<List<PersonaResponse>> buscarPorNombre(
            @Parameter(description = "Nombre a buscar") @RequestParam String nombre) {
        log.info("Buscando personas por nombre: {}", nombre);
        
        List<Persona> personas = personaApplicationService.buscarPersonasPorNombre(nombre);
        List<PersonaResponse> response = webMapper.toResponseList(personas);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/buscar/apellido")
    @Operation(summary = "Buscar por apellido", description = "Busca personas por apellido (búsqueda parcial)")
    @ApiResponse(responseCode = "200", description = "Búsqueda completada")
    public ResponseEntity<List<PersonaResponse>> buscarPorApellido(
            @Parameter(description = "Apellido a buscar") @RequestParam String apellido) {
        log.info("Buscando personas por apellido: {}", apellido);
        
        List<Persona> personas = personaApplicationService.buscarPersonasPorApellido(apellido);
        List<PersonaResponse> response = webMapper.toResponseList(personas);
        return ResponseEntity.ok(response);
    }
}