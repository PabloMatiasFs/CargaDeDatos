package com.company.application.usecase;

import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.PersonaId;
import com.company.domain.valueobject.Telefono;

import java.util.Optional;

/**
 * Caso de uso para actualizar información de una persona
 */
public class ActualizarPersonaUseCase {
    
    private final PersonaRepository personaRepository;

    public ActualizarPersonaUseCase(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Optional<Persona> execute(ActualizarPersonaCommand command) {
        PersonaId personaId = new PersonaId(command.getId());
        
        return personaRepository.findById(personaId)
            .map(persona -> {
                // Actualizar información personal básica
                if (command.getNombre() != null || command.getApellido() != null || command.getDireccion() != null) {
                    persona.actualizarInformacionPersonal(
                        command.getNombre() != null ? command.getNombre() : persona.getNombre(),
                        command.getApellido() != null ? command.getApellido() : persona.getApellido(),
                        command.getDireccion() != null ? command.getDireccion() : persona.getDireccion()
                    );
                }
                
                // Actualizar email si se proporciona
                if (command.getEmail() != null && !command.getEmail().isBlank()) {
                    Email nuevoEmail = new Email(command.getEmail());
                    persona.cambiarEmail(nuevoEmail);
                }
                
                // Actualizar teléfono si se proporciona
                if (command.getTelefono() != null && !command.getTelefono().isBlank()) {
                    Telefono nuevoTelefono = new Telefono(command.getTelefono());
                    persona.cambiarTelefono(nuevoTelefono);
                }
                
                return personaRepository.update(persona);
            });
    }

    /**
     * Comando para actualizar persona - permite actualizaciones parciales
     */
    public static class ActualizarPersonaCommand {
        private final Integer id;
        private final String nombre;
        private final String apellido;
        private final String email;
        private final String telefono;
        private final String direccion;

        public ActualizarPersonaCommand(Integer id, String nombre, String apellido, 
                                      String email, String telefono, String direccion) {
            this.id = id;
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.telefono = telefono;
            this.direccion = direccion;
        }

        public Integer getId() { return id; }
        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getEmail() { return email; }
        public String getTelefono() { return telefono; }
        public String getDireccion() { return direccion; }
    }
}