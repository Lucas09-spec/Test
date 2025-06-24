package com.Direccion.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Direccion.Model.Comuna;
import com.Direccion.Service.ComService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/api/v1/Comuna")
public class ComController {

    @Autowired
    private ComService comService;

    @Operation(summary = "Obtiene la lista de todas las Comunas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de Comunas encontrada",
            content = @Content(schema = @Schema(implementation = Comuna.class))),
        @ApiResponse(responseCode = "204", description = "No hay Comunas registradas",
            content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Comuna>> obtenerComuna() {
        List<Comuna> lista = comService.getComunas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtiene una Comuna por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Comuna encontrada",
            content = @Content(schema = @Schema(implementation = Comuna.class))),
        @ApiResponse(responseCode = "404", description = "Comuna no encontrada",
            content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Comuna> obtenerComunaPorId(@PathVariable("id") Long id) {
        try {
            Comuna comuna = comService.getComunaPorId(id);
            return ResponseEntity.ok(comuna);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crea una nueva Comuna")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Comuna creada correctamente",
            content = @Content(schema = @Schema(implementation = Comuna.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
            content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content)
    })
    @PostMapping
    public ResponseEntity<Comuna> guardarComuna(@RequestBody Comuna nuevo) {
        try {
            Comuna comunaGuardar = comService.saveComuna(nuevo);
            System.out.println(comunaGuardar);
            return ResponseEntity.status(201).body(comunaGuardar);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}