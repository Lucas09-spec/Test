package com.Proyecto.WebClient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;



@Component
public class TecnicoClient {

    private final WebClient webClient;

    public TecnicoClient(@Value("${tecnico-service.url}") String tecnicoServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(tecnicoServiceUrl).build();
    }

    public Map<String, Object> getTecnicoById(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new RuntimeException("Técnico no encontrado")))
            )
            .bodyToMono(Map.class)
            .block();
    }
}
