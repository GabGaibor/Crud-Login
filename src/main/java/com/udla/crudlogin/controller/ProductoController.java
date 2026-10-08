package com.udla.crudlogin.controller;

import com.udla.crudlogin.model.Producto;
import com.udla.crudlogin.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // READ - listar
    @GetMapping
    public String listar(Model model) {
        model.addAttribute("productos", service.listar());
        return "productos/lista";
    }

    // CREATE - formulario
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "Nuevo producto");
        model.addAttribute("accion", "/productos");
        return "productos/form";
    }

    // CREATE - guardar
    @PostMapping
    public String crear(@Valid @ModelAttribute("producto") Producto producto,
                        BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nuevo producto");
            model.addAttribute("accion", "/productos");
            return "productos/form";
        }
        service.crear(producto);
        flash.addFlashAttribute("mensaje", "Producto creado correctamente");
        return "redirect:/productos";
    }

    // UPDATE - formulario
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("producto", service.buscarPorId(id));
        model.addAttribute("titulo", "Editar producto #" + id);
        model.addAttribute("accion", "/productos/" + id);
        return "productos/form";
    }

    // UPDATE - guardar
    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("producto") Producto producto,
                             BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            producto.setId(id);
            model.addAttribute("titulo", "Editar producto #" + id);
            model.addAttribute("accion", "/productos/" + id);
            return "productos/form";
        }
        service.actualizar(id, producto);
        flash.addFlashAttribute("mensaje", "Producto actualizado correctamente");
        return "redirect:/productos";
    }

    // DELETE
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes flash) {
        service.eliminar(id);
        flash.addFlashAttribute("mensaje", "Producto eliminado correctamente");
        return "redirect:/productos";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String noEncontrado(NoSuchElementException ex, RedirectAttributes flash) {
        flash.addFlashAttribute("error", ex.getMessage());
        return "redirect:/productos";
    }
}
