package com.example.Cliente.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Cliente.model.Cliente;
import com.example.Cliente.repository.ClienteRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional

public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public List<Cliente> getClientes(){
        return clienteRepository.findAll();
    }
    


    public Cliente getClientePorId(Long Id){
    return clienteRepository.findById(Id)
    .orElseThrow(()-> new RuntimeException("Cliente no encontrado"));
    
}


public Cliente saveCliente(Cliente nuevo) {
    if (nuevo.getPnombre() == null || nuevo.getApaterno() == null || nuevo.getContrasena() == null || nuevo.getCorreo() == null) {
        throw new IllegalArgumentException("Todos los campos son obligatorios");
    }
    return clienteRepository.save(nuevo);
}
}


