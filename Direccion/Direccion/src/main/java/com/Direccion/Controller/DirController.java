package com.Direccion.Controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Direccion.Model.Direccion;
import com.Direccion.Service.DirService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("api/v1/Direccion")
public class DirController {

    @Autowired
    private DirService dirService;

    @Operation(summary = "Obtiene la lista de todas las Direcciones")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de Direcciones encontrada",
                content = @Content(schema = @Schema(implementation = Direccion.class))),
        @ApiResponse(responseCode = "204", description = "No hay Direcciones registradas",
                content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Direccion>> obtenerDir() {
        List<Direccion> lista = dirService.getDireccion();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtiene una Direccion por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Direccion encontrada",
                content = @Content(schema = @Schema(implementation = Direccion.class))),
        @ApiResponse(responseCode = "404", description = "Direccion no encontrada",
                content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Direccion> obtenerDireccionPorId(@PathVariable Long id) {
        try {
            Direccion direccion = dirService.getDireccionPorId(id);
            return ResponseEntity.ok(direccion);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Crea una nueva Direccion")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Direccion creada correctamente",
                content = @Content(schema = @Schema(implementation = Direccion.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos en la solicitud",
                content = @Content),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor",
                content = @Content)
    })
    @PostMapping
    public ResponseEntity<Direccion> guardarDir(@RequestBody Direccion nuevo) {
        try {
            Direccion direccionGuardada = dirService.saveDireccion(nuevo);
            System.out.println(direccionGuardada);
            return ResponseEntity.status(201).body(direccionGuardada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}