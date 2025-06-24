package com.Webproyecto;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class ContratoClient {

    private final WebClient webClient;

    public ContratoClient(@Value("${servicio-service.url}") String ServicioServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(ServicioServiceUrl).build();
    }

    public Map<String, Object> getContratoById(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> {
                        return reactor.core.publisher.Mono.error(
                            new RuntimeException("Servicio no encontrado"));
                    })
            )
            .bodyToMono(Map.class)
            .block();
    }
}