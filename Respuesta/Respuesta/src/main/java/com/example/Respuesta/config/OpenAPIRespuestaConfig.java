package com.example.Respuesta.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIRespuestaConfig {
    
    @Bean
    public OpenAPI apiRespuestaConfig(){
        return new OpenAPI()
                .info(new Info()
                        .title("Respuesta API")
                        .version("1.0")
                        .description("Documentación de los Endpoints del servicio de Respuesta"));
    }
}
