package com.example.Soporte.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Soporte.model.Soporte;
import com.example.Soporte.repository.SoporteRepository;
import com.example.Soporte.webclient.CategoriaClient;
import com.example.Soporte.webclient.EstadoClient;
import com.example.Soporte.webclient.UsuarioClient;

@ExtendWith(MockitoExtension.class)
public class SoporteServiceTest {

    @Mock
    private SoporteRepository soporteRepository;

    @Mock
    private CategoriaClient categoriaClient;

    @Mock
    private EstadoClient estadoClient;

    @Mock
    private UsuarioClient usuarioClient;

    @InjectMocks
    private SoporteService soporteService;

    @Test
    void findAll_returnsListOfSoportes() {
        Soporte soporte1 = Soporte.builder()
                .id(1L)
                .titulo("Soporte 1")
                .descripcion("Descripción 1")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        Soporte soporte2 = Soporte.builder()
                .id(2L)
                .titulo("Soporte 2")
                .descripcion("Descripción 2")
                .fechaInicio(LocalDate.now())
                .categoriaId(2L)
                .estadoId(2L)
                .usuarioId(2L)
                .build();

        List<Soporte> soportes = Arrays.asList(soporte1, soporte2);
        when(soporteRepository.findAll()).thenReturn(soportes);

        List<Soporte> result = soporteService.findAll();
        assertThat(result)
                .hasSize(2)
                .contains(soporte1, soporte2);
        verify(soporteRepository, times(1)).findAll();
    }

    @Test
    void findById_whenExists_returnsSoporte() {
        Soporte soporte = Soporte.builder()
                .id(1L)
                .titulo("Soporte 1")
                .descripcion("Descripción")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        when(soporteRepository.findById(1L)).thenReturn(Optional.of(soporte));

        Optional<Soporte> result = soporteService.findById(1L);
        assertThat(result).isPresent().contains(soporte);
        verify(soporteRepository, times(1)).findById(1L);
    }

    @Test
    void findById_whenNotExists_returnsEmpty() {
        when(soporteRepository.findById(99L)).thenReturn(Optional.empty());
        Optional<Soporte> result = soporteService.findById(99L);
        assertThat(result).isNotPresent();
        verify(soporteRepository, times(1)).findById(99L);
    }

    @Test
    void saveSoporte_whenAllClientsReturnValidData_savesAndReturnsSoporte() {
        Soporte nuevoSoporte = Soporte.builder()
                .titulo("Nuevo Soporte")
                .descripcion("Nueva descripción")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        // Simular respuestas válidas de los web clients
        Map<String, Object> categoriaData = new HashMap<>();
        categoriaData.put("id", 1L);
        categoriaData.put("nombre", "Categoría 1");

        Map<String, Object> estadoData = new HashMap<>();
        estadoData.put("id", 1L);
        estadoData.put("nombre", "Estado 1");

        Map<String, Object> usuarioData = new HashMap<>();
        usuarioData.put("id", 1L);
        usuarioData.put("nombre", "Usuario 1");

        when(categoriaClient.getCategoriaById(1L)).thenReturn(categoriaData);
        when(estadoClient.getEstadoById(1L)).thenReturn(estadoData);
        when(usuarioClient.getUsuarioById(1L)).thenReturn(usuarioData);

        Soporte soporteGuardado = Soporte.builder()
                .id(1L)
                .titulo("Nuevo Soporte")
                .descripcion("Nueva descripción")
                .fechaInicio(nuevoSoporte.getFechaInicio())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();
        when(soporteRepository.save(nuevoSoporte)).thenReturn(soporteGuardado);

        Soporte result = soporteService.saveSoporte(nuevoSoporte);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitulo()).isEqualTo("Nuevo Soporte");

        verify(categoriaClient, times(1)).getCategoriaById(1L);
        verify(estadoClient, times(1)).getEstadoById(1L);
        verify(usuarioClient, times(1)).getUsuarioById(1L);
        verify(soporteRepository, times(1)).save(nuevoSoporte);
    }

    @Test
    void saveSoporte_whenCategoriaNotFound_throwsException() {
        Soporte nuevoSoporte = Soporte.builder()
                .titulo("Nuevo Soporte")
                .descripcion("Nueva descripción")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        when(categoriaClient.getCategoriaById(1L)).thenReturn(Collections.emptyMap());
        RuntimeException exception = catchThrowableOfType(() -> soporteService.saveSoporte(nuevoSoporte),
                RuntimeException.class);
        assertThat(exception).hasMessage("Categoria no encontrada.");
    }

    @Test
    void saveSoporte_whenEstadoNotFound_throwsException() {
        Soporte nuevoSoporte = Soporte.builder()
                .titulo("Nuevo Soporte")
                .descripcion("Nueva descripción")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        Map<String, Object> categoriaData = new HashMap<>();
        categoriaData.put("id", 1L);
        when(categoriaClient.getCategoriaById(1L)).thenReturn(categoriaData);
        when(estadoClient.getEstadoById(1L)).thenReturn(Collections.emptyMap());
        RuntimeException exception = catchThrowableOfType(() -> soporteService.saveSoporte(nuevoSoporte),
                RuntimeException.class);
        assertThat(exception).hasMessage("Estado no encontrado.");
    }

    @Test
    void saveSoporte_whenUsuarioNotFound_throwsException() {
        Soporte nuevoSoporte = Soporte.builder()
                .titulo("Nuevo Soporte")
                .descripcion("Nueva descripción")
                .fechaInicio(LocalDate.now())
                .categoriaId(1L)
                .estadoId(1L)
                .usuarioId(1L)
                .build();

        Map<String, Object> categoriaData = new HashMap<>();
        categoriaData.put("id", 1L);
        when(categoriaClient.getCategoriaById(1L)).thenReturn(categoriaData);

        Map<String, Object> estadoData = new HashMap<>();
        estadoData.put("id", 1L);
        when(estadoClient.getEstadoById(1L)).thenReturn(estadoData);

        when(usuarioClient.getUsuarioById(1L)).thenReturn(Collections.emptyMap());
        RuntimeException exception = catchThrowableOfType(() -> soporteService.saveSoporte(nuevoSoporte),
                RuntimeException.class);
        assertThat(exception).hasMessage("Usuario no encontrado");
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        Long id = 1L;
        doNothing().when(soporteRepository).deleteById(id);
        soporteService.delete(id);
        verify(soporteRepository, times(1)).deleteById(id);
    }

    // Método auxiliar para capturar excepciones usando AssertJ
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
