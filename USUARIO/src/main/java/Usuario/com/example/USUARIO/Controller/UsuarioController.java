package Usuario.com.example.USUARIO.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Usuario.com.example.USUARIO.FeignClients.RoleFeignClient;
import Usuario.com.example.USUARIO.FeignClients.UserAccionDTO;
import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; 

    @Autowired
    private RoleFeignClient roleFeignClient;

    @Operation(
        summary = "Obtener todos los usuarios", 
        description = "Retorna la lista de usuarios registrados en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Usuarios encontrados",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Usuario.class)))
        ),
        @ApiResponse(responseCode = "204", description = "No se encontraron usuarios")
    })
    @GetMapping
    public ResponseEntity<List<Usuario>> obtenerusuarios() {
        List<Usuario> lista = usuarioService.obteneruUsuarios();
        if (lista.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(
        summary = "Obtener acciones por usuario", 
        description = "Retorna las acciones asociadas al usuario según su rol. Si el usuario no existe retorna 404."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Acciones retornadas correctamente", 
            content = @Content(schema = @Schema(implementation = UserAccionDTO.class))
        ),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/{usuarioId}/acciones")
    public ResponseEntity<UserAccionDTO> obtenerAccionesPorUsuarioId(@PathVariable Long usuarioId) {
        Usuario usuario = usuarioService.obtenerUsuarioPorId(usuarioId);
        
        if (usuario == null) {
            return ResponseEntity.notFound().build();
        }

        Long rolId = usuario.getRol();
        String rolNombre;
        String accion;

        switch (rolId.intValue()) {
            case 1 -> {
                rolNombre = "Administrador";
                accion = "Gestiona usuarios, roles y realiza respaldos del sistema.";
            }
            case 2 -> {
                rolNombre = "Coordinador";
                accion = "Asigna técnicos, gestiona proyectos y genera reportes.";
            }
            case 3 -> {
                rolNombre = "Técnico";
                accion = "Actualiza estado de proyectos, reporta problemas y gestiona inventario.";
            }
            case 4 -> {
                rolNombre = "Empleado";
                accion = "Apoya tareas administrativas internas.";
            }
            case 5 -> {
                rolNombre = "Cliente";
                accion = "Solicita instalaciones, revisa estado y deja reseñas.";
            }
            default -> {
                rolNombre = "Desconocido";
                accion = "Sin acciones definidas.";
            }
        }

        UserAccionDTO respuesta = new UserAccionDTO(usuario.getNombre(), rolNombre, accion);
        return ResponseEntity.ok(respuesta);
    }
}
