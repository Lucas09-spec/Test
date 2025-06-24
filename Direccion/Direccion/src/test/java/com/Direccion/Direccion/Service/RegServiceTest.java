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

import com.Direccion.Model.Region;
import com.Direccion.Repository.RegRepository;
import com.Direccion.Service.RegService;

@ExtendWith(MockitoExtension.class)
public class RegServiceTest {

    @Mock
    private RegRepository repository;

    @InjectMocks
    private RegService service;

    @Test
    void saveRegion_returnSaved() {
        Region region = new Region();
        region.setId_reg(1L);
        region.setNom_reg("Región Metropolitana");

        when(repository.save(region)).thenReturn(region);

        Region result = service.saveRegion(region);

        assertNotNull(result);
        assertEquals("Región Metropolitana", result.getNom_reg());
        verify(repository).save(region);
    }

    @Test
    void saveRegion_missingName_throwsException() {
        Region region = new Region(); // Nom_reg es null

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            service.saveRegion(region);
        });

        assertEquals("Todos los campos son obligatorios", exception.getMessage());
        verify(repository, never()).save(any());
    }

    @Test
    void getRegions_returnsList() {
        Region r1 = new Region();
        Region r2 = new Region();
        when(repository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Region> regiones = service.getRegions();

        assertEquals(2, regiones.size());
        verify(repository).findAll();
    }

    @Test
    void getRegionPorId_exists_returnsRegion() {
        Region r = new Region();
        r.setId_reg(5L);

        when(repository.findById(5L)).thenReturn(Optional.of(r));

        Region result = service.getRegionPorId(5L);

        assertEquals(5L, result.getId_reg());
        verify(repository).findById(5L);
    }

    @Test
    void getRegionPorId_notFound_throwsException() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            service.getRegionPorId(999L);
        });

        assertEquals("Region no encontrada", exception.getMessage());
        verify(repository).findById(999L);
    }
}
