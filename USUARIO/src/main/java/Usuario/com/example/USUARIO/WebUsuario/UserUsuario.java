package Usuario.com.example.USUARIO.WebUsuario;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.beans.factory.annotation.Value;

@Component
public class UserUsuario {
    private final WebClient webClient; 
   

 public UserUsuario(@Value("${Roles-service.url}") String UsuarioServiceUrl){
        this.webClient = WebClient.builder().baseUrl(UsuarioServiceUrl).build();
    }

    public boolean rolExistePorId(Long idRol) {
        try {
            webClient.get()
            .uri("/api/v1/Roles/{id}", idRol)
            .retrieve()
                    .bodyToMono(String.class) 
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        } catch (Exception e) {
            throw new RuntimeException("Error al verificar el rol: " + e.getMessage());
        }
    }
}

