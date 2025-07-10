Write-Host "========================================" -ForegroundColor Green
Write-Host "Configurando PostgreSQL para Personas" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Green

Write-Host ""
Write-Host "Creando bases de datos..." -ForegroundColor Yellow

# Crear base de datos principal
try {
    psql -U postgres -c "CREATE DATABASE personas_db;" 2>$null
    Write-Host "✓ Base de datos personas_db creada" -ForegroundColor Green
} catch {
    Write-Host "✗ Error al crear personas_db" -ForegroundColor Red
    Write-Host "Verifica que PostgreSQL esté corriendo y el usuario postgres exista" -ForegroundColor Red
    Read-Host "Presiona Enter para continuar"
    exit 1
}

# Crear base de datos de desarrollo
try {
    psql -U postgres -c "CREATE DATABASE personas_db_dev;" 2>$null
    Write-Host "✓ Base de datos personas_db_dev creada" -ForegroundColor Green
} catch {
    Write-Host "⚠ Base de datos personas_db_dev ya existe" -ForegroundColor Yellow
}

# Crear base de datos de producción
try {
    psql -U postgres -c "CREATE DATABASE personas_db_prod;" 2>$null
    Write-Host "✓ Base de datos personas_db_prod creada" -ForegroundColor Green
} catch {
    Write-Host "⚠ Base de datos personas_db_prod ya existe" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "Ejecutando script SQL..." -ForegroundColor Yellow

# Ejecutar el script SQL
try {
    psql -U postgres -d personas_db -f "Base de Datos PostgreSQL.sql"
    Write-Host ""
    Write-Host "========================================" -ForegroundColor Green
    Write-Host "¡Configuración completada exitosamente!" -ForegroundColor Green
    Write-Host "========================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Bases de datos creadas:" -ForegroundColor Cyan
    Write-Host "- personas_db (principal)" -ForegroundColor White
    Write-Host "- personas_db_dev (desarrollo)" -ForegroundColor White
    Write-Host "- personas_db_prod (producción)" -ForegroundColor White
    Write-Host ""
    Write-Host "Configuración de conexión:" -ForegroundColor Cyan
    Write-Host "- Host: localhost" -ForegroundColor White
    Write-Host "- Puerto: 5432" -ForegroundColor White
    Write-Host "- Usuario: postgres" -ForegroundColor White
    Write-Host "- Contraseña: pass123456" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host ""
    Write-Host "✗ Error al ejecutar el script SQL" -ForegroundColor Red
    Write-Host "Verifica que PostgreSQL esté corriendo" -ForegroundColor Red
}

Read-Host "Presiona Enter para continuar" 