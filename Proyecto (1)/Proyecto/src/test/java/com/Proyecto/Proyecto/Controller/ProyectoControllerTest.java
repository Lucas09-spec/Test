package com.Proyecto.Proyecto.Controller;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.Proyecto.Controller.ProyectoController;
import com.Proyecto.Model.Proyecto;
import com.Proyecto.Service.ProyectoService;
import com.Proyecto.WebClient.TecnicoClient;
import com.Proyecto.WebClient.WebClientC1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ProyectoController.class)
public class ProyectoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProyectoService proyectoService;

    @MockBean
    private WebClientC1 contratoClient;

    @MockBean
    private TecnicoClient tecnicoClient;

    @Autowired
    private ObjectMapper objectMapper;

    private Proyecto proyecto1;
    private Proyecto proyecto2;

    @BeforeEach
    void setUp() {
        proyecto1 = new Proyecto();
        proyecto1.setId_proyecto(1L);
        proyecto1.setId_contrato(10L);
        proyecto1.setId_tecnico(20L);
        proyecto1.setId_estado(30L);
        proyecto1.setFecha(LocalDate.of(2023, 1, 1));
        proyecto1.setComentarios("Proyecto 1");

        proyecto2 = new Proyecto();
        proyecto2.setId_proyecto(2L);
        proyecto2.setId_contrato(11L);
        proyecto2.setId_tecnico(21L);
        proyecto2.setId_estado(31L);
        proyecto2.setFecha(LocalDate.of(2023, 2, 2));
        proyecto2.setComentarios("Proyecto 2");
    }

    @Test
    void obtenerProyectos_retornaOkConLista() throws Exception {
        when(proyectoService.getProyectos()).thenReturn(List.of(proyecto1, proyecto2));

        mockMvc.perform(get("/api/v1/proyecto"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].Id_proyecto").value(1))
            .andExpect(jsonPath("$[0].comentarios").value("Proyecto 1"))
            .andExpect(jsonPath("$[1].Id_proyecto").value(2))
            .andExpect(jsonPath("$[1].comentarios").value("Proyecto 2"));
    }

    @Test
    void obtenerProyectos_retornaNoContent() throws Exception {
        when(proyectoService.getProyectos()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/proyecto"))
            .andExpect(status().isNoContent());
    }

    @Test
    void obtenerProyectoPorId_retornaOk() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.of(proyecto1));

        mockMvc.perform(get("/api/v1/proyecto/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.Id_proyecto").value(1))
            .andExpect(jsonPath("$.comentarios").value("Proyecto 1"));
    }

    @Test
    void obtenerProyectoPorId_retornaNotFound() throws Exception {
        when(proyectoService.getProyectoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/proyecto/99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void guardarProyecto_retornaOk() throws Exception {
        when(contratoClient.getContratoById(proyecto1.getId_contrato())).thenReturn(
            java.util.Map.of("Id_contrato", proyecto1.getId_contrato())
        );
        when(tecnicoClient.getTecnicoById(proyecto1.getId_tecnico())).thenReturn(
            java.util.Map.of("Id_tecnico", proyecto1.getId_tecnico())
        );
        when(proyectoService.saveProyecto(any(Proyecto.class))).thenReturn(proyecto1);

        mockMvc.perform(post("/api/v1/proyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(proyecto1)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.Id_proyecto").value(1))
            .andExpect(jsonPath("$.comentarios").value("Proyecto 1"));
    }

    @Test
    void guardarProyecto_retornaBadRequest_porCamposInvalidos() throws Exception {
        Proyecto invalidProyecto = new Proyecto();

        mockMvc.perform(post("/api/v1/proyecto")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidProyecto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void asignarTecnico_retornaOk() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.of(proyecto1));
        when(tecnicoClient.getTecnicoById(99L)).thenReturn(java.util.Map.of("Id_tecnico", 99L));
        proyecto1.setId_tecnico(99L);
        when(proyectoService.saveProyecto(any(Proyecto.class))).thenReturn(proyecto1);

        mockMvc.perform(put("/api/v1/proyecto/asignar-tecnico/1")
                .param("idTecnico", "99"))
            .andExpect(status().isOk())
            .andExpect(content().string("Técnico asignado correctamente."));
    }

    @Test
    void asignarTecnico_retornaNotFound() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/proyecto/asignar-tecnico/1")
                .param("idTecnico", "99"))
            .andExpect(status().isNotFound());
    }

    @Test
    void asignarTecnico_retornaBadRequest_porTecnicoNoExiste() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.of(proyecto1));
        when(tecnicoClient.getTecnicoById(99L)).thenReturn(null);

        mockMvc.perform(put("/api/v1/proyecto/asignar-tecnico/1")
                .param("idTecnico", "99"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("El ID del técnico no existe."));
    }

    @Test
    void cambiarEstado_retornaOk() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.of(proyecto1));
        proyecto1.setId_estado(50L);
        when(proyectoService.saveProyecto(any(Proyecto.class))).thenReturn(proyecto1);

        mockMvc.perform(put("/api/v1/proyecto/cambiar-estado/1")
                .param("nuevoEstadoId", "50"))
            .andExpect(status().isOk())
            .andExpect(content().string("Estado del proyecto actualizado correctamente."));
    }

    @Test
    void cambiarEstado_retornaNotFound() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/proyecto/cambiar-estado/1")
                .param("nuevoEstadoId", "50"))
            .andExpect(status().isNotFound());
    }

    @Test
    void eliminarProyecto_retornaOk() throws Exception {
        when(proyectoService.getProyectoById(1L)).thenReturn(Optional.of(proyecto1));
        doNothing().when(proyectoService).deleteProyecto(1L);

        mockMvc.perform(delete("/api/v1/proyecto/1"))
            .andExpect(status().isOk())
            .andExpect(content().string("Proyecto eliminado correctamente."));
    }

    @Test
    void eliminarProyecto_retornaNotFound() throws Exception {
        when(proyectoService.getProyectoById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/proyecto/99"))
            .andExpect(status().isNotFound());
    }
}
