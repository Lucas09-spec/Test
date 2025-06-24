package Preguntas.com.example.PreguntasFrecuentes.PreguntasServiceTest;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import Preguntas.com.example.PreguntasFrecuentes.Model.Preguntas;
import Preguntas.com.example.PreguntasFrecuentes.Repository.PreguntaRepository;
import Preguntas.com.example.PreguntasFrecuentes.Service.PreguntasService;

@ExtendWith(MockitoExtension.class)
public class PreguntasServicetest {

    @Mock
    private PreguntaRepository preguntaRepository;

    @InjectMocks
    private PreguntasService preguntasService;

    @Test
    void obtenPreguntas_retornaLista() {
        Preguntas p1 = new Preguntas();
        Preguntas p2 = new Preguntas();
        List<Preguntas> listaMock = Arrays.asList(p1, p2);

        when(preguntaRepository.findAll()).thenReturn(listaMock);

        List<Preguntas> resultado = preguntasService.obtenPreguntas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(preguntaRepository).findAll();
    }

    @Test
    void guardar_delegaEnRepositorio() {
        Preguntas pregunta = new Preguntas();
        when(preguntaRepository.save(pregunta)).thenReturn(pregunta);

        Preguntas resultado = preguntasService.guardar(pregunta);

        assertNotNull(resultado);
        verify(preguntaRepository).save(pregunta);
    }

    @Test
    void eliminar_llamaDeleteById() {
        Long id = 1L;

        // Método void, solo verificamos que se llama con el parámetro correcto
        doNothing().when(preguntaRepository).deleteById(id);

        preguntasService.eleminar(id);

        verify(preguntaRepository).deleteById(id);
    }
}