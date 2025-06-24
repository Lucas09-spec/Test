package Usuario.com.example.USUARIO.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import Usuario.com.example.USUARIO.Model.Usuario;
import Usuario.com.example.USUARIO.Repository.UsuarioRepository;
@Service

public class UserDetailsServiceImpl  implements UserDetailsService{
   @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findBycorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con username: " + correo));
        return UserDetailsImpl.build(usuario);
    }
}
