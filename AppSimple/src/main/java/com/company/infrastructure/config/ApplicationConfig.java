package com.company.infrastructure.config;

import com.company.application.service.PersonaApplicationService;
import com.company.domain.port.PersonaRepository;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de la aplicación
 * Configura la inyección de dependencias para la arquitectura hexagonal
 */
@Configuration
public class ApplicationConfig {

    /**
     * Configuración del servicio de aplicación
     */
    @Bean
    public PersonaApplicationService personaApplicationService(PersonaRepository personaRepository) {
        return new PersonaApplicationService(personaRepository);
    }

    /**
     * Configuración de OpenAPI/Swagger
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Personas")
                        .version("1.0.0")
                        .description("API REST para gestionar información de personas utilizando arquitectura hexagonal")
                        .license(new License()
                                .name("MIT")
                                .url("https://opensource.org/licenses/MIT")));
    }
}