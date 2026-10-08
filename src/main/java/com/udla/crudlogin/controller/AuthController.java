package com.udla.crudlogin.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String login(Authentication auth) {
        // Si ya inició sesión, no tiene sentido mostrar el login
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            return "redirect:/productos";
        }
        return "login";
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/productos";
    }
}
