package Usuario.com.example.USUARIO.controller;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import Usuario.com.example.USUARIO.Controller.UsuarioController;
import Usuario.com.example.USUARIO.FeignClients.RoleFeignClient;
import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Service.UsuarioService;

public class UsuarioControllerStandaloneTest {

    private MockMvc mockMvc;
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;  // Mock de la dependencia

    @Mock
    private RoleFeignClient roleFeignClient;  // También se mockea el FeignClient (aunque no se use en el test)

    @BeforeEach
    public void setup() {
        // Inicializa los mocks
        MockitoAnnotations.openMocks(this);

        // Instancia el controlador que queremos testear
        usuarioController = new UsuarioController();

        // Inyecta manualmente los mocks en el controlador usando ReflectionTestUtils
        ReflectionTestUtils.setField(usuarioController, "usuarioService", usuarioService);
        ReflectionTestUtils.setField(usuarioController, "roleFeignClient", roleFeignClient);

        // Configura MockMvc en modo standalone con el controlador
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
    }

    // Test: GET /api/v2/usuarios cuando la lista está vacía -> se espera retorno 204 No Content
    @Test
    public void testObtenerUsuarios_ReturnsNoContent() throws Exception {
        Mockito.when(usuarioService.obteneruUsuarios())
               .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/v2/usuarios"))
               .andExpect(status().isNoContent());
    }

    // Test: GET /api/v2/usuarios/{usuarioId}/acciones para un usuario existente  
    // Con rol 1 asumimos que se interpreta como "Administrador" y se retorna la acción correspondiente
    @Test
    public void testObtenerAccionesPorUsuarioId_ReturnsOk() throws Exception {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setNombre("Usuario Uno");
        user.setCorreo("uno@example.com");
        user.setRol(1L);  // Se asume que el rol 1 se traduce a "Administrador"

        Mockito.when(usuarioService.obtenerUsuarioPorId(1L))
               .thenReturn(user);

        mockMvc.perform(get("/api/v2/usuarios/1/acciones"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.nombreUsuario", is("Usuario Uno")))
               .andExpect(jsonPath("$.rolNombre", is("Administrador")))
               .andExpect(jsonPath("$.accion", 
                   is("Gestiona usuarios, roles y realiza respaldos del sistema.")));
    }

    // Test: GET /api/v2/usuarios/{usuarioId}/acciones cuando no se encuentra el usuario -> se espera 404
    @Test
    public void testObtenerAccionesPorUsuarioId_NotFound() throws Exception {
        Mockito.when(usuarioService.obtenerUsuarioPorId(1L))
               .thenReturn(null);

        mockMvc.perform(get("/api/v2/usuarios/1/acciones"))
               .andExpect(status().isNotFound());
    }
}
