package com.resena.Resena.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.resena.model.Resena;
import com.resena.repository.ResenaRepository;
import com.resena.WebClient.UserClient;
import com.resena.WebClient.WebServicio;
import com.resena.service.ResenaService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.sql.Date;
import java.util.*;

public class ResenaServiceTest {

    @InjectMocks
    private ResenaService resenaService;

    @Mock
    private ResenaRepository resenaRepository;

    @Mock
    private WebServicio webServicio;

    @Mock
    private UserClient webUser;

    private Resena resenaEjemplo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
    void testGetResenas() {
        List<Resena> lista = Arrays.asList(resenaEjemplo);
        when(resenaRepository.findAll()).thenReturn(lista);

        List<Resena> resultado = resenaService.getResenas();
        assertEquals(1, resultado.size());
        assertEquals("Muy buen servicio", resultado.get(0).getComentario());
    }

    @Test
    void testGetResenaPorIdFound() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.of(resenaEjemplo));
        Resena resultado = resenaService.getResenaPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdResena());
    }

    @Test
    void testGetResenaPorIdNotFound() {
        when(resenaRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resenaService.getResenaPorId(1L);
        });
        assertTrue(exception.getMessage().contains("no encontrada"));
    }

    @Test
    void testSaveResenaSuccess() {
        // Mock WebServicio and UserClient responses for validation
        when(webServicio.getServicioById(resenaEjemplo.getIdServicio()))
                .thenReturn(Map.of("id", resenaEjemplo.getIdServicio()));

        when(webUser.getUserById(resenaEjemplo.getIdUsuario()))
                .thenReturn(Map.of("id", resenaEjemplo.getIdUsuario()));

        when(resenaRepository.save(resenaEjemplo)).thenReturn(resenaEjemplo);

        Resena resultado = resenaService.saveResena(resenaEjemplo);
        assertNotNull(resultado);
        assertEquals(resenaEjemplo.getComentario(), resultado.getComentario());
    }

    @Test
    void testSaveResenaInvalidNota() {
        Resena resenaMal = new Resena(
            2L,
            100L,
            200L,
            "Comentario malo",
            Date.valueOf("2024-06-22"),
            10 // nota inválida > 5
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            resenaService.saveResena(resenaMal);
        });
        assertTrue(exception.getMessage().contains("nota"));
    }

    @Test
    void testDeleteResenaSuccess() {
        when(resenaRepository.existsById(1L)).thenReturn(true);
        doNothing().when(resenaRepository).deleteById(1L);

        assertDoesNotThrow(() -> resenaService.deleteResena(1L));
        verify(resenaRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteResenaNotFound() {
        when(resenaRepository.existsById(1L)).thenReturn(false);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            resenaService.deleteResena(1L);
        });
        assertTrue(exception.getMessage().contains("no encontrada"));
    }
}
