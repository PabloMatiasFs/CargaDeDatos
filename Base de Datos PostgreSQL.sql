-- Base de Datos PostgreSQL para el Sistema de Gestión de Personas
-- Migrado a Java 11 con Arquitectura Hexagonal

-- Crear base de datos principal
CREATE DATABASE personas_db;

-- Conectar a la base de datos
\c personas_db;

-- Asegurar que estamos usando el esquema public
SET search_path TO public;

-- Tabla de personas actualizada para la nueva arquitectura
CREATE TABLE IF NOT EXISTS public.datospersonas (
    idpersona SERIAL PRIMARY KEY,
    nombre VARCHAR(45) NOT NULL,
    apellido VARCHAR(45) NOT NULL,
    email VARCHAR(45) NOT NULL UNIQUE,
    tel VARCHAR(20) NOT NULL, -- Cambiado a VARCHAR para mayor flexibilidad
    direccion VARCHAR(100) NOT NULL, -- Aumentado el tamaño
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_nombre ON public.datospersonas(nombre);
CREATE INDEX IF NOT EXISTS idx_apellido ON public.datospersonas(apellido);
CREATE INDEX IF NOT EXISTS idx_email ON public.datospersonas(email);

-- Crear trigger para actualizar updated_at automáticamente
CREATE OR REPLACE FUNCTION public.update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_datospersonas_updated_at 
    BEFORE UPDATE ON public.datospersonas 
    FOR EACH ROW 
    EXECUTE FUNCTION public.update_updated_at_column();

-- Datos de ejemplo
INSERT INTO public.datospersonas (nombre, apellido, email, tel, direccion) VALUES 
('Juan', 'Pérez', 'juan.perez@email.com', '1234567890', 'Calle Principal 123'),
('María', 'García', 'maria.garcia@email.com', '0987654321', 'Avenida Central 456'),
('Carlos', 'López', 'carlos.lopez@email.com', '5555555555', 'Plaza Mayor 789')
ON CONFLICT (email) DO UPDATE SET 
    nombre = EXCLUDED.nombre,
    apellido = EXCLUDED.apellido,
    tel = EXCLUDED.tel,
    direccion = EXCLUDED.direccion;

-- Crear base de datos para desarrollo
CREATE DATABASE personas_db_dev;

-- Crear base de datos para producción
CREATE DATABASE personas_db_prod;

-- Verificar la creación de la tabla
\dt public.datospersonas;

-- Verificar los datos insertados
SELECT * FROM public.datospersonas; 