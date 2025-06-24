package com.Direccion.Direccion.Controller;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.Direccion.Controller.DirController;
import com.Direccion.Model.Direccion;
import com.Direccion.Service.DirService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(DirController.class)
public class DirControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DirService dirService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerDirecciones_devuelveLista() throws Exception {
        Direccion d1 = new Direccion();
        d1.setId_dir(1L);
        d1.setId_com(101L);
        d1.setId_reg(13L);
        d1.setNom_dir("Av. Siempre Viva 742");

        Direccion d2 = new Direccion();
        d2.setId_dir(2L);
        d2.setId_com(102L);
        d2.setId_reg(13L);
        d2.setNom_dir("Calle Falsa 123");

        List<Direccion> lista = Arrays.asList(d1, d2);

        when(dirService.getDireccion()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/Direccion"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nom_dir").value("Av. Siempre Viva 742"));
    }

    @Test
    void obtenerDireccionPorId_existente() throws Exception {
        Direccion direccion = new Direccion();
        direccion.setId_dir(1L);
        direccion.setId_com(101L);
        direccion.setId_reg(13L);
        direccion.setNom_dir("Calle Los Olmos 22");

        when(dirService.getDireccionPorId(1L)).thenReturn(direccion);

        mockMvc.perform(get("/api/v1/Direccion/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_dir").value(1L))
                .andExpect(jsonPath("$.nom_dir").value("Calle Los Olmos 22"));
    }

    @Test
    void obtenerDireccionPorId_noExiste() throws Exception {
        when(dirService.getDireccionPorId(999L)).thenThrow(new RuntimeException("No encontrada"));

        mockMvc.perform(get("/api/v1/Direccion/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarDireccion_valida_return201() throws Exception {
        Direccion nueva = new Direccion();
        nueva.setId_dir(3L);
        nueva.setId_com(103L);
        nueva.setId_reg(13L);
        nueva.setNom_dir("Pasaje Los Aromos");

        when(dirService.saveDireccion(any())).thenReturn(nueva);

        mockMvc.perform(post("/api/v1/Direccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom_dir").value("Pasaje Los Aromos"));
    }

    @Test
    void guardarDireccion_invalida_return400() throws Exception {
        Direccion invalida = new Direccion(); // Falta nom_dir o id_com

        when(dirService.saveDireccion(any())).thenThrow(new IllegalArgumentException("Todos los campos son obligatorios"));

        mockMvc.perform(post("/api/v1/Direccion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest());
    }
}