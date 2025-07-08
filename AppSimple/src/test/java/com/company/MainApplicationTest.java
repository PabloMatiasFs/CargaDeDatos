package com.company;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("Application Context Smoke Test")
class MainApplicationTest {

    @Test
    @DisplayName("Should load Spring application context successfully")
    void shouldLoadSpringApplicationContextSuccessfully() {
        // This test will pass if the Spring application context loads without errors
        // It serves as a smoke test to verify basic configuration
    }
}