package com.example.Respuesta.service;

import com.example.Respuesta.model.Respuesta;
import com.example.Respuesta.repository.RespuestaRepository;
import com.example.Respuesta.webclient.SoporteClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RespuestaServiceTest {

    @Mock
    private RespuestaRepository respuestaRepository;

    @Mock
    private SoporteClient soporteClient;

    @InjectMocks
    private RespuestaService respuestaService;

    @Test
    void findAll_returnsListOfRespuestas() {
        // Crear dos instancias ficticias de Respuesta
        Respuesta resp1 = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();
        Respuesta resp2 = Respuesta.builder()
                .id(2L)
                .soporteId(2L)
                .build();

        List<Respuesta> respuestas = Arrays.asList(resp1, resp2);
        when(respuestaRepository.findAll()).thenReturn(respuestas);

        List<Respuesta> result = respuestaService.findAll();
        assertThat(result)
                .hasSize(2)
                .contains(resp1, resp2);
        verify(respuestaRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsRespuesta() {
        Respuesta respuesta = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();
        when(respuestaRepository.findById(1L)).thenReturn(Optional.of(respuesta));

        Optional<Respuesta> result = respuestaService.findById(1L);
        assertThat(result).isPresent().contains(respuesta);
        verify(respuestaRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(respuestaRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Respuesta> result = respuestaService.findById(99L);
        assertThat(result).isNotPresent();
        verify(respuestaRepository, times(1)).findById(99L);
    }

    @Test
    void saveSoporte_whenSoporteFound_savesAndReturnsRespuesta() {
        Respuesta nuevaRespuesta = Respuesta.builder()
                .soporteId(1L)
                .build();

        // Simular respuesta válida del SoporteClient
        Map<String, Object> soporteData = new HashMap<>();
        soporteData.put("id", 1L);
        soporteData.put("titulo", "Soporte existente");

        when(soporteClient.getSoporteById(1L)).thenReturn(soporteData);

        Respuesta respuestaGuardada = Respuesta.builder()
                .id(1L)
                .soporteId(1L)
                .build();
        when(respuestaRepository.save(nuevaRespuesta)).thenReturn(respuestaGuardada);

        Respuesta result = respuestaService.saveSoporte(nuevaRespuesta);
        assertThat(result.getId()).isEqualTo(1L);
        verify(soporteClient, times(1)).getSoporteById(1L);
        verify(respuestaRepository, times(1)).save(nuevaRespuesta);
    }

    @Test
    void saveSoporte_whenSoporteNotFound_throwsException() {
        Respuesta nuevaRespuesta = Respuesta.builder()
                .soporteId(1L)
                .build();
        // Simula que el SoporteClient retorna un mapa vacío
        when(soporteClient.getSoporteById(1L)).thenReturn(Collections.emptyMap());

        RuntimeException exception = catchThrowableOfType(
                () -> respuestaService.saveSoporte(nuevaRespuesta),
                RuntimeException.class
        );
        assertThat(exception).hasMessage("Soporte no encontrado. No se puede agregar el pedido");
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        Long id = 1L;
        doNothing().when(respuestaRepository).deleteById(id);
        respuestaService.delete(id);
        verify(respuestaRepository, times(1)).deleteById(id);
    }

    // Método auxiliar para capturar excepciones usando AssertJ de forma personalizada
    private <T extends Throwable> T catchThrowableOfType(Runnable runnable, Class<T> type) {
        try {
            runnable.run();
        } catch (Throwable t) {
            if (type.isInstance(t)) {
                return type.cast(t);
            }
        }
        throw new AssertionError("No se lanzó la excepción esperada de tipo " + type.getName());
    }
}
