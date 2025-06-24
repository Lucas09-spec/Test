package Usuario.com.example.USUARIO.Security;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import Usuario.com.example.USUARIO.Model.Usuario;

import java.util.Collection;
import java.util.Objects;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl  implements UserDetails {
    private Long id;
    private String correo;
    private String password;

    public static UserDetailsImpl build(Usuario usuario) {
        return new UserDetailsImpl(
            usuario.getId(),
            usuario.getCorreo(),
            usuario.getPassword()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // Si implementas roles, debes retornarlos aquí
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDetailsImpl)) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }
}
