package com.Webproyecto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class DireccionClient {

    private final WebClient webClient;

    public DireccionClient(@Value("${direccion-service.url}") String direccionServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(direccionServiceUrl).build();
    }

    public Map<String, Object> getDireccionById(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> reactor.core.publisher.Mono.error(
                        new RuntimeException("Dirección no encontrada"))
                    )
            )
            .bodyToMono(Map.class)
            .block();
    }

    public boolean existeDireccion(Long id) {
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
