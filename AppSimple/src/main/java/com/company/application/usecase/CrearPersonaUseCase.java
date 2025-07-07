package com.company.application.usecase;

import com.company.domain.entity.Persona;
import com.company.domain.port.PersonaRepository;
import com.company.domain.valueobject.Email;
import com.company.domain.valueobject.Telefono;

/**
 * Caso de uso para crear una nueva persona
 */
public class CrearPersonaUseCase {
    
    private final PersonaRepository personaRepository;

    public CrearPersonaUseCase(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    public Persona execute(CrearPersonaCommand command) {
        // Crear value objects con validación
        Email email = new Email(command.getEmail());
        Telefono telefono = new Telefono(command.getTelefono());
        
        // Crear entidad de dominio
        Persona persona = new Persona(
            command.getNombre(),
            command.getApellido(),
            email,
            telefono,
            command.getDireccion()
        );
        
        // Persistir usando el puerto
        return personaRepository.save(persona);
    }

    /**
     * Comando para crear persona - encapsula los datos de entrada
     */
    public static class CrearPersonaCommand {
        private final String nombre;
        private final String apellido;
        private final String email;
        private final String telefono;
        private final String direccion;

        public CrearPersonaCommand(String nombre, String apellido, String email, 
                                 String telefono, String direccion) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.email = email;
            this.telefono = telefono;
            this.direccion = direccion;
        }

        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getEmail() { return email; }
        public String getTelefono() { return telefono; }
        public String getDireccion() { return direccion; }
    }
}