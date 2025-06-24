package com.Direccion.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Direccion.Model.Comuna;
import com.Direccion.Repository.ComRepository;

import jakarta.transaction.Transactional;

import io.swagger.v3.oas.annotations.media.Schema;

@Service
@Transactional
public class ComService {

    @Autowired
    private ComRepository comRepository;

    @Schema(description = "Obtiene todas las comunas registradas", 
            example = "[{id_com:1, id_reg:2, nom_com:'La Florida'}, {id_com:2, id_reg:2, nom_com:'Puente Alto'}]")
    public List<Comuna> getComunas(){
        return comRepository.findAll();
    }

    @Schema(description = "Obtiene una comuna por su ID", implementation = Comuna.class)
    public Comuna getComunaPorId(Long Id){
        return comRepository.findById(Id)
            .orElseThrow(() -> new RuntimeException("Comuna no encontrada"));
    }

    @Schema(description = "Guarda una nueva comuna en la base de datos",
            implementation = Comuna.class)
    public Comuna saveComuna(
        @Schema(description = "Objeto Comuna a guardar", required = true) Comuna nuevo) {

        if (nuevo.getNom_com() == null || nuevo.getId_reg() == null){
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        return comRepository.save(nuevo);
    }
}