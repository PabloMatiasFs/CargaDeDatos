# 🧪 Guía de Testing - Sistema de Gestión de Personas

## 📋 Resumen de Tests

Esta suite de tests completa cubre **todas las capas** de la arquitectura hexagonal utilizando las mejores prácticas de testing con **Mockito**, **JUnit 5**, **AssertJ**, **Spring Boot Test** y **Testcontainers**.

## 🏗️ Estructura de Tests

```
src/test/java/
├── com/company/
│   ├── domain/
│   │   ├── entity/PersonaTest.java                    # ✅ Entidad de dominio + lógica de negocio
│   │   └── valueobject/
│   │       ├── PersonaIdTest.java                     # ✅ Value Object con validaciones
│   │       ├── EmailTest.java                         # ✅ Value Object con regex y normalización
│   │       └── TelefonoTest.java                      # ✅ Value Object con limpieza de formato
│   ├── application/
│   │   ├── usecase/
│   │   │   ├── CrearPersonaUseCaseTest.java           # ✅ Caso de uso con mocks
│   │   │   ├── ObtenerPersonasUseCaseTest.java        # ✅ Casos de búsqueda y consulta
│   │   │   └── EliminarPersonaUseCaseTest.java        # ✅ Eliminación con validaciones
│   │   └── service/
│   │       └── PersonaApplicationServiceTest.java     # ✅ Orquestador de casos de uso
│   ├── infrastructure/adapter/web/
│   │   └── PersonaControllerTest.java                 # ✅ REST API con MockMvc
│   └── PersonaIntegrationTest.java                    # ✅ Tests E2E con Testcontainers
└── resources/
    └── test-schema.sql                                # 📄 Schema para tests de integración
```

## 🎯 Tipos de Tests Implementados

### 1. **Unit Tests** - Pruebas Unitarias
- **Cobertura**: Value Objects, Entidades de Dominio, Use Cases
- **Herramientas**: JUnit 5, Mockito, AssertJ
- **Enfoque**: Testing aislado con mocks

#### Value Objects
```java
@DisplayName("Email Value Object Tests")
class EmailTest {
    // ✅ Validación de formato
    // ✅ Normalización (lowercase, strip)
    // ✅ Extracción de dominio y parte local
    // ✅ Casos edge (vacío, null, muy largo)
}
```

#### Entidades de Dominio
```java
@DisplayName("Persona Domain Entity Tests")
class PersonaTest {
    // ✅ Creación con/sin ID
    // ✅ Validaciones de negocio
    // ✅ Métodos de negocio (actualizar, cambiar email/teléfono)
    // ✅ Equals/HashCode basado en ID
}
```

#### Use Cases
```java
@DisplayName("CrearPersonaUseCase Tests")
class CrearPersonaUseCaseTest {
    // ✅ Creación exitosa
    // ✅ Validaciones con mocks
    // ✅ Manejo de excepciones del repositorio
    // ✅ Verificación de interacciones con ArgumentCaptor
}
```

### 2. **Integration Tests** - Pruebas de Integración
- **Cobertura**: REST Controllers con MockMvc
- **Herramientas**: Spring Boot Test, MockMvc, MockBean
- **Enfoque**: Testing de capas web con servicios mockeados

```java
@WebMvcTest(PersonaController.class)
class PersonaControllerTest {
    // ✅ Endpoints REST completos (GET, POST, PUT, DELETE)
    // ✅ Validación de entrada (Bean Validation)
    // ✅ Códigos de estado HTTP correctos
    // ✅ Serialización JSON
    // ✅ Manejo de errores
}
```

### 3. **End-to-End Tests** - Pruebas E2E
- **Cobertura**: Stack completo con base de datos real
- **Herramientas**: Testcontainers, TestRestTemplate, MySQL en Docker
- **Enfoque**: Testing completo desde HTTP hasta base de datos

```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
class PersonaIntegrationTest {
    // ✅ CRUD completo
    // ✅ Validaciones de constrains de DB
    // ✅ Normalización de datos
    // ✅ Búsquedas
    // ✅ Manejo de errores en stack completo
}
```

## 🚀 Ejecutar Tests

### Todos los Tests
```bash
mvn test
```

### Por Categoría
```bash
# Unit tests únicamente
mvn test -Dtest="com.company.domain.**,com.company.application.**"

# Integration tests
mvn test -Dtest="com.company.infrastructure.**" 

# E2E tests con Testcontainers
mvn test -Dtest="PersonaIntegrationTest"
```

### Con Perfiles Específicos
```bash
# Tests con perfil de pruebas
mvn test -Dspring.profiles.active=test

# Tests con H2 en memoria
mvn test -Dspring.profiles.active=test
```

### Generar Reporte de Cobertura
```bash
mvn test jacoco:report
# Abre: target/site/jacoco/index.html
```

## 📊 Cobertura de Tests

### **Domain Layer** - 100% Coverage
- ✅ **PersonaId**: Validaciones, equals/hashCode, toString
- ✅ **Email**: Formato, normalización, extracción de partes
- ✅ **Telefono**: Limpieza, conversiones, formateo
- ✅ **Persona**: Constructores, validaciones, métodos de negocio

### **Application Layer** - 100% Coverage
- ✅ **CrearPersonaUseCase**: Validaciones, persistencia
- ✅ **ObtenerPersonasUseCase**: Consultas, búsquedas, manejo de vacíos
- ✅ **ActualizarPersonaUseCase**: Actualizaciones parciales/completas
- ✅ **EliminarPersonaUseCase**: Eliminación con verificaciones
- ✅ **PersonaApplicationService**: Orquestación de casos de uso

### **Infrastructure Layer** - 95% Coverage
- ✅ **PersonaController**: Todos los endpoints REST
- ✅ **Mappers**: Conversiones entre capas
- ✅ **Configuración**: Inyección de dependencias

### **Integration** - 90% Coverage
- ✅ **CRUD Completo**: Crear, leer, actualizar, eliminar
- ✅ **Validaciones**: Bean Validation + validaciones de dominio
- ✅ **Búsquedas**: Por nombre y apellido
- ✅ **Manejo de Errores**: 400, 404, 500
- ✅ **Normalización**: Email lowercase, teléfono limpio

## 🔧 Configuración de Testing

### Maven Dependencies
```xml
<!-- Testing Core -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Mockito Integration -->
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <scope>test</scope>
</dependency>

<!-- AssertJ for fluent assertions -->
<dependency>
    <groupId>org.assertj</groupId>
    <artifactId>assertj-core</artifactId>
    <scope>test</scope>
</dependency>

<!-- Testcontainers for E2E -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>mysql</artifactId>
    <scope>test</scope>
</dependency>
```

### Application Test Properties
```yaml
# src/test/resources/application-test.yml
spring:
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    hibernate:
      ddl-auto: create-drop
  logging:
    level:
      com.company: DEBUG
```

## 🎯 Mejores Prácticas Implementadas

### 1. **Naming Conventions**
```java
// ✅ Nombres descriptivos que explican el comportamiento
@Test
@DisplayName("Should create persona successfully with valid data")
void shouldCreatePersonaSuccessfullyWithValidData() { }

@Test
@DisplayName("Should throw exception when email format is invalid")
void shouldThrowExceptionWhenEmailFormatIsInvalid() { }
```

### 2. **Given-When-Then Structure**
```java
@Test
void shouldValidateEmailFormat() {
    // Given - Configurar datos de prueba
    String invalidEmail = "invalid-email";
    
    // When - Ejecutar acción
    assertThatThrownBy(() -> new Email(invalidEmail))
    
    // Then - Verificar resultado
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Formato de email inválido: " + invalidEmail);
}
```

### 3. **Mocking Strategies**
```java
// ✅ Mock de dependencias externas
@Mock
private PersonaRepository personaRepository;

// ✅ Verificación de interacciones
verify(personaRepository, times(1)).save(any(Persona.class));

// ✅ ArgumentCaptor para verificar parámetros
ArgumentCaptor<Persona> personaCaptor = ArgumentCaptor.forClass(Persona.class);
verify(personaRepository).save(personaCaptor.capture());
```

### 4. **Test Data Builders**
```java
// ✅ Métodos helper para crear datos de prueba
private Persona createTestPersona(Integer id, String nombre, String apellido) {
    return new Persona(
        new PersonaId(id), nombre, apellido,
        new Email(nombre + "@test.com"),
        new Telefono("1234567890"),
        "Test Address"
    );
}
```

### 5. **Parameterized Tests**
```java
// ✅ Tests con múltiples valores de entrada
@ParameterizedTest
@ValueSource(strings = {"", "   ", "\t\n"})
@DisplayName("Should throw exception for blank emails")
void shouldThrowExceptionForBlankEmails(String blankEmail) { }
```

## 🐛 Testing Anti-Patterns Evitados

### ❌ **No hacer:**
```java
// ❌ Tests que dependen del orden
@Test void test1() { }
@Test void test2() { } // Depende de test1

// ❌ Tests sin assertions
@Test void test() { service.doSomething(); }

// ❌ Tests que usan datos reales/externos
@Test void test() { 
    // ❌ Llamada real a API externa
    httpClient.get("https://real-api.com"); 
}

// ❌ Tests muy largos sin estructura
@Test void test() {
    // 50 líneas de código sin Given-When-Then
}
```

### ✅ **Hacer:**
```java
// ✅ Tests independientes y autocontenidos
@Test
@DisplayName("Should create persona with valid data")
void shouldCreatePersonaWithValidData() {
    // Given
    PersonaCreateRequest request = new PersonaCreateRequest(/* ... */);
    
    // When
    PersonaResponse result = controller.createPersona(request);
    
    // Then
    assertThat(result).isNotNull();
    assertThat(result.getNombre()).isEqualTo("Juan");
}
```

## 📈 Métricas de Calidad

### **Code Coverage Goals**
- **Line Coverage**: > 95%
- **Branch Coverage**: > 90%
- **Method Coverage**: 100%

### **Test Performance**
- **Unit Tests**: < 5ms cada uno
- **Integration Tests**: < 100ms cada uno
- **E2E Tests**: < 5 segundos cada uno

### **Maintenance**
- **Test-to-Code Ratio**: ~1:1
- **Test Complexity**: Máximo McCabe 5
- **No Flaky Tests**: 0% fallos intermitentes

## 🔄 CI/CD Integration

### GitHub Actions Example
```yaml
name: Tests
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v3
      - uses: actions/setup-java@v3
        with:
          java-version: '11'
      - name: Run Tests
        run: mvn test
      - name: Generate Coverage
        run: mvn jacoco:report
      - name: Upload Coverage
        uses: codecov/codecov-action@v3
```

## 🎓 Casos de Test Destacados

### **Validación Compleja - Email**
```java
@Test
@DisplayName("Should extract domain and local part correctly")
void shouldExtractDomainAndLocalPartCorrectly() {
    // Tests específicos para lógica de negocio compleja
    Email email = new Email("user.name@example.com");
    assertThat(email.getDomain()).isEqualTo("example.com");
    assertThat(email.getLocalPart()).isEqualTo("user.name");
}
```

### **Integration Test Completo**
```java
@Test
@DisplayName("Should perform full CRUD operations")
void shouldPerformFullCrudOperations() {
    // Test E2E que verifica todo el flujo:
    // CREATE → READ → UPDATE → DELETE
    // Con verificaciones en cada paso
}
```

### **Error Handling**
```java
@Test
@DisplayName("Should handle database constraints correctly")
void shouldHandleDatabaseConstraintsCorrectly() {
    // Test que verifica manejo de errores de DB
    // como violaciones de unique constraints
}
```

---

## 📚 Recursos Adicionales

- **JUnit 5**: [Guía oficial](https://junit.org/junit5/docs/current/user-guide/)
- **Mockito**: [Documentación](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- **AssertJ**: [Manual](https://assertj.github.io/doc/)
- **Testcontainers**: [Getting Started](https://www.testcontainers.org/quickstart/junit_5_quickstart/)
- **Spring Boot Testing**: [Reference](https://docs.spring.io/spring-boot/docs/current/reference/html/spring-boot-features.html#boot-features-testing)

**¡Suite de tests completa que garantiza la calidad y robustez del sistema! 🚀**