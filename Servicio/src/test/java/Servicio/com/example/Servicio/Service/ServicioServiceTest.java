package Servicio.com.example.Servicio.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import Servicio.com.example.Servicio.Model.Servicio;
import Servicio.com.example.Servicio.Repository.ServicioRepository;

public class ServicioServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @InjectMocks
    private ServicioService servicioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarServicio_debeGuardarCorrectamente() {
        Servicio nuevo = new Servicio(null, "Servicio A", 10000L, true);
        when(servicioRepository.save(nuevo)).thenReturn(nuevo);

        Servicio resultado = servicioService.guardarServicio(nuevo);

        assertEquals(nuevo, resultado);
        verify(servicioRepository).save(nuevo);
    }

    @Test
    void obtenerLista_debeRetornarListaDeServicios() {
        List<Servicio> lista = Arrays.asList(
            new Servicio(1L, "Servicio 1", 10000L, true),
            new Servicio(2L, "Servicio 2", 20000L, false)
        );
        when(servicioRepository.findAll()).thenReturn(lista);

        List<Servicio> resultado = servicioService.ObtenerLista();

        assertEquals(2, resultado.size());
        verify(servicioRepository).findAll();
    }

    @Test
    void eliminarServicio_existenteDebeEliminarYRetornarTrue() {
        long id = 1L;
        when(servicioRepository.existsById(id)).thenReturn(true);

        boolean resultado = servicioService.eliminarServicio(id);

        assertTrue(resultado);
        verify(servicioRepository).deleteById(id);
    }

    @Test
    void eliminarServicio_noExistenteDebeRetornarFalse() {
        long id = 2L;
        when(servicioRepository.existsById(id)).thenReturn(false);

        boolean resultado = servicioService.eliminarServicio(id);

        assertFalse(resultado);
        verify(servicioRepository, never()).deleteById(id);
    }

    @Test
    void actualizarServicio_existenteDebeActualizarYRetornarServicio() {
        long id = 1L;
        Servicio original = new Servicio(id, "Antiguo", 5000L, false);
        Servicio actualizado = new Servicio(id, "Nuevo", 10000L, true);

        when(servicioRepository.findById(id)).thenReturn(Optional.of(original));
        when(servicioRepository.save(any(Servicio.class))).thenReturn(actualizado);

        Servicio resultado = servicioService.actualizarServicio(id, actualizado);

        assertEquals("Nuevo", resultado.getDescripcion());
        assertEquals(10000L, resultado.getPrecio());
        assertTrue(resultado.isDisp());
        verify(servicioRepository).save(original);
    }

    @Test
    void actualizarServicio_noExistenteDebeRetornarNull() {
        long id = 99L;
        Servicio actualizado = new Servicio(id, "Nuevo", 10000L, true);

        when(servicioRepository.findById(id)).thenReturn(Optional.empty());

        Servicio resultado = servicioService.actualizarServicio(id, actualizado);

        assertNull(resultado);
        verify(servicioRepository, never()).save(any(Servicio.class));
    }
} 
