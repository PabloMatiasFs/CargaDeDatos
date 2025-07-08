package com.company.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Email Value Object Tests")
class EmailTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "test@example.com",
        "user123@domain.co.uk",
        "first.last@subdomain.domain.com",
        "user+tag@example.org",
        "user_name@test-domain.com",
        "123@example.com"
    })
    @DisplayName("Should create Email with valid email addresses")
    void shouldCreateEmailWithValidEmailAddresses(String validEmail) {
        // When
        Email email = new Email(validEmail);

        // Then
        assertThat(email.getValue()).isEqualTo(validEmail.toLowerCase());
    }

    @Test
    @DisplayName("Should normalize email to lowercase")
    void shouldNormalizeEmailToLowercase() {
        // Given
        String mixedCaseEmail = "Test.User@EXAMPLE.COM";
        String expectedLowercase = "test.user@example.com";

        // When
        Email email = new Email(mixedCaseEmail);

        // Then
        assertThat(email.getValue()).isEqualTo(expectedLowercase);
    }

    @Test
    @DisplayName("Should strip whitespace from email")
    void shouldStripWhitespaceFromEmail() {
        // Given
        String emailWithSpaces = "  test@example.com  ";
        String expectedClean = "test@example.com";

        // When
        Email email = new Email(emailWithSpaces);

        // Then
        assertThat(email.getValue()).isEqualTo(expectedClean);
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "   ",
        "\t\n"
    })
    @DisplayName("Should throw exception for blank or empty emails")
    void shouldThrowExceptionForBlankOrEmptyEmails(String blankEmail) {
        // When & Then
        assertThatThrownBy(() -> new Email(blankEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email no puede estar vacío");
    }

    @Test
    @DisplayName("Should throw exception for null email")
    void shouldThrowExceptionForNullEmail() {
        // Given
        String nullEmail = null;

        // When & Then
        assertThatThrownBy(() -> new Email(nullEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email no puede estar vacío");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "invalid-email",
        "@example.com",
        "user@",
        "user@domain",
        "user.example.com",
        "user @example.com",
        "user@exam ple.com",
        "user@@example.com",
        ".user@example.com",
        "user.@example.com",
        "user@.example.com",
        "user@example..com"
    })
    @DisplayName("Should throw exception for invalid email formats")
    void shouldThrowExceptionForInvalidEmailFormats(String invalidEmail) {
        // When & Then
        assertThatThrownBy(() -> new Email(invalidEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Formato de email inválido: " + invalidEmail);
    }

    @Test
    @DisplayName("Should throw exception when email exceeds maximum length")
    void shouldThrowExceptionWhenEmailExceedsMaximumLength() {
        // Given
        String longEmail = "a".repeat(40) + "@example.com"; // Total > 45 characters

        // When & Then
        assertThatThrownBy(() -> new Email(longEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email no puede exceder 45 caracteres");
    }

    @Test
    @DisplayName("Should extract domain correctly")
    void shouldExtractDomainCorrectly() {
        // Given
        String emailValue = "user@example.com";
        Email email = new Email(emailValue);

        // When
        String domain = email.getDomain();

        // Then
        assertThat(domain).isEqualTo("example.com");
    }

    @Test
    @DisplayName("Should extract local part correctly")
    void shouldExtractLocalPartCorrectly() {
        // Given
        String emailValue = "user.name@example.com";
        Email email = new Email(emailValue);

        // When
        String localPart = email.getLocalPart();

        // Then
        assertThat(localPart).isEqualTo("user.name");
    }

    @Test
    @DisplayName("Should be equal when emails have same value")
    void shouldBeEqualWhenEmailsHaveSameValue() {
        // Given
        String emailValue = "test@example.com";
        Email email1 = new Email(emailValue);
        Email email2 = new Email(emailValue);

        // When & Then
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    @DisplayName("Should be equal when emails are normalized to same value")
    void shouldBeEqualWhenEmailsAreNormalizedToSameValue() {
        // Given
        Email email1 = new Email("Test@Example.COM");
        Email email2 = new Email("  test@example.com  ");

        // When & Then
        assertThat(email1).isEqualTo(email2);
        assertThat(email1.hashCode()).isEqualTo(email2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when emails have different values")
    void shouldNotBeEqualWhenEmailsHaveDifferentValues() {
        // Given
        Email email1 = new Email("user1@example.com");
        Email email2 = new Email("user2@example.com");

        // When & Then
        assertThat(email1).isNotEqualTo(email2);
        assertThat(email1.hashCode()).isNotEqualTo(email2.hashCode());
    }

    @Test
    @DisplayName("Should have toString that returns email value")
    void shouldHaveToStringThatReturnsEmailValue() {
        // Given
        String emailValue = "test@example.com";
        Email email = new Email(emailValue);

        // When
        String toString = email.toString();

        // Then
        assertThat(toString).isEqualTo(emailValue);
    }
}