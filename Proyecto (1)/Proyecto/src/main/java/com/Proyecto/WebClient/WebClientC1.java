package com.Proyecto.WebClient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class WebClientC1 {

    private final WebClient webproyect;

    public WebClientC1(@Value("${contrato-service.url}") String contratoServiceUrl) {
        this.webproyect = WebClient.builder().baseUrl(contratoServiceUrl).build();
    }

    public Map<String, Object> getContratoById(Long id) {
        return this.webproyect.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new RuntimeException("Contrato no encontrado")))
            )
            .bodyToMono(Map.class)
            .block();
    }
}
