package com.example.Cliente.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;






import com.example.Cliente.model.Cliente;
import com.example.Cliente.service.ClienteService;

@RestController
@RequestMapping("/api/v1/cliente")

public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<Cliente>> obtenerClientes(){
        List<Cliente> lista = clienteService.getClientes();
        if (lista.isEmpty()){
            return ResponseEntity.noContent().build();
            
        }
        
        return  ResponseEntity.ok(lista);
    }


    
    
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerClienteporId(@PathVariable Long id){
        try {
            Cliente cliente = clienteService.getClientePorId(id);
            return ResponseEntity.ok(cliente);            
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }



@PostMapping
public ResponseEntity<Cliente> guardarCliente(@RequestBody Cliente nuevo) {
    try {
        Cliente clienteGuardado = clienteService.saveCliente(nuevo);
        System.out.println(clienteGuardado); // Verifica qué datos están siendo guardados
        return ResponseEntity.status(201).body(clienteGuardado);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(400).body(null); // Bad Request
    } catch (Exception e) {
        return ResponseEntity.status(500).body(null); // Internal Server Error
    }
}
}


