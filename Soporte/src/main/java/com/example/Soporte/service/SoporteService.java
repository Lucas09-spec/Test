package com.example.Soporte.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Soporte.model.Soporte;
import com.example.Soporte.repository.SoporteRepository;
import com.example.Soporte.webclient.CategoriaClient;
import com.example.Soporte.webclient.EstadoClient;
import com.example.Soporte.webclient.UsuarioClient;

@Service
public class SoporteService {
    @Autowired
    private SoporteRepository soporteRepository;
    @Autowired
    private CategoriaClient categoriaClient;
    @Autowired
    private EstadoClient estadoClient;
    @Autowired
    private UsuarioClient usuarioClient;


    

    public List<Soporte> findAll(){
        return soporteRepository.findAll();
    }

    public Optional<Soporte> findById(Long id){
        return  soporteRepository.findById(id);
    }

    public Soporte saveSoporte(Soporte nuevoSoporte){
        //verificar si el cliente existe consultando al microservicio cliente
        Map<String,Object> categoria = categoriaClient.getCategoriaById(nuevoSoporte.getCategoriaId());
        //verifico si me trajo el cliente o no
        if(categoria == null || categoria.isEmpty()){
            throw new RuntimeException("Categoria no encontrada.");
        }

        Map<String,Object> estado = estadoClient.getEstadoById(nuevoSoporte.getEstadoId());
        if(estado == null || estado.isEmpty()){
            throw new RuntimeException("Estado no encontrado.");
        }

        Map<String,Object> usuario = usuarioClient.getUsuarioById(nuevoSoporte.getUsuarioId());
        if(usuario == null || usuario.isEmpty()){
            throw new RuntimeException("Usuario no encontrado");
        }
        return soporteRepository.save(nuevoSoporte);



    }



    public void delete(Long id) {
        soporteRepository.deleteById(id);
    }

}
