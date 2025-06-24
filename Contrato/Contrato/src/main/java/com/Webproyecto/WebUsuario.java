package com.Webproyecto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebUsuario {

    private final WebClient webClient;

    public WebUsuario(@Value("${usuario-service.url}") String usuarioServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(usuarioServiceUrl).build();
    }

    public Map<String, Object> getUsuarioById(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> reactor.core.publisher.Mono.error(
                        new RuntimeException("Usuario no encontrado"))
                    )
            )
            .bodyToMono(Map.class)
            .block();
    }

    public boolean existeUsuario(Long id) {
        try {
            this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

