package Preguntas.com.example.PreguntasFrecuentes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PreguntasFrecuentesConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
        
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/v1/preguntas/**")
                        .allowedOrigins("*")            // Permite peticiones desde cualquier origen
                        .allowedMethods("GET", "POST", "DELETE") // Métodos HTTP permitidos
                        .allowedHeaders("*");           // Se permiten todos los encabezados
            }
        };
    }
}
