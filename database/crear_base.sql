-- Ejecutar una sola vez en SQL Server (SSMS o Azure Data Studio).
-- Las tablas "usuarios" y "productos" las crea la aplicación automáticamente.
IF DB_ID('crud_login') IS NULL
    CREATE DATABASE crud_login;
GO
