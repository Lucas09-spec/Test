package com.example.Categorias.service;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.example.Categorias.model.Categoria;
import com.example.Categorias.repository.CategoriaRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void getCategorias_returnsListOfCategorias() {
        // Categorías simuladas de soporte (3 campos: id, nombre, descripción)
        List<Categoria> categorias = Arrays.asList(
            new Categoria(1L, "Problemas Técnicos", "Soporte para errores técnicos"),
            new Categoria(2L, "Consultas de Facturación", "Dudas sobre cobros y pagos"),
            new Categoria(3L, "Soporte de Software", "Ayuda con aplicaciones y programas"),
            new Categoria(4L, "Reportar un Error (Bug)", "Errores en la plataforma"),
            new Categoria(5L, "Reembolsos / Devoluciones", "Solicitudes de devolución")
        );

        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.getCategorias();

        assertThat(resultado).hasSize(5);
        assertThat(resultado).extracting(Categoria::getNombre)
            .containsExactly(
                "Problemas Técnicos",
                "Consultas de Facturación",
                "Soporte de Software",
                "Reportar un Error (Bug)",
                "Reembolsos / Devoluciones"
            );
    }

    @Test
    void getCategoriaPorId_whenExists_returnsCategoria() {
        Categoria categoria = new Categoria(1L, "Problemas Técnicos", "Soporte para errores técnicos");

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Categoria resultado = categoriaService.getCategoriaPorId(1L);

        assertThat(resultado).isEqualTo(categoria);
    }

    @Test
    void getCategoriaPorId_whenNotExists_throwsException() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.getCategoriaPorId(99L))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Categoria No Encontrada");
    }

    @Test
    void saveCategoria_returnsSavedCategoria() {
        Categoria nueva = new Categoria(null, "Sugerencias y Comentarios", "Recomendaciones o ideas del cliente");
        Categoria guardada = new Categoria(6L, "Sugerencias y Comentarios", "Recomendaciones o ideas del cliente");

        when(categoriaRepository.save(nueva)).thenReturn(guardada);

        Categoria resultado = categoriaService.saveCategoria(nueva);

        assertThat(resultado).isEqualTo(guardada);
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        Long id = 3L;

        categoriaService.delete(id);

        verify(categoriaRepository, times(1)).deleteById(id);
    }
}


