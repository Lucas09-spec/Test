package com.example.Respuesta.webclient;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SoporteClient {
    private final WebClient webClient;


    public SoporteClient(@Value("${soporte-service.url}") String soporteServiceUrl){
        this.webClient = WebClient.builder().baseUrl(soporteServiceUrl).build();
    }

    public Map<String, Object> getSoporteById(Long id){
        return this.webClient.get()
        .uri("/{id}", id)
        .retrieve()
        .onStatus(status -> status.is4xxClientError(), 
        response -> response.bodyToMono(String.class)
        .map(body -> new RuntimeException("Soporte no encontrado")))
        .bodyToMono(Map.class).block();
    }
}
