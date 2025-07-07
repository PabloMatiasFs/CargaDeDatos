package com.company.domain.valueobject;

import java.util.Objects;

/**
 * Value Object para Teléfono
 * Encapsula la validación y lógica de números de teléfono
 */
public final class Telefono {
    
    private final String value;

    public Telefono(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Teléfono no puede estar vacío");
        }
        
        // Limpiar el número: remover espacios, guiones y paréntesis
        String cleanPhone = value.replaceAll("[\\s\\-\\(\\)]", "");
        
        // Validar que solo contenga dígitos y posiblemente un + al inicio
        if (!cleanPhone.matches("^\\+?[0-9]{7,15}$")) {
            throw new IllegalArgumentException("Formato de teléfono inválido: " + value);
        }
        
        this.value = cleanPhone;
    }

    public Telefono(Integer value) {
        this(value != null ? value.toString() : null);
    }

    public String getValue() {
        return value;
    }

    public Integer getValueAsInteger() {
        try {
            // Solo para números sin código de país
            if (!value.startsWith("+")) {
                return Integer.valueOf(value);
            }
            throw new NumberFormatException("Teléfono con código de país no se puede convertir a Integer");
        } catch (NumberFormatException e) {
            throw new IllegalStateException("No se puede convertir el teléfono a Integer: " + value);
        }
    }

    public String getFormattedValue() {
        if (value.length() == 10) {
            // Formato para números de 10 dígitos: (XXX) XXX-XXXX
            return String.format("(%s) %s-%s", 
                value.substring(0, 3), 
                value.substring(3, 6), 
                value.substring(6));
        }
        return value; // Retornar valor original si no es formato estándar
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Telefono telefono = (Telefono) o;
        return Objects.equals(value, telefono.value);
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