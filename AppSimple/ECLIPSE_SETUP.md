# Configuración para Eclipse

## Problemas comunes y soluciones

### 1. **Problema: No encuentra las clases de Spring Boot**
**Solución:**
- Asegúrate de que el proyecto esté importado como proyecto Maven
- Click derecho en el proyecto → Configure → Convert to Maven Project
- Update Project (F5)

### 2. **Problema: Errores de compilación con MapStruct**
**Solución:**
- Verifica que tienes Java 11 configurado
- Window → Preferences → Java → Installed JREs
- Asegúrate de que Java 11 esté seleccionado

### 3. **Problema: No encuentra PostgreSQL**
**Solución:**
- Verifica que PostgreSQL esté instalado y corriendo
- La aplicación está configurada para usar:
  - Host: localhost
  - Puerto: 5432
  - Base de datos: personas_db
  - Usuario: postgres
  - Contraseña: pass123456

### 4. **Problema: Errores de anotaciones**
**Solución:**
- Clean y rebuild el proyecto
- Project → Clean...
- Maven → Update Project

## Configuración paso a paso

### 1. Importar el proyecto
```
File → Import → Maven → Existing Maven Projects
Seleccionar la carpeta AppSimple
```

### 2. Configurar Java 11
```
Window → Preferences → Java → Installed JREs
Agregar Java 11 si no está disponible
```

### 3. Configurar Maven
```
Window → Preferences → Maven → User Settings
Verificar que apunte a settings.xml correcto
```

### 4. Ejecutar la aplicación

#### **Opción A: Como aplicación Java (Recomendado)**
```
Click derecho en MainApplication.java
Run As → Java Application
```

#### **Opción B: Como aplicación web en Tomcat**
```
1. Window → Show View → Servers
2. Click derecho en Servers → New → Server
3. Seleccionar Apache → Tomcat v9.0 Server
4. Configurar Tomcat 9.0
5. Click derecho en el proyecto → Run As → Run on Server
```

## Configuración de base de datos

### Crear base de datos PostgreSQL
```sql
CREATE DATABASE personas_db;
CREATE DATABASE personas_db_dev;
CREATE DATABASE personas_db_prod;
```

### Verificar conexión
```bash
psql -U postgres -d personas_db -c "\dt"
```

## Configuración específica para Tomcat 9.0

### **IMPORTANTE: Este proyecto es Spring Boot**
- ✅ **Spring Boot incluye su propio servidor embebido**
- ✅ **NO necesita Tomcat externo**
- ✅ **Recomendado: Ejecutar como Java Application**

### **Si quieres usar Tomcat 9.0 externo:**

1. **Configurar Tomcat en Eclipse:**
   ```
   Window → Preferences → Server → Runtime Environments
   Add → Apache → Tomcat v9.0 Server
   ```

2. **Configurar el proyecto:**
   ```
   Click derecho en el proyecto → Properties
   Project Facets → Dynamic Web Module 4.0
   Java 11
   ```

3. **Configurar servidor:**
   ```
   Window → Show View → Servers
   New Server → Apache → Tomcat v9.0
   ```

### **Configuración de deployment:**
- **Context Path**: `/personas-hexagonal`
- **Deploy Path**: `/`
- **Source Path**: `/src/main/webapp`

## Troubleshooting

### Si la aplicación no inicia:
1. Verificar logs en Console de Eclipse
2. Verificar que PostgreSQL esté corriendo
3. Verificar configuración en application.yml
4. Clean y rebuild el proyecto

### Si hay errores de dependencias:
1. Maven → Update Project
2. Maven → Clean
3. Maven → Install

### Si hay errores de compilación:
1. Project → Clean...
2. Verificar Java 11
3. Verificar encoding UTF-8 