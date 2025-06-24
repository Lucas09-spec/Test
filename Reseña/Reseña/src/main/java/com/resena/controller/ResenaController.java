package com.resena.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.resena.model.Resena;
import com.resena.service.ResenaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/Resena")
@Tag(name = "Reseñas", description = "Endpoints para gestión de reseñas")
public class ResenaController {

    @Autowired
    private ResenaService resenaService;

    @Operation(summary = "Permite obtener una lista con todas las reseñas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Generó la lista con todas las reseñas",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = Resena.class)))),
        @ApiResponse(responseCode = "204", description = "No existen reseñas registradas", content = @Content)
    })
    @GetMapping
    public ResponseEntity<List<Resena>> obtenerResena() {
        List<Resena> lista = resenaService.getResenas();
        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

    @Operation(summary = "Obtiene una reseña por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña encontrada",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resena> obtenerResenaPorId(@PathVariable("id") Long id) {
        try {
            Resena resena = resenaService.getResenaPorId(id);
            return ResponseEntity.ok(resena);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Permite crear una nueva reseña")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reseña creada exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o faltantes",
            content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "500", description = "Error interno al guardar la reseña",
            content = @Content(schema = @Schema(implementation = String.class)))
    })
    @PostMapping
    public ResponseEntity<?> guardarResena(@RequestBody Resena nuevo) {
        try {
            Resena resenaGuardado = resenaService.saveResena(nuevo);
            return ResponseEntity.status(201).body(resenaGuardado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ocurrió un error interno al guardar la reseña.");
        }
    }

    @Operation(summary = "Permite eliminar una reseña por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarResena(@PathVariable Long id) {
        try {
            resenaService.getResenaPorId(id); // valida existencia
            resenaService.deleteResena(id);   // elimina
            return ResponseEntity.ok("Reseña eliminada exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Permite actualizar una reseña por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Reseña actualizada exitosamente",
            content = @Content(schema = @Schema(implementation = Resena.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Reseña no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarResena(@PathVariable Long id, @RequestBody Resena actualizada) {
        try {
            Resena existente = resenaService.getResenaPorId(id);
            actualizada.setIdResena(existente.getIdResena());
            Resena guardada = resenaService.saveResena(actualizada);
            return ResponseEntity.ok(guardada);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
