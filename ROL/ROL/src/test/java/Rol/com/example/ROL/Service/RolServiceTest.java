package Rol.com.example.ROL.Service;

import Rol.com.example.ROL.Model.RolModel;
import Rol.com.example.ROL.Repository.RolRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RolServiceTest {

    @Mock
    private RolRepository repository;

    @InjectMocks
    private RolService service;

    @Test
    void guardarRol() {
        RolModel nuevoRol = new RolModel(1L, "Administrador");

        when(repository.save(nuevoRol)).thenReturn(nuevoRol);

        RolModel result = service.guardarRol(nuevoRol);

        assertThat(result).isSameAs(nuevoRol);
    }

    @Test
    void obtenerTodosLosRoles() {
        List<RolModel> listaMock = Arrays.asList(
                new RolModel(1L, "Admin"),
                new RolModel(2L, "User")
        );

        when(repository.findAll()).thenReturn(listaMock);

        List<RolModel> resultado = service.getRol();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreRol()).isEqualTo("Admin");
    }

    @Test
    void obtenerRolPorIdExistente() {
        RolModel rol = new RolModel(1L, "Admin");

        when(repository.findById(1L)).thenReturn(Optional.of(rol));

        RolModel resultado = service.obtenerRolPorId(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNombreRol()).isEqualTo("Admin");
    }

    @Test
    void obtenerRolPorIdNoExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        RolModel resultado = service.obtenerRolPorId(99L);

        assertThat(resultado).isNull();
    }



    
}
