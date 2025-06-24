package com.example.Soporte.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;


@Component
public class CategoriaClient {
    private final WebClient webClient;

    public CategoriaClient(@Value("${categoria-service.url}") String categoriaServiceUrl){
        this.webClient = WebClient.builder().baseUrl(categoriaServiceUrl).build();
    }

    public Map<String, Object> getCategoriaById(Long id){
        return this.webClient.get()
        .uri("/{id}", id)
        .retrieve()
        .onStatus(status -> status.is4xxClientError(), 
        response -> response.bodyToMono(String.class)
        .map(body -> new RuntimeException("Categoria no encontrada")))
        .bodyToMono(Map.class).block();
    }

    
}
