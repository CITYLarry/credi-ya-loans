# CrediYa - Loans Service

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)

## Descripción


CrediYa Loans Service es un microservicio para la gestión y originación de préstamos desarrollado con Spring Boot y arquitectura hexagonal. Este servicio proporciona funcionalidades para la solicitud, evaluación y manejo de créditos en la plataforma CrediYa.

## Características


- 🏗️ **Arquitectura Hexagonal**: Implementación de Clean Architecture con separación clara de responsabilidades
- ⚡ **Reactive Programming**: Construido con Spring WebFlux para programación reactiva no bloqueante
- 🗄️ **Base de Datos en Memoria**: Utiliza H2 con R2DBC para desarrollo y pruebas
- 📚 **Documentación API**: Documentación automática con OpenAPI/Swagger
- ✅ **Validación**: Validación robusta de datos de entrada
- 🧪 **Testing**: Suite completa de pruebas unitarias e integración
- 🛠️ **MapStruct**: Mapeo automático entre DTOs y entidades de dominio
- 📊 **Lombok**: Reducción de código boilerplate

## Tecnologías


- **Java 17**
- **Spring Boot 3.5.5**
- **Spring WebFlux** (Reactive Web)
- **Spring Data R2DBC** (Reactive Database Connectivity)
- **H2 Database** (In-memory database)
- **MapStruct** (Bean mapping)
- **Lombok** (Code generation)
- **OpenAPI 3** (API documentation)
- **JUnit 5** (Testing)
- **Gradle** (Build tool)

## Arquitectura


El proyecto sigue una arquitectura hexagonal (ports and adapters) organizada en módulos Gradle independientes con las siguientes capas:

```

├── applications/                    # Capa de aplicación
│   ├── app-loans/                   # Aplicación principal ejecutable (préstamos)
│   ├── exception/                   # Excepciones de negocio
│   ├── port-in/                     # Puertos de entrada (use cases)
│   └── service/                     # Implementación de casos de uso
├── domain/                          # Capa de dominio
│   ├── model/                       # Entidades de dominio
│   └── port-out/                    # Puertos de salida (repositories)
└── infrastructure/                  # Capa de infraestructura
  └── adapter/                     # Adaptadores
    ├── drivin/                  # Adaptadores de entrada
    │   └── web/                 # Controladores REST
    └── driven/                  # Adaptadores de salida
      └── persistence/         # Persistencia de datos
```

### Descripción de Módulos


- **applications/app-loans**: Módulo principal que contiene la aplicación ejecutable de Spring Boot para préstamos
- **applications/exception**: Excepciones específicas del negocio
- **applications/port-in**: Puertos de entrada que definen los casos de uso
- **applications/service**: Implementación de los casos de uso y lógica de aplicación
- **domain/model**: Entidades de dominio con reglas de negocio
- **domain/port-out**: Puertos de salida para persistencia y servicios externos
- **infrastructure/adapter/drivin/web**: Adaptadores de entrada (controladores REST)
- **infrastructure/adapter/driven/persistence**: Adaptadores de salida (repositorios, mappers)

## Requisitos

- **Java 17** o superior
- **Gradle 8.x**
- **Git**

## Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone https://github.com/CITYLarry/crediya-auth.git
cd crediya-auth
```

### 2. Compilar el proyecto

```bash
./gradlew build
```

### 3. Ejecutar la aplicación

```bash
./gradlew bootRun
```

La aplicación estará disponible en: `http://localhost:8080`


## Endpoints de la API

### Solicitud de Préstamo

```http
POST /api/v1/loans
Content-Type: application/json
```

**Cuerpo de la petición:**
```json
{
  "amount": 5000000,
  "term": 24,
  "applicantId": 1,
  "purpose": "Compra de vehículo"
}
```

**Respuesta exitosa (201):**
```json
{
  "id": 1001,
  "amount": 5000000,
  "term": 24,
  "status": "PENDING",
  "message": "Solicitud de préstamo registrada exitosamente"
}
```

## Documentación de la API

Una vez que la aplicación esté ejecutándose, puedes acceder a la documentación interactiva de la API:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Base de Datos

### Consola H2


La aplicación utiliza una base de datos H2 en memoria. Puedes acceder a la consola H2 para inspeccionar los datos:

- **URL**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:credityadb`
- **Usuario**: `sa`
- **Contraseña**: *(vacía)*

### Esquema de Base de Datos


El esquema se inicializa automáticamente desde `src/main/resources/schema.sql` del módulo correspondiente.

## Testing

### Ejecutar todas las pruebas

```bash
./gradlew test
```

### Ejecutar pruebas con reporte

```bash
./gradlew test --info
```

Los reportes de pruebas se generan en: `build/reports/tests/test/index.html`

## Configuración

La configuración de la aplicación se encuentra en `src/main/resources/application.yaml`:

```yaml
spring:
  r2dbc:
    url: r2dbc:h2:mem:///credityadb;DB_CLOSE_DELAY=-1
    username: sa
    password: ''
  h2:
    console:
      enabled: true
      path: /h2-console
  sql:
    init:
      mode: always
```

## Validaciones de Dominio


El modelo `Loan` incluye las siguientes validaciones:

- **Monto**: Entre 100,000 y 50,000,000
- **Plazo**: Entre 6 y 60 meses
- **Campos obligatorios**: amount, term, applicantId, purpose

## Manejo de Errores


La aplicación maneja diferentes tipos de errores:

- **400 Bad Request**: Datos de entrada inválidos
- **404 Not Found**: Solicitud o préstamo no encontrado
- **409 Conflict**: Préstamo duplicado o conflicto de negocio
- **500 Internal Server Error**: Errores internos del servidor

## Desarrollo

### Estructura del Código

- **Domain Models**: Entidades de negocio con validaciones incorporadas
- **Use Cases**: Lógica de aplicación encapsulada en casos de uso
- **Ports**: Interfaces que definen contratos entre capas
- **Adapters**: Implementaciones concretas de los puertos

### Comandos Útiles

```bash
# Limpiar y compilar
./gradlew clean build

# Ejecutar en modo desarrollo
./gradlew bootRun

# Generar JAR ejecutable
./gradlew bootJar

# Verificar dependencias
./gradlew dependencies
```

## Licencia

Este proyecto es parte de la plataforma CrediYa desarrollado por Pragma.

## Contacto

- **Desarrollador**: Larry Mateo Ramirez C.
- **Proyecto**: CrediYa Loans Service
- **Fecha**: Agosto 2025

---

Para más información sobre el proyecto, consulta la documentación técnica o contacta al equipo de desarrollo.
