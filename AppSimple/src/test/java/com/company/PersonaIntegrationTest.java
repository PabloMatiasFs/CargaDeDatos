package com.company;

import com.company.infrastructure.adapter.web.dto.PersonaCreateRequest;
import com.company.infrastructure.adapter.web.dto.PersonaResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DisplayName("Persona Integration Tests with Testcontainers")
class PersonaIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass")
            .withInitScript("test-schema.sql");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/personas-api/api/v1/personas";
    }

    @Test
    @DisplayName("Should create, read, update and delete persona - Full CRUD Integration Test")
    void shouldPerformFullCrudOperations() throws Exception {
        String baseUrl = getBaseUrl();

        // 1. CREATE - Crear una nueva persona
        PersonaCreateRequest createRequest = new PersonaCreateRequest(
                "Juan",
                "Pérez",
                "juan.perez@integration.test",
                "1234567890",
                "Calle Principal 123"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonaCreateRequest> createEntity = new HttpEntity<>(createRequest, headers);

        ResponseEntity<PersonaResponse> createResponse = restTemplate.postForEntity(
                baseUrl, createEntity, PersonaResponse.class);

        // Verificar creación exitosa
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        PersonaResponse createdPersona = createResponse.getBody();
        assertThat(createdPersona.getId()).isNotNull();
        assertThat(createdPersona.getNombre()).isEqualTo("Juan");
        assertThat(createdPersona.getApellido()).isEqualTo("Pérez");
        assertThat(createdPersona.getEmail()).isEqualTo("juan.perez@integration.test");
        assertThat(createdPersona.getTelefono()).isEqualTo("1234567890");
        assertThat(createdPersona.getDireccion()).isEqualTo("Calle Principal 123");
        assertThat(createdPersona.getNombreCompleto()).isEqualTo("Juan Pérez");

        Integer personaId = createdPersona.getId();

        // 2. READ - Obtener la persona por ID
        ResponseEntity<PersonaResponse> getResponse = restTemplate.getForEntity(
                baseUrl + "/" + personaId, PersonaResponse.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        PersonaResponse retrievedPersona = getResponse.getBody();
        assertThat(retrievedPersona.getId()).isEqualTo(personaId);
        assertThat(retrievedPersona.getNombre()).isEqualTo("Juan");

        // 3. READ ALL - Obtener todas las personas
        ResponseEntity<String> getAllResponse = restTemplate.getForEntity(baseUrl, String.class);
        assertThat(getAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<PersonaResponse> allPersonas = objectMapper.readValue(
                getAllResponse.getBody(), new TypeReference<List<PersonaResponse>>() {});
        assertThat(allPersonas).hasSize(1);
        assertThat(allPersonas.get(0).getId()).isEqualTo(personaId);

        // 4. UPDATE - Actualizar la persona
        PersonaCreateRequest updateRequest = new PersonaCreateRequest(
                "Carlos",
                "García",
                "carlos.garcia@integration.test",
                "0987654321",
                "Avenida Central 456"
        );

        HttpEntity<PersonaCreateRequest> updateEntity = new HttpEntity<>(updateRequest, headers);
        ResponseEntity<PersonaResponse> updateResponse = restTemplate.exchange(
                baseUrl + "/" + personaId, HttpMethod.PUT, updateEntity, PersonaResponse.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody()).isNotNull();
        PersonaResponse updatedPersona = updateResponse.getBody();
        assertThat(updatedPersona.getId()).isEqualTo(personaId);
        assertThat(updatedPersona.getNombre()).isEqualTo("Carlos");
        assertThat(updatedPersona.getApellido()).isEqualTo("García");
        assertThat(updatedPersona.getEmail()).isEqualTo("carlos.garcia@integration.test");
        assertThat(updatedPersona.getTelefono()).isEqualTo("0987654321");
        assertThat(updatedPersona.getDireccion()).isEqualTo("Avenida Central 456");

        // 5. DELETE - Eliminar la persona
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + personaId, HttpMethod.DELETE, null, Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        // 6. VERIFY DELETE - Verificar que la persona fue eliminada
        ResponseEntity<PersonaResponse> getAfterDeleteResponse = restTemplate.getForEntity(
                baseUrl + "/" + personaId, PersonaResponse.class);

        assertThat(getAfterDeleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should handle validation errors correctly")
    void shouldHandleValidationErrors() {
        String baseUrl = getBaseUrl();

        // Crear request con datos inválidos
        PersonaCreateRequest invalidRequest = new PersonaCreateRequest(
                "", // nombre vacío
                "", // apellido vacío
                "invalid-email", // email inválido
                "", // teléfono vacío
                "" // dirección vacía
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonaCreateRequest> entity = new HttpEntity<>(invalidRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, entity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("Should return 404 for non-existent persona")
    void shouldReturn404ForNonExistentPersona() {
        String baseUrl = getBaseUrl();
        Integer nonExistentId = 99999;

        ResponseEntity<PersonaResponse> response = restTemplate.getForEntity(
                baseUrl + "/" + nonExistentId, PersonaResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Should search personas by nombre")
    void shouldSearchPersonasByNombre() throws Exception {
        String baseUrl = getBaseUrl();

        // Crear algunas personas para buscar
        PersonaCreateRequest persona1 = new PersonaCreateRequest(
                "Juan", "Pérez", "juan.perez@test.com", "1111111111", "Dirección 1");
        PersonaCreateRequest persona2 = new PersonaCreateRequest(
                "Juana", "García", "juana.garcia@test.com", "2222222222", "Dirección 2");
        PersonaCreateRequest persona3 = new PersonaCreateRequest(
                "Pedro", "López", "pedro.lopez@test.com", "3333333333", "Dirección 3");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Crear las personas
        restTemplate.postForEntity(baseUrl, new HttpEntity<>(persona1, headers), PersonaResponse.class);
        restTemplate.postForEntity(baseUrl, new HttpEntity<>(persona2, headers), PersonaResponse.class);
        restTemplate.postForEntity(baseUrl, new HttpEntity<>(persona3, headers), PersonaResponse.class);

        // Buscar por nombre "Juan"
        ResponseEntity<String> searchResponse = restTemplate.getForEntity(
                baseUrl + "/buscar/nombre?nombre=Juan", String.class);

        assertThat(searchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<PersonaResponse> foundPersonas = objectMapper.readValue(
                searchResponse.getBody(), new TypeReference<List<PersonaResponse>>() {});

        // Debe encontrar tanto "Juan" como "Juana" (búsqueda parcial)
        assertThat(foundPersonas).hasSize(2);
        assertThat(foundPersonas)
                .extracting(PersonaResponse::getNombre)
                .containsExactlyInAnyOrder("Juan", "Juana");
    }

    @Test
    @DisplayName("Should search personas by apellido")
    void shouldSearchPersonasByApellido() throws Exception {
        String baseUrl = getBaseUrl();

        // Crear persona para buscar
        PersonaCreateRequest persona = new PersonaCreateRequest(
                "María", "García", "maria.garcia@test.com", "4444444444", "Dirección 4");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.postForEntity(baseUrl, new HttpEntity<>(persona, headers), PersonaResponse.class);

        // Buscar por apellido
        ResponseEntity<String> searchResponse = restTemplate.getForEntity(
                baseUrl + "/buscar/apellido?apellido=García", String.class);

        assertThat(searchResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        List<PersonaResponse> foundPersonas = objectMapper.readValue(
                searchResponse.getBody(), new TypeReference<List<PersonaResponse>>() {});

        assertThat(foundPersonas).hasSize(1);
        assertThat(foundPersonas.get(0).getApellido()).isEqualTo("García");
    }

    @Test
    @DisplayName("Should handle database constraints correctly")
    void shouldHandleDatabaseConstraintsCorrectly() {
        String baseUrl = getBaseUrl();

        // Crear primera persona
        PersonaCreateRequest persona1 = new PersonaCreateRequest(
                "Juan", "Pérez", "unique@test.com", "1234567890", "Dirección 1");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<PersonaResponse> response1 = restTemplate.postForEntity(
                baseUrl, new HttpEntity<>(persona1, headers), PersonaResponse.class);

        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        // Intentar crear segunda persona con el mismo email (si hay constraint unique)
        PersonaCreateRequest persona2 = new PersonaCreateRequest(
                "Pedro", "López", "unique@test.com", "0987654321", "Dirección 2");

        ResponseEntity<String> response2 = restTemplate.postForEntity(
                baseUrl, new HttpEntity<>(persona2, headers), String.class);

        // Dependiendo de la configuración de DB, puede ser 400 o 500
        assertThat(response2.getStatusCode().is4xxClientError() || response2.getStatusCode().is5xxServerError())
                .isTrue();
    }

    @Test
    @DisplayName("Should test email normalization")
    void shouldTestEmailNormalization() {
        String baseUrl = getBaseUrl();

        PersonaCreateRequest request = new PersonaCreateRequest(
                "Test", "User", "Test.USER@EXAMPLE.COM", "1234567890", "Test Address");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<PersonaResponse> response = restTemplate.postForEntity(
                baseUrl, new HttpEntity<>(request, headers), PersonaResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        
        // El email debe estar normalizado a lowercase
        assertThat(response.getBody().getEmail()).isEqualTo("test.user@example.com");
    }

    @Test
    @DisplayName("Should test phone number cleaning")
    void shouldTestPhoneNumberCleaning() {
        String baseUrl = getBaseUrl();

        PersonaCreateRequest request = new PersonaCreateRequest(
                "Test", "User", "test@example.com", "(123) 456-7890", "Test Address");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ResponseEntity<PersonaResponse> response = restTemplate.postForEntity(
                baseUrl, new HttpEntity<>(request, headers), PersonaResponse.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        
        // El teléfono debe estar limpio de formato
        assertThat(response.getBody().getTelefono()).isEqualTo("1234567890");
    }
}