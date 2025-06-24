package Estado.com.example.ESTADO.controller;

import Estado.com.example.ESTADO.Controller.ESTADOController;
import Estado.com.example.ESTADO.Model.ESTADO;
import Estado.com.example.ESTADO.Service.ESTADOService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ESTADOController.class)
public class ESTADOControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ESTADOService estadoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void obtenerEstado_returnsOk_whenListNotEmpty() throws Exception {
        ESTADO estado1 = ESTADO.builder().id(1L).build();
        ESTADO estado2 = ESTADO.builder().id(2L).build();
        List<ESTADO> estados = Arrays.asList(estado1, estado2);
        when(estadoService.obEstados()).thenReturn(estados);

        mockMvc.perform(get("/api/v1/estado"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id", is(1)));

        verify(estadoService, times(1)).obEstados();
    }

    @Test
    void obtenerEstado_returnsNoContent_whenListEmpty() throws Exception {
        when(estadoService.obEstados()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/estado"))
            .andExpect(status().isNoContent());

        verify(estadoService, times(1)).obEstados();
    }

    @Test
    void guardarEstado_returnsCreated_whenSuccessful() throws Exception {
        ESTADO nuevoEstado = ESTADO.builder().build();
        ESTADO estadoGuardado = ESTADO.builder().id(1L).build();
        when(estadoService.guardarEstado(nuevoEstado)).thenReturn(estadoGuardado);

        mockMvc.perform(post("/api/v1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEstado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(estadoService, times(1)).guardarEstado(nuevoEstado);
    }

    @Test
    void guardarEstado_returnsInternalServerError_whenExceptionOccurs() throws Exception {
        ESTADO nuevoEstado = ESTADO.builder().build();
        when(estadoService.guardarEstado(nuevoEstado)).thenThrow(new RuntimeException("Error interno"));

        mockMvc.perform(post("/api/v1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEstado)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(containsString("Error interno")));

        verify(estadoService, times(1)).guardarEstado(nuevoEstado);
    }
}
