package Estado.com.example.ESTADO.service;

import Estado.com.example.ESTADO.Model.ESTADO;
import Estado.com.example.ESTADO.Repository.ESTADORepository;
import Estado.com.example.ESTADO.Service.ESTADOService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ESTADOServiceTest {

    @Mock
    private ESTADORepository estadoRepository;

    @InjectMocks
    private ESTADOService estadoService;

    @Test
    void obEstados_returnsListOfEstados() {
        // Simulación de dos objetos ESTADO. Se asume que la clase ESTADO tiene un método builder()
        ESTADO estado1 = ESTADO.builder().id(1L).build();
        ESTADO estado2 = ESTADO.builder().id(2L).build();
        List<ESTADO> estados = Arrays.asList(estado1, estado2);
        when(estadoRepository.findAll()).thenReturn(estados);

        List<ESTADO> result = estadoService.obEstados();
        assertThat(result).hasSize(2).contains(estado1, estado2);
        verify(estadoRepository, times(1)).findAll();
    }

    @Test
    void guardarEstado_returnsSavedEstado() {
        // Se crea un ESTADO nuevo (sin id) y se simula el guardado asignándole un id
        ESTADO nuevoEstado = ESTADO.builder().build(); 
        ESTADO estadoGuardado = ESTADO.builder().id(1L).build();
        when(estadoRepository.save(nuevoEstado)).thenReturn(estadoGuardado);

        ESTADO result = estadoService.guardarEstado(nuevoEstado);
        assertThat(result.getId()).isEqualTo(1L);
        verify(estadoRepository, times(1)).save(nuevoEstado);
    }
}
