package com.example.Respuesta.controller;

import com.example.Respuesta.model.Respuesta;
import com.example.Respuesta.service.RespuestaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RespuestaController.class)
public class RespuestaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RespuestaService respuestaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRespuestas_returnsOk() throws Exception {
        Respuesta r1 = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();
        Respuesta r2 = Respuesta.builder()
                .id(2L)
                .soporteId(2L)
                .build();
        List<Respuesta> respuestas = Arrays.asList(r1, r2);
        when(respuestaService.findAll()).thenReturn(respuestas);

        mockMvc.perform(get("/api/v1/respuestas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].soporteId").value(1));
    }

    @Test
    void getRespuestaById_whenExists_returnsOk() throws Exception {
        Respuesta respuesta = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();
        when(respuestaService.findById(1L)).thenReturn(Optional.of(respuesta));

        mockMvc.perform(get("/api/v1/respuestas/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.soporteId").value(1));
    }

    @Test
    void getRespuestaById_whenNotFound_returnsNotFound() throws Exception {
        when(respuestaService.findById(99L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/v1/respuestas/{id}", 99L))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRespuesta_whenSuccessful_returnsOk() throws Exception {
        Respuesta nuevaRespuesta = Respuesta.builder()
                .soporteId(1L)
                .build();
        Respuesta respuestaGuardada = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();
        when(respuestaService.saveSoporte(nuevaRespuesta)).thenReturn(respuestaGuardada);

        mockMvc.perform(post("/api/v1/respuestas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaRespuesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.soporteId").value(1));
    }

    @Test
    void createRespuesta_whenException_returnsBadRequest() throws Exception {
        Respuesta nuevaRespuesta = Respuesta.builder()
                .soporteId(1L)
                .build();
        when(respuestaService.saveSoporte(nuevaRespuesta)).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(post("/api/v1/respuestas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevaRespuesta)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRespuesta_whenExistsAndSuccessful_returnsOk() throws Exception {
        Respuesta respuestaExistente = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();

        Respuesta respuestaActualizada = Respuesta.builder()
                // No se asigna id aquí; se fija en el controller
                .soporteId(1L)
                .build();

        Respuesta respuestaGuardada = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();

        when(respuestaService.findById(1L)).thenReturn(Optional.of(respuestaExistente));
        when(respuestaService.saveSoporte(Mockito.any(Respuesta.class))).thenReturn(respuestaGuardada);

        mockMvc.perform(put("/api/v1/respuestas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(respuestaActualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.soporteId").value(1));
    }

    @Test
    void updateRespuesta_whenNotFound_returnsNotFound() throws Exception {
        Respuesta respuestaActualizada = Respuesta.builder()
                .soporteId(1L)
                .build();
        when(respuestaService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/respuestas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(respuestaActualizada)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateRespuesta_whenException_returnsBadRequest() throws Exception {
        Respuesta respuestaExistente = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();

        Respuesta respuestaActualizada = Respuesta.builder()
                .soporteId(1L)
                .build();

        when(respuestaService.findById(1L)).thenReturn(Optional.of(respuestaExistente));
        when(respuestaService.saveSoporte(Mockito.any(Respuesta.class))).thenThrow(new RuntimeException("Error"));

        mockMvc.perform(put("/api/v1/respuestas/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(respuestaActualizada)))
                .andExpect(status().isBadRequest());
    }
}
