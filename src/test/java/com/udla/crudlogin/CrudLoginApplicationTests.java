package com.udla.crudlogin;

import com.udla.crudlogin.repository.ProductoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CrudLoginApplicationTests {

    @Autowired MockMvc mvc;
    @Autowired ProductoRepository productos;

    @Test
    void urlProtegidaSinLoginRedirigeAlLogin() throws Exception {
        mvc.perform(get("/productos"))
           .andExpect(status().is3xxRedirection())
           .andExpect(redirectedUrlPattern("**/login"));
        mvc.perform(get("/productos/nuevo")).andExpect(status().is3xxRedirection());
        mvc.perform(post("/productos/1/eliminar").with(csrf())).andExpect(status().is3xxRedirection());
    }

    @Test
    void loginCorrectoEIncorrecto() throws Exception {
        mvc.perform(formLogin("/login").user("admin").password("admin123"))
           .andExpect(authenticated())
           .andExpect(redirectedUrl("/productos"));
        mvc.perform(formLogin("/login").user("admin").password("mala"))
           .andExpect(unauthenticated())
           .andExpect(redirectedUrl("/login?error"));
    }

    @Test
    @WithMockUser
    void crudCompleto() throws Exception {
        mvc.perform(get("/productos")).andExpect(status().isOk())
           .andExpect(view().name("productos/lista"));

        long antes = productos.count();
        mvc.perform(post("/productos").with(csrf())
                .param("nombre", "Monitor").param("descripcion", "24 pulgadas")
                .param("precio", "199.99").param("stock", "7"))
           .andExpect(redirectedUrl("/productos"));
        assertThat(productos.count()).isEqualTo(antes + 1);

        Long id = productos.findAll().stream().filter(p -> p.getNombre().equals("Monitor")).findFirst().get().getId();

        mvc.perform(get("/productos/" + id + "/editar")).andExpect(status().isOk());
        mvc.perform(post("/productos/" + id).with(csrf())
                .param("nombre", "Monitor HD").param("descripcion", "")
                .param("precio", "180").param("stock", "3"))
           .andExpect(redirectedUrl("/productos"));
        assertThat(productos.findById(id).get().getNombre()).isEqualTo("Monitor HD");

        mvc.perform(post("/productos").with(csrf()).param("nombre", "").param("precio", "-1").param("stock", "1"))
           .andExpect(status().isOk()).andExpect(view().name("productos/form"));

        mvc.perform(post("/productos/" + id + "/eliminar").with(csrf()))
           .andExpect(redirectedUrl("/productos"));
        assertThat(productos.findById(id)).isEmpty();
    }
}
