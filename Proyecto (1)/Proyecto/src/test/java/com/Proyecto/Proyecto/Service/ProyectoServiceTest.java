package com.Proyecto.Proyecto.Service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.*;

import com.Proyecto.Model.Proyecto;
import com.Proyecto.Repository.ProyectoRepository;
import com.Proyecto.Service.ProyectoService;
import com.Proyecto.WebClient.EstadoClient;
import com.Proyecto.WebClient.TecnicoClient;
import com.Proyecto.WebClient.WebClientC1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProyectoServiceTest {

    @Mock
    private ProyectoRepository proyectoRepository;

    @Mock
    private WebClientC1 webClientC1;

    @Mock
    private TecnicoClient tecnicoClient;

    @Mock
    private EstadoClient estadoClient;

    @InjectMocks
    private ProyectoService proyectoService;

    private Proyecto proyecto1;

    @BeforeEach
    void setup() {
        proyecto1 = new Proyecto(1L, 10L, 20L, LocalDate.of(2023,1,1), "Comentario test", 30L);
    }

    @Test
    void getProyectos_debeDevolverLista() {
        List<Proyecto> lista = List.of(proyecto1);
        when(proyectoRepository.findAll()).thenReturn(lista);

        List<Proyecto> resultado = proyectoService.getProyectos();

        assertThat(resultado).isEqualTo(lista);
        verify(proyectoRepository).findAll();
    }

    @Test
    void getProyectoById_debeDevolverProyecto() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto1));

        Optional<Proyecto> resultado = proyectoService.getProyectoById(1L);

        assertTrue(resultado.isPresent());
        assertEquals(proyecto1, resultado.get());
        verify(proyectoRepository).findById(1L);
    }

    @Test
    void saveProyecto_debeGuardarYDevolverProyecto() {
        when(proyectoRepository.save(proyecto1)).thenReturn(proyecto1);

        Proyecto resultado = proyectoService.saveProyecto(proyecto1);

        assertEquals(proyecto1, resultado);
        verify(proyectoRepository).save(proyecto1);
    }

    @Test
    void cambiarEstadoProyecto_conEstadoValido_cambiaYGuarda() {
        Long nuevoEstadoId = 50L;
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto1));
        when(estadoClient.getEstadoById(nuevoEstadoId)).thenReturn(Map.of("id", nuevoEstadoId));
        when(proyectoRepository.save(any(Proyecto.class))).thenAnswer(i -> i.getArgument(0));

        Proyecto resultado = proyectoService.cambiarEstadoProyecto(1L, nuevoEstadoId);

        assertEquals(nuevoEstadoId, resultado.getId_estado());
        verify(proyectoRepository).findById(1L);
        verify(estadoClient).getEstadoById(nuevoEstadoId);
        verify(proyectoRepository).save(any(Proyecto.class));
    }

    @Test
    void cambiarEstadoProyecto_conEstadoInvalido_lanzaExcepcion() {
        Long nuevoEstadoId = 999L;
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto1));
        doThrow(new RuntimeException("No existe")).when(estadoClient).getEstadoById(nuevoEstadoId);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proyectoService.cambiarEstadoProyecto(1L, nuevoEstadoId);
        });

        assertEquals("El ID de estado no existe.", exception.getMessage());
        verify(proyectoRepository).findById(1L);
        verify(estadoClient).getEstadoById(nuevoEstadoId);
    }

    @Test
    void cambiarEstadoProyecto_conProyectoNoExistente_lanzaExcepcion() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proyectoService.cambiarEstadoProyecto(1L, 50L);
        });

        assertEquals("Proyecto con ID 1 no encontrado.", exception.getMessage());
        verify(proyectoRepository).findById(1L);
        verifyNoInteractions(estadoClient);
    }

    @Test
    void asignarTecnicoAProyecto_conTecnicoValido_asignaYGuarda() {
        Long idTecnico = 99L;
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto1));
        when(tecnicoClient.getTecnicoById(idTecnico)).thenReturn(Map.of("Id_tecnico", idTecnico));
        when(proyectoRepository.save(any(Proyecto.class))).thenAnswer(i -> i.getArgument(0));

        Proyecto resultado = proyectoService.asignarTecnicoAProyecto(1L, idTecnico);

        assertEquals(idTecnico, resultado.getId_tecnico());
        verify(proyectoRepository).findById(1L);
        verify(tecnicoClient).getTecnicoById(idTecnico);
        verify(proyectoRepository).save(any(Proyecto.class));
    }

    @Test
    void asignarTecnicoAProyecto_conTecnicoInvalido_lanzaExcepcion() {
        Long idTecnico = 999L;
        when(proyectoRepository.findById(1L)).thenReturn(Optional.of(proyecto1));
        doThrow(new RuntimeException("No existe")).when(tecnicoClient).getTecnicoById(idTecnico);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proyectoService.asignarTecnicoAProyecto(1L, idTecnico);
        });

        assertEquals("El ID de técnico no existe.", exception.getMessage());
        verify(proyectoRepository).findById(1L);
        verify(tecnicoClient).getTecnicoById(idTecnico);
    }

    @Test
    void asignarTecnicoAProyecto_conProyectoNoExistente_lanzaExcepcion() {
        when(proyectoRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            proyectoService.asignarTecnicoAProyecto(1L, 50L);
        });

        assertEquals("Proyecto con ID 1 no encontrado.", exception.getMessage());
        verify(proyectoRepository).findById(1L);
        verifyNoInteractions(tecnicoClient);
    }

    @Test
    void deleteProyecto_debeLlamarDeleteById() {
        doNothing().when(proyectoRepository).deleteById(1L);

        proyectoService.deleteProyecto(1L);

        verify(proyectoRepository).deleteById(1L);
    }
}
