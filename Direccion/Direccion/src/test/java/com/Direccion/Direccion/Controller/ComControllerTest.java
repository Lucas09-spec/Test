package com.Direccion.Direccion.Controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.Direccion.Controller.ComController;
import com.Direccion.Model.Comuna;
import com.Direccion.Service.ComService;

@WebMvcTest(ComController.class)
public class ComControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerComunas_devuelveLista() throws Exception {
        Comuna comuna1 = new Comuna();
        comuna1.setId_com(1L);
        comuna1.setId_reg(13L);
        comuna1.setNom_com("Ñuñoa");

        Comuna comuna2 = new Comuna();
        comuna2.setId_com(2L);
        comuna2.setId_reg(13L);
        comuna2.setNom_com("Providencia");

        List<Comuna> comunas = Arrays.asList(comuna1, comuna2);

        when(service.getComunas()).thenReturn(comunas);

        mockMvc.perform(get("/api/v1/Comuna"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nom_com").value("Ñuñoa"));
    }

    @Test
    void obtenerComunaPorId_devuelveUnaComuna() throws Exception {
        Comuna comuna = new Comuna();
        comuna.setId_com(1L);
        comuna.setId_reg(13L);
        comuna.setNom_com("Ñuñoa");

        when(service.getComunaPorId(1L)).thenReturn(comuna);

        mockMvc.perform(get("/api/v1/Comuna/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_com").value(1L))
                .andExpect(jsonPath("$.nom_com").value("Ñuñoa"));
    }

    @Test
    void obtenerComunaPorId_noExiste_retorna404() throws Exception {
        when(service.getComunaPorId(99L)).thenThrow(new RuntimeException("Comuna no encontrada"));

        mockMvc.perform(get("/api/v1/Comuna/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarComuna_retorna201() throws Exception {
        Comuna nueva = new Comuna();
        nueva.setId_com(1L);
        nueva.setId_reg(13L);
        nueva.setNom_com("Ñuñoa");

        when(service.saveComuna(any(Comuna.class))).thenReturn(nueva);

        mockMvc.perform(post("/api/v1/Comuna")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom_com").value("Ñuñoa"));
    }

    @Test
    void guardarComuna_camposInvalidos_retorna400() throws Exception {
        Comuna invalida = new Comuna(); // sin nom_com ni id_reg

        when(service.saveComuna(any())).thenThrow(new IllegalArgumentException("Todos los campos son obligatorios"));

        mockMvc.perform(post("/api/v1/Comuna")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest());
    }
}