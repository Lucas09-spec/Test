package Usuario.com.example.USUARIO.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Repository.UsuarioRepository;
import Usuario.com.example.USUARIO.Service.UsuarioService;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UsuarioService usuarioService;

    // Test para guardar un usuario sin procesamiento de contraseña.
    @Test
    public void testGuardarUsuario() {
         Usuario nuevo = new Usuario();
         nuevo.setId(1L);
         nuevo.setCorreo("example@example.com");
         nuevo.setPassword("password");
         
         when(usuarioRepository.save(nuevo)).thenReturn(nuevo);
         
         Usuario guardado = usuarioService.guardarusuario(nuevo);
         assertThat(guardado).isNotNull();
         assertThat(guardado.getCorreo()).isEqualTo("example@example.com");
         verify(usuarioRepository, times(1)).save(nuevo);
    }
    
    // Test para obtener la lista de usuarios.
    @Test
    public void testObtenerUsuarios() {
         Usuario user1 = new Usuario();
         user1.setId(1L);
         user1.setCorreo("a@example.com");
         
         Usuario user2 = new Usuario();
         user2.setId(2L);
         user2.setCorreo("b@example.com");
         
         when(usuarioRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
         
         List<Usuario> usuarios = usuarioService.obteneruUsuarios();
         assertThat(usuarios).hasSize(2);
         verify(usuarioRepository, times(1)).findAll();
    }
    
    // Test para crear un usuario, verificando el encode de la contraseña.
    @Test
    public void testCrearUsuario() {
         Usuario usuario = new Usuario();
         usuario.setId(1L);
         usuario.setCorreo("test@example.com");
         usuario.setPassword("rawPassword");
         
         // Simula que al codificar se transforma la contraseña
         when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
         
         // Cuando se guarda el usuario se retorna un usuario con la contraseña codificada.
         Usuario saved = new Usuario();
         saved.setId(1L);
         saved.setCorreo("test@example.com");
         saved.setPassword("encodedPassword");
         when(usuarioRepository.save(usuario)).thenReturn(saved);
         
         Usuario result = usuarioService.crearUsuario(usuario);
         assertThat(result).isNotNull();
         assertThat(result.getPassword()).isEqualTo("encodedPassword");
         verify(passwordEncoder, times(1)).encode("rawPassword");
         verify(usuarioRepository, times(1)).save(usuario);
    }
    
    // Test para validar la contraseña.
    @Test
    public void testValidarPassword() {
         when(passwordEncoder.matches("raw", "encoded")).thenReturn(true);
         
         boolean isValid = usuarioService.validarPassword("raw", "encoded");
         assertThat(isValid).isTrue();
         verify(passwordEncoder, times(1)).matches("raw", "encoded");
    }
    
    // Test para obtener un usuario por su ID.
    @Test
    public void testObtenerUsuarioPorId() {
         Usuario user = new Usuario();
         user.setId(1L);
         user.setCorreo("user@example.com");
         user.setPassword("password");
         when(usuarioRepository.findById(1L)).thenReturn(Optional.of(user));
         
         Usuario result = usuarioService.obtenerUsuarioPorId(1L);
         assertThat(result).isNotNull();
         assertThat(result.getCorreo()).isEqualTo("user@example.com");
         verify(usuarioRepository, times(1)).findById(1L);
    }
}
