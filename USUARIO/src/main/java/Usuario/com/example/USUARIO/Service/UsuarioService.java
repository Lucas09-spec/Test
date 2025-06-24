package Usuario.com.example.USUARIO.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import Usuario.com.example.USUARIO.FeignClients.RoleFeignClient;
import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.ServerEndpoint;

@Service
@Transactional
public class UsuarioService {
 @Autowired 
 private UsuarioRepository usuarioRepository; 


 public Usuario guardarusuario(Usuario nuevo){
    return usuarioRepository.save(nuevo);
 }

 public List<Usuario>  obteneruUsuarios(){
    return usuarioRepository.findAll();
 }
   @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario crearUsuario(Usuario usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }
    public boolean validarPassword(String rawPassword, String encodedPassword) {
      return passwordEncoder.matches(rawPassword, encodedPassword);
  }
  public Usuario obtenerUsuarioPorId(Long id) {
   return usuarioRepository.findById(id).orElse(null);
}

}
