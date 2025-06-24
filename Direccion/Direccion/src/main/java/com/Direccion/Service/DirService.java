package com.Direccion.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Direccion.Model.Direccion;
import com.Direccion.Repository.DireccionRepository;

import jakarta.transaction.Transactional;

import io.swagger.v3.oas.annotations.media.Schema;

@Service
@Transactional
public class DirService {

    @Autowired
    private DireccionRepository direccionRepository;

    @Schema(description = "Obtiene todas las direcciones registradas",
            example = "[{Id_dir:1, Id_reg:3, Id_com:5, nom_dir:'Calle Falsa 123'}, {Id_dir:2, Id_reg:4, Id_com:7, nom_dir:'Avenida Siempre Viva'}]")
    public List<Direccion> getDireccion() {
        return direccionRepository.findAll();
    }

    @Schema(description = "Obtiene una dirección por su ID",
            implementation = Direccion.class)
    public Direccion getDireccionPorId(Long Id) {
        return direccionRepository.findById(Id)
            .orElseThrow(() -> new RuntimeException("Todos los campos son obligatorios"));
    }

    @Schema(description = "Guarda una nueva dirección en la base de datos",
            implementation = Direccion.class)
    public Direccion saveDireccion(
        @Schema(description = "Objeto Direccion a guardar", required = true) Direccion nuevo) {
        
        if (nuevo.getId_com() == null || nuevo.getNom_dir() == null) {
            throw new IllegalArgumentException("Todos los argumentos deben ser obligatorios");
        }
        return direccionRepository.save(nuevo);
    }
}
