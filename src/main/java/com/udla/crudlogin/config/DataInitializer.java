package com.udla.crudlogin.config;

import com.udla.crudlogin.model.Producto;
import com.udla.crudlogin.model.Usuario;
import com.udla.crudlogin.repository.ProductoRepository;
import com.udla.crudlogin.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

/**
 * Crea el usuario administrador y productos de ejemplo la primera vez que arranca.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarios;
    private final ProductoRepository productos;
    private final PasswordEncoder encoder;

    public DataInitializer(UsuarioRepository usuarios, ProductoRepository productos, PasswordEncoder encoder) {
        this.usuarios = usuarios;
        this.productos = productos;
        this.encoder = encoder;
    }

    @Override
    public void run(String... args) {
        if (!usuarios.existsByUsername("admin")) {
            usuarios.save(new Usuario("admin", encoder.encode("admin123"), "Administrador", "ADMIN"));
        }
        if (productos.count() == 0) {
            productos.save(new Producto("Laptop", "Laptop 15 pulgadas", new BigDecimal("850.00"), 5));
            productos.save(new Producto("Mouse", "Mouse inalámbrico", new BigDecimal("15.50"), 40));
            productos.save(new Producto("Teclado", "Teclado mecánico", new BigDecimal("45.90"), 20));
        }
    }
}
