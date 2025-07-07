package com.company.domain.valueobject;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Value Object para Email
 * Encapsula la validación y lógica de emails
 */
public final class Email {
    
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"
    );
    
    private final String value;

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email no puede estar vacío");
        }
        
        String cleanEmail = value.strip().toLowerCase();
        
        if (!EMAIL_PATTERN.matcher(cleanEmail).matches()) {
            throw new IllegalArgumentException("Formato de email inválido: " + value);
        }
        
        if (cleanEmail.length() > 45) {
            throw new IllegalArgumentException("Email no puede exceder 45 caracteres");
        }
        
        this.value = cleanEmail;
    }

    public String getValue() {
        return value;
    }

    public String getDomain() {
        return value.substring(value.indexOf('@') + 1);
    }

    public String getLocalPart() {
        return value.substring(0, value.indexOf('@'));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}