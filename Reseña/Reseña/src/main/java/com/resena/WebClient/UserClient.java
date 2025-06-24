package com.resena.WebClient;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class UserClient {

    private final WebClient webClient;

    public UserClient(@Value("${usuario-service.url}") String userServiceUrl) {
        this.webClient = WebClient.builder()
                                  .baseUrl(userServiceUrl)
                                  .build();
    }

    public Map<String, Object> getUserById(Long id) {
        return this.webClient.get()
            .uri("/{id}", id)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(body -> Mono.error(new RuntimeException("Usuario no encontrado")))
            )
            .bodyToMono(Map.class)
            .block();
    }
}
