package com.example.Categorias.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPICategoriaConfig {

    @Bean
    public OpenAPI apiCategoriaConfig(){
        return new OpenAPI()
            .info(new Info()
                .title("Categoría API")
                .version("1.0")
                .description("Documentación de los Endpoints del servicio de Categoría"));
    }
}
