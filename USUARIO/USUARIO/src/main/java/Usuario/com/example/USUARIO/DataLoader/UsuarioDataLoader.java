
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Service.UsuarioService;

@Component
public class UsuarioDataLoader implements CommandLineRunner {

    private final UsuarioService usuarioService;

    public UsuarioDataLoader(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public void run(String... args) throws Exception {
        if (usuarioService.obteneruUsuarios().isEmpty()) {
            usuarioService.guardarUsuario(new Usuario(
                null, 
                "admin@example.com", 
                "admin123", 
                "Administrador", 
                "Sistema", 
                "123456789", 
                1L,          // rol administrador
                1L           // direccionId ejemplo
            ));

            usuarioService.guardarUsuario(new Usuario(
                null,
                "coordinador@example.com",
                "coord123",
                "Coordinador",
                "Proyecto",
                "987654321",
                2L,         // rol coordinador
                2L          // direccionId ejemplo
            ));

            // Puedes agregar más usuarios aquí con sus roles y dirección
            System.out.println("Usuarios precargados insertados.");
        }
    }
}
