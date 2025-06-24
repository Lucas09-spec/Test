package com.Contrato.Contrato.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.sql.Date;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.Controller.ContratoController;
import com.model.Contrato;
import com.service.ContratoService;
import com.Webproyecto.WebUsuario;
import com.Webproyecto.ServicioClient;
import com.Webproyecto.DireccionClient;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ContratoController.class)
public class ContratoControlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContratoService contratoService;

    @MockBean
    private WebUsuario webUsuario;

    @MockBean
    private ServicioClient servicioClient;

    @MockBean
    private DireccionClient direccionClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerContratos_debeRetornarLista200() throws Exception {
        Contrato contrato = new Contrato();
        contrato.setId(1L);
        contrato.setFecha_contrato(Date.valueOf("2024-01-01"));
        contrato.setFecha_inicio(Date.valueOf("2024-01-05"));
        contrato.setFecha_final(Date.valueOf("2024-12-31"));
        contrato.setId_usuario(1L);
        contrato.setId_direcc(1L);
        contrato.setId_servicio(1L);
        contrato.setTotal(100000);

        when(contratoService.getContratos()).thenReturn(Arrays.asList(contrato));

        mockMvc.perform(get("/api/v1/contrato"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void obtenerContratoPorId_debeRetornar200() throws Exception {
        Contrato contrato = new Contrato();
        contrato.setId(1L);

        when(contratoService.getContratoPorId(1L)).thenReturn(contrato);

        mockMvc.perform(get("/api/v1/contrato/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void guardarContrato_debeRetornar201() throws Exception {
        Contrato contrato = new Contrato();
        contrato.setFecha_contrato(Date.valueOf("2024-01-01"));
        contrato.setFecha_inicio(Date.valueOf("2024-01-05"));
        contrato.setFecha_final(Date.valueOf("2024-12-31"));
        contrato.setId_usuario(1L);
        contrato.setId_direcc(1L);
        contrato.setId_servicio(1L);
        contrato.setTotal(100000);

        when(webUsuario.existeUsuario(1L)).thenReturn(true);
        when(servicioClient.existeServicio(1L)).thenReturn(true);
        when(direccionClient.existeDireccion(1L)).thenReturn(true);
        when(contratoService.saveContrato(any(Contrato.class))).thenReturn(contrato);

        mockMvc.perform(post("/api/v1/contrato")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(contrato)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id_usuario").value(1));
    }

    @Test
    void actualizarContrato_debeRetornar200() throws Exception {
        Contrato actualizado = new Contrato();
        actualizado.setFecha_contrato(Date.valueOf("2024-01-01"));
        actualizado.setFecha_inicio(Date.valueOf("2024-01-05"));
        actualizado.setFecha_final(Date.valueOf("2024-12-31"));
        actualizado.setId_usuario(1L);
        actualizado.setId_direcc(1L);
        actualizado.setId_servicio(1L);
        actualizado.setTotal(100000);

        when(webUsuario.existeUsuario(1L)).thenReturn(true);
        when(servicioClient.existeServicio(1L)).thenReturn(true);
        when(direccionClient.existeDireccion(1L)).thenReturn(true);
        when(contratoService.actualizarContrato(eq(1L), any(Contrato.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/contrato/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.total").value(100000));
    }
}
