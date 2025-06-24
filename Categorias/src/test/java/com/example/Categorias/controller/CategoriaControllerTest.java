package com.example.Categorias.controller;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import com.example.Categorias.service.CategoriaService;
import com.example.Categorias.model.Categoria;
import java.util.List;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

// Cargar el controlador que se va a simular
@WebMvcTest(CategoriaController.class)
public class CategoriaControllerTest {

    // Se inyecta un mock de CategoriaService
    @MockBean
    private CategoriaService categoriaService;

    // Crear un mock proporcionado por Spring para simular peticiones HTTP
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllCategorias_returnsOKAndJson() {
        // Crear una lista ficticia de categorías para la respuesta del servicio
        List<Categoria> listaCategorias = Arrays.asList(
            new Categoria(1L, "Problemas Técnicos", "Soporte para errores técnicos"),
            new Categoria(2L, "Consultas de Facturación", "Dudas sobre cobros y pagos")
        );

        // Simular el comportamiento del service: al invocar getCategorias se retorna la lista ficticia
        when(categoriaService.getCategorias()).thenReturn(listaCategorias);

        // Ejecutar la petición GET al endpoint y verificar la respuesta
        try {
            mockMvc.perform(get("/api/v1/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
