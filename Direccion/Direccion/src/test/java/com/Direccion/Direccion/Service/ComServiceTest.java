package com.Direccion.Direccion.Service;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Direccion.Model.Comuna;
import com.Direccion.Repository.ComRepository;
import com.Direccion.Service.ComService;

@ExtendWith(MockitoExtension.class)
public class ComServiceTest {

    @Mock
    private ComRepository repository;

    @InjectMocks
    private ComService service;

    @Test
    void saveComuna_returnSaved() {
        Comuna comuna = new Comuna();
        comuna.setId_com(1L);
        comuna.setId_reg(2L);
        comuna.setNom_com("Ñuñoa");

        when(repository.save(comuna)).thenReturn(comuna);

        Comuna result = service.saveComuna(comuna);

        assertNotNull(result);
        assertEquals("Ñuñoa", result.getNom_com());
        verify(repository).save(comuna);
    }

    @Test
    void saveComuna_missingFields_throwsException() {
        Comuna comuna = new Comuna(); // campos vacíos

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.saveComuna(comuna);
        });

        assertEquals("Todos los campos son obligatorios", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void getComunas_returnsList() {
        Comuna comuna1 = new Comuna();
        Comuna comuna2 = new Comuna();
        when(repository.findAll()).thenReturn(Arrays.asList(comuna1, comuna2));

        List<Comuna> lista = service.getComunas();

        assertEquals(2, lista.size());
        verify(repository).findAll();
    }

    @Test
    void getComunaPorId_exists_returnsComuna() {
        Comuna comuna = new Comuna();
        comuna.setId_com(10L);

        when(repository.findById(10L)).thenReturn(Optional.of(comuna));

        Comuna result = service.getComunaPorId(10L);

        assertEquals(10L, result.getId_com());
        verify(repository).findById(10L);
    }

    @Test
    void getComunaPorId_notFound_throwsException() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.getComunaPorId(99L);
        });

        assertEquals("Comuna no encontrada", exception.getMessage());
        verify(repository).findById(99L);
    }
}
