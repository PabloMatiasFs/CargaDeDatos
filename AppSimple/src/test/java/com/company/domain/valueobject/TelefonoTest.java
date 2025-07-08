package com.company.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Telefono Value Object Tests")
class TelefonoTest {

    @ParameterizedTest
    @ValueSource(strings = {
        "1234567890",
        "12345678901",
        "123456789012345",
        "+1234567890",
        "+521234567890"
    })
    @DisplayName("Should create Telefono with valid phone numbers")
    void shouldCreateTelefonoWithValidPhoneNumbers(String validPhone) {
        // When
        Telefono telefono = new Telefono(validPhone);

        // Then
        assertThat(telefono.getValue()).isEqualTo(validPhone.replaceAll("[\\s\\-\\(\\)]", ""));
    }

    @Test
    @DisplayName("Should clean phone number removing spaces, dashes and parentheses")
    void shouldCleanPhoneNumberRemovingSpacesDashesAndParentheses() {
        // Given
        String phoneWithFormatting = "(123) 456-7890";
        String expectedClean = "1234567890";

        // When
        Telefono telefono = new Telefono(phoneWithFormatting);

        // Then
        assertThat(telefono.getValue()).isEqualTo(expectedClean);
    }

    @Test
    @DisplayName("Should handle various phone formatting patterns")
    void shouldHandleVariousPhoneFormattingPatterns() {
        // Given
        String[] formattedPhones = {
            "123 456 7890",
            "123-456-7890",
            "(123)456-7890",
            "(123) 456-7890",
            " 123 456 7890 "
        };
        String expected = "1234567890";

        // When & Then
        for (String formatted : formattedPhones) {
            Telefono telefono = new Telefono(formatted);
            assertThat(telefono.getValue())
                .as("Failed for input: %s", formatted)
                .isEqualTo(expected);
        }
    }

    @Test
    @DisplayName("Should create Telefono from Integer value")
    void shouldCreateTelefonoFromIntegerValue() {
        // Given
        Integer phoneNumber = 1234567890;

        // When
        Telefono telefono = new Telefono(phoneNumber);

        // Then
        assertThat(telefono.getValue()).isEqualTo("1234567890");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "",
        "   ",
        "\t\n"
    })
    @DisplayName("Should throw exception for blank or empty phone numbers")
    void shouldThrowExceptionForBlankOrEmptyPhoneNumbers(String blankPhone) {
        // When & Then
        assertThatThrownBy(() -> new Telefono(blankPhone))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Teléfono no puede estar vacío");
    }

    @Test
    @DisplayName("Should throw exception for null phone number string")
    void shouldThrowExceptionForNullPhoneNumberString() {
        // Given
        String nullPhone = null;

        // When & Then
        assertThatThrownBy(() -> new Telefono(nullPhone))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Teléfono no puede estar vacío");
    }

    @Test
    @DisplayName("Should throw exception for null Integer phone number")
    void shouldThrowExceptionForNullIntegerPhoneNumber() {
        // Given
        Integer nullPhone = null;

        // When & Then
        assertThatThrownBy(() -> new Telefono(nullPhone))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Teléfono no puede estar vacío");
    }

    @ParameterizedTest
    @ValueSource(strings = {
        "abc123",
        "123abc",
        "123-abc-789",
        "123456",      // Too short
        "12345678901234567", // Too long
        "++1234567890",
        "123@456#789"
    })
    @DisplayName("Should throw exception for invalid phone formats")
    void shouldThrowExceptionForInvalidPhoneFormats(String invalidPhone) {
        // When & Then
        assertThatThrownBy(() -> new Telefono(invalidPhone))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Formato de teléfono inválido: " + invalidPhone);
    }

    @Test
    @DisplayName("Should convert to Integer for phone numbers without country code")
    void shouldConvertToIntegerForPhoneNumbersWithoutCountryCode() {
        // Given
        String phoneNumber = "1234567890";
        Telefono telefono = new Telefono(phoneNumber);

        // When
        Integer phoneAsInteger = telefono.getValueAsInteger();

        // Then
        assertThat(phoneAsInteger).isEqualTo(1234567890);
    }

    @Test
    @DisplayName("Should throw exception when converting phone with country code to Integer")
    void shouldThrowExceptionWhenConvertingPhoneWithCountryCodeToInteger() {
        // Given
        String phoneWithCountryCode = "+1234567890";
        Telefono telefono = new Telefono(phoneWithCountryCode);

        // When & Then
        assertThatThrownBy(() -> telefono.getValueAsInteger())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No se puede convertir el teléfono a Integer: +1234567890");
    }

    @Test
    @DisplayName("Should format 10-digit phone number with standard formatting")
    void shouldFormat10DigitPhoneNumberWithStandardFormatting() {
        // Given
        String tenDigitPhone = "1234567890";
        Telefono telefono = new Telefono(tenDigitPhone);

        // When
        String formatted = telefono.getFormattedValue();

        // Then
        assertThat(formatted).isEqualTo("(123) 456-7890");
    }

    @Test
    @DisplayName("Should return original value for non-standard length phone numbers")
    void shouldReturnOriginalValueForNonStandardLengthPhoneNumbers() {
        // Given
        String nonStandardPhone = "+521234567890";
        Telefono telefono = new Telefono(nonStandardPhone);

        // When
        String formatted = telefono.getFormattedValue();

        // Then
        assertThat(formatted).isEqualTo(nonStandardPhone);
    }

    @Test
    @DisplayName("Should be equal when phones have same value")
    void shouldBeEqualWhenPhonesHaveSameValue() {
        // Given
        String phoneValue = "1234567890";
        Telefono telefono1 = new Telefono(phoneValue);
        Telefono telefono2 = new Telefono(phoneValue);

        // When & Then
        assertThat(telefono1).isEqualTo(telefono2);
        assertThat(telefono1.hashCode()).isEqualTo(telefono2.hashCode());
    }

    @Test
    @DisplayName("Should be equal when phones are normalized to same value")
    void shouldBeEqualWhenPhonesAreNormalizedToSameValue() {
        // Given
        Telefono telefono1 = new Telefono("(123) 456-7890");
        Telefono telefono2 = new Telefono("123-456-7890");

        // When & Then
        assertThat(telefono1).isEqualTo(telefono2);
        assertThat(telefono1.hashCode()).isEqualTo(telefono2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when phones have different values")
    void shouldNotBeEqualWhenPhonesHaveDifferentValues() {
        // Given
        Telefono telefono1 = new Telefono("1234567890");
        Telefono telefono2 = new Telefono("0987654321");

        // When & Then
        assertThat(telefono1).isNotEqualTo(telefono2);
        assertThat(telefono1.hashCode()).isNotEqualTo(telefono2.hashCode());
    }

    @Test
    @DisplayName("Should have toString that returns phone value")
    void shouldHaveToStringThatReturnsPhoneValue() {
        // Given
        String phoneValue = "1234567890";
        Telefono telefono = new Telefono(phoneValue);

        // When
        String toString = telefono.toString();

        // Then
        assertThat(toString).isEqualTo(phoneValue);
    }
}