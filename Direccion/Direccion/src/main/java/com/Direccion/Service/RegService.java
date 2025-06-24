package com.Direccion.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Direccion.Model.Region;
import com.Direccion.Repository.RegRepository;

import jakarta.transaction.Transactional;

import io.swagger.v3.oas.annotations.media.Schema;

@Service
@Transactional
public class RegService {

    @Autowired 
    private RegRepository regRepository;

    @Schema(description = "Obtiene todas las regiones registradas",
            example = "[{Id_reg:1, Nom_reg:'Coquimbo'}, {Id_reg:2, Nom_reg:'Valparaíso'}]")
    public List<Region> getRegions() {
        return regRepository.findAll();
    }

    @Schema(description = "Obtiene una región por su ID",
            implementation = Region.class)
    public Region getRegionPorId(Long Id) {
        return regRepository.findById(Id)
            .orElseThrow(() -> new RuntimeException("Region no encontrada"));
    }

    @Schema(description = "Guarda una nueva región en la base de datos",
            implementation = Region.class)
    public Region saveRegion(
        @Schema(description = "Objeto Region a guardar", required = true) Region nuevo) {
        
        if (nuevo.getNom_reg() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }
        return regRepository.save(nuevo);
    }
}

