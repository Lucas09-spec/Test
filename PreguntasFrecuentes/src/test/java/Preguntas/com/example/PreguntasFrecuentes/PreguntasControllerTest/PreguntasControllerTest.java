package Preguntas.com.example.PreguntasFrecuentes.PreguntasControllerTest;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import Preguntas.com.example.PreguntasFrecuentes.Controller.Preguntascontroller;
import Preguntas.com.example.PreguntasFrecuentes.Model.Preguntas;
import Preguntas.com.example.PreguntasFrecuentes.Service.PreguntasService;

@WebMvcTest(Preguntascontroller.class)
public class PreguntasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PreguntasService preguntasService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void listar_deberiaRetornarLista() throws Exception {
        Preguntas p1 = new Preguntas();
        Preguntas p2 = new Preguntas();
        List<Preguntas> lista = Arrays.asList(p1, p2);

        when(preguntasService.obtenPreguntas()).thenReturn(lista);

        mockMvc.perform(get("/api/v1/preguntas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(lista.size()));

        verify(preguntasService).obtenPreguntas();
    }

    @Test
    void crear_deberiaGuardarYPasarObjeto() throws Exception {
        Preguntas pregunta = new Preguntas();
        pregunta.setId(1L);
        // Aquí setea campos si los tienes en Preguntas

        when(preguntasService.guardar(any(Preguntas.class))).thenReturn(pregunta);

        mockMvc.perform(post("/api/v1/preguntas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(pregunta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pregunta.getId()));

        verify(preguntasService).guardar(any(Preguntas.class));
    }

    @Test
    void eliminar_deberiaLlamarServicioYResponder204() throws Exception {
        Long id = 1L;
        doNothing().when(preguntasService).eleminar(id);

        mockMvc.perform(delete("/api/v1/preguntas/{id}", id))
                .andExpect(status().isNoContent());

        verify(preguntasService).eleminar(id);
    }
}
