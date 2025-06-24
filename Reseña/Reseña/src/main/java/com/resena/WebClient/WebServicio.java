package com.resena.WebClient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebServicio {

    private final WebClient webClient;

    public WebServicio(@Value("${servicio-service.url}") String servicioServiceUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(servicioServiceUrl)
                                  .build();
    }

    public Map<String, Object> getServicioById(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(
                        new RuntimeException("Servicio no encontrado con ID: " + id)))
            )
            .bodyToMono(Map.class)
            .block();
    }
}
