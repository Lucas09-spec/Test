package com.Direccion.Direccion.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.Direccion.Model.Direccion;
import com.Direccion.Repository.DireccionRepository;
import com.Direccion.Service.DirService;

@ExtendWith(MockitoExtension.class)
public class DirServiceTest {

    @Mock
    private DireccionRepository repository;

    @InjectMocks

    private DirService service;

       @Test
    void saveDireccion_returnSaved() {
        Direccion dir = new Direccion();
        dir.setId_dir(1L);
        dir.setId_com(2L);
        dir.setId_reg(3L);
        dir.setNom_dir("Avenida Siempre Viva 742");

        when(repository.save(dir)).thenReturn(dir);

        Direccion result = service.saveDireccion(dir);

        assertNotNull(result);
        assertEquals("Avenida Siempre Viva 742", result.getNom_dir());
        verify(repository).save(dir);
    }

    @Test
    void saveDireccion_missingFields_throwsException() {
        Direccion dir = new Direccion(); // campos nulos

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.saveDireccion(dir);
        });

        assertEquals("Todos los argumentos deben ser obligatorios", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void getDireccion_returnsList() {
        Direccion dir1 = new Direccion();
        Direccion dir2 = new Direccion();
        when(repository.findAll()).thenReturn(Arrays.asList(dir1, dir2));

        List<Direccion> lista = service.getDireccion();

        assertEquals(2, lista.size());
        verify(repository).findAll();
    }

    @Test
    void getDireccionPorId_exists_returnsDireccion() {
        Direccion dir = new Direccion();
        dir.setId_dir(100L);

        when(repository.findById(100L)).thenReturn(Optional.of(dir));

        Direccion result = service.getDireccionPorId(100L);

        assertEquals(100L, result.getId_dir());
        verify(repository).findById(100L);
    }

    @Test
    void getDireccionPorId_notFound_throwsException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.getDireccionPorId(999L);
        });

        assertEquals("Todos los campos son obligatorios", exception.getMessage());
        verify(repository).findById(999L);
    }
}
