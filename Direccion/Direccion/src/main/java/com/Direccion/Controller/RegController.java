package com.Direccion.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Direccion.Model.Region;
import com.Direccion.Service.RegService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/Region")
public class RegController {

    @Autowired 
    private RegService regService;

    @Operation(summary = "Obtiene la lista de todas las Regiones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de Regiones encontrada",
            content = @Content(schema = @Schema(implementation = Region.class))),
        @ApiResponse(responseCode = "204", description = "No hay Regiones registradas",
            content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Region>> obtenerRegion() {
        List<Region> lista = regService.getRegions();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtiene una Región por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Región encontrada",
            content = @Content(schema = @Schema(implementation = Region.class))),
        @ApiResponse(responseCode = "404", description = "Región no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Region> obtenerRegionPorId(@PathVariable("id") Long id) {
        try {
            Region region = regService.getRegionPorId(id);
            return ResponseEntity.ok(region);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crea una nueva Región")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Región creada correctamente",
            content = @Content(schema = @Schema(implementation = Region.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<Region> guardarRegion(@RequestBody Region nuevo) {
        try {
            Region regionGuardada = regService.saveRegion(nuevo);
            System.out.println(regionGuardada);
            return ResponseEntity.status(201).body(regionGuardada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}


