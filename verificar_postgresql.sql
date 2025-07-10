-- Script de verificación para PostgreSQL
-- Ejecutar este script para verificar la configuración

-- Conectar a la base de datos
\c personas_db;

-- Mostrar esquemas disponibles
\dn

-- Mostrar tablas en el esquema public
\dt public.*

-- Verificar que la tabla datospersonas existe
SELECT table_name, table_schema 
FROM information_schema.tables 
WHERE table_schema = 'public' AND table_name = 'datospersonas';

-- Verificar la estructura de la tabla
\d public.datospersonas

-- Verificar datos
SELECT * FROM public.datospersonas;

-- Verificar índices
SELECT indexname, tablename 
FROM pg_indexes 
WHERE schemaname = 'public' AND tablename = 'datospersonas';

-- Verificar triggers
SELECT trigger_name, event_manipulation, action_statement
FROM information_schema.triggers 
WHERE trigger_schema = 'public' AND event_object_table = 'datospersonas'; 