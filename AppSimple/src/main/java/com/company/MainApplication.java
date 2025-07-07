package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Aplicación principal del sistema de gestión de personas
 * Migrada a Java 11 con arquitectura hexagonal
 */
@SpringBootApplication
public class MainApplication {

    private static final Logger log = LoggerFactory.getLogger(MainApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MainApplication.class);
        Environment env = app.run(args).getEnvironment();
        
        String protocol = "http";
        if (env.getProperty("server.ssl.key-store") != null) {
            protocol = "https";
        }
        
        String serverPort = env.getProperty("server.port");
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null || contextPath.isBlank()) {
            contextPath = "";
        }
        
        log.info("\n\n" +
            "----------------------------------------------------------\n" +
            "\tAplicación '{}' ejecutándose! URLs de acceso:\n" +
            "\tLocal: \t\t{}://localhost:{}{}\n" +
            "\tSwagger: \t{}://localhost:{}{}/swagger-ui.html\n" +
            "\tProfiles: \t{}\n" +
            "----------------------------------------------------------",
            env.getProperty("spring.application.name"),
            protocol,
            serverPort,
            contextPath,
            protocol,
            serverPort,
            contextPath,
            Arrays.toString(env.getActiveProfiles()));
    }
}
