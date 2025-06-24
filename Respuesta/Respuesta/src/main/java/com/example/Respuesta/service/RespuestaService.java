package com.example.Respuesta.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Respuesta.model.Respuesta;
import com.example.Respuesta.repository.RespuestaRepository;
import com.example.Respuesta.webclient.SoporteClient;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class RespuestaService {
    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private SoporteClient soporteClient;


    

    public List<Respuesta> findAll(){
        return respuestaRepository.findAll();
    }

    public Optional<Respuesta> findById(Long id){
        return  respuestaRepository.findById(id);
    }

    public Respuesta saveSoporte(Respuesta nuevoRespuesta){
        
        Map<String,Object> soporte = soporteClient.getSoporteById(nuevoRespuesta.getSoporteId());
        
        if(soporte == null || soporte.isEmpty()){
            throw new RuntimeException("Soporte no encontrado. No se puede agregar el pedido");
        }
        return respuestaRepository.save(nuevoRespuesta);

    }

    public void delete(Long id) {
        respuestaRepository.deleteById(id);
    }

}
