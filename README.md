# CRUD + Login con patrón MVC

Spring Boot 3 · Spring MVC · Thymeleaf · Spring Security · JPA · SQL Server

## Estructura MVC

```
src/main/java/com/udla/crudlogin/
├── model/          → MODELO: entidades Usuario y Producto (tablas)
├── repository/     → MODELO: acceso a datos (Spring Data JPA)
├── service/        → lógica de negocio (ProductoService, UsuarioDetailsService)
├── controller/     → CONTROLADOR: AuthController, ProductoController
└── config/         → SecurityConfig (login/rutas protegidas), DataInitializer
src/main/resources/templates/   → VISTA: login.html, productos/lista.html, productos/form.html
```

## Requisitos
- Java 17 o superior
- Maven (o el de tu IDE)
- SQL Server con autenticación SQL habilitada (puerto 1433)

## Cómo ejecutar
1. Ejecuta `database/crear_base.sql` en SQL Server (crea la base `crud_login`).
2. Edita usuario/contraseña en `src/main/resources/application.properties`.
3. Ejecuta:
   ```bash
   mvn spring-boot:run
   ```
4. Abre http://localhost:8080 → usuario **admin**, contraseña **admin123**.

Las tablas `usuarios` y `productos` se crean solas al arrancar; también se insertan el admin y 3 productos de ejemplo.

## Seguridad
- Toda URL excepto `/login` y `/css/**` exige sesión iniciada; si no, redirige a `/login`.
- Contraseñas guardadas con hash BCrypt.
- Protección CSRF en todos los formularios.
- Logout invalida la sesión.

## Pruebas
`mvn test` corre pruebas automáticas (con H2 en memoria) de: rutas protegidas, login correcto/incorrecto y CRUD completo.
