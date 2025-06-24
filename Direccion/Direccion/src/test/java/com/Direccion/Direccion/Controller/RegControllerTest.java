package com.Direccion.Direccion.Controller;

import com.Direccion.Controller.RegController;
import com.Direccion.Model.Region;
import com.Direccion.Service.RegService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RegController.class)
public class RegControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegService regService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerRegion_devuelveLista() throws Exception {
        Region r1 = new Region();
        r1.setId_reg(1L);
        r1.setNom_reg("Región Metropolitana");

        Region r2 = new Region();
        r2.setId_reg(2L);
        r2.setNom_reg("Valparaíso");

        List<Region> regiones = Arrays.asList(r1, r2);

        when(regService.getRegions()).thenReturn(regiones);

        mockMvc.perform(get("/api/v1/Region"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].nom_reg").value("Región Metropolitana"));
    }

    @Test
    void obtenerRegionPorId_existente() throws Exception {
        Region region = new Region();
        region.setId_reg(1L);
        region.setNom_reg("Coquimbo");

        when(regService.getRegionPorId(1L)).thenReturn(region);

        mockMvc.perform(get("/api/v1/Region/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id_reg").value(1L))
                .andExpect(jsonPath("$.nom_reg").value("Coquimbo"));
    }

    @Test
    void obtenerRegionPorId_noExiste() throws Exception {
        when(regService.getRegionPorId(999L)).thenThrow(new RuntimeException("No encontrada"));

        mockMvc.perform(get("/api/v1/Region/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void guardarRegion_valida_return201() throws Exception {
        Region nueva = new Region();
        nueva.setId_reg(3L);
        nueva.setNom_reg("Ñuble");

        when(regService.saveRegion(any())).thenReturn(nueva);

        mockMvc.perform(post("/api/v1/Region")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nueva)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom_reg").value("Ñuble"));
    }

    @Test
    void guardarRegion_invalida_return400() throws Exception {
        Region invalida = new Region(); // nom_reg es null

        when(regService.saveRegion(any())).thenThrow(new IllegalArgumentException("Todos los campos son obligatorios"));

        mockMvc.perform(post("/api/v1/Region")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalida)))
                .andExpect(status().isBadRequest());
    }
}