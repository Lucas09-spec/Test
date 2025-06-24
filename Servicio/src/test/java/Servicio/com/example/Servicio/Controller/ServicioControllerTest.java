package Servicio.com.example.Servicio.Controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import Servicio.com.example.Servicio.Model.Servicio;
import Servicio.com.example.Servicio.Service.ServicioService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ServicioController.class)
public class ServicioControllerTest {

    @MockBean
    private ServicioService servicioService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerServicios_retornaOKyJson() throws Exception {
        List<Servicio> servicios = Arrays.asList(
            new Servicio(1L, "Internet Fibra", 19990L, true)
        );

        when(servicioService.ObtenerLista()).thenReturn(servicios);

        mockMvc.perform(get("/api/v1/Servicio"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].descripcion").value("Internet Fibra"))
            .andExpect(jsonPath("$[0].precio").value(19990))
            .andExpect(jsonPath("$[0].disp").value(true));
    }

    @Test
    void guardarServicio_retornaCreated() throws Exception {
        Servicio nuevo = new Servicio(null, "Internet Satelital", 24990L, true);
        Servicio guardado = new Servicio(2L, "Internet Satelital", 24990L, true);

        when(servicioService.guardarServicio(nuevo)).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/Servicio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(2L))
            .andExpect(jsonPath("$.descripcion").value("Internet Satelital"))
            .andExpect(jsonPath("$.precio").value(24990))
            .andExpect(jsonPath("$.disp").value(true));
    }

    @Test
    void eliminarServicio_existente_retornaNoContent() throws Exception {
        long id = 1L;
        when(servicioService.eliminarServicio(id)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/Servicio/{id}", id))
            .andExpect(status().isNoContent());
    }

    @Test
    void eliminarServicio_noExistente_retornaNotFound() throws Exception {
        long id = 99L;
        when(servicioService.eliminarServicio(id)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/Servicio/{id}", id))
            .andExpect(status().isNotFound());
    }

    @Test
    void actualizarServicio_existente_retornaOk() throws Exception {
        long id = 1L;
        Servicio actualizado = new Servicio(id, "Internet Actualizado", 29990L, false);

        when(servicioService.actualizarServicio(eq(id), any(Servicio.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/Servicio/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.descripcion").value("Internet Actualizado"))
            .andExpect(jsonPath("$.precio").value(29990))
            .andExpect(jsonPath("$.disp").value(false));
    }

    @Test
    void actualizarServicio_noExistente_retornaNotFound() throws Exception {
        long id = 99L;
        Servicio actualizado = new Servicio(id, "Internet X", 10000L, false);

        when(servicioService.actualizarServicio(eq(id), any(Servicio.class))).thenReturn(null);

        mockMvc.perform(put("/api/v1/Servicio/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
            .andExpect(status().isNotFound());
    }
} 
