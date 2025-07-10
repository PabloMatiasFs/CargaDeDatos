@echo off
echo ========================================
echo Configurando PostgreSQL para Personas
echo ========================================

echo.
echo Creando bases de datos...

REM Crear base de datos principal
psql -U postgres -c "CREATE DATABASE personas_db;" 2>nul
if %errorlevel% neq 0 (
    echo Error: No se pudo crear personas_db
    echo Verifica que PostgreSQL esté corriendo y el usuario postgres exista
    pause
    exit /b 1
)

REM Crear base de datos de desarrollo
psql -U postgres -c "CREATE DATABASE personas_db_dev;" 2>nul

REM Crear base de datos de producción
psql -U postgres -c "CREATE DATABASE personas_db_prod;" 2>nul

echo.
echo Ejecutando script SQL...

REM Ejecutar el script SQL
psql -U postgres -d personas_db -f "Base de Datos PostgreSQL.sql"

if %errorlevel% equ 0 (
    echo.
    echo ========================================
    echo ¡Configuración completada exitosamente!
    echo ========================================
    echo.
    echo Bases de datos creadas:
    echo - personas_db (principal)
    echo - personas_db_dev (desarrollo)
    echo - personas_db_prod (producción)
    echo.
    echo Configuración de conexión:
    echo - Host: localhost
    echo - Puerto: 5432
    echo - Usuario: postgres
    echo - Contraseña: pass123456
    echo.
) else (
    echo.
    echo Error al ejecutar el script SQL
    echo Verifica que PostgreSQL esté corriendo
)

pause 