package Usuario.com.example.USUARIO.FeignClients;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAccionDTO {
    private String nombreUsuario;
    private String rolNombre;
    private String accion;

}
