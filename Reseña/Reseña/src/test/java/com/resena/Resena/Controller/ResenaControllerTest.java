package com.resena.Resena.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.resena.controller.ResenaController;
import com.resena.model.Resena;
import com.resena.service.ResenaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ResenaController.class)
public class ResenaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResenaService resenaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Resena resenaEjemplo;

    @BeforeEach
    void setUp() {
        resenaEjemplo = new Resena(
            1L,
            100L,
            200L,
            "Muy buen servicio",
            Date.valueOf("2024-06-22"),
            5
        );
    }

    @Test
    void testObtenerResenas() throws Exception {
        List<Resena> lista = Arrays.asList(resenaEjemplo);
        when(resenaService.getResenas()).thenReturn(lista);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Resena"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].comentario").value("Muy buen servicio"));
    }

    @Test
    void testObtenerResenasVacia() throws Exception {
        when(resenaService.getResenas()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Resena"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testObtenerResenaPorId() throws Exception {
        when(resenaService.getResenaPorId(1L)).thenReturn(resenaEjemplo);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Resena/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comentario").value("Muy buen servicio"));
    }

    @Test
    void testObtenerResenaPorIdNoEncontrado() throws Exception {
        when(resenaService.getResenaPorId(99L)).thenThrow(new RuntimeException("No encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/Resena/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testGuardarResena() throws Exception {
        when(resenaService.saveResena(any(Resena.class))).thenReturn(resenaEjemplo);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/Resena")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaEjemplo)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.nota").value(5));
    }

    @Test
    void testGuardarResenaConNotaInvalida() throws Exception {
        Resena reseñaMala = new Resena(2L, 100L, 200L, "Mala nota", Date.valueOf("2024-06-22"), 8);
        when(resenaService.saveResena(any())).thenThrow(new IllegalArgumentException("La nota debe ser un valor entre 1 y 5"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/v1/Resena")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reseñaMala)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("La nota debe ser un valor entre 1 y 5"));
    }

    @Test
    void testEliminarResena() throws Exception {
        when(resenaService.getResenaPorId(1L)).thenReturn(resenaEjemplo);
        doNothing().when(resenaService).deleteResena(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/Resena/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Reseña eliminada exitosamente."));
    }

    @Test
    void testEliminarResenaNoExistente() throws Exception {
        when(resenaService.getResenaPorId(99L)).thenThrow(new RuntimeException("No encontrada"));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/Resena/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarResena() throws Exception {
        Resena actualizada = new Resena(1L, 100L, 200L, "Comentario actualizado", Date.valueOf("2024-06-22"), 4);

        when(resenaService.getResenaPorId(1L)).thenReturn(resenaEjemplo);
        when(resenaService.saveResena(any(Resena.class))).thenReturn(actualizada);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/Resena/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizada)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.comentario").value("Comentario actualizado"))
            .andExpect(jsonPath("$.nota").value(4));
    }

    @Test
    void testActualizarResenaNoEncontrada() throws Exception {
        when(resenaService.getResenaPorId(99L)).thenThrow(new RuntimeException("No encontrada"));

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/Resena/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(resenaEjemplo)))
            .andExpect(status().isNotFound());
    }

    @Test
    void testActualizarResenaInvalida() throws Exception {
        when(resenaService.getResenaPorId(1L)).thenReturn(resenaEjemplo);
        when(resenaService.saveResena(any(Resena.class))).thenThrow(new IllegalArgumentException("Comentario obligatorio"));

        Resena conError = new Resena(1L, 100L, 200L, "", Date.valueOf("2024-06-22"), 3);

        mockMvc.perform(MockMvcRequestBuilders
                .put("/api/v1/Resena/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(conError)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Comentario obligatorio"));
    }
}
