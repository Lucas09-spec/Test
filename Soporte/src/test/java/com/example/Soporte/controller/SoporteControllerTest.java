package com.example.Soporte.controller;

import com.example.Soporte.model.Soporte;
import com.example.Soporte.service.SoporteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SoporteController.class)
public class SoporteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SoporteService soporteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllSoportes_returnsOk() throws Exception {
        Soporte soporte1 = Soporte.builder()
                .id(1L)
                .titulo("Soporte 1")
                .descripcion("Desc 1")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        Soporte soporte2 = Soporte.builder()
                .id(2L)
                .titulo("Soporte 2")
                .descripcion("Desc 2")
                .fechaInicio(LocalDate.now())
                .categoriaId(2L)
                .estadoId(2L)
                .usuarioId(2L)
                .build();
        List<Soporte> soportes = Arrays.asList(soporte1, soporte2);
        when(soporteService.findAll()).thenReturn(soportes);

        mockMvc.perform(get("/api/v1/soportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].titulo").value("Soporte 1"));
    }

    @Test
    void getSoporteById_whenExists_returnsOk() throws Exception {
        Soporte soporte = Soporte.builder()
                .id(1L)
                .titulo("Soporte 1")
                .descripcion("Desc 1")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        when(soporteService.findById(1L)).thenReturn(Optional.of(soporte));

        mockMvc.perform(get("/api/v1/soportes/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Soporte 1"));
    }

    @Test
    void getSoporteById_whenNotFound_returnsNotFound() throws Exception {
        when(soporteService.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/soportes/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createSoporte_whenSuccessful_returnsOk() throws Exception {
        Soporte nuevoSoporte = Soporte.builder()
                .titulo("Nuevo Soporte")
                .descripcion("Desc nueva")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        Soporte soporteGuardado = Soporte.builder()
                .id(1L)
                .titulo("Nuevo Soporte")
                .descripcion("Desc nueva")
                .fechaInicio(nuevoSoporte.getFechaInicio())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        when(soporteService.saveSoporte(nuevoSoporte)).thenReturn(soporteGuardado);

        mockMvc.perform(post("/api/v1/soportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoSoporte)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Nuevo Soporte"));
    }

    @Test
    void createSoporte_whenException_returnsBadRequest() throws Exception {
        Soporte nuevoSoporte = Soporte.builder()
                .titulo("Nuevo Soporte")
                .descripcion("Desc nueva")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        when(soporteService.saveSoporte(nuevoSoporte)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/v1/soportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoSoporte)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateSoporte_whenExistsAndSuccessful_returnsOk() throws Exception {
        Soporte soporteExistente = Soporte.builder()
                .id(1L)
                .titulo("Soporte 1")
                .descripcion("Desc 1")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        Soporte soporteActualizado = Soporte.builder()
                .titulo("Soporte Actualizado")
                .descripcion("Nueva Desc")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        Soporte soporteGuardado = Soporte.builder()
                .id(1L)
                .titulo("Soporte Actualizado")
                .descripcion("Nueva Desc")
                .fechaInicio(soporteActualizado.getFechaInicio())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        when(soporteService.findById(1L)).thenReturn(Optional.of(soporteExistente));
        when(soporteService.saveSoporte(Mockito.any(Soporte.class))).thenReturn(soporteGuardado);

        mockMvc.perform(put("/api/v1/soportes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(soporteActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Soporte Actualizado"));
    }

    @Test
    void updateSoporte_whenNotFound_returnsNotFound() throws Exception {
        Soporte soporteActualizado = Soporte.builder()
                .titulo("Soporte Actualizado")
                .descripcion("Nueva Desc")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        when(soporteService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/soportes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(soporteActualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateSoporte_whenException_returnsBadRequest() throws Exception {
        Soporte soporteExistente = Soporte.builder()
                .id(1L)
                .titulo("Soporte 1")
                .descripcion("Desc 1")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        Soporte soporteActualizado = Soporte.builder()
                .titulo("Soporte Actualizado")
                .descripcion("Nueva Desc")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        when(soporteService.findById(1L)).thenReturn(Optional.of(soporteExistente));
        when(soporteService.saveSoporte(Mockito.any(Soporte.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/v1/soportes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(soporteActualizado)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteSoporte_returnsNoContent() throws Exception {
        doNothing().when(soporteService).delete(1L);
        mockMvc.perform(delete("/api/v1/soportes/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
